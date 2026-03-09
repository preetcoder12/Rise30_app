"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.streakRouter = void 0;
const express_1 = require("express");
const streakService_1 = require("./streakService");
const router = (0, express_1.Router)();
exports.streakRouter = router;
/**
 * GET /api/streaks/:userId/:challengeId/status
 * Get current streak status with forgiveness options
 */
router.get("/:userId/:challengeId/status", async (req, res) => {
    try {
        const userId = Array.isArray(req.params.userId) ? req.params.userId[0] : req.params.userId;
        const challengeId = Array.isArray(req.params.challengeId) ? req.params.challengeId[0] : req.params.challengeId;
        const status = await (0, streakService_1.getStreakStatus)(userId, challengeId);
        if (!status) {
            return res.status(404).json({ error: "Challenge not found" });
        }
        res.json({
            streak: {
                length: status.streak.length,
                missedDays: status.streak.missedDays,
                freezesUsed: status.streak.freezesUsed,
                freezesRemaining: status.freezesRemaining,
                lastCompletedDay: status.streak.lastCompletedDay,
                isActive: status.streak.isActive,
                lastUpdated: status.streak.lastUpdated,
            },
            forgiveness: {
                canRecover: status.canRecover,
                canUseFreeze: status.canUseFreeze,
                daysMissed: status.daysMissed,
                isStreakBroken: status.isStreakBroken,
                recoveryDeadline: status.recoveryDeadline,
            },
            message: status.message,
        });
    }
    catch (error) {
        console.error("Error getting streak status:", error);
        res.status(500).json({ error: "Failed to get streak status" });
    }
});
/**
 * POST /api/streaks/:userId/:challengeId/complete
 * Mark a day as complete
 */
router.post("/:userId/:challengeId/complete", async (req, res) => {
    try {
        const userId = Array.isArray(req.params.userId) ? req.params.userId[0] : req.params.userId;
        const challengeId = Array.isArray(req.params.challengeId) ? req.params.challengeId[0] : req.params.challengeId;
        const { dayNumber } = req.body;
        if (!dayNumber || typeof dayNumber !== "number") {
            return res.status(400).json({ error: "dayNumber is required" });
        }
        const result = await (0, streakService_1.completeDay)(userId, challengeId, dayNumber);
        res.json(result);
    }
    catch (error) {
        console.error("Error completing day:", error);
        res.status(500).json({ error: "Failed to complete day" });
    }
});
/**
 * POST /api/streaks/:userId/:challengeId/recover
 * Recover a missed day (user clicks "Recover Streak")
 */
router.post("/:userId/:challengeId/recover", async (req, res) => {
    try {
        const userId = Array.isArray(req.params.userId) ? req.params.userId[0] : req.params.userId;
        const challengeId = Array.isArray(req.params.challengeId) ? req.params.challengeId[0] : req.params.challengeId;
        const result = await (0, streakService_1.recoverMissedDay)(userId, challengeId);
        res.json(result);
    }
    catch (error) {
        console.error("Error recovering streak:", error);
        res.status(500).json({ error: "Failed to recover streak" });
    }
});
/**
 * GET /api/streaks/:userId/:challengeId/progress
 * Get daily progress for a challenge
 */
router.get("/:userId/:challengeId/progress", async (req, res) => {
    try {
        const userId = Array.isArray(req.params.userId) ? req.params.userId[0] : req.params.userId;
        const challengeId = Array.isArray(req.params.challengeId) ? req.params.challengeId[0] : req.params.challengeId;
        const duration = parseInt(req.query.duration) || 30;
        const progress = await (0, streakService_1.getDailyProgress)(userId, challengeId, duration);
        res.json({ progress });
    }
    catch (error) {
        console.error("Error getting progress:", error);
        res.status(500).json({ error: "Failed to get progress" });
    }
});
/**
 * POST /api/streaks/:userId/:challengeId/check
 * Check and update streak status (call when user opens app)
 */
router.post("/:userId/:challengeId/check", async (req, res) => {
    try {
        const userId = Array.isArray(req.params.userId) ? req.params.userId[0] : req.params.userId;
        const challengeId = Array.isArray(req.params.challengeId) ? req.params.challengeId[0] : req.params.challengeId;
        const result = await (0, streakService_1.checkAndUpdateStreak)(userId, challengeId);
        res.json(result);
    }
    catch (error) {
        console.error("Error checking streak:", error);
        res.status(500).json({ error: "Failed to check streak" });
    }
});
