import { Router, Request, Response } from 'express'
import { prisma } from '../supabaseClient'
import bcrypt from 'bcrypt'
import jwt from 'jsonwebtoken'

const router = Router()
const JWT_SECRET = process.env.JWT_SECRET || 'rise30_secret_key'

// REGISTER
router.post('/register', async (req: Request, res: Response) => {
  try {
    const { email, password, displayName } = req.body

    const existingUser = await prisma.user.findUnique({ where: { email } })
    if (existingUser) {
      return res.status(400).json({ success: false, error: 'User already exists' })
    }

    const hashedPassword = await bcrypt.hash(password, 10)
    const user = await prisma.user.create({
      data: {
        email,
        password: hashedPassword,
        displayName,
        provider: 'email'
      }
    })

    const token = jwt.sign({ userId: user.id, email: user.email }, JWT_SECRET, { expiresIn: '30d' })

    res.json({ success: true, user: { id: user.id, email: user.email, displayName: user.displayName }, token })
  } catch (error) {
    console.error('Registration error:', error)
    res.status(500).json({ success: false, error: 'Failed to register' })
  }
})

// LOGIN
router.post('/login', async (req: Request, res: Response) => {
  try {
    const { email, password } = req.body

    const user = await prisma.user.findUnique({ where: { email } })
    if (!user || user.provider !== 'email' || !user.password) {
      return res.status(401).json({ success: false, error: 'Invalid email or password' })
    }

    const isMatch = await bcrypt.compare(password, user.password)
    if (!isMatch) {
      return res.status(401).json({ success: false, error: 'Invalid email or password' })
    }

    const token = jwt.sign({ userId: user.id, email: user.email }, JWT_SECRET, { expiresIn: '30d' })

    res.json({ success: true, user: { id: user.id, email: user.email, displayName: user.displayName }, token })
  } catch (error) {
    res.status(500).json({ success: false, error: 'Login failed' })
  }
})

// SYNC (for Google login in app)
router.post('/sync', async (req: Request, res: Response) => {
  try {
    const { userId, email, displayName } = req.body
    
    const user = await prisma.user.upsert({
      where: { email },
      update: {
        id: userId, // Ensure ID matches Supabase
        displayName: displayName || undefined
      },
      create: { 
        id: userId, 
        email, 
        displayName,
        provider: 'google'
      }
    })
    
    res.json({ success: true, user })
  } catch (error) {
    console.error("Error syncing user:", error)
    res.status(500).json({ success: false, error: "Failed to sync user" })
  }
})

export default router
