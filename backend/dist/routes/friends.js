"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const express_1 = require("express");
const supabaseClient_1 = require("../supabaseClient");
const router = (0, express_1.Router)();
// Send friend request or add directly
router.post('/request', async (req, res) => {
    try {
        const { userId, friendId } = req.body;
        if (userId === friendId) {
            return res.status(400).json({ success: false, error: "Cannot add yourself" });
        }
        const friendship = await supabaseClient_1.prisma.friendship.upsert({
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
        });
        res.json({ success: true, friendship });
    }
    catch (error) {
        console.error('Error sending request:', error);
        res.status(500).json({ success: false, error: 'Failed to send request' });
    }
});
// Accept friend request
router.post('/accept', async (req, res) => {
    try {
        const { userId, friendId } = req.body;
        // Update the request where current user is friendId
        const friendship = await supabaseClient_1.prisma.friendship.update({
            where: {
                userId_friendId: { userId: friendId, friendId: userId }
            },
            data: {
                status: 'accepted'
            }
        });
        // Create reciprocal friendship or just manage it this way?
        // Let's create reciprocal for easier querying
        await supabaseClient_1.prisma.friendship.upsert({
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
        });
        res.json({ success: true, friendship });
    }
    catch (error) {
        console.error('Error accepting request:', error);
        res.status(500).json({ success: false, error: 'Failed to accept request' });
    }
});
// Get friends list
router.get('/list/:userId', async (req, res) => {
    try {
        const userId = req.params.userId;
        const friendships = await supabaseClient_1.prisma.friendship.findMany({
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
        });
        res.json({ success: true, friends: friendships.map(f => f.friend) });
    }
    catch (error) {
        console.error('Error fetching friends:', error);
        res.status(500).json({ success: false, error: 'Failed to fetch friends' });
    }
});
// Get status between two users
router.get('/status/:userId/:friendId', async (req, res) => {
    try {
        const userId = req.params.userId;
        const friendId = req.params.friendId;
        // Check if user sent request
        const friendship = await supabaseClient_1.prisma.friendship.findUnique({
            where: {
                userId_friendId: { userId, friendId }
            }
        });
        // Check if friend sent request to user
        const reverseFriendship = await supabaseClient_1.prisma.friendship.findUnique({
            where: {
                userId_friendId: { userId: friendId, friendId: userId }
            }
        });
        if (friendship && friendship.status === 'accepted') {
            res.json({ success: true, status: 'accepted' });
        }
        else if (friendship && friendship.status === 'pending') {
            res.json({ success: true, status: 'pending' });
        }
        else if (reverseFriendship && reverseFriendship.status === 'pending') {
            res.json({ success: true, status: 'received' });
        }
        else {
            res.json({ success: true, status: 'none' });
        }
    }
    catch (error) {
        res.status(500).json({ success: false, error: 'Failed' });
    }
});
// Get pending receiving requests
router.get('/pending/:userId', async (req, res) => {
    try {
        const userId = req.params.userId;
        const pendingRequests = await supabaseClient_1.prisma.friendship.findMany({
            where: {
                friendId: userId,
                status: 'pending'
            },
            include: {
                user: {
                    select: {
                        id: true,
                        displayName: true,
                        email: true,
                        avatarUrl: true
                    }
                }
            }
        });
        res.json({ success: true, requests: pendingRequests.map(f => f.user) });
    }
    catch (error) {
        console.error('Error fetching pending requests:', error);
        res.status(500).json({ success: false, error: 'Failed' });
    }
});
exports.default = router;
