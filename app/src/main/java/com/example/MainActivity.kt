package com.example

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
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
import androidx.compose.ui.platform.LocalContext
import com.example.ui.components.AchievementToast
import com.example.ui.components.ExtraUndoDialog
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
import com.example.ui.screens.ModeSelectionScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.theme.Romania2048Theme
import com.example.viewmodel.AppScreen
import com.example.viewmodel.GameViewModel

class MainActivity : ComponentActivity() {

    private val gameViewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
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

        // UMP is checked on every app launch. Ad requests start only after consent allows them.
        gameViewModel.requestConsent(this)
    }

    override fun onPause() {
        super.onPause()
        gameViewModel.onPauseApp()
    }

    override fun onResume() {
        super.onResume()
        gameViewModel.onResumeApp()
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

    val isAdsRemoved by viewModel.isAdsRemoved.collectAsState()
    val formattedPrice by viewModel.formattedPrice.collectAsState()
    val billingStatusMessage by viewModel.billingStatusMessage.collectAsState()
    val showExtraUndoDialog by viewModel.showExtraUndoDialog.collectAsState()

    val context = LocalContext.current
    val activity = context as? Activity

    val runSafeAdOpportunity: (() -> Unit) -> Unit = { action ->
        if (activity != null) {
            viewModel.runInterstitialThen(activity, action)
        } else {
            action()
        }
    }

    BackHandler(enabled = currentScreen != AppScreen.HOME) {
        if (currentScreen == AppScreen.GAME) {
            runSafeAdOpportunity { viewModel.navigateTo(AppScreen.HOME) }
        } else {
            viewModel.navigateTo(AppScreen.HOME)
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        when (currentScreen) {
            AppScreen.HOME -> HomeScreen(
                gameState = gameState,
                onNavigate = { viewModel.navigateTo(it) },
                isAdsRemoved = isAdsRemoved,
                formattedPrice = formattedPrice,
                billingStatusMessage = billingStatusMessage,
                onPurchaseRemoveAds = {
                    activity?.let(viewModel::purchaseRemoveAds)
                },
                onRestorePurchases = viewModel::restorePurchases,
                onClearBillingMessage = viewModel::clearBillingMessage
            )

            AppScreen.MODE_SELECTION -> ModeSelectionScreen(
                gameState = gameState,
                onNavigate = { viewModel.navigateTo(it) },
                onSelectMode = viewModel::selectGameMode
            )

            AppScreen.GAME -> GameScreen(
                viewModel = viewModel,
                gameState = gameState,
                isDarkTheme = isDarkTheme,
                onNavigate = { target ->
                    if (target != AppScreen.GAME) {
                        runSafeAdOpportunity { viewModel.navigateTo(target) }
                    } else {
                        viewModel.navigateTo(target)
                    }
                }
            )

            AppScreen.LEVELS -> LevelsScreen(
                gameState = gameState,
                onNavigate = { viewModel.navigateTo(it) },
                onSelectLevel = viewModel::startAdventureLevel
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

        AchievementToast(
            achievement = latestAchievement,
            onDismiss = viewModel::dismissAchievementToast,
            modifier = Modifier.align(Alignment.TopCenter)
        )

        if (showRestartDialog) {
            RestartConfirmDialog(
                onConfirm = {
                    runSafeAdOpportunity(viewModel::confirmRestart)
                },
                onDismiss = viewModel::cancelRestartDialog
            )
        }

        if (showVictoryDialog) {
            VictoryDialog(
                onContinue = {
                    runSafeAdOpportunity(viewModel::continuePlayingPast2048)
                },
                onRestart = {
                    runSafeAdOpportunity(viewModel::confirmRestart)
                },
                onHome = {
                    runSafeAdOpportunity { viewModel.navigateTo(AppScreen.HOME) }
                }
            )
        }

        if (gameState.isGameOver) {
            GameOverDialog(
                score = gameState.score,
                highScore = gameState.highScore,
                onContinueGame = activity?.let { act ->
                    { viewModel.handleGameOverContinue(act) }
                },
                onRestart = {
                    runSafeAdOpportunity(viewModel::confirmRestart)
                },
                onHome = {
                    runSafeAdOpportunity { viewModel.navigateTo(AppScreen.HOME) }
                }
            )
        }

        if (showExtraUndoDialog) {
            ExtraUndoDialog(
                onConfirm = {
                    activity?.let(viewModel::performExtraUndo)
                },
                onDismiss = viewModel::dismissExtraUndoDialog
            )
        }

        levelCompleteDialog?.let { level ->
            LevelCompleteDialog(
                level = level,
                onDismiss = {
                    runSafeAdOpportunity(viewModel::dismissLevelCompleteDialog)
                }
            )
        }
    }
}
