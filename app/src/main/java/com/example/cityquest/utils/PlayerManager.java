package com.example.cityquest.utils;

import android.content.Context;

import androidx.media3.exoplayer.ExoPlayer;

public class PlayerManager {
    private static ExoPlayer exoPlayer;

    public static ExoPlayer getInstance(Context context) {
        if (exoPlayer == null) {
            exoPlayer = new ExoPlayer.Builder(context).build();
        }
        return exoPlayer;
    }

    public static void releasePlayer() {
        if (exoPlayer != null) {
            exoPlayer.stop();          // Stop playback
            exoPlayer.release();       // Release resources
            exoPlayer = null;
        }
    }
}

