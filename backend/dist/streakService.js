"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.getOrCreateStreak = getOrCreateStreak;
exports.calculateExpectedDay = calculateExpectedDay;
exports.isWithinRecoveryWindow = isWithinRecoveryWindow;
exports.getStreakStatus = getStreakStatus;
exports.completeDay = completeDay;
exports.recoverMissedDay = recoverMissedDay;
exports.getDailyProgress = getDailyProgress;
exports.checkAndUpdateStreak = checkAndUpdateStreak;
const client_1 = require("../prisma/client");
const crypto_1 = __importDefault(require("crypto"));
const prisma = new client_1.PrismaClient();
// Constants for forgiveness mechanics
const MAX_FREEZES_PER_CHALLENGE = 2;
const RECOVERY_WINDOW_HOURS = 48;
const MAX_MISSED_DAYS_BEFORE_RESET = 3;
/**
 * Get or create a streak for a user-challenge pair
 */
async function getOrCreateStreak(userId, challengeId) {
    // Use findFirst with userId and challengeId until unique constraint migration is applied
    let streaks = await prisma.streak.findMany({
        where: { userId, challengeId },
        take: 1,
    });
    let streak = streaks[0];
    if (!streak) {
        streak = await prisma.streak.create({
            data: {
                userId,
                challengeId,
                length: 0,
                lastUpdated: new Date(),
            },
        });
        // Initialize forgiveness fields
        streak.missedDays = 0;
        streak.freezesUsed = 0;
        streak.isActive = true;
        streak.lastCompletedDay = 0;
    }
    else {
        // Ensure forgiveness fields have defaults
        streak.missedDays = streak.missedDays ?? 0;
        streak.freezesUsed = streak.freezesUsed ?? 0;
        streak.isActive = streak.isActive ?? true;
        streak.lastCompletedDay = streak.lastCompletedDay ?? 0;
    }
    return streak;
}
/**
 * Calculate expected day number based on challenge start date
 */
function calculateExpectedDay(challenge) {
    const now = new Date();
    const start = new Date(challenge.startDate);
    const diffTime = now.getTime() - start.getTime();
    const diffDays = Math.floor(diffTime / (1000 * 60 * 60 * 24));
    return Math.max(1, diffDays + 1);
}
/**
 * Check if user is within recovery window for a missed day
 */
function isWithinRecoveryWindow(lastUpdated) {
    const now = new Date();
    const diffHours = (now.getTime() - lastUpdated.getTime()) / (1000 * 60 * 60);
    return diffHours <= RECOVERY_WINDOW_HOURS;
}
/**
 * Get the current streak status with forgiveness options
 */
async function getStreakStatus(userId, challengeId) {
    const streak = await getOrCreateStreak(userId, challengeId);
    const challenge = await prisma.challenge.findUnique({
        where: { id: challengeId },
    });
    if (!challenge)
        return null;
    const expectedDay = calculateExpectedDay(challenge);
    const daysBehind = expectedDay - streak.lastCompletedDay - 1;
    // Calculate recovery options
    const canRecover = daysBehind === 1 &&
        streak.missedDays < MAX_MISSED_DAYS_BEFORE_RESET &&
        isWithinRecoveryWindow(streak.lastUpdated);
    const canUseFreeze = streak.freezesUsed < MAX_FREEZES_PER_CHALLENGE &&
        daysBehind === 1;
    const freezesRemaining = MAX_FREEZES_PER_CHALLENGE - streak.freezesUsed;
    const isStreakBroken = streak.missedDays >= MAX_MISSED_DAYS_BEFORE_RESET ||
        (daysBehind > 1 && !canUseFreeze);
    // Calculate recovery deadline
    let recoveryDeadline = null;
    if (canRecover) {
        recoveryDeadline = new Date(streak.lastUpdated);
        recoveryDeadline.setHours(recoveryDeadline.getHours() + RECOVERY_WINDOW_HOURS);
    }
    // Generate appropriate message
    let message = "";
    if (daysBehind === 0) {
        message = "You're on track! Keep it up!";
    }
    else if (canRecover) {
        message = `You missed Day ${streak.lastCompletedDay + 1}. Complete it now to restore your streak!`;
    }
    else if (canUseFreeze) {
        message = `Your streak freeze protected your ${streak.length}-day streak!`;
    }
    else if (isStreakBroken) {
        message = "Your streak was reset, but your challenge continues. Keep going!";
    }
    return {
        streak,
        canRecover,
        canUseFreeze,
        freezesRemaining,
        daysMissed: daysBehind,
        isStreakBroken,
        recoveryDeadline,
        message,
    };
}
/**
 * Mark a day as complete with streak logic
 */
async function completeDay(userId, challengeId, dayNumber) {
    const streak = await getOrCreateStreak(userId, challengeId);
    const challenge = await prisma.challenge.findUnique({
        where: { id: challengeId },
    });
    if (!challenge) {
        return {
            success: false,
            streakLength: streak.length,
            usedFreeze: false,
            recovered: false,
            message: "Challenge not found",
        };
    }
    const expectedDay = calculateExpectedDay(challenge);
    const daysBehind = expectedDay - streak.lastCompletedDay - 1;
    // Check if this is a recovery (completing a missed day)
    const isRecovery = dayNumber === streak.lastCompletedDay + 1 && daysBehind > 0;
    // Check if we need to use a freeze
    let usedFreeze = false;
    if (daysBehind === 1 && streak.freezesUsed < MAX_FREEZES_PER_CHALLENGE) {
        // Auto-use freeze for single missed day
        usedFreeze = true;
        const newFreezesUsed = (streak.freezesUsed || 0) + 1;
        await prisma.$executeRaw `
      UPDATE "Streak" 
      SET 
        "freezesUsed" = ${newFreezesUsed},
        "lastUpdated" = ${new Date().toISOString()}
      WHERE id = ${streak.id}
    `;
    }
    // Calculate new streak length
    let newStreakLength = streak.length;
    if (isRecovery || usedFreeze) {
        // Streak continues
        newStreakLength += 1;
    }
    else if (daysBehind === 0) {
        // Normal progression
        newStreakLength += 1;
    }
    else {
        // Streak broken, start over
        newStreakLength = 1;
    }
    // Update or create daily entry using raw query to handle schema changes
    const existingEntry = await prisma.dailyEntry.findFirst({
        where: { userId, challengeId, dayNumber },
    });
    const now = new Date();
    if (existingEntry) {
        await prisma.dailyEntry.update({
            where: { id: existingEntry.id },
            data: {
                completed: true,
                completedAt: now,
            },
        });
    }
    else {
        // Use raw query to avoid schema mismatch issues
        await prisma.$executeRaw `
      INSERT INTO "DailyEntry" (id, "userId", "challengeId", "dayNumber", completed, "completedAt")
      VALUES (${crypto_1.default.randomUUID()}, ${userId}, ${challengeId}, ${dayNumber}, true, ${now.toISOString()})
    `;
    }
    // Update streak with forgiveness fields using raw query
    const updatedStreak = await prisma.$queryRaw `
    UPDATE "Streak" 
    SET 
      length = ${newStreakLength},
      "lastCompletedDay" = ${dayNumber},
      "lastUpdated" = ${new Date().toISOString()},
      "missedDays" = ${isRecovery ? streak.missedDays : 0},
      "isActive" = true
    WHERE id = ${streak.id}
    RETURNING *
  `;
    // Generate success message
    let message = "";
    if (usedFreeze) {
        message = `🔥 Day ${dayNumber} complete! Your streak freeze protected your ${newStreakLength}-day streak!`;
    }
    else if (isRecovery) {
        message = `🎉 Streak recovered! You're back on track with ${newStreakLength} days!`;
    }
    else if (newStreakLength === 1 && streak.length > 0) {
        message = `Day ${dayNumber} complete! Starting a new streak - keep going!`;
    }
    else {
        message = `🔥 Day ${dayNumber} complete! ${newStreakLength}-day streak!`;
    }
    return {
        success: true,
        streakLength: newStreakLength,
        usedFreeze,
        recovered: isRecovery,
        message,
    };
}
/**
 * Manually recover a missed day (user clicks "Recover Streak")
 */
async function recoverMissedDay(userId, challengeId) {
    const status = await getStreakStatus(userId, challengeId);
    if (!status) {
        return { success: false, message: "Challenge not found", streakLength: 0 };
    }
    if (!status.canRecover) {
        return {
            success: false,
            message: "Recovery window has expired or not eligible for recovery",
            streakLength: status.streak.length,
        };
    }
    const missedDayNumber = status.streak.lastCompletedDay + 1;
    // Complete the missed day
    const result = await completeDay(userId, challengeId, missedDayNumber);
    return {
        success: result.success,
        message: result.message,
        streakLength: result.streakLength,
    };
}
/**
 * Get daily progress for a challenge
 */
async function getDailyProgress(userId, challengeId, duration) {
    const entries = await prisma.dailyEntry.findMany({
        where: { userId, challengeId },
        orderBy: { dayNumber: "asc" },
    });
    const progress = [];
    for (let i = 1; i <= duration; i++) {
        const entry = entries.find((e) => e.dayNumber === i);
        progress.push({
            dayNumber: i,
            completed: entry?.completed ?? false,
            completedAt: entry?.completedAt ?? null,
        });
    }
    return progress;
}
/**
 * Check and update streak status (call this when user opens app)
 * Returns true if streak was broken
 */
async function checkAndUpdateStreak(userId, challengeId) {
    const streak = await getOrCreateStreak(userId, challengeId);
    const challenge = await prisma.challenge.findUnique({
        where: { id: challengeId },
    });
    if (!challenge) {
        return {
            streakBroken: false,
            daysMissed: 0,
            message: "",
            canRecover: false,
        };
    }
    const expectedDay = calculateExpectedDay(challenge);
    const daysBehind = expectedDay - streak.lastCompletedDay - 1;
    // If no days missed, nothing to do
    if (daysBehind <= 0) {
        return {
            streakBroken: false,
            daysMissed: 0,
            message: "",
            canRecover: false,
        };
    }
    // Update missed days count using raw query
    await prisma.$executeRaw `
    UPDATE "Streak" 
    SET 
      "missedDays" = ${daysBehind},
      "lastUpdated" = ${new Date().toISOString()}
    WHERE id = ${streak.id}
  `;
    const canRecover = daysBehind === 1 &&
        streak.missedDays < MAX_MISSED_DAYS_BEFORE_RESET &&
        isWithinRecoveryWindow(streak.lastUpdated);
    const streakBroken = daysBehind > MAX_MISSED_DAYS_BEFORE_RESET;
    let message = "";
    if (canRecover) {
        message = `You missed Day ${streak.lastCompletedDay + 1}. Complete it now to restore your ${streak.length}-day streak!`;
    }
    else if (daysBehind === 1 && streak.freezesUsed < MAX_FREEZES_PER_CHALLENGE) {
        message = `Your streak freeze will protect your streak when you complete today's task!`;
    }
    else if (streakBroken) {
        message = `Your ${streak.length}-day streak was reset after ${daysBehind} missed days. Your challenge continues!`;
    }
    return {
        streakBroken,
        daysMissed: daysBehind,
        message,
        canRecover,
    };
}
