import { Router } from 'express'
import { PrismaClient } from '../../prisma/client'

const router = Router()
const prisma = new PrismaClient()

// Save onboarding data
router.post('/:userId', async (req, res) => {
  const { userId } = req.params
  const { goal, challengeType, motivation, difficulty, challengeName, dailyTarget, reminderTime, startDate } = req.body

  console.log('[Onboarding POST] Saving onboarding data for user:', userId)

  try {
    // Check if user exists
    const user = await prisma.user.findUnique({
      where: { id: userId }
    })

    if (!user) {
      console.log('[Onboarding POST] User not found:', userId)
      res.status(404).json({ success: false, error: 'User not found' })
      return
    }

    // Upsert onboarding data (create or update)
    const onboarding = await prisma.onboardingData.upsert({
      where: { userId },
      update: {
        goal,
        challengeType,
        motivation,
        difficulty,
        challengeName,
        dailyTarget,
        reminderTime,
        startDate,
        updatedAt: new Date()
      },
      create: {
        userId,
        goal,
        challengeType,
        motivation,
        difficulty,
        challengeName,
        dailyTarget,
        reminderTime,
        startDate
      }
    })

    console.log('[Onboarding POST] Saved successfully:', onboarding.id)
    res.json({ success: true, onboarding })
  } catch (error: any) {
    console.error('[Onboarding POST] Error:', error)
    res.status(500).json({ success: false, error: 'Failed to save onboarding data', details: error.message })
  }
})

// Get onboarding data for recommendations
router.get('/:userId', async (req, res) => {
  const { userId } = req.params

  console.log('[Onboarding GET] Fetching onboarding data for user:', userId)

  try {
    const onboarding = await prisma.onboardingData.findUnique({
      where: { userId }
    })

    if (!onboarding) {
      console.log('[Onboarding GET] No onboarding data found for user:', userId)
      res.status(404).json({ success: false, error: 'Onboarding data not found' })
      return
    }

    console.log('[Onboarding GET] Found onboarding data:', onboarding.id)
    res.json({ success: true, onboarding })
  } catch (error: any) {
    console.error('[Onboarding GET] Error:', error)
    res.status(500).json({ success: false, error: 'Failed to fetch onboarding data', details: error.message })
  }
})

// Get recommendations based on onboarding data
router.get('/:userId/recommendations', async (req, res) => {
  const { userId } = req.params

  console.log('[Onboarding Recommendations] Getting recommendations for user:', userId)

  try {
    const onboarding = await prisma.onboardingData.findUnique({
      where: { userId }
    })

    if (!onboarding) {
      res.status(404).json({ success: false, error: 'Onboarding data not found' })
      return
    }

    // Generate recommendations based on goal and difficulty
    const recommendations = generateRecommendations(onboarding)

    res.json({ success: true, recommendations })
  } catch (error: any) {
    console.error('[Onboarding Recommendations] Error:', error)
    res.status(500).json({ success: false, error: 'Failed to generate recommendations', details: error.message })
  }
})

// Helper function to generate recommendations
function generateRecommendations(onboarding: any) {
  const { goal, difficulty, motivation } = onboarding
  
  const recommendations: any = {
    challenges: [],
    tips: [],
    reminderStrategy: ''
  }

  // Goal-based challenge recommendations
  switch (goal) {
    case 'Fitness':
      recommendations.challenges = [
        { name: '30 Day Pushup Challenge', icon: '💪', target: '50 pushups' },
        { name: 'Morning Run Streak', icon: '🏃', target: '2km daily' },
        { name: 'Plank Master', icon: '🧘', target: '5 min plank' }
      ]
      recommendations.tips = ['Start with warm-ups', 'Track your reps', 'Rest days are important']
      break
    case 'Study':
      recommendations.challenges = [
        { name: 'Deep Work Sessions', icon: '📚', target: '2 hours daily' },
        { name: 'Learn Something New', icon: '🎓', target: '30 min learning' },
        { name: 'Reading Challenge', icon: '📖', target: '20 pages daily' }
      ]
      recommendations.tips = ['Use Pomodoro technique', 'Eliminate distractions', 'Review notes weekly']
      break
    case 'Mental Health':
      recommendations.challenges = [
        { name: 'Daily Meditation', icon: '🧘', target: '10 minutes' },
        { name: 'Gratitude Journal', icon: '📔', target: '3 things daily' },
        { name: 'Digital Detox', icon: '📵', target: '1 hour offline' }
      ]
      recommendations.tips = ['Practice mindfulness', 'Get enough sleep', 'Connect with nature']
      break
    case 'Productivity':
      recommendations.challenges = [
        { name: 'Early Bird', icon: '🌅', target: 'Wake up at 6 AM' },
        { name: 'Task Completion', icon: '✅', target: '3 main tasks' },
        { name: 'No Procrastination', icon: '⚡', target: 'Zero snooze' }
      ]
      recommendations.tips = ['Plan your day', 'Prioritize tasks', 'Use time blocking']
      break
    default:
      recommendations.challenges = [
        { name: 'Morning Ritual', icon: '🌞', target: 'Complete all habits' },
        { name: 'Consistency King', icon: '👑', target: '30 day streak' },
        { name: 'Self Improvement', icon: '🌱', target: 'Daily growth' }
      ]
  }

  // Difficulty-based adjustments
  switch (difficulty) {
    case 'Beginner':
      recommendations.tips.push('Start small - consistency beats intensity')
      recommendations.reminderStrategy = 'Gentle reminders twice daily'
      break
    case 'Intermediate':
      recommendations.tips.push('Push your limits gradually')
      recommendations.reminderStrategy = 'Regular reminders with progress updates'
      break
    case 'Advanced':
      recommendations.tips.push('Challenge yourself daily')
      recommendations.reminderStrategy = 'Intensive tracking with accountability'
      break
  }

  return recommendations
}

export default router
