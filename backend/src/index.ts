import express, { Request, Response } from "express";
import cors from "cors";
import helmet from "helmet";
import morgan from "morgan";
import dotenv from "dotenv";
<<<<<<< HEAD
import { PrismaClient } from "@prisma/client";
import { storageRouter } from "./storageRoutes";
import { streakRouter } from "./streakRoutes";
=======
import { prisma } from "./supabaseClient";
import challengesRouter from "./routes/challenges";
import waterChallengeRouter from "./routes/waterChallenge";
import usersRouter from "./routes/users";
>>>>>>> 5376e4c04b5b0e6f10f16eb23b1028ae8562da77

dotenv.config();

const app = express();

app.use(helmet());
app.use(cors({ origin: true, credentials: true }));
app.use(express.json());
app.use(morgan("dev"));

app.get("/health", (_req: Request, res: Response) => {
  res.json({ status: "ok", timestamp: new Date().toISOString() });
});

<<<<<<< HEAD
app.use("/storage", storageRouter);
app.use("/api/streaks", streakRouter);
=======
// Production API routes
app.use("/api/challenges", challengesRouter);
app.use("/api/water-challenge", waterChallengeRouter);
app.use("/api/users", usersRouter);

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
>>>>>>> 5376e4c04b5b0e6f10f16eb23b1028ae8562da77

const PORT = process.env.PORT || 4000;

app.listen(PORT, () => {
  console.log(`Rise30 backend listening on port ${PORT}`);
});

process.on("SIGINT", async () => {
  await prisma.$disconnect();
  process.exit(0);
});

