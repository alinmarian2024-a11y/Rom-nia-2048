package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.SoundManager
import com.example.data.GameRepository
import com.example.model.Achievement
import com.example.model.GAME_LEVELS
import com.example.model.GameLevel
import com.example.model.GameState
import com.example.model.GridSnapshot
import com.example.model.RomaniaItem
import com.example.model.TileRegistry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

enum class AppScreen {
    HOME,
    GAME,
    LEVELS,
    COLLECTION,
    ACHIEVEMENTS,
    SETTINGS,
    ABOUT
}

enum class Direction {
    UP, DOWN, LEFT, RIGHT
}

class GameViewModel(application: Application) : AndroidViewModel(application) {

    val repository = GameRepository(application)
    val soundManager = SoundManager(application)

    private val _currentScreen = MutableStateFlow(AppScreen.HOME)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val _gameState = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    // Dialog & Pop-up states
    private val _showRestartDialog = MutableStateFlow(false)
    val showRestartDialog: StateFlow<Boolean> = _showRestartDialog.asStateFlow()

    private val _showResetConfirmDialog = MutableStateFlow(false)
    val showResetConfirmDialog: StateFlow<Boolean> = _showResetConfirmDialog.asStateFlow()

    private val _showPauseModal = MutableStateFlow(false)
    val showPauseModal: StateFlow<Boolean> = _showPauseModal.asStateFlow()

    private val _showVictoryDialog = MutableStateFlow(false)
    val showVictoryDialog: StateFlow<Boolean> = _showVictoryDialog.asStateFlow()

    private val _showLevelCompleteDialog = MutableStateFlow<GameLevel?>(null)
    val showLevelCompleteDialog: StateFlow<GameLevel?> = _showLevelCompleteDialog.asStateFlow()

    private val _latestUnlockedAchievement = MutableStateFlow<Achievement?>(null)
    val latestUnlockedAchievement: StateFlow<Achievement?> = _latestUnlockedAchievement.asStateFlow()

    // Settings States
    val isMusicEnabled = MutableStateFlow(repository.isMusicEnabled())
    val isSfxEnabled = MutableStateFlow(repository.isSfxEnabled())
    val isVibrationEnabled = MutableStateFlow(repository.isVibrationEnabled())
    val themePreference = MutableStateFlow(repository.getThemePreference())
    val isRomanianTheme = MutableStateFlow(repository.isRomanianThemeEnabled())
    val isAnimationsEnabled = MutableStateFlow(repository.isAnimationsEnabled())
    val isConfirmRestart = MutableStateFlow(repository.isConfirmRestartEnabled())

    // 25 Achievements definition
    val allAchievementsList = listOf(
        Achievement("1", "🥨", "PRIMUL COVRIG", "Obține prima piesă de nivel 2.", maxProgress = 1),
        Achievement("2", "🥧", "POFTA VINE MÂNCÂND", "Obține piesa 4.", maxProgress = 1),
        Achievement("3", "🥣", "MĂMĂLIGAR DEVOTAT", "Obține piesa 8.", maxProgress = 1),
        Achievement("4", "🧀", "BRÂNZĂ BUNA", "Obține piesa 16.", maxProgress = 1),
        Achievement("5", "🍗", "MAESTRU AL COPANELOR", "Obține piesa 32.", maxProgress = 1),
        Achievement("6", "🥘", "REGELE SARMALELOR", "Obține piesa 64.", maxProgress = 1),
        Achievement("7", "🍲", "MASTERCHEF ROMÂN", "Obține piesa 128.", maxProgress = 1),
        Achievement("8", "☕", "CAFEAUA DE DIMINEAȚĂ", "Obține piesa 256.", maxProgress = 1),
        Achievement("9", "🏰", "TURIST ÎN ROMÂNIA", "Obține piesa 512.", maxProgress = 1),
        Achievement("10", "🏛️", "BOIER MODERN", "Obține piesa 1024.", maxProgress = 1),
        Achievement("11", "🇷🇴", "PATRIOT", "Ajunge la piesa 2048.", maxProgress = 1),
        Achievement("12", "🌟", "LUCEAFĂRUL JOCULUI", "Ajunge la piesa 4096.", maxProgress = 1),
        Achievement("13", "🦅", "VULTURUL CARPAȚILOR", "Ajunge la piesa 8192.", maxProgress = 1),
        Achievement("14", "🔥", "NU MĂ OPRESC", "Continuă jocul după atingerea 2048.", maxProgress = 1),
        Achievement("15", "💪", "ÎNCĂ O DATĂ", "Folosește UNDO de 3 ori într-un joc.", maxProgress = 3),
        Achievement("16", "🧠", "STRATEG", "Realizează 10 mutări valide.", maxProgress = 10),
        Achievement("17", "🏆", "MAESTRU 2048", "Obține un scor peste 10.000 de puncte.", maxProgress = 10000),
        Achievement("18", "🥇", "LEGENDĂ RURALĂ", "Obține un scor peste 25.000 de puncte.", maxProgress = 25000),
        Achievement("19", "👑", "REGELE ROMÂNIEI", "Obține un scor peste 50.000 de puncte.", maxProgress = 50000),
        Achievement("20", "🗺", "EXPLORATOR", "Deblochează cel puțin 5 niveluri.", maxProgress = 5),
        Achievement("21", "🎨", "COLECȚIONAR HARNIC", "Deblochează 8 piese în Colecție.", maxProgress = 8),
        Achievement("22", "⚡", "VITEZĂ SUPREMĂ", "Efectuează 50 de mutări.", maxProgress = 50),
        Achievement("23", "🔄", "REÎNCEPUT PROSPER", "Începe un joc nou după ce ai obținut un scor bun.", maxProgress = 1),
        Achievement("24", "🌙", "NOPȚI ALBE", "Comută aplicația în modul Dark.", maxProgress = 1),
        Achievement("25", "🇷🇴", "INIMĂ DE ROMÂN", "Completează toate nivelurile din joc.", maxProgress = 7)
    )

    init {
        loadSavedState()
        syncAudioSettings()
    }

    private fun loadSavedState() {
        val loadedState = repository.loadGameState()
        if (isGridEmpty(loadedState.grid)) {
            // New game start
            val initialGrid = createNewGameGrid()
            _gameState.value = loadedState.copy(grid = initialGrid)
            repository.saveGameState(_gameState.value)
        } else {
            _gameState.value = loadedState
        }
    }

    private fun syncAudioSettings() {
        soundManager.isMusicEnabled = isMusicEnabled.value
        soundManager.isSfxEnabled = isSfxEnabled.value
        soundManager.isVibrationEnabled = isVibrationEnabled.value
        if (isMusicEnabled.value) {
            soundManager.startMusic()
        } else {
            soundManager.stopMusic()
        }
    }

    fun navigateTo(screen: AppScreen) {
        soundManager.playTap()
        _currentScreen.value = screen
    }

    private fun isGridEmpty(grid: List<List<Int>>): Boolean {
        for (r in 0..3) {
            for (c in 0..3) {
                if (grid[r][c] != 0) return false
            }
        }
        return true
    }

    private fun createNewGameGrid(): List<List<Int>> {
        val grid = MutableList(4) { MutableList(4) { 0 } }
        spawnRandomTileInGrid(grid)
        spawnRandomTileInGrid(grid)
        return grid
    }

    private fun spawnRandomTileInGrid(grid: MutableList<MutableList<Int>>): Boolean {
        val emptyCells = mutableListOf<Pair<Int, Int>>()
        for (r in 0..3) {
            for (c in 0..3) {
                if (grid[r][c] == 0) emptyCells.add(Pair(r, c))
            }
        }
        if (emptyCells.isEmpty()) return false
        val (r, c) = emptyCells[Random.nextInt(emptyCells.size)]
        grid[r][c] = if (Random.nextFloat() < 0.9f) 2 else 4
        return true
    }

    // Move logic for 2048
    fun move(direction: Direction) {
        val current = _gameState.value
        if (current.isGameOver) return
        if (_showPauseModal.value) return

        val currentGrid = current.grid
        val (newGrid, scoreGained, moved) = processMove(currentGrid, direction)

        if (!moved) return // No change

        // Sound effect
        if (scoreGained > 0) {
            soundManager.playMerge(scoreGained)
        } else {
            soundManager.playMove()
        }

        // Save snapshot for UNDO
        val newUndoStack = current.undoStack.toMutableList()
        newUndoStack.add(0, GridSnapshot(currentGrid, current.score))
        if (newUndoStack.size > 3) newUndoStack.removeAt(newUndoStack.lastIndex)

        // Spawn new tile
        val mutableNewGrid = newGrid.map { it.toMutableList() }.toMutableList()
        spawnRandomTileInGrid(mutableNewGrid)

        val newScore = current.score + scoreGained
        val newHighScore = maxOf(current.highScore, newScore)
        val newMovesCount = current.movesCount + 1

        // Find max tile
        var maxTile = current.highestTileAchieved
        val newlyUnlockedCollection = current.unlockedCollectionValues.toMutableSet()

        for (r in 0..3) {
            for (c in 0..3) {
                val v = mutableNewGrid[r][c]
                if (v > maxTile) maxTile = v
                if (v > 0) newlyUnlockedCollection.add(v)
            }
        }

        var isWonNow = current.isWon
        var showVic = false
        if (maxTile >= 2048 && !current.isWon && !current.keepPlayingPast2048) {
            isWonNow = true
            showVic = true
            soundManager.playVictory()
        }

        val gameOverNow = checkGameOver(mutableNewGrid)
        if (gameOverNow && !current.isGameOver) {
            soundManager.playGameOver()
        }

        // Update levels
        val unlockedLevs = current.unlockedLevels.toMutableSet()
        checkLevelProgression(newScore, maxTile, unlockedLevs)

        var newState = current.copy(
            grid = mutableNewGrid,
            score = newScore,
            highScore = newHighScore,
            movesCount = newMovesCount,
            highestTileAchieved = maxTile,
            isWon = isWonNow,
            isGameOver = gameOverNow,
            undoStack = newUndoStack,
            unlockedLevels = unlockedLevs,
            unlockedCollectionValues = newlyUnlockedCollection
        )

        // Check Achievements
        newState = evaluateAchievements(newState)

        _gameState.value = newState
        repository.saveGameState(newState)

        if (showVic) {
            _showVictoryDialog.value = true
        }
    }

    private fun processMove(
        grid: List<List<Int>>,
        direction: Direction
    ): Triple<List<List<Int>>, Int, Boolean> {
        val resultGrid = List(4) { MutableList(4) { 0 } }
        var totalScoreGained = 0
        var hasMoved = false

        when (direction) {
            Direction.LEFT -> {
                for (r in 0..3) {
                    val row = grid[r].filter { it != 0 }
                    val mergedRow = mutableListOf<Int>()
                    var i = 0
                    while (i < row.size) {
                        if (i + 1 < row.size && row[i] == row[i + 1]) {
                            val mergedValue = row[i] * 2
                            mergedRow.add(mergedValue)
                            totalScoreGained += mergedValue
                            i += 2
                        } else {
                            mergedRow.add(row[i])
                            i++
                        }
                    }
                    while (mergedRow.size < 4) mergedRow.add(0)
                    for (c in 0..3) {
                        resultGrid[r][c] = mergedRow[c]
                        if (grid[r][c] != resultGrid[r][c]) hasMoved = true
                    }
                }
            }
            Direction.RIGHT -> {
                for (r in 0..3) {
                    val row = grid[r].filter { it != 0 }
                    val mergedRow = mutableListOf<Int>()
                    var i = row.size - 1
                    while (i >= 0) {
                        if (i - 1 >= 0 && row[i] == row[i - 1]) {
                            val mergedValue = row[i] * 2
                            mergedRow.add(0, mergedValue)
                            totalScoreGained += mergedValue
                            i -= 2
                        } else {
                            mergedRow.add(0, row[i])
                            i--
                        }
                    }
                    while (mergedRow.size < 4) mergedRow.add(0, 0)
                    for (c in 0..3) {
                        resultGrid[r][c] = mergedRow[c]
                        if (grid[r][c] != resultGrid[r][c]) hasMoved = true
                    }
                }
            }
            Direction.UP -> {
                for (c in 0..3) {
                    val col = (0..3).map { grid[it][c] }.filter { it != 0 }
                    val mergedCol = mutableListOf<Int>()
                    var i = 0
                    while (i < col.size) {
                        if (i + 1 < col.size && col[i] == col[i + 1]) {
                            val mergedValue = col[i] * 2
                            mergedCol.add(mergedValue)
                            totalScoreGained += mergedValue
                            i += 2
                        } else {
                            mergedCol.add(col[i])
                            i++
                        }
                    }
                    while (mergedCol.size < 4) mergedCol.add(0)
                    for (r in 0..3) {
                        resultGrid[r][c] = mergedCol[r]
                        if (grid[r][c] != resultGrid[r][c]) hasMoved = true
                    }
                }
            }
            Direction.DOWN -> {
                for (c in 0..3) {
                    val col = (0..3).map { grid[it][c] }.filter { it != 0 }
                    val mergedCol = mutableListOf<Int>()
                    var i = col.size - 1
                    while (i >= 0) {
                        if (i - 1 >= 0 && col[i] == col[i - 1]) {
                            val mergedValue = col[i] * 2
                            mergedCol.add(0, mergedValue)
                            totalScoreGained += mergedValue
                            i -= 2
                        } else {
                            mergedCol.add(0, col[i])
                            i--
                        }
                    }
                    while (mergedCol.size < 4) mergedCol.add(0, 0)
                    for (r in 0..3) {
                        resultGrid[r][c] = mergedCol[r]
                        if (grid[r][c] != resultGrid[r][c]) hasMoved = true
                    }
                }
            }
        }

        return Triple(resultGrid, totalScoreGained, hasMoved)
    }

    private fun checkGameOver(grid: List<List<Int>>): Boolean {
        for (r in 0..3) {
            for (c in 0..3) {
                if (grid[r][c] == 0) return false
                if (r + 1 < 4 && grid[r][c] == grid[r + 1][c]) return false
                if (c + 1 < 4 && grid[r][c] == grid[r][c + 1]) return false
            }
        }
        return true
    }

    private fun checkLevelProgression(score: Int, maxTile: Int, unlockedLevels: MutableSet<Int>) {
        for (level in GAME_LEVELS) {
            if (!unlockedLevels.contains(level.levelNumber)) {
                if (maxTile >= level.targetTile || score >= level.minScore) {
                    unlockedLevels.add(level.levelNumber)
                    _showLevelCompleteDialog.value = level
                    soundManager.playBigMerge()
                }
            }
        }
    }

    fun undoMove() {
        val current = _gameState.value
        if (current.undoStack.isEmpty()) return

        soundManager.playUndo()
        val previous = current.undoStack.first()
        val remainingStack = current.undoStack.drop(1)

        val newState = current.copy(
            grid = previous.grid,
            score = previous.score,
            isGameOver = false,
            undoCount = current.undoCount + 1,
            undoStack = remainingStack
        )

        val updatedWithAchievements = evaluateAchievements(newState)
        _gameState.value = updatedWithAchievements
        repository.saveGameState(updatedWithAchievements)
    }

    fun requestRestart() {
        soundManager.playTap()
        if (isConfirmRestart.value) {
            _showRestartDialog.value = true
        } else {
            confirmRestart()
        }
    }

    fun cancelRestartDialog() {
        soundManager.playTap()
        _showRestartDialog.value = false
    }

    fun confirmRestart() {
        soundManager.playTap()
        _showRestartDialog.value = false
        _showPauseModal.value = false
        _showVictoryDialog.value = false

        val current = _gameState.value
        val newGrid = createNewGameGrid()

        val newState = current.copy(
            grid = newGrid,
            score = 0,
            isGameOver = false,
            isWon = false,
            keepPlayingPast2048 = false,
            undoStack = emptyList()
        )

        _gameState.value = newState
        repository.saveGameState(newState)
    }

    fun togglePause() {
        soundManager.playTap()
        _showPauseModal.value = !_showPauseModal.value
    }

    fun continuePlayingPast2048() {
        soundManager.playTap()
        _showVictoryDialog.value = false
        val newState = _gameState.value.copy(keepPlayingPast2048 = true)
        val updated = evaluateAchievements(newState)
        _gameState.value = updated
        repository.saveGameState(updated)
    }

    fun dismissLevelCompleteDialog() {
        soundManager.playTap()
        _showLevelCompleteDialog.value = null
    }

    fun dismissAchievementToast() {
        _latestUnlockedAchievement.value = null
    }

    // Settings actions
    fun setMusic(enabled: Boolean) {
        isMusicEnabled.value = enabled
        repository.setMusicEnabled(enabled)
        syncAudioSettings()
    }

    fun setSfx(enabled: Boolean) {
        isSfxEnabled.value = enabled
        repository.setSfxEnabled(enabled)
        syncAudioSettings()
    }

    fun setVibration(enabled: Boolean) {
        isVibrationEnabled.value = enabled
        repository.setVibrationEnabled(enabled)
        syncAudioSettings()
    }

    fun setTheme(theme: String) {
        themePreference.value = theme
        repository.setThemePreference(theme)
        if (theme == "DARK") {
            val updated = evaluateAchievements(_gameState.value)
            _gameState.value = updated
        }
    }

    fun setRomanianTheme(enabled: Boolean) {
        isRomanianTheme.value = enabled
        repository.setRomanianThemeEnabled(enabled)
    }

    fun setAnimations(enabled: Boolean) {
        isAnimationsEnabled.value = enabled
        repository.setAnimationsEnabled(enabled)
    }

    fun setConfirmRestartPref(enabled: Boolean) {
        isConfirmRestart.value = enabled
        repository.setConfirmRestartEnabled(enabled)
    }

    fun requestResetData() {
        soundManager.playTap()
        _showResetConfirmDialog.value = true
    }

    fun cancelResetData() {
        soundManager.playTap()
        _showResetConfirmDialog.value = false
    }

    fun confirmResetData() {
        soundManager.playTap()
        _showResetConfirmDialog.value = false
        repository.resetAllData()

        val newGrid = createNewGameGrid()
        _gameState.value = GameState(
            grid = newGrid,
            score = 0,
            highScore = 0,
            unlockedLevels = setOf(1),
            unlockedCollectionValues = setOf(2)
        )
        repository.saveGameState(_gameState.value)
    }

    // Achievement evaluation engine
    private fun evaluateAchievements(state: GameState): GameState {
        val currentUnlocked = state.unlockedAchievementIds.toMutableSet()
        var newlyUnlockedAchievement: Achievement? = null

        fun check(idInt: Int, condition: Boolean) {
            if (!currentUnlocked.contains(idInt) && condition) {
                currentUnlocked.add(idInt)
                val ach = allAchievementsList.find { it.id == idInt.toString() }
                if (ach != null) {
                    newlyUnlockedAchievement = ach.copy(isUnlocked = true)
                }
            }
        }

        val tile = state.highestTileAchieved
        val score = state.score

        check(1, tile >= 2)
        check(2, tile >= 4)
        check(3, tile >= 8)
        check(4, tile >= 16)
        check(5, tile >= 32)
        check(6, tile >= 64)
        check(7, tile >= 128)
        check(8, tile >= 256)
        check(9, tile >= 512)
        check(10, tile >= 1024)
        check(11, tile >= 2048)
        check(12, tile >= 4096)
        check(13, tile >= 8192)
        check(14, state.keepPlayingPast2048)
        check(15, state.undoCount >= 3)
        check(16, state.movesCount >= 10)
        check(17, score >= 10000)
        check(18, score >= 25000)
        check(19, score >= 50000)
        check(20, state.unlockedLevels.size >= 5)
        check(21, state.unlockedCollectionValues.size >= 8)
        check(22, state.movesCount >= 50)
        check(23, state.movesCount >= 1 && score > 100)
        check(24, themePreference.value == "DARK")
        check(25, state.unlockedLevels.size >= 7)

        if (newlyUnlockedAchievement != null) {
            soundManager.playAchievement()
            _latestUnlockedAchievement.value = newlyUnlockedAchievement
        }

        return state.copy(unlockedAchievementIds = currentUnlocked)
    }

    override fun onCleared() {
        super.onCleared()
        soundManager.release()
    }
}
