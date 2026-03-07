import { Router, Request, Response } from 'express'
import { prisma } from '../supabaseClient'

const router = Router()

// Get user profile with stats
router.get('/:userId/profile', async (req: Request, res: Response) => {
  try {
    const userId = req.params.userId as string

    const user = await prisma.user.findUnique({
      where: { id: userId },
      include: {
        challenges: {
          include: {
            dailyTasks: true,
            streaks: true
          }
        }
      }
    })

    if (!user) {
      return res.status(404).json({ success: false, error: 'User not found' })
    }

    // Calculate overall stats
    const totalChallenges = user.challenges.length
    const activeChallenges = user.challenges.filter(c => c.isActive).length
    const completedChallenges = user.challenges.filter(c => 
      c.dailyTasks.every(d => d.completed)
    ).length
    
    const totalCompletedDays = user.challenges.reduce((acc, c) => 
      acc + c.dailyTasks.filter(d => d.completed).length, 0
    )
    
    const longestStreak = Math.max(...user.challenges.map(c => 
      c.streaks[0]?.length || 0
    ), 0)

    res.json({
      success: true,
      profile: {
        id: user.id,
        email: user.email,
        createdAt: user.createdAt,
        stats: {
          totalChallenges,
          activeChallenges,
          completedChallenges,
          totalCompletedDays,
          longestStreak
        }
      }
    })
  } catch (error) {
    console.error('Error fetching user profile:', error)
    res.status(500).json({ success: false, error: 'Failed to fetch profile' })
  }
})

// Get user analytics/dashboard data
router.get('/:userId/analytics', async (req: Request, res: Response) => {
  try {
    const userId = req.params.userId as string

    const challenges = await prisma.challenge.findMany({
      where: { userId },
      include: {
        dailyTasks: true,
        streaks: true
      }
    })

    // Calculate category breakdown
    const categoryStats: Record<string, { count: number; completed: number }> = {}
    challenges.forEach(c => {
      if (!categoryStats[c.category]) {
        categoryStats[c.category] = { count: 0, completed: 0 }
      }
      categoryStats[c.category].count++
      const completedDays = c.dailyTasks.filter(d => d.completed).length
      if (completedDays === c.duration) {
        categoryStats[c.category].completed++
      }
    })

    // Calculate weekly progress
    const last7Days = Array.from({ length: 7 }, (_, i) => {
      const date = new Date()
      date.setDate(date.getDate() - i)
      date.setHours(0, 0, 0, 0)
      return date
    }).reverse()

    const weeklyProgress = last7Days.map(date => {
      const completedCount = challenges.reduce((acc, challenge) => {
        const dayEntry = challenge.dailyTasks.find(d => {
          const entryDate = new Date(d.date)
          entryDate.setHours(0, 0, 0, 0)
          return entryDate.getTime() === date.getTime() && d.completed
        })
        return acc + (dayEntry ? 1 : 0)
      }, 0)
      
      return {
        date: date.toISOString().split('T')[0],
        completed: completedCount,
        total: challenges.length
      }
    })

    // Calculate completion rate
    const totalDays = challenges.reduce((acc, c) => acc + c.duration, 0)
    const completedDays = challenges.reduce((acc, c) => 
      acc + c.dailyTasks.filter(d => d.completed).length, 0
    )
    const completionRate = totalDays > 0 ? Math.round((completedDays / totalDays) * 100) : 0

    res.json({
      success: true,
      analytics: {
        totalChallenges: challenges.length,
        activeChallenges: challenges.filter(c => c.isActive).length,
        completedChallenges: challenges.filter(c => 
          c.dailyTasks.every(d => d.completed)
        ).length,
        completionRate,
        categoryBreakdown: categoryStats,
        weeklyProgress,
        longestStreak: Math.max(...challenges.map(c => c.streaks[0]?.length || 0), 0),
        currentStreaks: challenges.map(c => ({
          challengeId: c.id,
          challengeName: c.name,
          streak: c.streaks[0]?.length || 0
        }))
      }
    })
  } catch (error) {
    console.error('Error fetching analytics:', error)
    res.status(500).json({ success: false, error: 'Failed to fetch analytics' })
  }
})

// Update user profile
router.put('/:userId/profile', async (req: Request, res: Response) => {
  try {
    const userId = req.params.userId as string
    const { displayName, avatarUrl } = req.body

    // Note: Add these fields to User model if needed
    const user = await prisma.user.update({
      where: { id: userId },
      data: {}
    })

    res.json({ success: true, user })
  } catch (error) {
    console.error('Error updating profile:', error)
    res.status(500).json({ success: false, error: 'Failed to update profile' })
  }
})

// Get user achievements/badges
router.get('/:userId/achievements', async (req: Request, res: Response) => {
  try {
    const userId = req.params.userId as string

    const challenges = await prisma.challenge.findMany({
      where: { userId },
      include: {
        dailyTasks: true,
        streaks: true
      }
    })

    const achievements = []

    // First Challenge
    if (challenges.length >= 1) {
      achievements.push({
        id: 'first_challenge',
        name: 'Getting Started',
        description: 'Created your first challenge',
        icon: '🎯',
        unlockedAt: challenges[0]?.createdAt
      })
    }

    // Challenge Master (5+ challenges)
    if (challenges.length >= 5) {
      achievements.push({
        id: 'challenge_master',
        name: 'Challenge Master',
        description: 'Created 5 challenges',
        icon: '👑',
        unlockedAt: new Date()
      })
    }

    // Streak Warrior (7+ day streak)
    const maxStreak = Math.max(...challenges.map(c => c.streaks[0]?.length || 0), 0)
    if (maxStreak >= 7) {
      achievements.push({
        id: 'streak_warrior',
        name: 'Streak Warrior',
        description: 'Achieved a 7-day streak',
        icon: '🔥',
        unlockedAt: new Date()
      })
    }

    // Perfect Week (all days completed in a week)
    const completedChallenges = challenges.filter(c => 
      c.dailyTasks.every(d => d.completed)
    )
    if (completedChallenges.length >= 1) {
      achievements.push({
        id: 'perfect_challenge',
        name: 'Perfectionist',
        description: 'Completed a challenge 100%',
        icon: '⭐',
        unlockedAt: completedChallenges[0]?.dailyTasks.find(d => d.completedAt)?.completedAt
      })
    }

    // Water Champion (water challenge completed)
    const waterChallenge = challenges.find(c => c.type === 'water')
    if (waterChallenge?.dailyTasks.every(d => d.completed)) {
      achievements.push({
        id: 'water_champion',
        name: 'Hydration Hero',
        description: 'Completed the 30-day water challenge',
        icon: '💧',
        unlockedAt: waterChallenge.dailyTasks.find(d => d.completedAt)?.completedAt
      })
    }

    res.json({ success: true, achievements })
  } catch (error) {
    console.error('Error fetching achievements:', error)
    res.status(500).json({ success: false, error: 'Failed to fetch achievements' })
  }
})

export default router
