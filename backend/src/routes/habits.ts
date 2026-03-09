import { Router, Request, Response } from 'express'
import { prisma } from '../supabaseClient'

const router = Router()

// Helper to parse timezone offset string (e.g., "+05:30" or "-08:00")
function parseTimezoneOffset(offset: string): number {
  const sign = offset.startsWith('-') ? -1 : 1
  const parts = offset.replace(/^[+-]/, '').split(':')
  const hours = parseInt(parts[0]) || 0
  const minutes = parseInt(parts[1]) || 0
  return sign * (hours * 60 + minutes) * 60 * 1000
}

// Get daily habits for a user on a specific date (default today)
router.get('/:userId', async (req: Request, res: Response) => {
  try {
    const userId = req.params.userId as string
    // Get timezone offset from query (e.g., "+05:30" for India)
    const tzOffset = req.query.tz as string || '+00:00'
    const dateStr = req.query.date as string
    
    // Calculate today's date based on timezone offset
    const now = new Date()
    const offsetMs = parseTimezoneOffset(tzOffset)
    const localNow = new Date(now.getTime() + offsetMs)
    const effectiveDateStr = dateStr || localNow.toISOString().split('T')[0]
    
    // Create date range for the day in UTC (compensating for timezone)
    const start = new Date(effectiveDateStr + 'T00:00:00.000Z')
    start.setTime(start.getTime() - offsetMs)
    const end = new Date(effectiveDateStr + 'T23:59:59.999Z')
    end.setTime(end.getTime() - offsetMs)

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
