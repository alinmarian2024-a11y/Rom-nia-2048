package com.example.data

import android.content.Context
import android.content.SharedPreferences
import com.example.model.GameState
import com.example.model.GridSnapshot
import org.json.JSONArray
import org.json.JSONObject

class GameRepository(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("romania_2048_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_GRID = "key_grid"
        private const val KEY_SCORE = "key_score"
        private const val KEY_HIGH_SCORE = "key_high_score"
        private const val KEY_CURRENT_LEVEL = "key_current_level"
        private const val KEY_HIGHEST_TILE = "key_highest_tile"
        private const val KEY_MOVES_COUNT = "key_moves_count"
        private const val KEY_UNDO_COUNT = "key_undo_count"
        private const val KEY_IS_GAME_OVER = "key_is_game_over"
        private const val KEY_IS_WON = "key_is_won"
        private const val KEY_KEEP_PLAYING = "key_keep_playing"
        private const val KEY_UNLOCKED_LEVELS = "key_unlocked_levels"
        private const val KEY_UNLOCKED_COLLECTION = "key_unlocked_collection"
        private const val KEY_UNLOCKED_ACHIEVEMENTS = "key_unlocked_achievements"

        // Settings
        private const val KEY_PREF_MUSIC = "pref_music"
        private const val KEY_PREF_SFX = "pref_sfx"
        private const val KEY_PREF_VIBRATION = "pref_vibration"
        private const val KEY_PREF_THEME = "pref_theme" // "SYSTEM", "LIGHT", "DARK"
        private const val KEY_PREF_ROMANIAN_THEME = "pref_romanian_theme"
        private const val KEY_PREF_ANIMATIONS = "pref_animations"
        private const val KEY_PREF_CONFIRM_RESTART = "pref_confirm_restart"
    }

    fun saveGameState(state: GameState) {
        val editor = prefs.edit()
        editor.putInt(KEY_SCORE, state.score)
        editor.putInt(KEY_HIGH_SCORE, state.highScore)
        editor.putInt(KEY_CURRENT_LEVEL, state.currentLevel)
        editor.putInt(KEY_HIGHEST_TILE, state.highestTileAchieved)
        editor.putInt(KEY_MOVES_COUNT, state.movesCount)
        editor.putInt(KEY_UNDO_COUNT, state.undoCount)
        editor.putBoolean(KEY_IS_GAME_OVER, state.isGameOver)
        editor.putBoolean(KEY_IS_WON, state.isWon)
        editor.putBoolean(KEY_KEEP_PLAYING, state.keepPlayingPast2048)

        // Save grid as JSON string
        val jsonGrid = JSONArray()
        for (r in 0..3) {
            val jsonRow = JSONArray()
            for (c in 0..3) {
                jsonRow.put(state.grid[r][c])
            }
            jsonGrid.put(jsonRow)
        }
        editor.putString(KEY_GRID, jsonGrid.toString())

        // Save sets
        editor.putStringSet(KEY_UNLOCKED_LEVELS, state.unlockedLevels.map { it.toString() }.toSet())
        editor.putStringSet(KEY_UNLOCKED_COLLECTION, state.unlockedCollectionValues.map { it.toString() }.toSet())
        editor.putStringSet(KEY_UNLOCKED_ACHIEVEMENTS, state.unlockedAchievementIds.map { it.toString() }.toSet())

        editor.apply()
    }

    fun loadGameState(): GameState {
        val highScore = prefs.getInt(KEY_HIGH_SCORE, 0)
        val score = prefs.getInt(KEY_SCORE, 0)
        val currentLevel = prefs.getInt(KEY_CURRENT_LEVEL, 1)
        val highestTile = prefs.getInt(KEY_HIGHEST_TILE, 0)
        val movesCount = prefs.getInt(KEY_MOVES_COUNT, 0)
        val undoCount = prefs.getInt(KEY_UNDO_COUNT, 0)
        val isGameOver = prefs.getBoolean(KEY_IS_GAME_OVER, false)
        val isWon = prefs.getBoolean(KEY_IS_WON, false)
        val keepPlaying = prefs.getBoolean(KEY_KEEP_PLAYING, false)

        val unlockedLevels = prefs.getStringSet(KEY_UNLOCKED_LEVELS, setOf("1"))
            ?.mapNotNull { it.toIntOrNull() }?.toSet() ?: setOf(1)

        val unlockedCollection = prefs.getStringSet(KEY_UNLOCKED_COLLECTION, setOf("2"))
            ?.mapNotNull { it.toIntOrNull() }?.toSet() ?: setOf(2)

        val unlockedAchievements = prefs.getStringSet(KEY_UNLOCKED_ACHIEVEMENTS, emptySet())
            ?.mapNotNull { it.toIntOrNull() }?.toSet() ?: emptySet()

        val gridStr = prefs.getString(KEY_GRID, null)
        val grid = if (gridStr != null) {
            try {
                val jsonGrid = JSONArray(gridStr)
                List(4) { r ->
                    val jsonRow = jsonGrid.getJSONArray(r)
                    List(4) { c ->
                        jsonRow.getInt(c)
                    }
                }
            } catch (e: Exception) {
                List(4) { List(4) { 0 } }
            }
        } else {
            List(4) { List(4) { 0 } }
        }

        return GameState(
            grid = grid,
            score = score,
            highScore = highScore,
            currentLevel = currentLevel,
            highestTileAchieved = highestTile,
            movesCount = movesCount,
            undoCount = undoCount,
            isGameOver = isGameOver,
            isWon = isWon,
            keepPlayingPast2048 = keepPlaying,
            unlockedLevels = unlockedLevels,
            unlockedCollectionValues = unlockedCollection,
            unlockedAchievementIds = unlockedAchievements
        )
    }

    // Settings
    fun isMusicEnabled(): Boolean = prefs.getBoolean(KEY_PREF_MUSIC, true)
    fun setMusicEnabled(enabled: Boolean) = prefs.edit().putBoolean(KEY_PREF_MUSIC, enabled).apply()

    fun isSfxEnabled(): Boolean = prefs.getBoolean(KEY_PREF_SFX, true)
    fun setSfxEnabled(enabled: Boolean) = prefs.edit().putBoolean(KEY_PREF_SFX, enabled).apply()

    fun isVibrationEnabled(): Boolean = prefs.getBoolean(KEY_PREF_VIBRATION, true)
    fun setVibrationEnabled(enabled: Boolean) = prefs.edit().putBoolean(KEY_PREF_VIBRATION, enabled).apply()

    fun getThemePreference(): String = prefs.getString(KEY_PREF_THEME, "SYSTEM") ?: "SYSTEM"
    fun setThemePreference(theme: String) = prefs.edit().putString(KEY_PREF_THEME, theme).apply()

    fun isRomanianThemeEnabled(): Boolean = prefs.getBoolean(KEY_PREF_ROMANIAN_THEME, true)
    fun setRomanianThemeEnabled(enabled: Boolean) = prefs.edit().putBoolean(KEY_PREF_ROMANIAN_THEME, enabled).apply()

    fun isAnimationsEnabled(): Boolean = prefs.getBoolean(KEY_PREF_ANIMATIONS, true)
    fun setAnimationsEnabled(enabled: Boolean) = prefs.edit().putBoolean(KEY_PREF_ANIMATIONS, enabled).apply()

    fun isConfirmRestartEnabled(): Boolean = prefs.getBoolean(KEY_PREF_CONFIRM_RESTART, true)
    fun setConfirmRestartEnabled(enabled: Boolean) = prefs.edit().putBoolean(KEY_PREF_CONFIRM_RESTART, enabled).apply()

    fun resetAllData() {
        val editor = prefs.edit()
        val highScore = prefs.getInt(KEY_HIGH_SCORE, 0)
        editor.clear()
        // Keep high score or reset if user chose full reset
        editor.apply()
    }
}
