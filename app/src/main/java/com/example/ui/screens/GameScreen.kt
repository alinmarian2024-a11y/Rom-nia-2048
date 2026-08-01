package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.app.Activity
import androidx.compose.ui.platform.LocalContext
import com.example.model.GameState
import com.example.ui.components.BoardView
import com.example.ui.components.GameOverDialog
import com.example.ui.components.LevelCompleteDialog
import com.example.ui.components.PauseModal
import com.example.ui.components.RestartConfirmDialog
import com.example.ui.components.VictoryDialog
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.TricolorBlue
import com.example.ui.theme.TricolorRed
import com.example.viewmodel.AppScreen
import com.example.viewmodel.Direction
import com.example.viewmodel.GameViewModel

@Composable
fun GameScreen(
    viewModel: GameViewModel,
    gameState: GameState,
    isDarkTheme: Boolean,
    onNavigate: (AppScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    var showQuickMenu by remember { mutableStateOf(false) }
    val isPaused by viewModel.showPauseModal.collectAsState()
    val scrollState = rememberScrollState()

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Top Bar
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(
                    onClick = { showQuickMenu = true },
                    modifier = Modifier.testTag("btn_quick_menu")
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Meniu Rapid",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(28.dp)
                    )
                }

                val isAdventure = gameState.gameMode == com.example.model.GameMode.ADVENTURE
                Text(
                    text = if (isAdventure) "🗺️ NIVEL ${gameState.currentLevel}" else "♾️ MOD INFINIT",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )

                IconButton(
                    onClick = { viewModel.togglePause() },
                    modifier = Modifier.testTag("btn_pause")
                ) {
                    Icon(
                        imageVector = Icons.Default.Pause,
                        contentDescription = "Pauză",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Score & Record Panel
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("card_score")
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)
                    ) {
                        Text(
                            text = "SCOR",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "%08d".format(gameState.score),
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("card_high_score")
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)
                    ) {
                        Text(
                            text = "RECORD",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldAccent
                        )
                        Text(
                            text = "%08d".format(gameState.highScore),
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Black,
                            color = GoldAccent
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Undo & Restart Buttons
            val context = LocalContext.current
            val activity = context as? Activity
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                val freeUndosRemaining = maxOf(0, 3 - gameState.undoCount)
                val isFreeUndoAvailable = freeUndosRemaining > 0 && gameState.undoStack.isNotEmpty()

                OutlinedButton(
                    onClick = { viewModel.handleUndoClick(activity) },
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .testTag("btn_undo")
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.AutoMirrored.Filled.Undo, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isFreeUndoAvailable) {
                                "UNDO ($freeUndosRemaining/3)"
                            } else {
                                "UNDO 🎬"
                            },
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp
                        )
                    }
                }

                Button(
                    onClick = { viewModel.requestRestart() },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = TricolorRed),
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .testTag("btn_restart")
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Refresh, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "RESTART",
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 4x4 Grid Board
            val isAnimationsEnabled by viewModel.isAnimationsEnabled.collectAsState()

            BoardView(
                grid = gameState.grid,
                tiles = gameState.tiles,
                isDarkTheme = isDarkTheme,
                isAnimationsEnabled = isAnimationsEnabled,
                onMove = { direction -> viewModel.move(direction) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Objective & Progress Banner
            val isAdventureMode = gameState.gameMode == com.example.model.GameMode.ADVENTURE
            val levelObj = com.example.model.GAME_LEVELS.find { it.levelNumber == gameState.currentLevel }
            val targetItem = levelObj?.let { com.example.model.TileRegistry.getItem(it.targetTile) }

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier.fillMaxWidth().testTag("objective_banner")
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(14.dp)
                ) {
                    Text(
                        text = if (isAdventureMode && targetItem != null) {
                            "🎯 OBIECTIV NIVEL ${gameState.currentLevel}: Descoperă ${targetItem.emoji} ${targetItem.name} (${levelObj.targetTile})"
                        } else {
                            "🎯 OBIECTIV: Ajunge la piesa 2048!"
                        },
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldAccent,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = if (isAdventureMode && targetItem != null) {
                            "„Combină două piese identice pentru a descoperi ${targetItem.name}!”"
                        } else {
                            "„Combină două piese identice pentru a construi România, piesă cu piesă!”"
                        },
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Quick Menu Drawer / Modal
        if (showQuickMenu) {
            PauseModal(
                onResume = { showQuickMenu = false },
                onRestart = {
                    showQuickMenu = false
                    viewModel.requestRestart()
                },
                onSettings = {
                    showQuickMenu = false
                    onNavigate(AppScreen.SETTINGS)
                },
                onHome = {
                    showQuickMenu = false
                    onNavigate(AppScreen.HOME)
                }
            )
        }

        if (isPaused) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.8f))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {},
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(32.dp)
                ) {
                    Text(
                        text = "JOC ÎN PAUZĂ",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(48.dp))
                    Button(
                        onClick = { viewModel.resumeFromPause() },
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .height(64.dp)
                    ) {
                        Text(
                            text = "CONTINUĂ JOCUL",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        }
    }
}

