"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.storageRouter = void 0;
const supabase_1 = require("./supabase");
const express_1 = require("express");
const BUCKET_NAME = "rise30-assets"; // create this bucket in Supabase Storage
exports.storageRouter = (0, express_1.Router)();
// Simple signed URL generator for a file path
exports.storageRouter.get("/signed-url", async (req, res) => {
    const path = req.query.path;
    if (!path) {
        return res.status(400).json({ error: "Missing 'path' query param" });
    }
    const { data, error } = await supabase_1.supabase.storage
        .from(BUCKET_NAME)
        .createSignedUrl(path, 60 * 10); // 10 minutes
    if (error || !data) {
        return res.status(500).json({ error: error?.message ?? "Failed to create signed URL" });
    }
    return res.json({ url: data.signedUrl });
});
