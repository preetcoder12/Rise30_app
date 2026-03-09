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
        // Calculate 24 hours ago
        const twentyFourHoursAgo = new Date(Date.now() - 24 * 60 * 60 * 1000);
        console.log(`[Habits GET] userId=${userId}, 24h ago=${twentyFourHoursAgo.toISOString()}`);
        // Get all habits for this user that were completed within last 24 hours
        // Use updatedAt as fallback if completedAt is not set (for backwards compatibility)
        const habits = await supabaseClient_1.prisma.dailyHabit.findMany({
            where: {
                userId,
                completed: true,
                OR: [
                    { completedAt: { gte: twentyFourHoursAgo } },
                    { completedAt: null, updatedAt: { gte: twentyFourHoursAgo } }
                ]
            },
            orderBy: {
                completedAt: 'desc'
            }
        });
        console.log(`[Habits GET] Found ${habits.length} habits:`, habits.map(h => ({ name: h.name, completedAt: h.completedAt, updatedAt: h.updatedAt })));
        res.json({ success: true, habits });
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
        console.log(`[Habits TOGGLE] userId=${userId}, name=${name}, completed=${completed}`);
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
        console.log(`[Habits TOGGLE] Saved habit:`, { id: habit.id, name: habit.name, completed: habit.completed, completedAt: habit.completedAt });
        res.json({ success: true, habit });
    }
    catch (error) {
        console.error('Error toggling habit:', error);
        console.error('Error code:', error.code);
        console.error('Error meta:', error.meta);
        res.status(500).json({ success: false, error: 'Failed to toggle habit', details: error.message });
    }
});
exports.default = router;
