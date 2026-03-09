"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
const express_1 = __importDefault(require("express"));
const cors_1 = __importDefault(require("cors"));
const helmet_1 = __importDefault(require("helmet"));
const morgan_1 = __importDefault(require("morgan"));
const dotenv_1 = __importDefault(require("dotenv"));
const supabaseClient_1 = require("./supabaseClient");
const storageRoutes_1 = require("./storageRoutes");
const streakRoutes_1 = require("./streakRoutes");
const challenges_1 = __importDefault(require("./routes/challenges"));
const waterChallenge_1 = __importDefault(require("./routes/waterChallenge"));
const users_1 = __importDefault(require("./routes/users"));
const habits_1 = __importDefault(require("./routes/habits"));
const friends_1 = __importDefault(require("./routes/friends"));
const auth_1 = __importDefault(require("./routes/auth"));
dotenv_1.default.config();
const app = (0, express_1.default)();
app.use((0, helmet_1.default)());
app.use((0, cors_1.default)({ origin: true, credentials: true }));
app.use(express_1.default.json());
app.use((0, morgan_1.default)("dev"));
app.get("/health", (_req, res) => {
    res.json({ status: "ok", timestamp: new Date().toISOString() });
});
// Routes
app.use("/storage", storageRoutes_1.storageRouter);
app.use("/api/streaks", streakRoutes_1.streakRouter);
app.use("/api/challenges", challenges_1.default);
app.use("/api/water-challenge", waterChallenge_1.default);
app.use("/api/users", users_1.default);
app.use("/api/habits", habits_1.default);
app.use("/api/friends", friends_1.default);
app.use("/api/auth", auth_1.default);
app.get("/api/motivation", (_req, res) => {
    const quotes = [
        { text: "The crossroad is where you find your true strength.", author: "Rise30" },
        { text: "Consistency is more important than intensity.", author: "Bruce Lee" },
        { text: "Small steps every day lead to big results.", author: "Anonymous" },
        { text: "The only bad workout is the one that didn't happen.", author: "Unknown" },
        { text: "Deep dive into your potential.", author: "Rise30" }
    ];
    const quote = quotes[Math.floor(Math.random() * quotes.length)];
    res.json({ success: true, quote });
});
app.get("/api/deep-dive", (_req, res) => {
    const deepDives = [
        {
            title: "The Science of Habit Stacking",
            content: "Learn how to build bulletproof routines by anchoring new habits to current ones.",
            category: "Productivity",
            readTime: "3 min"
        },
        {
            title: "Mindfulness and the Prefrontal Cortex",
            content: "How 10 minutes of silence reshapes your brain for better focus and less stress.",
            category: "Mindfulness",
            readTime: "5 min"
        },
        {
            title: "Optimal Hydration for Brain Peak",
            content: "Why even 1% dehydration can drop your cognitive performance by up to 20%.",
            category: "Health",
            readTime: "4 min"
        }
    ];
    const deepDive = deepDives[Math.floor(Math.random() * deepDives.length)];
    res.json({ success: true, deepDive });
});
// Auth handled by authRouter
const PORT = process.env.PORT || 4000;
app.listen(PORT, () => {
    console.log(`Rise30 backend listening on port ${PORT}`);
});
process.on("SIGINT", async () => {
    await supabaseClient_1.prisma.$disconnect();
    process.exit(0);
});
