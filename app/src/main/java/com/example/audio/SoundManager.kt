package com.example.audio

import android.content.Context
import android.media.MediaPlayer
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class SoundManager(private val context: Context) {

    private var vibrator: Vibrator? = try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
    } catch (e: Exception) {
        null
    }

    var isSfxEnabled: Boolean = true
    var isMusicEnabled: Boolean = true
    var isVibrationEnabled: Boolean = true

    private var mediaPlayer: MediaPlayer? = null
    private var currentTrackResId: Int = 0
    private var isPlayingGameplayTrack: Boolean = false

    private val scope = CoroutineScope(Dispatchers.Default)

    /**
     * Dynamically finds raw audio resource IDs by resource name.
     * This avoids compile-time dependencies on specific files and ensures
     * no old/deleted audio resources are referenced in code.
     */
    private fun getRawResId(resName: String): Int {
        return try {
            context.resources.getIdentifier(resName, "raw", context.packageName)
        } catch (e: Exception) {
            0
        }
    }

    /**
     * Returns the raw resource ID for the main menu background music.
     */
    private fun getMenuMusicResId(): Int {
        val candidates = listOf("menu_theme", "menu_music", "menu", "bg_menu", "theme_menu")
        for (name in candidates) {
            val resId = getRawResId(name)
            if (resId != 0) return resId
        }
        return 0
    }

    /**
     * Returns list of raw resource IDs for gameplay background music tracks.
     */
    private fun getGameplayMusicResIds(): List<Int> {
        val tracks = mutableListOf<Int>()
        val names = listOf(
            "game_theme_1", "game_theme_2", "game_theme_3",
            "gameplay_music", "game_music", "bg_game", "gameplay_1", "gameplay_2"
        )
        for (name in names) {
            val resId = getRawResId(name)
            if (resId != 0 && !tracks.contains(resId)) {
                tracks.add(resId)
            }
        }
        return tracks
    }

    private var currentGameplayIndex = 0

    fun triggerVibration(durationMs: Long = 25) {
        if (!isVibrationEnabled) return
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(durationMs)
            }
        } catch (e: Exception) {
            // Ignore
        }
    }

    // --- SFX METHODS ---

    fun playTap() {
        if (!isSfxEnabled) return
        triggerVibration(15)
    }

    fun playMove() {
        if (!isSfxEnabled) return
        // Tile moves are clean and soft (vibration feedback only, no synthetic beep sounds)
        triggerVibration(12)
    }

    fun playMerge(value: Int = 4) {
        if (!isSfxEnabled) return
        triggerVibration(25)
    }

    fun playBigMerge() {
        if (!isSfxEnabled) return
        triggerVibration(50)
    }

    fun playVictory() {
        if (!isSfxEnabled) return
        triggerVibration(100)
    }

    fun playGameOver() {
        if (!isSfxEnabled) return
        triggerVibration(80)
    }

    fun playUndo() {
        if (!isSfxEnabled) return
        triggerVibration(15)
    }

    fun playAchievement() {
        if (!isSfxEnabled) return
        triggerVibration(60)
    }

    // --- MUSIC METHODS ---

    /**
     * Starts background music based on state (Menu vs Gameplay).
     * If no real audio file exists in res/raw/, music remains completely silent
     * and NEVER generates procedural beep or synth sounds.
     */
    fun startMusic(isGameplay: Boolean = false) {
        if (!isMusicEnabled) {
            stopMusic()
            return
        }

        val targetResId = if (isGameplay) {
            val gameplayTracks = getGameplayMusicResIds()
            if (gameplayTracks.isEmpty()) 0 else gameplayTracks[currentGameplayIndex % gameplayTracks.size]
        } else {
            getMenuMusicResId()
        }

        if (targetResId == 0) {
            // No real audio file present in project: stop and remain silent
            stopMusic()
            return
        }

        if (mediaPlayer != null && currentTrackResId == targetResId && isPlayingGameplayTrack == isGameplay) {
            try {
                if (mediaPlayer?.isPlaying == false) {
                    mediaPlayer?.start()
                }
            } catch (e: Exception) {
                playTrack(targetResId, isGameplay)
            }
            return
        }

        playTrack(targetResId, isGameplay)
    }

    private fun playTrack(resId: Int, isGameplay: Boolean) {
        stopMusic()
        if (resId == 0) return

        try {
            currentTrackResId = resId
            isPlayingGameplayTrack = isGameplay

            mediaPlayer = MediaPlayer.create(context, resId)?.apply {
                // Uniform volume level (35% volume across menu and gameplay)
                setVolume(0.35f, 0.35f)

                if (isGameplay) {
                    val gameplayTracks = getGameplayMusicResIds()
                    if (gameplayTracks.size > 1) {
                        isLooping = false
                        setOnCompletionListener {
                            currentGameplayIndex = (currentGameplayIndex + 1) % gameplayTracks.size
                            val nextTrackRes = gameplayTracks[currentGameplayIndex]
                            playTrack(nextTrackRes, isGameplay = true)
                        }
                    } else {
                        isLooping = true
                    }
                } else {
                    isLooping = true
                }

                start()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun stopMusic() {
        try {
            mediaPlayer?.let { player ->
                if (player.isPlaying) {
                    player.stop()
                }
                player.release()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            mediaPlayer = null
            currentTrackResId = 0
        }
    }

    fun pauseMusic() {
        try {
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.pause()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun resumeMusic() {
        if (!isMusicEnabled) return
        try {
            if (mediaPlayer != null && mediaPlayer?.isPlaying == false) {
                mediaPlayer?.start()
            } else if (mediaPlayer == null) {
                startMusic(isPlayingGameplayTrack)
            }
        } catch (e: Exception) {
            startMusic(isPlayingGameplayTrack)
        }
    }

    fun release() {
        stopMusic()
    }
}
