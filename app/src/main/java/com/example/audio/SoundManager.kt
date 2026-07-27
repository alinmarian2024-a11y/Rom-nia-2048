package com.example.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.media.ToneGenerator
import android.media.AudioManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.sin

class SoundManager(private val context: Context) {

    private var toneGen: ToneGenerator? = try {
        ToneGenerator(AudioManager.STREAM_MUSIC, 70)
    } catch (e: Exception) {
        null
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

    private var musicJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Default)

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

    fun playTap() {
        if (!isSfxEnabled) return
        try {
            toneGen?.startTone(ToneGenerator.TONE_PROP_BEEP, 30)
        } catch (e: Exception) {
            playSynthesizedTone(frequency = 523, durationMs = 40)
        }
    }

    fun playMove() {
        if (!isSfxEnabled) return
        playSynthesizedTone(frequency = 330, durationMs = 35)
    }

    fun playMerge(value: Int = 4) {
        if (!isSfxEnabled) return
        triggerVibration(30)
        val freq = when (value) {
            4 -> 440
            8 -> 523
            16 -> 587
            32 -> 659
            64 -> 698
            128 -> 784
            256 -> 880
            512 -> 987
            1024 -> 1046
            2048 -> 1175
            else -> 1318
        }
        playSynthesizedTone(frequency = freq, durationMs = 80)
    }

    fun playBigMerge() {
        if (!isSfxEnabled) return
        triggerVibration(60)
        scope.launch {
            playSynthesizedTone(frequency = 523, durationMs = 60)
            delay(50)
            playSynthesizedTone(frequency = 659, durationMs = 60)
            delay(50)
            playSynthesizedTone(frequency = 784, durationMs = 120)
        }
    }

    fun playVictory() {
        if (!isSfxEnabled) return
        triggerVibration(100)
        scope.launch {
            val notes = intArrayOf(523, 659, 784, 1046)
            for (note in notes) {
                playSynthesizedTone(frequency = note, durationMs = 100)
                delay(90)
            }
        }
    }

    fun playGameOver() {
        if (!isSfxEnabled) return
        triggerVibration(80)
        scope.launch {
            val notes = intArrayOf(400, 350, 300, 250)
            for (note in notes) {
                playSynthesizedTone(frequency = note, durationMs = 120)
                delay(110)
            }
        }
    }

    fun playUndo() {
        if (!isSfxEnabled) return
        playSynthesizedTone(frequency = 300, durationMs = 50)
    }

    fun playAchievement() {
        if (!isSfxEnabled) return
        triggerVibration(70)
        scope.launch {
            playSynthesizedTone(frequency = 659, durationMs = 70)
            delay(60)
            playSynthesizedTone(frequency = 880, durationMs = 150)
        }
    }

    fun startMusic() {
        if (musicJob?.isActive == true) return
        musicJob = scope.launch {
            val melody = intArrayOf(261, 293, 329, 349, 392, 349, 329, 293)
            var index = 0
            while (isActive) {
                if (isMusicEnabled) {
                    val note = melody[index % melody.size]
                    playSynthesizedTone(frequency = note, durationMs = 180, volume = 0.12f)
                    index++
                }
                delay(450)
            }
        }
    }

    fun stopMusic() {
        musicJob?.cancel()
        musicJob = null
    }

    private fun playSynthesizedTone(frequency: Int, durationMs: Int, volume: Float = 0.3f) {
        try {
            val sampleRate = 22050
            val numSamples = (sampleRate * (durationMs / 1000.0)).toInt().coerceAtLeast(100)
            val sample = DoubleArray(numSamples)
            val buffer = ShortArray(numSamples)

            for (i in 0 until numSamples) {
                val angle = 2.0 * Math.PI * i / (sampleRate.toDouble() / frequency)
                // Soft sine wave with gentle fade out
                val fade = 1.0 - (i.toDouble() / numSamples.toDouble())
                sample[i] = sin(angle) * fade
            }

            for (i in sample.indices) {
                buffer[i] = (sample[i] * 32767 * volume).toInt().toShort()
            }

            val audioTrack = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_GAME)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(sampleRate)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setBufferSizeInBytes(buffer.size * 2)
                .setTransferMode(AudioTrack.MODE_STATIC)
                .build()

            audioTrack.write(buffer, 0, buffer.size)
            audioTrack.play()
            scope.launch {
                delay(durationMs.toLong() + 50)
                audioTrack.release()
            }
        } catch (e: Exception) {
            // Ignore audio fallback errors
        }
    }

    fun release() {
        stopMusic()
        toneGen?.release()
        toneGen = null
    }
}
