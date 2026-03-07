import { Router } from "express";
import { PrismaClient } from "./generated/prisma";

const prisma = new PrismaClient();
const router = Router();

// Create a new custom challenge
router.post("/create", async (req, res) => {
  try {
    const {
      userId,
      name,
      description,
      type,
      category,
      duration,
      startDate,
      targetValue,
      unit,
      color,
      icon,
    } = req.body;

    if (!userId || !name || !duration) {
      return res.status(400).json({ error: "userId, name, and duration are required" });
    }

    const start = startDate ? new Date(startDate) : new Date();
    const end = new Date(start);
    end.setDate(end.getDate() + duration);

    const challenge = await prisma.challenge.create({
      data: {
        userId,
        name,
        description: description || "",
        type: type || "custom",
        category: category || "general",
        duration,
        startDate: start,
        endDate: end,
        targetValue: targetValue || null,
        unit: unit || null,
        color: color || "#FFD54F",
        icon: icon || "🎯",
        isActive: true,
      },
    });

    // Create daily entries for the challenge
    const dailyEntries = [];
    for (let i = 1; i <= duration; i++) {
      const entryDate = new Date(start);
      entryDate.setDate(entryDate.getDate() + i - 1);
      dailyEntries.push({
        userId,
        challengeId: challenge.id,
        dayNumber: i,
        date: entryDate,
        completed: false,
      });
    }

    await prisma.dailyEntry.createMany({
      data: dailyEntries,
    });

    // Create initial streak
    await prisma.streak.create({
      data: {
        userId,
        challengeId: challenge.id,
        length: 0,
      },
    });

    res.json({ success: true, challenge });
  } catch (error) {
    console.error("Error creating challenge:", error);
    res.status(500).json({ error: "Failed to create challenge" });
  }
});

// Get all challenges for a user
router.get("/user/:userId", async (req, res) => {
  try {
    const { userId } = req.params;
    const { active } = req.query;

    const where: any = { userId };
    if (active === "true") {
      where.isActive = true;
      where.endDate = { gte: new Date() };
    }

    const challenges = await prisma.challenge.findMany({
      where,
      include: {
        dailyTasks: {
          orderBy: { dayNumber: "asc" },
        },
        streaks: true,
        _count: {
          select: {
            dailyTasks: {
              where: { completed: true },
            },
          },
        },
      },
      orderBy: { createdAt: "desc" },
    });

    // Calculate progress for each challenge
    const challengesWithProgress = challenges.map((challenge) => {
      const completedDays = challenge._count.dailyTasks;
      const progress = Math.round((completedDays / challenge.duration) * 100);
      const currentStreak = challenge.streaks[0]?.length || 0;

      return {
        ...challenge,
        progress,
        completedDays,
        currentStreak,
      };
    });

    res.json({ challenges: challengesWithProgress });
  } catch (error) {
    console.error("Error fetching challenges:", error);
    res.status(500).json({ error: "Failed to fetch challenges" });
  }
});

// Get single challenge with all details
router.get("/:challengeId", async (req, res) => {
  try {
    const { challengeId } = req.params;

    const challenge = await prisma.challenge.findUnique({
      where: { id: challengeId },
      include: {
        dailyTasks: {
          orderBy: { dayNumber: "asc" },
        },
        streaks: true,
      },
    });

    if (!challenge) {
      return res.status(404).json({ error: "Challenge not found" });
    }

    const completedDays = challenge.dailyTasks.filter((d) => d.completed).length;
    const currentStreak = challenge.streaks[0]?.length || 0;

    // Calculate current day
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    const startDate = new Date(challenge.startDate);
    startDate.setHours(0, 0, 0, 0);
    const currentDay = Math.floor((today.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24)) + 1;

    res.json({
      challenge,
      progress: {
        completedDays,
        totalDays: challenge.duration,
        percentage: Math.round((completedDays / challenge.duration) * 100),
        currentStreak,
        currentDay: Math.min(currentDay, challenge.duration),
      },
    });
  } catch (error) {
    console.error("Error fetching challenge:", error);
    res.status(500).json({ error: "Failed to fetch challenge" });
  }
});

// Mark a day as complete/incomplete
router.post("/:challengeId/day/:dayNumber/toggle", async (req, res) => {
  try {
    const { challengeId, dayNumber } = req.params;
    const { userId, completed, notes, value } = req.body;

    if (!userId) {
      return res.status(400).json({ error: "userId is required" });
    }

    const dayNum = parseInt(dayNumber);

    // Update the daily entry
    const updatedEntry = await prisma.dailyEntry.updateMany({
      where: {
        userId,
        challengeId,
        dayNumber: dayNum,
      },
      data: {
        completed: completed ?? true,
        completedAt: completed ? new Date() : null,
        notes: notes || undefined,
        value: value || undefined,
      },
    });

    // Update streak
    if (completed) {
      await updateStreak(userId, challengeId);
    }

    // Get updated challenge data
    const challenge = await prisma.challenge.findUnique({
      where: { id: challengeId },
      include: {
        dailyTasks: {
          where: { dayNumber: dayNum },
        },
        streaks: true,
      },
    });

    res.json({
      success: true,
      entry: challenge?.dailyTasks[0],
      currentStreak: challenge?.streaks[0]?.length || 0,
    });
  } catch (error) {
    console.error("Error toggling day completion:", error);
    res.status(500).json({ error: "Failed to update day" });
  }
});

// Mark today's task complete (convenience endpoint)
router.post("/:challengeId/today", async (req, res) => {
  try {
    const { challengeId } = req.params;
    const { userId, completed, notes, value } = req.body;

    if (!userId) {
      return res.status(400).json({ error: "userId is required" });
    }

    const challenge = await prisma.challenge.findUnique({
      where: { id: challengeId },
    });

    if (!challenge) {
      return res.status(404).json({ error: "Challenge not found" });
    }

    // Calculate current day
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    const startDate = new Date(challenge.startDate);
    startDate.setHours(0, 0, 0, 0);
    const currentDay = Math.floor((today.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24)) + 1;

    if (currentDay < 1 || currentDay > challenge.duration) {
      return res.status(400).json({ error: "Challenge not active today" });
    }

    // Update the daily entry
    await prisma.dailyEntry.updateMany({
      where: {
        userId,
        challengeId,
        dayNumber: currentDay,
      },
      data: {
        completed: completed ?? true,
        completedAt: completed ? new Date() : null,
        notes: notes || undefined,
        value: value || undefined,
      },
    });

    // Update streak
    if (completed) {
      await updateStreak(userId, challengeId);
    }

    // Get updated data
    const updatedChallenge = await prisma.challenge.findUnique({
      where: { id: challengeId },
      include: {
        dailyTasks: {
          where: { dayNumber: currentDay },
        },
        streaks: true,
        _count: {
          select: {
            dailyTasks: {
              where: { completed: true },
            },
          },
        },
      },
    });

    res.json({
      success: true,
      dayNumber: currentDay,
      entry: updatedChallenge?.dailyTasks[0],
      currentStreak: updatedChallenge?.streaks[0]?.length || 0,
      totalCompleted: updatedChallenge?._count.dailyTasks || 0,
    });
  } catch (error) {
    console.error("Error marking today complete:", error);
    res.status(500).json({ error: "Failed to update today's task" });
  }
});

// Get challenge analytics
router.get("/:challengeId/analytics", async (req, res) => {
  try {
    const { challengeId } = req.params;

    const challenge = await prisma.challenge.findUnique({
      where: { id: challengeId },
      include: {
        dailyTasks: {
          orderBy: { dayNumber: "asc" },
        },
        streaks: true,
      },
    });

    if (!challenge) {
      return res.status(404).json({ error: "Challenge not found" });
    }

    const entries = challenge.dailyTasks;
    const completedDays = entries.filter((e) => e.completed).length;
    const currentStreak = challenge.streaks[0]?.length || 0;

    // Calculate longest streak
    let longestStreak = 0;
    let currentCount = 0;
    for (const entry of entries) {
      if (entry.completed) {
        currentCount++;
        longestStreak = Math.max(longestStreak, currentCount);
      } else {
        currentCount = 0;
      }
    }

    // Weekly breakdown
    const weeklyData = [];
    const weeks = Math.ceil(challenge.duration / 7);
    for (let i = 0; i < weeks; i++) {
      const weekEntries = entries.filter(
        (e) => e.dayNumber > i * 7 && e.dayNumber <= (i + 1) * 7
      );
      weeklyData.push({
        week: i + 1,
        completed: weekEntries.filter((e) => e.completed).length,
        total: weekEntries.length,
      });
    }

    // Last 7 days
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    const last7Days = [];
    for (let i = 6; i >= 0; i--) {
      const date = new Date(today);
      date.setDate(date.getDate() - i);
      
      const startDate = new Date(challenge.startDate);
      startDate.setHours(0, 0, 0, 0);
      const dayDiff = Math.floor((date.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24));
      const dayNumber = dayDiff + 1;

      const entry = entries.find((e) => e.dayNumber === dayNumber);

      last7Days.push({
        date: date.toISOString().split("T")[0],
        dayNumber,
        completed: entry?.completed || false,
        isFuture: dayNumber > challenge.duration || date > today,
        isValid: dayNumber >= 1 && dayNumber <= challenge.duration,
      });
    }

    res.json({
      summary: {
        totalDays: challenge.duration,
        completedDays,
        completionRate: Math.round((completedDays / challenge.duration) * 100),
        currentStreak,
        longestStreak,
      },
      weeklyData,
      last7Days,
      allDays: entries.map((e) => ({
        dayNumber: e.dayNumber,
        completed: e.completed,
        completedAt: e.completedAt,
        notes: e.notes,
        value: e.value,
      })),
    });
  } catch (error) {
    console.error("Error fetching analytics:", error);
    res.status(500).json({ error: "Failed to fetch analytics" });
  }
});

// Update challenge
router.patch("/:challengeId", async (req, res) => {
  try {
    const { challengeId } = req.params;
    const { name, description, color, icon, isActive } = req.body;

    const challenge = await prisma.challenge.update({
      where: { id: challengeId },
      data: {
        name: name || undefined,
        description: description || undefined,
        color: color || undefined,
        icon: icon || undefined,
        isActive: isActive !== undefined ? isActive : undefined,
      },
    });

    res.json({ success: true, challenge });
  } catch (error) {
    console.error("Error updating challenge:", error);
    res.status(500).json({ error: "Failed to update challenge" });
  }
});

// Delete challenge
router.delete("/:challengeId", async (req, res) => {
  try {
    const { challengeId } = req.params;

    await prisma.challenge.delete({
      where: { id: challengeId },
    });

    res.json({ success: true });
  } catch (error) {
    console.error("Error deleting challenge:", error);
    res.status(500).json({ error: "Failed to delete challenge" });
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

export { router as challengeRouter };
