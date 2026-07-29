package com.example.viewmodel

import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.SoundManager
import com.example.data.GameRepository
import com.example.model.Achievement
import com.example.model.GAME_LEVELS
import com.example.model.GameLevel
import com.example.model.GameMode
import com.example.model.GameState
import com.example.model.GridSnapshot
import com.example.model.RomaniaItem
import com.example.model.Tile
import com.example.model.TileRegistry
import com.example.monetization.AdManager
import com.example.monetization.BillingManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

enum class AppScreen {
    HOME,
    MODE_SELECTION,
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
    val adManager = AdManager(application)
    val billingManager = BillingManager(application, repository.isAdsRemoved()) { removed ->
        repository.setAdsRemoved(removed)
    }

    val isAdsRemoved: StateFlow<Boolean> = billingManager.isAdsRemoved
    val formattedPrice: StateFlow<String?> = billingManager.formattedPrice
    val billingStatusMessage: StateFlow<String?> = billingManager.billingStatusMessage
    val isAdReady: StateFlow<Boolean> = adManager.isAdReady

    private val _showExtraUndoDialog = MutableStateFlow(false)
    val showExtraUndoDialog: StateFlow<Boolean> = _showExtraUndoDialog.asStateFlow()

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
    val musicVolume = MutableStateFlow(repository.getMusicVolume())
    val sfxVolume = MutableStateFlow(repository.getSfxVolume())
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
        val loadedState = repository.loadGameState(GameMode.INFINITE)
        if (isGridEmpty(loadedState.grid)) {
            val initialGrid = createNewGameGrid()
            val initialTiles = syncTilesFromGrid(initialGrid)
            _gameState.value = loadedState.copy(grid = initialGrid, tiles = initialTiles)
            repository.saveGameState(_gameState.value)
        } else {
            val initialTiles = syncTilesFromGrid(loadedState.grid)
            _gameState.value = loadedState.copy(tiles = initialTiles)
        }
    }

    fun selectGameMode(mode: GameMode) {
        soundManager.playTap()
        val loadedState = repository.loadGameState(mode)
        val activeGrid = if (isGridEmpty(loadedState.grid)) createNewGameGrid() else loadedState.grid
        val activeTiles = syncTilesFromGrid(activeGrid)

        _gameState.value = loadedState.copy(
            gameMode = mode,
            grid = activeGrid,
            tiles = activeTiles
        )
        repository.saveGameState(_gameState.value)

        if (mode == GameMode.INFINITE) {
            _currentScreen.value = AppScreen.GAME
            if (isMusicEnabled.value) soundManager.startMusic(isGameplay = true)
        } else {
            _currentScreen.value = AppScreen.LEVELS
            if (isMusicEnabled.value) soundManager.startMusic(isGameplay = false)
        }
    }

    fun startAdventureLevel(levelNum: Int) {
        soundManager.playTap()
        val current = _gameState.value
        val newGrid = createNewGameGrid()
        val newTiles = syncTilesFromGrid(newGrid)

        val newState = current.copy(
            gameMode = GameMode.ADVENTURE,
            currentLevel = levelNum,
            grid = newGrid,
            tiles = newTiles,
            score = 0,
            isGameOver = false,
            isWon = false,
            undoStack = emptyList()
        )

        _gameState.value = newState
        repository.saveGameState(newState)
        _currentScreen.value = AppScreen.GAME
        if (isMusicEnabled.value) soundManager.startMusic(isGameplay = true)
    }

    fun syncTilesFromGrid(grid: List<List<Int>>, existingTiles: List<Tile> = emptyList()): List<Tile> {
        var maxId = existingTiles.maxOfOrNull { it.id } ?: 0L
        val usedIds = mutableSetOf<Long>()
        val result = mutableListOf<Tile>()
        for (r in 0..3) {
            for (c in 0..3) {
                val v = grid[r][c]
                if (v != 0) {
                    val match = existingTiles.find { it.row == r && it.col == c && it.value == v && !usedIds.contains(it.id) }
                    if (match != null) {
                        usedIds.add(match.id)
                        result.add(match.copy(isMerged = false, isNew = false))
                    } else {
                        maxId++
                        usedIds.add(maxId)
                        result.add(Tile(id = maxId, value = v, row = r, col = c, isNew = false, isMerged = false))
                    }
                }
            }
        }
        return result
    }

    fun onPauseApp() {
        soundManager.pauseMusic()
    }

    fun onResumeApp() {
        if (isMusicEnabled.value) {
            val isGameplay = _currentScreen.value == AppScreen.GAME
            soundManager.startMusic(isGameplay)
        }
    }

    private fun syncAudioSettings() {
        soundManager.isMusicEnabled = isMusicEnabled.value
        soundManager.musicVolume = musicVolume.value
        soundManager.sfxVolume = sfxVolume.value
        soundManager.isSfxEnabled = isSfxEnabled.value
        soundManager.isVibrationEnabled = isVibrationEnabled.value
        if (isMusicEnabled.value) {
            val isGameplay = _currentScreen.value == AppScreen.GAME
            soundManager.startMusic(isGameplay)
        } else {
            soundManager.stopMusic()
        }
    }

    fun navigateTo(screen: AppScreen) {
        soundManager.playTap()
        _currentScreen.value = screen
        if (isMusicEnabled.value) {
            val isGameplay = screen == AppScreen.GAME
            soundManager.startMusic(isGameplay)
        }
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

    // Move logic for 2048 with smooth tile animation support
    fun move(direction: Direction) {
        val current = _gameState.value
        if (current.isGameOver) return
        if (_showPauseModal.value) return

        val currentGrid = current.grid
        val currentTiles = if (current.tiles.isEmpty()) syncTilesFromGrid(currentGrid) else current.tiles

        val (newGrid, newTiles, scoreGained, moved) = processMoveWithTiles(currentGrid, currentTiles, direction)

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

        val newScore = current.score + scoreGained
        val newHighScore = maxOf(current.highScore, newScore)
        val newMovesCount = current.movesCount + 1

        // Find max tile
        var maxTile = current.highestTileAchieved
        val newlyUnlockedCollection = current.unlockedCollectionValues.toMutableSet()

        for (r in 0..3) {
            for (c in 0..3) {
                val v = newGrid[r][c]
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

        val gameOverNow = checkGameOver(newGrid)
        if (gameOverNow && !current.isGameOver) {
            soundManager.playGameOver()
        }

        // Update levels
        val unlockedLevs = current.unlockedLevels.toMutableSet()
        val completedLevs = current.completedLevels.toMutableSet()

        if (current.gameMode == com.example.model.GameMode.ADVENTURE) {
            val levelObj = GAME_LEVELS.find { it.levelNumber == current.currentLevel }
            if (levelObj != null) {
                if (maxTile >= levelObj.targetTile || newScore >= levelObj.minScore) {
                    if (!completedLevs.contains(levelObj.levelNumber)) {
                        completedLevs.add(levelObj.levelNumber)
                        val nextLevelNum = levelObj.levelNumber + 1
                        if (nextLevelNum <= GAME_LEVELS.size) {
                            unlockedLevs.add(nextLevelNum)
                        }
                        _showLevelCompleteDialog.value = levelObj
                        soundManager.playBigMerge()
                    }
                }
            }
        } else {
            checkLevelProgression(newScore, maxTile, unlockedLevs, completedLevs)
        }

        var newState = current.copy(
            grid = newGrid,
            tiles = newTiles,
            score = newScore,
            highScore = newHighScore,
            movesCount = newMovesCount,
            highestTileAchieved = maxTile,
            isWon = isWonNow,
            isGameOver = gameOverNow,
            undoStack = newUndoStack,
            unlockedLevels = unlockedLevs,
            completedLevels = completedLevs,
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

    private data class MoveResult(
        val grid: List<List<Int>>,
        val tiles: List<Tile>,
        val scoreGained: Int,
        val hasMoved: Boolean
    )

    private fun processMoveWithTiles(
        currentGrid: List<List<Int>>,
        currentTiles: List<Tile>,
        direction: Direction
    ): MoveResult {
        val cleanCurrentTiles = syncTilesFromGrid(currentGrid, currentTiles)
        var maxId = cleanCurrentTiles.maxOfOrNull { it.id } ?: 0L
        fun nextId(): Long = ++maxId

        val tileMap = mutableMapOf<Pair<Int, Int>, Tile>()
        for (t in cleanCurrentTiles) {
            tileMap[Pair(t.row, t.col)] = t
        }

        val newTiles = mutableListOf<Tile>()
        var totalScoreGained = 0
        var hasMoved = false

        when (direction) {
            Direction.LEFT -> {
                for (r in 0..3) {
                    val line = (0..3).mapNotNull { c -> tileMap[Pair(r, c)] }
                    var targetCol = 0
                    var i = 0
                    while (i < line.size) {
                        if (i + 1 < line.size && line[i].value == line[i + 1].value) {
                            val t1 = line[i]
                            val mergedVal = t1.value * 2
                            totalScoreGained += mergedVal

                            newTiles.add(t1.copy(row = r, col = targetCol, value = mergedVal, isMerged = true, isNew = false))

                            hasMoved = true
                            targetCol++
                            i += 2
                        } else {
                            val t = line[i]
                            val updated = t.copy(row = r, col = targetCol, isMerged = false, isNew = false)
                            newTiles.add(updated)
                            if (t.row != r || t.col != targetCol) {
                                hasMoved = true
                            }
                            targetCol++
                            i++
                        }
                    }
                }
            }
            Direction.RIGHT -> {
                for (r in 0..3) {
                    val line = (0..3).mapNotNull { c -> tileMap[Pair(r, c)] }.reversed()
                    var targetCol = 3
                    var i = 0
                    while (i < line.size) {
                        if (i + 1 < line.size && line[i].value == line[i + 1].value) {
                            val t1 = line[i]
                            val mergedVal = t1.value * 2
                            totalScoreGained += mergedVal

                            newTiles.add(t1.copy(row = r, col = targetCol, value = mergedVal, isMerged = true, isNew = false))

                            hasMoved = true
                            targetCol--
                            i += 2
                        } else {
                            val t = line[i]
                            val updated = t.copy(row = r, col = targetCol, isMerged = false, isNew = false)
                            newTiles.add(updated)
                            if (t.row != r || t.col != targetCol) {
                                hasMoved = true
                            }
                            targetCol--
                            i++
                        }
                    }
                }
            }
            Direction.UP -> {
                for (c in 0..3) {
                    val line = (0..3).mapNotNull { r -> tileMap[Pair(r, c)] }
                    var targetRow = 0
                    var i = 0
                    while (i < line.size) {
                        if (i + 1 < line.size && line[i].value == line[i + 1].value) {
                            val t1 = line[i]
                            val mergedVal = t1.value * 2
                            totalScoreGained += mergedVal

                            newTiles.add(t1.copy(row = targetRow, col = c, value = mergedVal, isMerged = true, isNew = false))

                            hasMoved = true
                            targetRow++
                            i += 2
                        } else {
                            val t = line[i]
                            val updated = t.copy(row = targetRow, col = c, isMerged = false, isNew = false)
                            newTiles.add(updated)
                            if (t.row != targetRow || t.col != c) {
                                hasMoved = true
                            }
                            targetRow++
                            i++
                        }
                    }
                }
            }
            Direction.DOWN -> {
                for (c in 0..3) {
                    val line = (0..3).mapNotNull { r -> tileMap[Pair(r, c)] }.reversed()
                    var targetRow = 3
                    var i = 0
                    while (i < line.size) {
                        if (i + 1 < line.size && line[i].value == line[i + 1].value) {
                            val t1 = line[i]
                            val mergedVal = t1.value * 2
                            totalScoreGained += mergedVal

                            newTiles.add(t1.copy(row = targetRow, col = c, value = mergedVal, isMerged = true, isNew = false))

                            hasMoved = true
                            targetRow--
                            i += 2
                        } else {
                            val t = line[i]
                            val updated = t.copy(row = targetRow, col = c, isMerged = false, isNew = false)
                            newTiles.add(updated)
                            if (t.row != targetRow || t.col != c) {
                                hasMoved = true
                            }
                            targetRow--
                            i++
                        }
                    }
                }
            }
        }

        if (!hasMoved) {
            return MoveResult(currentGrid, cleanCurrentTiles, 0, false)
        }

        val resultGrid = List(4) { MutableList(4) { 0 } }
        for (t in newTiles) {
            resultGrid[t.row][t.col] = t.value
        }

        val emptyCells = mutableListOf<Pair<Int, Int>>()
        for (r in 0..3) {
            for (c in 0..3) {
                if (resultGrid[r][c] == 0) emptyCells.add(Pair(r, c))
            }
        }

        if (emptyCells.isNotEmpty()) {
            val (spawnR, spawnC) = emptyCells[Random.nextInt(emptyCells.size)]
            val spawnVal = if (Random.nextFloat() < 0.9f) 2 else 4
            resultGrid[spawnR][spawnC] = spawnVal
            val spawnedTile = Tile(
                id = nextId(),
                value = spawnVal,
                row = spawnR,
                col = spawnC,
                isNew = true,
                isMerged = false
            )
            newTiles.add(spawnedTile)
        }

        return MoveResult(resultGrid, newTiles, totalScoreGained, true)
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

    private fun checkLevelProgression(
        score: Int,
        maxTile: Int,
        unlockedLevels: MutableSet<Int>,
        completedLevels: MutableSet<Int>
    ) {
        for (level in GAME_LEVELS) {
            if (maxTile >= level.targetTile || score >= level.minScore) {
                if (!completedLevels.contains(level.levelNumber)) {
                    completedLevels.add(level.levelNumber)
                    val nextLevelNum = level.levelNumber + 1
                    if (nextLevelNum <= GAME_LEVELS.size) {
                        unlockedLevels.add(nextLevelNum)
                    }
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
        val restoredTiles = syncTilesFromGrid(previous.grid)

        val newState = current.copy(
            grid = previous.grid,
            tiles = restoredTiles,
            score = previous.score,
            isGameOver = false,
            undoCount = current.undoCount + 1,
            undoStack = remainingStack
        )

        val updatedWithAchievements = evaluateAchievements(newState)
        _gameState.value = updatedWithAchievements
        repository.saveGameState(updatedWithAchievements)
    }

    // Monetization & Extra Undo / Continue functions
    fun handleUndoClick(activity: Activity?) {
        if (_showPauseModal.value) return
        val current = _gameState.value
        // 3 free undos rule: if under 3 undos used and stack is not empty
        if (current.undoCount < 3 && current.undoStack.isNotEmpty()) {
            undoMove()
        } else {
            _showExtraUndoDialog.value = true
        }
    }

    fun dismissExtraUndoDialog() {
        _showExtraUndoDialog.value = false
    }

    fun performExtraUndo(activity: Activity) {
        if (isAdsRemoved.value) {
            _showExtraUndoDialog.value = false
            grantExtraUndoInternal()
        } else {
            adManager.showRewardedAd(
                activity = activity,
                onRewarded = {
                    _showExtraUndoDialog.value = false
                    grantExtraUndoInternal()
                },
                onClosedOrFailed = {
                    // Ad closed before completion or not ready - reward not granted
                }
            )
        }
    }

    private fun grantExtraUndoInternal() {
        val current = _gameState.value
        soundManager.playUndo()

        val previous = current.undoStack.firstOrNull()
        val newState = if (previous != null) {
            val remainingStack = current.undoStack.drop(1)
            val restoredTiles = syncTilesFromGrid(previous.grid)
            current.copy(
                grid = previous.grid,
                tiles = restoredTiles,
                score = previous.score,
                isGameOver = false,
                undoCount = current.undoCount + 1,
                undoStack = remainingStack
            )
        } else {
            // Free up lowest tiles if stack is empty
            val newGrid = current.grid.map { it.toMutableList() }
            var cleared = 0
            for (r in 0..3) {
                for (c in 0..3) {
                    if ((newGrid[r][c] == 2 || newGrid[r][c] == 4) && cleared < 2) {
                        newGrid[r][c] = 0
                        cleared++
                    }
                }
            }
            val restoredTiles = syncTilesFromGrid(newGrid)
            current.copy(
                grid = newGrid,
                tiles = restoredTiles,
                isGameOver = false,
                undoCount = current.undoCount + 1
            )
        }

        val updated = evaluateAchievements(newState)
        _gameState.value = updated
        repository.saveGameState(updated)
    }

    fun handleGameOverContinue(activity: Activity) {
        if (isAdsRemoved.value) {
            continueGameAfterGameOver()
        } else {
            adManager.showRewardedAd(
                activity = activity,
                onRewarded = {
                    continueGameAfterGameOver()
                },
                onClosedOrFailed = {
                    // Ad closed before completion or failed - reward not granted
                }
            )
        }
    }

    fun continueGameAfterGameOver() {
        val current = _gameState.value
        soundManager.playUndo()

        val previous = current.undoStack.firstOrNull()
        val newState = if (previous != null) {
            val remainingStack = current.undoStack.drop(1)
            val restoredTiles = syncTilesFromGrid(previous.grid)
            current.copy(
                grid = previous.grid,
                tiles = restoredTiles,
                score = previous.score,
                isGameOver = false,
                undoStack = remainingStack
            )
        } else {
            // Clear space for 2 lowest tiles
            val newGrid = current.grid.map { it.toMutableList() }
            var cleared = 0
            for (r in 0..3) {
                for (c in 0..3) {
                    if ((newGrid[r][c] == 2 || newGrid[r][c] == 4) && cleared < 2) {
                        newGrid[r][c] = 0
                        cleared++
                    }
                }
            }
            val restoredTiles = syncTilesFromGrid(newGrid)
            current.copy(
                grid = newGrid,
                tiles = restoredTiles,
                isGameOver = false
            )
        }

        val updated = evaluateAchievements(newState)
        _gameState.value = updated
        repository.saveGameState(updated)
    }

    fun purchaseRemoveAds(activity: Activity) {
        billingManager.launchBillingFlow(activity)
    }

    fun restorePurchases() {
        billingManager.queryPurchases()
    }

    fun clearBillingMessage() {
        billingManager.clearStatusMessage()
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
        val newTiles = syncTilesFromGrid(newGrid)

        val newState = current.copy(
            grid = newGrid,
            tiles = newTiles,
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
        val willPause = !_showPauseModal.value
        _showPauseModal.value = willPause
        soundManager.setMusicPausedState(willPause)
    }
    
    fun resumeFromPause() {
        soundManager.playTap()
        _showPauseModal.value = false
        soundManager.setMusicPausedState(false)
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
        val currentLevelCompleted = _showLevelCompleteDialog.value
        _showLevelCompleteDialog.value = null
        if (currentLevelCompleted != null && _gameState.value.gameMode == com.example.model.GameMode.ADVENTURE) {
            val nextLevelNum = currentLevelCompleted.levelNumber + 1
            if (nextLevelNum <= GAME_LEVELS.size) {
                startAdventureLevel(nextLevelNum)
            } else {
                navigateTo(AppScreen.LEVELS)
            }
        }
    }

    fun dismissAchievementToast() {
        _latestUnlockedAchievement.value = null
    }

    // Settings actions
    fun setMusicVolume(vol: Float) {
        isMusicEnabled.value = true
        repository.setMusicEnabled(true)
        soundManager.isMusicEnabled = true
        musicVolume.value = vol
        repository.setMusicVolume(vol)
        soundManager.musicVolume = vol
        soundManager.updateMusicVolume()
    }

    fun setSfxVolume(vol: Float) {
        isSfxEnabled.value = true
        repository.setSfxEnabled(true)
        soundManager.isSfxEnabled = true
        sfxVolume.value = vol
        repository.setSfxVolume(vol)
        soundManager.sfxVolume = vol
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
        val newTiles = syncTilesFromGrid(newGrid)
        _gameState.value = GameState(
            grid = newGrid,
            tiles = newTiles,
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