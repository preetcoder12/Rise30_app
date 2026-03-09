"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.prisma = exports.supabase = void 0;
exports.syncUser = syncUser;
const supabase_js_1 = require("@supabase/supabase-js");
const client_1 = require("../prisma/client");
const supabaseUrl = process.env.SUPABASE_URL || '';
const supabaseKey = process.env.SUPABASE_SERVICE_ROLE_KEY || '';
exports.supabase = (0, supabase_js_1.createClient)(supabaseUrl, supabaseKey);
exports.prisma = new client_1.PrismaClient();
// Helper to sync user from Supabase Auth to our database
async function syncUser(supabaseUserId, email) {
    const user = await exports.prisma.user.upsert({
        where: { email },
        update: {},
        create: {
            id: supabaseUserId,
            email,
        },
    });
    return user;
}
