import { createClient } from '@supabase/supabase-js'
import { PrismaClient } from '@prisma/client'

const supabaseUrl = process.env.SUPABASE_URL || ''
const supabaseKey = process.env.SUPABASE_SERVICE_KEY || ''

export const supabase = createClient(supabaseUrl, supabaseKey)
export const prisma = new PrismaClient()

// Helper to sync user from Supabase Auth to our database
export async function syncUser(supabaseUserId: string, email: string) {
  const user = await prisma.user.upsert({
    where: { email },
    update: {},
    create: {
      id: supabaseUserId,
      email,
    },
  })
  return user
}
