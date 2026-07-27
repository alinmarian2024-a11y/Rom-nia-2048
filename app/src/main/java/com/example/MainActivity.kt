package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.GameState
import com.example.ui.components.AchievementToast
import com.example.ui.components.GameOverDialog
import com.example.ui.components.LevelCompleteDialog
import com.example.ui.components.RestartConfirmDialog
import com.example.ui.components.VictoryDialog
import com.example.ui.screens.AboutScreen
import com.example.ui.screens.AchievementsScreen
import com.example.ui.screens.CollectionScreen
import com.example.ui.screens.GameScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LevelsScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.theme.Romania2048Theme
import com.example.viewmodel.AppScreen
import com.example.viewmodel.GameViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val gameViewModel: GameViewModel = viewModel()
            val themePref by gameViewModel.themePreference.collectAsState()
            val isRomanianTheme by gameViewModel.isRomanianTheme.collectAsState()

            val isDarkTheme = when (themePref) {
                "DARK" -> true
                "LIGHT" -> false
                else -> isSystemInDarkTheme()
            }

            Romania2048Theme(
                themePreference = themePref,
                isRomanianTheme = isRomanianTheme
            ) {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainAppContent(
                        viewModel = gameViewModel,
                        isDarkTheme = isDarkTheme,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun MainAppContent(
    viewModel: GameViewModel,
    isDarkTheme: Boolean,
    modifier: Modifier = Modifier
) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val gameState by viewModel.gameState.collectAsState()

    val showRestartDialog by viewModel.showRestartDialog.collectAsState()
    val showVictoryDialog by viewModel.showVictoryDialog.collectAsState()
    val levelCompleteDialog by viewModel.showLevelCompleteDialog.collectAsState()
    val latestAchievement by viewModel.latestUnlockedAchievement.collectAsState()

    // Handle back button behavior
    BackHandler(enabled = currentScreen != AppScreen.HOME) {
        viewModel.navigateTo(AppScreen.HOME)
    }

    Box(modifier = modifier.fillMaxSize()) {
        when (currentScreen) {
            AppScreen.HOME -> HomeScreen(
                gameState = gameState,
                onNavigate = { viewModel.navigateTo(it) }
            )
            AppScreen.GAME -> GameScreen(
                viewModel = viewModel,
                gameState = gameState,
                isDarkTheme = isDarkTheme,
                onNavigate = { viewModel.navigateTo(it) }
            )
            AppScreen.LEVELS -> LevelsScreen(
                gameState = gameState,
                onNavigate = { viewModel.navigateTo(it) },
                onSelectLevel = { levelNum ->
                    // Set current level
                    viewModel.navigateTo(AppScreen.GAME)
                }
            )
            AppScreen.COLLECTION -> CollectionScreen(
                gameState = gameState,
                onNavigate = { viewModel.navigateTo(it) }
            )
            AppScreen.ACHIEVEMENTS -> AchievementsScreen(
                viewModel = viewModel,
                gameState = gameState,
                onNavigate = { viewModel.navigateTo(it) }
            )
            AppScreen.SETTINGS -> SettingsScreen(
                viewModel = viewModel,
                onNavigate = { viewModel.navigateTo(it) }
            )
            AppScreen.ABOUT -> AboutScreen(
                onNavigate = { viewModel.navigateTo(it) }
            )
        }

        // Achievement Toast Overlay
        AchievementToast(
            achievement = latestAchievement,
            onDismiss = { viewModel.dismissAchievementToast() },
            modifier = Modifier.align(Alignment.TopCenter)
        )

        // Global Dialog Overlays
        if (showRestartDialog) {
            RestartConfirmDialog(
                onConfirm = { viewModel.confirmRestart() },
                onDismiss = { viewModel.cancelRestartDialog() }
            )
        }

        if (showVictoryDialog) {
            VictoryDialog(
                onContinue = { viewModel.continuePlayingPast2048() },
                onRestart = { viewModel.confirmRestart() },
                onHome = { viewModel.navigateTo(AppScreen.HOME) }
            )
        }

        if (gameState.isGameOver) {
            GameOverDialog(
                score = gameState.score,
                highScore = gameState.highScore,
                onRestart = { viewModel.confirmRestart() },
                onHome = { viewModel.navigateTo(AppScreen.HOME) }
            )
        }

        levelCompleteDialog?.let { level ->
            LevelCompleteDialog(
                level = level,
                onDismiss = { viewModel.dismissLevelCompleteDialog() }
            )
        }
    }
}
