/*
  Warnings:

  - A unique constraint covering the columns `[userId,challengeId,dayNumber]` on the table `DailyEntry` will be added. If there are existing duplicate values, this will fail.
  - A unique constraint covering the columns `[userId,challengeId,date]` on the table `DailyEntry` will be added. If there are existing duplicate values, this will fail.
  - Added the required column `category` to the `Challenge` table without a default value. This is not possible if the table is not empty.
  - Added the required column `updatedAt` to the `Challenge` table without a default value. This is not possible if the table is not empty.
  - Added the required column `date` to the `DailyEntry` table without a default value. This is not possible if the table is not empty.

*/
-- AlterTable
ALTER TABLE "Challenge" ADD COLUMN     "category" TEXT NOT NULL,
ADD COLUMN     "color" TEXT,
ADD COLUMN     "createdAt" TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP,
ADD COLUMN     "description" TEXT,
ADD COLUMN     "icon" TEXT,
ADD COLUMN     "isActive" BOOLEAN NOT NULL DEFAULT true,
ADD COLUMN     "targetValue" DOUBLE PRECISION,
ADD COLUMN     "unit" TEXT,
ADD COLUMN     "updatedAt" TIMESTAMP(3) NOT NULL;

-- AlterTable
ALTER TABLE "DailyEntry" ADD COLUMN     "date" TIMESTAMP(3) NOT NULL,
ADD COLUMN     "notes" TEXT,
ADD COLUMN     "value" DOUBLE PRECISION;

-- CreateTable
CREATE TABLE "WaterEntry" (
    "id" TEXT NOT NULL,
    "userId" TEXT NOT NULL,
    "challengeId" TEXT NOT NULL,
    "date" TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "amount" DOUBLE PRECISION NOT NULL,
    "targetAmount" DOUBLE PRECISION NOT NULL,
    "completed" BOOLEAN NOT NULL DEFAULT false,
    "createdAt" TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updatedAt" TIMESTAMP(3) NOT NULL,

    CONSTRAINT "WaterEntry_pkey" PRIMARY KEY ("id")
);

-- CreateIndex
CREATE UNIQUE INDEX "WaterEntry_userId_challengeId_date_key" ON "WaterEntry"("userId", "challengeId", "date");

-- CreateIndex
CREATE UNIQUE INDEX "DailyEntry_userId_challengeId_dayNumber_key" ON "DailyEntry"("userId", "challengeId", "dayNumber");

-- CreateIndex
CREATE UNIQUE INDEX "DailyEntry_userId_challengeId_date_key" ON "DailyEntry"("userId", "challengeId", "date");

-- AddForeignKey
ALTER TABLE "WaterEntry" ADD CONSTRAINT "WaterEntry_userId_fkey" FOREIGN KEY ("userId") REFERENCES "User"("id") ON DELETE RESTRICT ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE "WaterEntry" ADD CONSTRAINT "WaterEntry_challengeId_fkey" FOREIGN KEY ("challengeId") REFERENCES "Challenge"("id") ON DELETE RESTRICT ON UPDATE CASCADE;
