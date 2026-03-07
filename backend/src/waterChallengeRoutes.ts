import { Router } from "express";
import { PrismaClient } from "./generated/prisma";

const prisma = new PrismaClient();
const router = Router();

// Create a new 30-day water challenge
router.post("/create", async (req, res) => {
  try {
    const { userId, startDate } = req.body;
    
    if (!userId) {
      return res.status(400).json({ error: "userId is required" });
    }

    const start = startDate ? new Date(startDate) : new Date();
    const end = new Date(start);
    end.setDate(end.getDate() + 30);

    const challenge = await prisma.challenge.create({
      data: {
        userId,
        name: "30-Day Water Challenge",
        type: "water",
        category: "health",
        duration: 30,
        startDate: start,
        endDate: end,
        targetValue: 2.0,
        unit: "liters",
        isActive: true,
        user: {
          connect: { id: userId }
        }
      },
    });

    // Create initial streak record
    await prisma.streak.create({
      data: {
        userId,
        challengeId: challenge.id,
        length: 0,
      },
    });

    res.json({ success: true, challenge });
  } catch (error) {
    console.error("Error creating water challenge:", error);
    res.status(500).json({ error: "Failed to create challenge" });
  }
});

// Get user's active water challenge
router.get("/active/:userId", async (req, res) => {
  try {
    const { userId } = req.params;
    
    const challenge = await prisma.challenge.findFirst({
      where: {
        userId,
        type: "water",
        endDate: {
          gte: new Date(),
        },
      },
      include: {
        waterEntries: {
          orderBy: {
            date: "desc",
          },
        },
        streaks: true,
      },
    });

    if (!challenge) {
      return res.json({ active: false });
    }

    // Calculate current progress
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    
    const todayEntry = challenge.waterEntries.find(
      (entry) => {
        const entryDate = new Date(entry.date);
        entryDate.setHours(0, 0, 0, 0);
        return entryDate.getTime() === today.getTime();
      }
    );

    const completedDays = challenge.waterEntries.filter(e => e.completed).length;
    const currentStreak = challenge.streaks[0]?.length || 0;

    res.json({
      active: true,
      challenge,
      progress: {
        currentDay: Math.floor((today.getTime() - new Date(challenge.startDate).getTime()) / (1000 * 60 * 60 * 24)) + 1,
        totalDays: 30,
        completedDays,
        currentStreak,
        todayAmount: todayEntry?.amount || 0,
        todayTarget: todayEntry?.targetAmount || 2.0,
        todayCompleted: todayEntry?.completed || false,
      },
    });
  } catch (error) {
    console.error("Error fetching active challenge:", error);
    res.status(500).json({ error: "Failed to fetch challenge" });
  }
});

// Log water intake for today
router.post("/log", async (req, res) => {
  try {
    const { userId, challengeId, amount } = req.body;
    
    if (!userId || !challengeId || amount === undefined) {
      return res.status(400).json({ error: "userId, challengeId, and amount are required" });
    }

    const today = new Date();
    today.setHours(0, 0, 0, 0);

    // Check if entry exists for today
    const existingEntry = await prisma.waterEntry.findFirst({
      where: {
        userId,
        challengeId,
        date: {
          gte: today,
          lt: new Date(today.getTime() + 24 * 60 * 60 * 1000),
        },
      },
    });

    const targetAmount = 2.0;
    const newAmount = existingEntry ? existingEntry.amount + amount : amount;
    const completed = newAmount >= targetAmount;

    let waterEntry;
    if (existingEntry) {
      waterEntry = await prisma.waterEntry.update({
        where: { id: existingEntry.id },
        data: {
          amount: newAmount,
          completed,
        },
      });
    } else {
      waterEntry = await prisma.waterEntry.create({
        data: {
          userId,
          challengeId,
          date: today,
          amount: newAmount,
          targetAmount,
          completed,
        },
      });
    }

    // Update streak if completed
    if (completed && (!existingEntry || !existingEntry.completed)) {
      await updateStreak(userId, challengeId);
    }

    res.json({
      success: true,
      waterEntry,
      remaining: Math.max(0, targetAmount - newAmount),
      progress: (newAmount / targetAmount) * 100,
    });
  } catch (error) {
    console.error("Error logging water intake:", error);
    res.status(500).json({ error: "Failed to log water intake" });
  }
});

// Get analytics for water challenge
router.get("/analytics/:userId/:challengeId", async (req, res) => {
  try {
    const { userId, challengeId } = req.params;

    const entries = await prisma.waterEntry.findMany({
      where: {
        userId,
        challengeId,
      },
      orderBy: {
        date: "asc",
      },
    });

    const challenge = await prisma.challenge.findUnique({
      where: { id: challengeId },
      include: { streaks: true },
    });

    if (!challenge) {
      return res.status(404).json({ error: "Challenge not found" });
    }

    // Calculate analytics
    const totalDays = entries.length;
    const completedDays = entries.filter(e => e.completed).length;
    const totalLiters = entries.reduce((sum, e) => sum + e.amount, 0);
    const averageDaily = totalDays > 0 ? totalLiters / totalDays : 0;
    const currentStreak = challenge.streaks[0]?.length || 0;

    // Weekly breakdown
    const weeklyData = [];
    for (let i = 0; i < 4; i++) {
      const weekStart = new Date(challenge.startDate);
      weekStart.setDate(weekStart.getDate() + i * 7);
      const weekEnd = new Date(weekStart);
      weekEnd.setDate(weekEnd.getDate() + 7);

      const weekEntries = entries.filter(e => {
        const entryDate = new Date(e.date);
        return entryDate >= weekStart && entryDate < weekEnd;
      });

      weeklyData.push({
        week: i + 1,
        daysCompleted: weekEntries.filter(e => e.completed).length,
        totalLiters: weekEntries.reduce((sum, e) => sum + e.amount, 0),
      });
    }

    // Daily progress for the last 7 days
    const last7Days = [];
    for (let i = 6; i >= 0; i--) {
      const date = new Date();
      date.setDate(date.getDate() - i);
      date.setHours(0, 0, 0, 0);

      const entry = entries.find(e => {
        const entryDate = new Date(e.date);
        entryDate.setHours(0, 0, 0, 0);
        return entryDate.getTime() === date.getTime();
      });

      last7Days.push({
        date: date.toISOString().split("T")[0],
        amount: entry?.amount || 0,
        completed: entry?.completed || false,
        target: 2.0,
      });
    }

    res.json({
      summary: {
        totalDays,
        completedDays,
        completionRate: totalDays > 0 ? (completedDays / totalDays) * 100 : 0,
        totalLiters,
        averageDaily,
        currentStreak,
        longestStreak: currentStreak, // Could calculate historical max
      },
      weeklyData,
      last7Days,
    });
  } catch (error) {
    console.error("Error fetching analytics:", error);
    res.status(500).json({ error: "Failed to fetch analytics" });
  }
});

// Helper function to update streak
async function updateStreak(userId: string, challengeId: string) {
  const streak = await prisma.streak.findFirst({
    where: {
      userId,
      challengeId,
    },
  });

  if (streak) {
    const lastUpdated = new Date(streak.lastUpdated);
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    lastUpdated.setHours(0, 0, 0, 0);

    const diffDays = Math.floor((today.getTime() - lastUpdated.getTime()) / (1000 * 60 * 60 * 24));

    if (diffDays === 1) {
      // Consecutive day
      await prisma.streak.update({
        where: { id: streak.id },
        data: {
          length: streak.length + 1,
          lastUpdated: new Date(),
        },
      });
    } else if (diffDays > 1) {
      // Streak broken, reset
      await prisma.streak.update({
        where: { id: streak.id },
        data: {
          length: 1,
          lastUpdated: new Date(),
        },
      });
    }
  }
}

// Get all challenges for user
router.get("/all/:userId", async (req, res) => {
  try {
    const { userId } = req.params;
    
    const challenges = await prisma.challenge.findMany({
      where: {
        userId,
        type: "water",
      },
      include: {
        waterEntries: true,
        streaks: true,
      },
      orderBy: {
        startDate: "desc",
      },
    });

    res.json({ challenges });
  } catch (error) {
    console.error("Error fetching challenges:", error);
    res.status(500).json({ error: "Failed to fetch challenges" });
  }
});

export { router as waterChallengeRouter };
