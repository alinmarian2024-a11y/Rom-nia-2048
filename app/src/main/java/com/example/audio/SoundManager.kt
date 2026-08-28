package com.example.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.media.MediaPlayer
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SoundManager(private val baseContext: Context) {

    private val context: Context = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        baseContext.createAttributionContext("default")
    } else {
        baseContext
    }

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
    
    var musicVolume: Float = 1.0f
    var sfxVolume: Float = 1.0f
    var isPausedScale: Float = 1.0f

    private var mediaPlayer: MediaPlayer? = null
    private var currentTrackResId: Int = 0
    private var isPlayingGameplayTrack: Boolean = false
    private val scope = CoroutineScope(Dispatchers.Default)

    fun updateMusicVolume() {
        val vol = musicVolume * 0.35f * isPausedScale
        mediaPlayer?.setVolume(vol, vol)
    }

    fun setMusicPausedState(isPaused: Boolean) {
        isPausedScale = if (isPaused) 0.2f else 1.0f
        updateMusicVolume()
    }

    private fun getRawResId(resName: String): Int {
        return try {
            context.resources.getIdentifier(resName, "raw", context.packageName)
        } catch (e: Exception) {
            0
        }
    }

    private fun getMenuMusicResId(): Int {
        val candidates = listOf("menu_theme", "menu_music", "menu", "bg_menu", "theme_menu")
        for (name in candidates) {
            val resId = getRawResId(name)
            if (resId != 0) return resId
        }
        return 0
    }

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

    /**
     * Synthesize soft, organic sounds like a xylophone or glass ding using sine waves
     * with an exponential envelope, mixed with a harmonic. This avoids "robotic beeps".
     */
    private fun playOrganicSound(freq1: Double, freq2: Double, durationMs: Int, volumeScale: Float = 1.0f) {
        if (!isSfxEnabled || sfxVolume <= 0f) return
        scope.launch(Dispatchers.IO) {
            try {
                val sampleRate = 44100
                val numSamples = (durationMs * sampleRate / 1000.0).toInt()
                val generatedSnd = ByteArray(2 * numSamples)

                val maxVolume = 32767.0 * sfxVolume * volumeScale * 0.4 
                val decayRate = numSamples / 4.0 

                for (i in 0 until numSamples) {
                    val envelope = Math.exp(-i.toDouble() / decayRate) 
                    val wave1 = Math.sin(2 * Math.PI * i / (sampleRate / freq1))
                    val wave2 = if (freq2 > 0) Math.sin(2 * Math.PI * i / (sampleRate / freq2)) * 0.3 else 0.0
                    
                    val dVal = (wave1 + wave2) * envelope
                    
                    val valShort = (dVal * maxVolume).toInt().toShort()
                    val idx = i * 2
                    generatedSnd[idx] = (valShort.toInt() and 0x00ff).toByte()
                    generatedSnd[idx + 1] = ((valShort.toInt() and 0xff00) ushr 8).toByte()
                }

                val audioTrack = AudioTrack.Builder()
                    .setAudioAttributes(AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_GAME)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build())
                    .setAudioFormat(AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(sampleRate)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build())
                    .setBufferSizeInBytes(generatedSnd.size)
                    .setTransferMode(AudioTrack.MODE_STATIC)
                    .build()

                audioTrack.write(generatedSnd, 0, generatedSnd.size)
                audioTrack.play()
                
                delay(durationMs.toLong() + 100)
                audioTrack.release()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // --- SFX METHODS ---
    fun playTap() {
        if (!isSfxEnabled) return
        triggerVibration(15)
        playOrganicSound(800.0, 1600.0, 60, 0.5f) // very short click-like soft pop
    }

    fun playMove() {
        if (!isSfxEnabled) return
        triggerVibration(12)
        playOrganicSound(300.0, 600.0, 100, 0.6f) // soft low marimba knock
    }

    fun playMerge(value: Int = 4) {
        if (!isSfxEnabled) return
        triggerVibration(25)
        // Higher pitch for larger merges (cap at 1200)
        val baseFreq = Math.min(400.0 + (value * 10), 1200.0)
        playOrganicSound(baseFreq, baseFreq * 2.0, 200, 0.8f) // pleasant ding
    }

    fun playBigMerge() {
        if (!isSfxEnabled) return
        triggerVibration(50)
        playOrganicSound(600.0, 900.0, 400, 1.0f) // harmonious chord
    }

    fun playVictory() {
        if (!isSfxEnabled) return
        triggerVibration(100)
        scope.launch {
            playOrganicSound(523.25, 1046.50, 150, 1.0f) // C
            delay(150)
            playOrganicSound(659.25, 1318.51, 150, 1.0f) // E
            delay(150)
            playOrganicSound(783.99, 1567.98, 300, 1.0f) // G
        }
    }

    fun playGameOver() {
        if (!isSfxEnabled) return
        triggerVibration(80)
        scope.launch {
            playOrganicSound(392.00, 783.99, 200, 1.0f) // G
            delay(200)
            playOrganicSound(311.13, 622.25, 200, 1.0f) // Eb
            delay(200)
            playOrganicSound(261.63, 523.25, 400, 1.0f) // C
        }
    }

    fun playUndo() {
        if (!isSfxEnabled) return
        triggerVibration(15)
        scope.launch {
            playOrganicSound(600.0, 0.0, 80, 0.6f)
            delay(90)
            playOrganicSound(400.0, 0.0, 100, 0.6f)
        }
    }

    fun playAchievement() {
        if (!isSfxEnabled) return
        triggerVibration(60)
        scope.launch {
            playOrganicSound(523.25, 1046.50, 150, 0.9f)
            delay(150)
            playOrganicSound(783.99, 1567.98, 350, 1.0f)
        }
    }

    // --- MUSIC METHODS ---

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
            stopMusic()
            return
        }

        if (mediaPlayer != null && currentTrackResId == targetResId && isPlayingGameplayTrack == isGameplay) {
            try {
                if (mediaPlayer?.isPlaying == false) {
                    mediaPlayer?.start()
                }
                updateMusicVolume() // ensure volume is correct
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
                val vol = musicVolume * 0.35f * isPausedScale
                setVolume(vol, vol)
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
                setOnErrorListener { _, _, _ -> 
                    true // Consume the error to prevent crash/log spam
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
                updateMusicVolume()
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
