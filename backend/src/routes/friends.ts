import { Router, Request, Response } from 'express'
import { prisma } from '../supabaseClient'

const router = Router()

// Send friend request or add directly
router.post('/request', async (req: Request, res: Response) => {
  try {
    const { userId, friendId } = req.body

    if (userId === friendId) {
      return res.status(400).json({ success: false, error: "Cannot add yourself" })
    }

    const friendship = await prisma.friendship.upsert({
      where: {
        userId_friendId: { userId, friendId }
      },
      update: {
        status: 'pending'
      },
      create: {
        userId,
        friendId,
        status: 'pending'
      }
    })

    res.json({ success: true, friendship })
  } catch (error) {
    console.error('Error sending request:', error)
    res.status(500).json({ success: false, error: 'Failed to send request' })
  }
})

// Accept friend request
router.post('/accept', async (req: Request, res: Response) => {
  try {
    const { userId, friendId } = req.body

    // Update the request where current user is friendId
    const friendship = await prisma.friendship.update({
      where: {
        userId_friendId: { userId: friendId, friendId: userId }
      },
      data: {
        status: 'accepted'
      }
    })

    // Create reciprocal friendship or just manage it this way?
    // Let's create reciprocal for easier querying
    await prisma.friendship.upsert({
      where: {
        userId_friendId: { userId: userId, friendId: friendId }
      },
      update: {
        status: 'accepted'
      },
      create: {
        userId: userId,
        friendId: friendId,
        status: 'accepted'
      }
    })

    res.json({ success: true, friendship })
  } catch (error) {
    console.error('Error accepting request:', error)
    res.status(500).json({ success: false, error: 'Failed to accept request' })
  }
})

// Get friends list
router.get('/list/:userId', async (req: Request, res: Response) => {
  try {
    const userId = req.params.userId as string
    const friendships = await prisma.friendship.findMany({
      where: {
        userId,
        status: 'accepted'
      },
      include: {
        friend: {
          select: {
            id: true,
            displayName: true,
            email: true,
            avatarUrl: true
          }
        }
      }
    })

    res.json({ success: true, friends: friendships.map(f => f.friend) })
  } catch (error) {
    console.error('Error fetching friends:', error)
    res.status(500).json({ success: false, error: 'Failed to fetch friends' })
  }
})

// Get status between two users
router.get('/status/:userId/:friendId', async (req: Request, res: Response) => {
  try {
    const userId = req.params.userId as string
    const friendId = req.params.friendId as string
    
    // Check if user sent request
    const friendship = await prisma.friendship.findUnique({
      where: {
        userId_friendId: { userId, friendId }
      }
    })
    
    // Check if friend sent request to user
    const reverseFriendship = await prisma.friendship.findUnique({
      where: {
        userId_friendId: { userId: friendId, friendId: userId }
      }
    })

    if (friendship && friendship.status === 'accepted') {
        res.json({ success: true, status: 'accepted' })
    } else if (friendship && friendship.status === 'pending') {
        res.json({ success: true, status: 'pending' })
    } else if (reverseFriendship && reverseFriendship.status === 'pending') {
        res.json({ success: true, status: 'received' })
    } else {
        res.json({ success: true, status: 'none' })
    }
  } catch (error) {
    res.status(500).json({ success: false, error: 'Failed' })
  }
})

// Get pending receiving requests
router.get('/pending/:userId', async (req: Request, res: Response) => {
  try {
    const userId = req.params.userId as string
    const pendingRequests = await prisma.friendship.findMany({
      where: {
        friendId: userId,
        status: 'pending'
      },
      include: {
        user: { // the person who sent the request
          select: {
            id: true,
            displayName: true,
            email: true,
            avatarUrl: true
          }
        }
      }
    })

    res.json({ success: true, requests: pendingRequests.map(f => f.user) })
  } catch (error) {
    console.error('Error fetching pending requests:', error)
    res.status(500).json({ success: false, error: 'Failed' })
  }
})

export default router

