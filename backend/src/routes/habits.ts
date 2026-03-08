import { Router, Request, Response } from 'express'
import { prisma } from '../supabaseClient'

const router = Router()

// Get daily habits for a user on a specific date (default today)
router.get('/:userId', async (req: Request, res: Response) => {
  try {
    const userId = req.params.userId as string
    const dateStr = req.query.date as string || new Date().toISOString().split('T')[0]
    
    // Create date range for the day
    const start = new Date(dateStr)
    start.setHours(0, 0, 0, 0)
    const end = new Date(dateStr)
    end.setHours(23, 59, 59, 999)

    const habits = await prisma.dailyHabit.findMany({
      where: {
        userId,
        date: {
          gte: start,
          lte: end
        }
      }
    })

    res.json({ success: true, habits })
  } catch (error) {
    console.error('Error fetching habits:', error)
    res.status(500).json({ success: false, error: 'Failed to fetch habits' })
  }
})

// Toggle a habit (create if not exists for today)
router.post('/toggle', async (req: Request, res: Response) => {
  try {
    const { userId, name, completed } = req.body
    const today = new Date()
    today.setHours(0, 0, 0, 0)

    const habit = await prisma.dailyHabit.upsert({
      where: {
        userId_name_date: {
          userId,
          name,
          date: today
        }
      },
      update: {
        completed
      },
      create: {
        userId,
        name,
        completed,
        date: today
      }
    })

    res.json({ success: true, habit })
  } catch (error) {
    console.error('Error toggling habit:', error)
    res.status(500).json({ success: false, error: 'Failed to toggle habit' })
  }
})

export default router
