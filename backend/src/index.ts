import express, { Request, Response } from "express";
import cors from "cors";
import helmet from "helmet";
import morgan from "morgan";
import dotenv from "dotenv";
import { prisma } from "./supabaseClient";
import { storageRouter } from "./storageRoutes";
import { streakRouter } from "./streakRoutes";
import challengesRouter from "./routes/challenges";
import waterChallengeRouter from "./routes/waterChallenge";
import usersRouter from "./routes/users";
import habitsRouter from "./routes/habits";

dotenv.config();

const app = express();

app.use(helmet());
app.use(cors({ origin: true, credentials: true }));
app.use(express.json());
app.use(morgan("dev"));

app.get("/health", (_req: Request, res: Response) => {
  res.json({ status: "ok", timestamp: new Date().toISOString() });
});

// Routes
app.use("/storage", storageRouter);
app.use("/api/streaks", streakRouter);
app.use("/api/challenges", challengesRouter);
app.use("/api/water-challenge", waterChallengeRouter);
app.use("/api/users", usersRouter);
app.use("/api/habits", habitsRouter);

app.get("/api/motivation", (_req: Request, res: Response) => {
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

app.get("/api/deep-dive", (_req: Request, res: Response) => {
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


// User sync endpoint
app.post("/api/auth/sync", async (req: Request, res: Response) => {
  try {
    const { userId, email } = req.body;
    
    const user = await prisma.user.upsert({
      where: { email },
      update: {},
      create: { id: userId, email }
    });
    
    res.json({ success: true, user });
  } catch (error) {
    console.error("Error syncing user:", error);
    res.status(500).json({ success: false, error: "Failed to sync user" });
  }
});

const PORT = process.env.PORT || 4000;

app.listen(PORT, () => {
  console.log(`Rise30 backend listening on port ${PORT}`);
});

process.on("SIGINT", async () => {
  await prisma.$disconnect();
  process.exit(0);
});
