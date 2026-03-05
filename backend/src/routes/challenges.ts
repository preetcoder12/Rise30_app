import { Router } from 'express'
import { prisma } from '../supabaseClient'

const router = Router()

// Get all challenges for a user
router.get('/user/:userId', async (req, res) => {
  try {
    const { userId } = req.params
    
    const challenges = await prisma.challenge.findMany({
      where: { userId },
      include: {
        dailyTasks: {
          orderBy: { dayNumber: 'asc' }
        },
        streaks: true,
        _count: {
          select: { dailyTasks: { where: { completed: true } } }
        }
      },
      orderBy: { createdAt: 'desc' }
    })

    const today = new Date()
    today.setHours(0, 0, 0, 0)
    
    const formattedChallenges = challenges.map(c => {
      // Calculate current day number
      const startDate = new Date(c.startDate)
      startDate.setHours(0, 0, 0, 0)
      const currentDayNumber = Math.min(
        Math.floor((today.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24)) + 1,
        c.duration
      )
      
      // Check if today's task is completed
      const todayEntry = c.dailyTasks.find(d => d.dayNumber === currentDayNumber)
      const isTodayCompleted = todayEntry?.completed || false
      
      return {
        id: c.id,
        name: c.name,
        description: c.description,
        type: c.type,
        category: c.category,
        duration: c.duration,
        startDate: c.startDate,
        endDate: c.endDate,
        targetValue: c.targetValue,
        unit: c.unit,
        color: c.color,
        icon: c.icon,
        isActive: c.isActive,
        progress: {
          completedDays: c._count.dailyTasks,
          totalDays: c.duration,
          percentage: Math.round((c._count.dailyTasks / c.duration) * 100),
          currentStreak: c.streaks[0]?.length || 0,
          isTodayCompleted,
          currentDayNumber
        }
      }
    })

    res.json({ success: true, challenges: formattedChallenges })
  } catch (error) {
    console.error('Error fetching challenges:', error)
    res.status(500).json({ success: false, error: 'Failed to fetch challenges' })
  }
})

// Create a new challenge
router.post('/', async (req, res) => {
  try {
    const {
      userId,
      name,
      description,
      type,
      category,
      duration,
      targetValue,
      unit,
      color,
      icon
    } = req.body

    // Ensure user exists in database
    const user = await prisma.user.findUnique({ where: { id: userId } })
    if (!user) {
      // Auto-create user with a placeholder email if not exists
      await prisma.user.create({
        data: {
          id: userId,
          email: `user_${userId.substring(0, 8)}@rise30.app`
        }
      })
    }

    const startDate = new Date()
    const endDate = new Date()
    endDate.setDate(endDate.getDate() + duration)

    const challenge = await prisma.challenge.create({
      data: {
        userId,
        name,
        description,
        type: type || 'custom',
        category: category || 'personal',
        duration,
        startDate,
        endDate,
        targetValue,
        unit,
        color,
        icon,
        isActive: true
      }
    })

    // Create daily entries for the challenge
    const dailyEntries = []
    for (let i = 1; i <= duration; i++) {
      const date = new Date(startDate)
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

    // Create initial streak
    await prisma.streak.create({
      data: {
        userId,
        challengeId: challenge.id,
        length: 0
      }
    })

    res.json({ success: true, challenge })
  } catch (error) {
    console.error('Error creating challenge:', error)
    res.status(500).json({ success: false, error: 'Failed to create challenge' })
  }
})

// Get challenge details with all daily entries
router.get('/:challengeId', async (req, res) => {
  try {
    const { challengeId } = req.params

    const challenge = await prisma.challenge.findUnique({
      where: { id: challengeId },
      include: {
        dailyTasks: {
          orderBy: { dayNumber: 'asc' }
        },
        streaks: true
      }
    })

    if (!challenge) {
      return res.status(404).json({ success: false, error: 'Challenge not found' })
    }

    const completedDays = challenge.dailyTasks.filter(d => d.completed).length
    const currentDay = Math.min(
      Math.floor((Date.now() - new Date(challenge.startDate).getTime()) / (1000 * 60 * 60 * 24)) + 1,
      challenge.duration
    )

    res.json({
      success: true,
      challenge: {
        ...challenge,
        progress: {
          completedDays,
          totalDays: challenge.duration,
          percentage: Math.round((completedDays / challenge.duration) * 100),
          currentDay,
          currentStreak: challenge.streaks[0]?.length || 0
        }
      }
    })
  } catch (error) {
    console.error('Error fetching challenge:', error)
    res.status(500).json({ success: false, error: 'Failed to fetch challenge' })
  }
})

// Toggle day completion
router.post('/:challengeId/day/:dayNumber/toggle', async (req, res) => {
  try {
    const { challengeId, dayNumber } = req.params
    const { userId, completed } = req.body

    // Verify challenge belongs to user first
    const challengeVerify = await prisma.challenge.findFirst({
      where: { id: challengeId, userId }
    })
    
    if (!challengeVerify) {
      return res.status(403).json({ success: false, error: 'Challenge not found or access denied' })
    }

    const updatedEntry = await prisma.dailyEntry.updateMany({
      where: {
        challengeId,
        userId,
        dayNumber: parseInt(dayNumber)
      },
      data: {
        completed,
        completedAt: completed ? new Date() : null
      }
    })

    // Update streak - use verified challenge
    const challenge = await prisma.challenge.findUnique({
      where: { id: challengeId },
      include: {
        dailyTasks: { where: { completed: true } },
        streaks: true
      }
    })

    if (challenge && challenge.streaks.length > 0) {
      const completedDays = challenge.dailyTasks
        .filter(d => d.completed)
        .map(d => d.dayNumber)
        .sort((a, b) => a - b)

      let currentStreak = 0
      for (let i = completedDays.length - 1; i >= 0; i--) {
        if (i === completedDays.length - 1 || completedDays[i] === completedDays[i + 1] - 1) {
          currentStreak++
        } else {
          break
        }
      }

      await prisma.streak.update({
        where: { id: challenge.streaks[0].id },
        data: { length: currentStreak, lastUpdated: new Date() }
      })
    }

    res.json({ success: true, updated: updatedEntry.count })
  } catch (error) {
    console.error('Error toggling day:', error)
    res.status(500).json({ success: false, error: 'Failed to toggle day' })
  }
})

// Mark today as complete
router.post('/:challengeId/mark-today', async (req, res) => {
  try {
    const { challengeId } = req.params
    const { userId } = req.body

    // Verify challenge belongs to user
    const challenge = await prisma.challenge.findFirst({
      where: { id: challengeId, userId }
    })

    if (!challenge) {
      return res.status(404).json({ success: false, error: 'Challenge not found or access denied' })
    }

    const currentDay = Math.min(
      Math.floor((Date.now() - new Date(challenge.startDate).getTime()) / (1000 * 60 * 60 * 24)) + 1,
      challenge.duration
    )

    await prisma.dailyEntry.updateMany({
      where: {
        challengeId,
        userId,
        dayNumber: currentDay
      },
      data: {
        completed: true,
        completedAt: new Date()
      }
    })

    res.json({ success: true, dayNumber: currentDay })
  } catch (error) {
    console.error('Error marking today:', error)
    res.status(500).json({ success: false, error: 'Failed to mark today' })
  }
})

// Delete challenge
router.delete('/:challengeId', async (req, res) => {
  try {
    const { challengeId } = req.params
    const { userId } = req.body

    await prisma.challenge.deleteMany({
      where: { id: challengeId, userId }
    })

    res.json({ success: true })
  } catch (error) {
    console.error('Error deleting challenge:', error)
    res.status(500).json({ success: false, error: 'Failed to delete challenge' })
  }
})

export default router
