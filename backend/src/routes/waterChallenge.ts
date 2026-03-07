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
    start.setHours(0, 0, 0, 0)
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

    // Create daily entries with normalized dates (midnight UTC)
    const dailyEntries = []
    for (let i = 1; i <= 30; i++) {
      const date = new Date(start)
      date.setDate(date.getDate() + (i - 1))
      date.setHours(0, 0, 0, 0)
      dailyEntries.push({
        userId,
        challengeId: challenge.id,
        dayNumber: i,
        date,
        completed: false,
        value: 0
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

    // Calculate current day number
    const startDate = new Date(challenge.startDate)
    startDate.setHours(0, 0, 0, 0)
    const currentDay = Math.min(
      Math.floor((Date.now() - startDate.getTime()) / (1000 * 60 * 60 * 24)) + 1,
      30
    )

    // Find today's entry by day number (more reliable than date comparison)
    const todayEntry = challenge.dailyTasks.find((entry: any) => entry.dayNumber === currentDay)

    const completedDays = challenge.dailyTasks.filter((e: any) => e.completed).length
    const currentStreak = challenge.streaks[0]?.length || 0

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
    
    // Verify challenge belongs to user
    const challengeVerify = await prisma.challenge.findFirst({
      where: { id: challengeId, userId }
    })
    
    if (!challengeVerify) {
      return res.status(403).json({ error: 'Challenge not found or access denied' })
    }
    
    const now = new Date()
    
    // Calculate current day number based on challenge start date
    const challenge = await prisma.challenge.findUnique({
      where: { id: challengeId }
    })
    
    if (!challenge) {
      return res.status(404).json({ error: 'Challenge not found' })
    }
    
    const startDate = new Date(challenge.startDate)
    startDate.setHours(0, 0, 0, 0)
    const currentDayNumber = Math.min(
      Math.floor((now.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24)) + 1,
      challenge.duration
    )

    // Find existing entry by day number (most reliable)
    let existingEntry = await prisma.dailyEntry.findFirst({
      where: {
        userId,
        challengeId,
        dayNumber: currentDayNumber
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
      const todayDate = new Date()
      todayDate.setHours(0, 0, 0, 0)
      updatedEntry = await prisma.dailyEntry.create({
        data: {
          userId,
          challengeId,
          dayNumber: currentDayNumber,
          date: todayDate,
          value: amount,
          completed,
          completedAt: completed ? new Date() : null
        }
      })
    }

    // Update streak
    const challengeWithStreak = await prisma.challenge.findUnique({
      where: { id: challengeId },
      include: {
        dailyTasks: { where: { completed: true } },
        streaks: true
      }
    })

    if (challengeWithStreak && challengeWithStreak.streaks.length > 0) {
      const completedDays = challengeWithStreak.dailyTasks
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
        where: { id: challengeWithStreak.streaks[0].id },
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
