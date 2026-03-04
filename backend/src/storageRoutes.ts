import type { Request, Response, Router } from "express";
import { supabase } from "./supabase";
import { Router as createRouter } from "express";

const BUCKET_NAME = "rise30-assets"; // create this bucket in Supabase Storage

export const storageRouter: Router = createRouter();

// Simple signed URL generator for a file path
storageRouter.get("/signed-url", async (req: Request, res: Response) => {
  const path = req.query.path as string | undefined;

  if (!path) {
    return res.status(400).json({ error: "Missing 'path' query param" });
  }

  const { data, error } = await supabase.storage
    .from(BUCKET_NAME)
    .createSignedUrl(path, 60 * 10); // 10 minutes

  if (error || !data) {
    return res.status(500).json({ error: error?.message ?? "Failed to create signed URL" });
  }

  return res.json({ url: data.signedUrl });
});

