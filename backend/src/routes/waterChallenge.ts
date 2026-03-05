import { Router, Request, Response } from 'express'
import { prisma } from '../supabaseClient'

const router = Router()

// Create water challenge
router.post('/create', async (req: Request, res: Response) => {
  try {
    const { userId, startDate } = req.body
    
    if (!userId) {
      return res.status(400).json({ error: 'userId is required' })
    }

    const start = startDate ? new Date(startDate) : new Date()
    const end = new Date(start)
    end.setDate(end.getDate() + 30)

    const challenge = await prisma.challenge.create({
      data: {
        userId,
        name: '30-Day Water Challenge',
        type: 'water',
        category: 'health',
        duration: 30,
        startDate: start,
        endDate: end,
        targetValue: 2.0,
        unit: 'liters',
        color: '#4FC3F7',
        icon: '💧',
        isActive: true
      }
    })

    // Create daily entries
    const dailyEntries = []
    for (let i = 1; i <= 30; i++) {
      const date = new Date(start)
      date.setDate(date.getDate() + (i - 1))
      dailyEntries.push({
        userId,
        challengeId: challenge.id,
        dayNumber: i,
        date,
        completed: false
      })
    }
    await prisma.dailyEntry.createMany({ data: dailyEntries })

    // Create streak
    await prisma.streak.create({
      data: { userId, challengeId: challenge.id, length: 0 }
    })

    res.json({ success: true, challenge })
  } catch (error) {
    console.error('Error creating water challenge:', error)
    res.status(500).json({ error: 'Failed to create challenge' })
  }
})

// Get active water challenge
router.get('/active/:userId', async (req: Request, res: Response) => {
  try {
    const userId = req.params.userId as string
    
    const challenge = await prisma.challenge.findFirst({
      where: {
        userId,
        type: 'water',
        isActive: true,
        endDate: { gte: new Date() }
      },
      include: {
        dailyTasks: { orderBy: { dayNumber: 'asc' } },
        streaks: true
      }
    })

    if (!challenge) {
      return res.json({ active: false })
    }

    const today = new Date()
    today.setHours(0, 0, 0, 0)

    const todayEntry = challenge.dailyTasks.find((entry: any) => {
      const entryDate = new Date(entry.date)
      entryDate.setHours(0, 0, 0, 0)
      return entryDate.getTime() === today.getTime()
    })

    const completedDays = challenge.dailyTasks.filter((e: any) => e.completed).length
    const currentStreak = challenge.streaks[0]?.length || 0
    const currentDay = Math.min(
      Math.floor((Date.now() - new Date(challenge.startDate).getTime()) / (1000 * 60 * 60 * 24)) + 1,
      30
    )

    res.json({
      active: true,
      challenge,
      progress: {
        currentDay,
        totalDays: 30,
        completedDays,
        currentStreak,
        todayAmount: todayEntry?.value || 0,
        todayTarget: 2.0
      }
    })
  } catch (error) {
    console.error('Error fetching water challenge:', error)
    res.status(500).json({ error: 'Failed to fetch challenge' })
  }
})

// Log water intake
router.post('/log', async (req: Request, res: Response) => {
  try {
    const { userId, challengeId, amount } = req.body
    
    const today = new Date()
    today.setHours(0, 0, 0, 0)

    // Update or create daily entry
    const existingEntry = await prisma.dailyEntry.findFirst({
      where: {
        userId,
        challengeId,
        date: { gte: today }
      }
    })

    let updatedEntry
    if (existingEntry) {
      const newValue = (existingEntry.value || 0) + amount
      const completed = newValue >= 2.0
      
      updatedEntry = await prisma.dailyEntry.update({
        where: { id: existingEntry.id },
        data: {
          value: newValue,
          completed,
          completedAt: completed ? new Date() : null
        }
      })
    } else {
      const completed = amount >= 2.0
      updatedEntry = await prisma.dailyEntry.create({
        data: {
          userId,
          challengeId,
          dayNumber: Math.floor((Date.now() - today.getTime()) / (1000 * 60 * 60 * 24)) + 1,
          date: today,
          value: amount,
          completed,
          completedAt: completed ? new Date() : null
        }
      })
    }

    // Update streak
    const challenge = await prisma.challenge.findUnique({
      where: { id: challengeId },
      include: {
        dailyTasks: { where: { completed: true } },
        streaks: true
      }
    })

    if (challenge && challenge.streaks.length > 0) {
      const completedDays = challenge.dailyTasks
        .map((d: any) => d.dayNumber)
        .sort((a: number, b: number) => a - b)

      let streak = 0
      for (let i = completedDays.length - 1; i >= 0; i--) {
        if (i === completedDays.length - 1 || completedDays[i] === completedDays[i + 1] - 1) {
          streak++
        } else {
          break
        }
      }

      await prisma.streak.update({
        where: { id: challenge.streaks[0].id },
        data: { length: streak, lastUpdated: new Date() }
      })
    }

    const progress = Math.min(((updatedEntry.value || 0) / 2.0) * 100, 100)

    res.json({
      success: true,
      waterEntry: updatedEntry,
      progress
    })
  } catch (error) {
    console.error('Error logging water:', error)
    res.status(500).json({ error: 'Failed to log water' })
  }
})

export default router
