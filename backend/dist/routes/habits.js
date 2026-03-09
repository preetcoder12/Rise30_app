"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const express_1 = require("express");
const supabaseClient_1 = require("../supabaseClient");
const router = (0, express_1.Router)();
// Helper to parse timezone offset string (e.g., "+05:30" or "-08:00")
function parseTimezoneOffset(offset) {
    const sign = offset.startsWith('-') ? -1 : 1;
    const parts = offset.replace(/^[+-]/, '').split(':');
    const hours = parseInt(parts[0]) || 0;
    const minutes = parseInt(parts[1]) || 0;
    return sign * (hours * 60 + minutes) * 60 * 1000;
}
// Get daily habits for a user - returns habits completed within last 24 hours
router.get('/:userId', async (req, res) => {
    try {
        const userId = req.params.userId;
        // Get all completed habits for this user from last 48 hours
        const cutoff = new Date(Date.now() - 48 * 60 * 60 * 1000);
        const habits = await supabaseClient_1.prisma.dailyHabit.findMany({
            where: {
                userId,
                completed: true,
                completedAt: {
                    gte: cutoff
                }
            },
            orderBy: {
                completedAt: 'desc'
            }
        });
        // Filter to only return habits completed within last 24 hours
        const twentyFourHoursAgo = Date.now() - 24 * 60 * 60 * 1000;
        const activeHabits = habits.filter(h => {
            const completedTime = new Date(h.completedAt).getTime();
            return completedTime >= twentyFourHoursAgo;
        });
        res.json({ success: true, habits: activeHabits });
    }
    catch (error) {
        console.error('Error fetching habits:', error);
        res.status(500).json({ success: false, error: 'Failed to fetch habits' });
    }
});
// Toggle a habit (create if not exists for today)
router.post('/toggle', async (req, res) => {
    try {
        const { userId, name, completed } = req.body;
        const today = new Date();
        today.setHours(0, 0, 0, 0);
        const habit = await supabaseClient_1.prisma.dailyHabit.upsert({
            where: {
                userId_name_date: {
                    userId,
                    name,
                    date: today
                }
            },
            update: {
                completed,
                completedAt: completed ? new Date() : null
            },
            create: {
                userId,
                name,
                completed,
                completedAt: completed ? new Date() : null,
                date: today
            }
        });
        res.json({ success: true, habit });
    }
    catch (error) {
        console.error('Error toggling habit:', error);
        res.status(500).json({ success: false, error: 'Failed to toggle habit' });
    }
});
exports.default = router;
