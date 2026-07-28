package com.example.data

import android.content.Context
import android.content.SharedPreferences
import com.example.model.GameMode
import com.example.model.GameState
import org.json.JSONArray

class GameRepository(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("romania_2048_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_GAME_MODE = "key_game_mode"

        // Shared / Global
        private const val KEY_HIGH_SCORE = "key_high_score"
        private const val KEY_HIGHEST_TILE = "key_highest_tile"
        private const val KEY_UNLOCKED_LEVELS = "key_unlocked_levels"
        private const val KEY_COMPLETED_LEVELS = "key_completed_levels"
        private const val KEY_UNLOCKED_COLLECTION = "key_unlocked_collection"
        private const val KEY_UNLOCKED_ACHIEVEMENTS = "key_unlocked_achievements"

        // Infinite Mode Data
        private const val KEY_INFINITE_GRID = "key_infinite_grid"
        private const val KEY_INFINITE_SCORE = "key_infinite_score"
        private const val KEY_INFINITE_MOVES = "key_infinite_moves"
        private const val KEY_INFINITE_UNDO = "key_infinite_undo"
        private const val KEY_INFINITE_GAME_OVER = "key_infinite_game_over"
        private const val KEY_INFINITE_WON = "key_infinite_won"
        private const val KEY_INFINITE_KEEP_PLAYING = "key_infinite_keep_playing"

        // Adventure Mode Data
        private const val KEY_ADVENTURE_GRID = "key_adventure_grid"
        private const val KEY_ADVENTURE_SCORE = "key_adventure_score"
        private const val KEY_ADVENTURE_LEVEL = "key_adventure_level"
        private const val KEY_ADVENTURE_MOVES = "key_adventure_moves"
        private const val KEY_ADVENTURE_UNDO = "key_adventure_undo"
        private const val KEY_ADVENTURE_GAME_OVER = "key_adventure_game_over"

        // Legacy Keys
        private const val KEY_GRID_LEGACY = "key_grid"
        private const val KEY_SCORE_LEGACY = "key_score"

        // Settings
        private const val KEY_PREF_MUSIC = "pref_music"
        private const val KEY_PREF_SFX = "pref_sfx"
        private const val KEY_PREF_VIBRATION = "pref_vibration"
        private const val KEY_PREF_THEME = "pref_theme"
        private const val KEY_PREF_ROMANIAN_THEME = "pref_romanian_theme"
        private const val KEY_PREF_ANIMATIONS = "pref_animations"
        private const val KEY_PREF_CONFIRM_RESTART = "pref_confirm_restart"
        private const val KEY_IS_ADS_REMOVED = "key_is_ads_removed"
    }

    fun saveGameState(state: GameState) {
        val editor = prefs.edit()
        editor.putString(KEY_GAME_MODE, state.gameMode.name)
        editor.putInt(KEY_HIGH_SCORE, state.highScore)
        editor.putInt(KEY_HIGHEST_TILE, state.highestTileAchieved)

        // Save mode-specific state
        if (state.gameMode == GameMode.INFINITE) {
            editor.putInt(KEY_INFINITE_SCORE, state.score)
            editor.putInt(KEY_INFINITE_MOVES, state.movesCount)
            editor.putInt(KEY_INFINITE_UNDO, state.undoCount)
            editor.putBoolean(KEY_INFINITE_GAME_OVER, state.isGameOver)
            editor.putBoolean(KEY_INFINITE_WON, state.isWon)
            editor.putBoolean(KEY_INFINITE_KEEP_PLAYING, state.keepPlayingPast2048)
            editor.putString(KEY_INFINITE_GRID, gridToJson(state.grid))
        } else {
            editor.putInt(KEY_ADVENTURE_SCORE, state.score)
            editor.putInt(KEY_ADVENTURE_LEVEL, state.currentLevel)
            editor.putInt(KEY_ADVENTURE_MOVES, state.movesCount)
            editor.putInt(KEY_ADVENTURE_UNDO, state.undoCount)
            editor.putBoolean(KEY_ADVENTURE_GAME_OVER, state.isGameOver)
            editor.putString(KEY_ADVENTURE_GRID, gridToJson(state.grid))
        }

        // Save sets
        editor.putStringSet(KEY_UNLOCKED_LEVELS, state.unlockedLevels.map { it.toString() }.toSet())
        editor.putStringSet(KEY_COMPLETED_LEVELS, state.completedLevels.map { it.toString() }.toSet())
        editor.putStringSet(KEY_UNLOCKED_COLLECTION, state.unlockedCollectionValues.map { it.toString() }.toSet())
        editor.putStringSet(KEY_UNLOCKED_ACHIEVEMENTS, state.unlockedAchievementIds.map { it.toString() }.toSet())

        editor.apply()
    }

    fun loadGameState(targetMode: GameMode = GameMode.INFINITE): GameState {
        val modeStr = prefs.getString(KEY_GAME_MODE, targetMode.name) ?: targetMode.name
        val activeMode = try { GameMode.valueOf(modeStr) } catch (e: Exception) { targetMode }

        val highScore = prefs.getInt(KEY_HIGH_SCORE, 0)
        val highestTile = prefs.getInt(KEY_HIGHEST_TILE, 0)

        val unlockedLevels = prefs.getStringSet(KEY_UNLOCKED_LEVELS, setOf("1"))
            ?.mapNotNull { it.toIntOrNull() }?.toSet() ?: setOf(1)

        val completedLevels = prefs.getStringSet(KEY_COMPLETED_LEVELS, emptySet())
            ?.mapNotNull { it.toIntOrNull() }?.toSet() ?: emptySet()

        val unlockedCollection = prefs.getStringSet(KEY_UNLOCKED_COLLECTION, setOf("2"))
            ?.mapNotNull { it.toIntOrNull() }?.toSet() ?: setOf(2)

        val unlockedAchievements = prefs.getStringSet(KEY_UNLOCKED_ACHIEVEMENTS, emptySet())
            ?.mapNotNull { it.toIntOrNull() }?.toSet() ?: emptySet()

        return if (activeMode == GameMode.INFINITE) {
            val score = prefs.getInt(KEY_INFINITE_SCORE, prefs.getInt(KEY_SCORE_LEGACY, 0))
            val moves = prefs.getInt(KEY_INFINITE_MOVES, 0)
            val undoCount = prefs.getInt(KEY_INFINITE_UNDO, 0)
            val isGameOver = prefs.getBoolean(KEY_INFINITE_GAME_OVER, false)
            val isWon = prefs.getBoolean(KEY_INFINITE_WON, false)
            val keepPlaying = prefs.getBoolean(KEY_INFINITE_KEEP_PLAYING, false)

            val gridStr = prefs.getString(KEY_INFINITE_GRID, prefs.getString(KEY_GRID_LEGACY, null))
            val grid = parseGrid(gridStr)

            GameState(
                gameMode = GameMode.INFINITE,
                grid = grid,
                score = score,
                highScore = highScore,
                highestTileAchieved = highestTile,
                movesCount = moves,
                undoCount = undoCount,
                isGameOver = isGameOver,
                isWon = isWon,
                keepPlayingPast2048 = keepPlaying,
                unlockedLevels = unlockedLevels,
                completedLevels = completedLevels,
                unlockedCollectionValues = unlockedCollection,
                unlockedAchievementIds = unlockedAchievements
            )
        } else {
            val currentLevel = prefs.getInt(KEY_ADVENTURE_LEVEL, 1)
            val score = prefs.getInt(KEY_ADVENTURE_SCORE, 0)
            val moves = prefs.getInt(KEY_ADVENTURE_MOVES, 0)
            val undoCount = prefs.getInt(KEY_ADVENTURE_UNDO, 0)
            val isGameOver = prefs.getBoolean(KEY_ADVENTURE_GAME_OVER, false)

            val gridStr = prefs.getString(KEY_ADVENTURE_GRID, null)
            val grid = parseGrid(gridStr)

            GameState(
                gameMode = GameMode.ADVENTURE,
                grid = grid,
                score = score,
                highScore = highScore,
                currentLevel = currentLevel,
                highestTileAchieved = highestTile,
                movesCount = moves,
                undoCount = undoCount,
                isGameOver = isGameOver,
                unlockedLevels = unlockedLevels,
                completedLevels = completedLevels,
                unlockedCollectionValues = unlockedCollection,
                unlockedAchievementIds = unlockedAchievements
            )
        }
    }

    private fun gridToJson(grid: List<List<Int>>): String {
        val jsonGrid = JSONArray()
        for (r in 0..3) {
            val jsonRow = JSONArray()
            for (c in 0..3) {
                jsonRow.put(grid[r][c])
            }
            jsonGrid.put(jsonRow)
        }
        return jsonGrid.toString()
    }

    private fun parseGrid(gridStr: String?): List<List<Int>> {
        if (gridStr == null) return List(4) { List(4) { 0 } }
        return try {
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

    fun isAdsRemoved(): Boolean = prefs.getBoolean(KEY_IS_ADS_REMOVED, false)
    fun setAdsRemoved(removed: Boolean) = prefs.edit().putBoolean(KEY_IS_ADS_REMOVED, removed).apply()

    fun resetAllData() {
        prefs.edit().clear().apply()
    }
}
