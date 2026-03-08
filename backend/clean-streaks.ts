import { PrismaClient } from '@prisma/client';

const prisma = new PrismaClient();

async function cleanDuplicates() {
  await prisma.$executeRawUnsafe(`
    DELETE FROM "Streak" 
    WHERE id NOT IN (
      SELECT MAX(id) 
      FROM "Streak" 
      GROUP BY "userId", "challengeId"
    )
  `);
  console.log("Cleanup complete");
}

cleanDuplicates()
  .catch(console.error)
  .finally(() => prisma.$disconnect());
