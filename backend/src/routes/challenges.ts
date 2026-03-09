import { Router } from 'express'
import { randomUUID } from 'crypto'
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

    const durationInt = parseInt(duration.toString())
    if (isNaN(durationInt)) {
      return res.status(400).json({ success: false, error: 'Invalid duration provided' })
    }
    
    // Validate duration range (min 7 days, max 30 days)
    if (durationInt < 7 || durationInt > 30) {
      return res.status(400).json({ success: false, error: 'Duration must be between 7 and 30 days' })
    }

    // Ensure user exists in database
    const user = await prisma.user.findUnique({ where: { id: userId } })
    if (!user) {
      // Auto-create user with a placeholder email if not exists
      await prisma.user.create({
        data: {
          id: userId,
          email: `user_${userId}@rise30.app`
        }
      })
    }

    const startDate = new Date()
    const endDate = new Date()
    endDate.setDate(endDate.getDate() + durationInt)

    const challenge = await prisma.challenge.create({
      data: {
        userId,
        name,
        description,
        type: type || 'custom',
        category: category || 'personal',
        duration: durationInt,
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
    for (let i = 1; i <= durationInt; i++) {
      const date = new Date(startDate)
      date.setDate(date.getDate() + (i - 1))
      
      dailyEntries.push({
        id: randomUUID(),
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
        id: randomUUID(),
        userId,
        challengeId: challenge.id,
        length: 0
      }
    })

    res.json({ success: true, challenge })
  } catch (error: any) {
    console.error('Error creating challenge:', error)
    console.error('Error message:', error.message)
    console.error('Error code:', error.code)
    res.status(500).json({ success: false, error: 'Failed to create challenge', details: error.message })
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
    const { userId, tzOffset } = req.body

    // Verify challenge belongs to user
    const challenge = await prisma.challenge.findFirst({
      where: { id: challengeId, userId }
    })

    if (!challenge) {
      return res.status(404).json({ success: false, error: 'Challenge not found or access denied' })
    }

    // Parse timezone offset (e.g., "+05:30" for India)
    const parseOffset = (offset: string): number => {
      const sign = offset.startsWith('-') ? -1 : 1
      const parts = offset.replace(/^[+-]/, '').split(':')
      const hours = parseInt(parts[0]) || 0
      const minutes = parseInt(parts[1]) || 0
      return sign * (hours * 60 + minutes) * 60 * 1000
    }
    
    const offsetMs = tzOffset ? parseOffset(tzOffset as string) : 0
    
    // Calculate current day based on local timezone
    const now = new Date()
    const localNow = new Date(now.getTime() + offsetMs)
    const startDate = new Date(challenge.startDate)
    const localStart = new Date(startDate.getTime() + offsetMs)
    
    // Reset both to midnight for accurate day calculation
    localNow.setHours(0, 0, 0, 0)
    localStart.setHours(0, 0, 0, 0)
    
    const currentDay = Math.min(
      Math.floor((localNow.getTime() - localStart.getTime()) / (1000 * 60 * 60 * 24)) + 1,
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

// Update challenge
router.put('/:challengeId', async (req, res) => {
  try {
    const { challengeId } = req.params
    const { userId, name, description, duration, category, color } = req.body

    const challenge = await prisma.challenge.findFirst({
      where: { id: challengeId, userId }
    })

    if (!challenge) {
      return res.status(404).json({ success: false, error: 'Challenge not found or access denied' })
    }

    const durationInt = parseInt(duration?.toString())
    if (duration && isNaN(durationInt)) {
      return res.status(400).json({ success: false, error: 'Invalid duration provided' })
    }

    const updatedChallenge = await prisma.challenge.update({
      where: { id: challengeId },
      data: {
        name: name !== undefined ? name : challenge.name,
        description: description !== undefined ? description : challenge.description,
        duration: durationInt !== undefined ? durationInt : challenge.duration,
        category: category !== undefined ? category : challenge.category,
        color: color !== undefined ? color : challenge.color,
      }
    })

    // If duration was changed, sync daily entries
    if (durationInt !== undefined && durationInt !== challenge.duration) {
      if (durationInt < challenge.duration) {
        // Delete extra entries
        await prisma.dailyEntry.deleteMany({
          where: {
            challengeId,
            dayNumber: { gt: durationInt }
          }
        })
      } else if (durationInt > challenge.duration) {
        // Create missing entries
        const newEntries = []
        for (let i = challenge.duration + 1; i <= durationInt; i++) {
          const date = new Date(challenge.startDate)
          date.setDate(date.getDate() + (i - 1))
          
          newEntries.push({
            id: randomUUID(),
            userId: challenge.userId,
            challengeId,
            dayNumber: i,
            date,
            completed: false
          })
        }
        if (newEntries.length > 0) {
          await prisma.dailyEntry.createMany({ data: newEntries })
        }
      }
      
      // Update end date
      const newEndDate = new Date(challenge.startDate)
      newEndDate.setDate(newEndDate.getDate() + durationInt)
      await prisma.challenge.update({
        where: { id: challengeId },
        data: { endDate: newEndDate }
      })
    }

    res.json({ success: true, challenge: updatedChallenge })
  } catch (error) {
    console.error('Error updating challenge:', error)
    res.status(500).json({ success: false, error: 'Failed to update challenge' })
  }
})

// Delete challenge
router.delete('/:challengeId', async (req, res) => {
  try {
    const { challengeId } = req.params
    const { userId } = req.query

    if (!userId) {
       return res.status(400).json({ success: false, error: 'userId parameter is required' })
    }

    const challenge = await prisma.challenge.findFirst({
      where: { id: challengeId, userId: userId as string }
    })

    if (!challenge) {
      return res.status(404).json({ success: false, error: 'Challenge not found or access denied' })
    }

    // Delete related records manually to simulate Cascade delete
    await prisma.dailyEntry.deleteMany({ where: { challengeId } })
    await prisma.waterEntry.deleteMany({ where: { challengeId } })
    await prisma.streak.deleteMany({ where: { challengeId } })

    await prisma.challenge.delete({
      where: { id: challengeId }
    })

    res.json({ success: true })
  } catch (error) {
    console.error('Error deleting challenge:', error)
    res.status(500).json({ success: false, error: 'Failed to delete challenge' })
  }
})

export default router
