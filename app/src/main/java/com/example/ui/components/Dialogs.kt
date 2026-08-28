package com.example.ui.components

import com.example.ui.strings.Localization
import com.example.ui.strings.Language
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.GameLevel
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.TricolorBlue
import com.example.ui.theme.TricolorRed

@Composable
fun RestartConfirmDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = Localization.strings.restartDialogTitle,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        },
        text = {
            Text(text = Localization.strings.restartDialogDesc)
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = TricolorRed),
                modifier = Modifier.testTag("btn_confirm_restart")
            ) {
                Text(text = Localization.strings.restartDialogConfirm, color = Color.White, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                modifier = Modifier.testTag("btn_cancel_restart")
            ) {
                Text(text = Localization.strings.btnCancel)
            }
        },
        shape = RoundedCornerShape(18.dp)
    )
}

@Composable
fun PauseModal(
    onResume: () -> Unit,
    onRestart: () -> Unit,
    onSettings: () -> Unit,
    onHome: () -> Unit
) {
    Dialog(onDismissRequest = onResume) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .testTag("pause_modal")
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = Localization.strings.pauseDialogTitle,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = onResume,
                    modifier = Modifier.fillMaxWidth().testTag("pause_btn_resume")
                ) {
                    Text(Localization.strings.btnContinuePlaying, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = onRestart,
                    colors = ButtonDefaults.buttonColors(containerColor = TricolorRed),
                    modifier = Modifier.fillMaxWidth().testTag("pause_btn_restart")
                ) {
                    Text("🔄 " + Localization.strings.restartBtn, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedButton(
                    onClick = onSettings,
                    modifier = Modifier.fillMaxWidth().testTag("pause_btn_settings")
                ) {
                    Text("⚙ " + Localization.strings.btnSettings)
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedButton(
                    onClick = onHome,
                    modifier = Modifier.fillMaxWidth().testTag("pause_btn_home")
                ) {
                    Text(Localization.strings.btnHome)
                }
            }
        }
    }
}

@Composable
fun VictoryDialog(
    onContinue: () -> Unit,
    onRestart: () -> Unit,
    onHome: () -> Unit
) {
    Dialog(onDismissRequest = onContinue) {
        Card(
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .border(2.dp, GoldAccent, RoundedCornerShape(22.dp))
                .testTag("victory_dialog")
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(24.dp)
            ) {
                Text(text = "🎉 🇷🇴", fontSize = 44.sp)

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = Localization.strings.victoryTitle,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = GoldAccent,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = Localization.strings.victoryDesc,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = onContinue,
                    colors = ButtonDefaults.buttonColors(containerColor = TricolorBlue),
                    modifier = Modifier.fillMaxWidth().testTag("victory_btn_continue")
                ) {
                    Text(Localization.strings.continueGameBtn, color = Color.White, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = onRestart,
                    colors = ButtonDefaults.buttonColors(containerColor = TricolorRed),
                    modifier = Modifier.fillMaxWidth().testTag("victory_btn_restart")
                ) {
                    Text(Localization.strings.btnPlayAgain, color = Color.White, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedButton(
                    onClick = onHome,
                    modifier = Modifier.fillMaxWidth().testTag("victory_btn_home")
                ) {
                    Text(Localization.strings.btnHome)
                }
            }
        }
    }
}

@Composable
fun GameOverDialog(
    score: Int,
    highScore: Int,
    onContinueGame: (() -> Unit)? = null,
    isAdsRemoved: Boolean = false,
    onRestart: () -> Unit,
    onHome: () -> Unit
) {
    val isNewRecord = score > 0 && score >= highScore

    Dialog(onDismissRequest = {}) {
        Card(
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .testTag("game_over_dialog")
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(24.dp)
            ) {
                Text(text = "💔", fontSize = 40.sp)

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = Localization.strings.gameOverTitle,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TricolorRed,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = Localization.strings.gameOverDesc,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                if (isNewRecord) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Card(
                        colors = CardDefaults.cardColors(containerColor = GoldAccent.copy(alpha = 0.2f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = Localization.strings.gameOverNewRecord + "$score",
                            fontWeight = FontWeight.Bold,
                            color = GoldAccent,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth().padding(8.dp)
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = Localization.strings.gameOverScore(score, highScore),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Voluntary Rewarded Ad Continue Option
                if (onContinueGame != null) {
                    Button(
                        onClick = onContinueGame,
                        colors = ButtonDefaults.buttonColors(containerColor = GoldAccent),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("gameover_btn_continue")
                    ) {
                        Text(
                            text = if (isAdsRemoved) Localization.strings.continueGameBtn else Localization.strings.continueAdBtn,
                            color = Color.Black,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                }

                Button(
                    onClick = onRestart,
                    colors = ButtonDefaults.buttonColors(containerColor = TricolorBlue),
                    modifier = Modifier.fillMaxWidth().testTag("gameover_btn_restart")
                ) {
                    Text(Localization.strings.btnPlayAgain, color = Color.White, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedButton(
                    onClick = onHome,
                    modifier = Modifier.fillMaxWidth().testTag("gameover_btn_home")
                ) {
                    Text(Localization.strings.btnHome)
                }
            }
        }
    }
}

@Composable
fun ExtraUndoDialog(
    isAdsRemoved: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (isAdsRemoved) Localization.strings.extraUndoTitle else Localization.strings.extraUndoTitle,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        },
        text = {
            Text(
                text = if (isAdsRemoved)
                    Localization.strings.extraUndoDescFree
                else
                    Localization.strings.extraUndoDescAd
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = TricolorBlue),
                modifier = Modifier.testTag("btn_confirm_extra_undo")
            ) {
                Text(
                    text = if (isAdsRemoved) Localization.strings.extraUndoBtnFree else Localization.strings.extraUndoBtnAd,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                modifier = Modifier.testTag("btn_cancel_extra_undo")
            ) {
                Text(text = Localization.strings.btnCancel)
            }
        },
        shape = RoundedCornerShape(18.dp)
    )
}

@Composable
fun LevelCompleteDialog(
    level: GameLevel,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .border(2.dp, GoldAccent, RoundedCornerShape(22.dp))
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(24.dp)
            ) {
                Text(text = "🎉 🎖️", fontSize = 40.sp)

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = Localization.strings.levelCompleteTitle,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = GoldAccent,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = level.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Text(
                            text = Localization.strings.rewardUnlocked,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "${level.rewardName} (+${level.rewardBonus}" + Localization.strings.pointsSuffix + ")",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = TricolorBlue),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(Localization.strings.btnContinuePlaying, color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun ResetConfirmDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = Localization.strings.resetProgressTitle,
                fontWeight = FontWeight.Bold,
                color = TricolorRed
            )
        },
        text = {
            Text(text = Localization.strings.resetProgressDesc)
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = TricolorRed),
                modifier = Modifier.testTag("btn_confirm_reset_all")
            ) {
                Text(text = Localization.strings.resetProgressConfirm, color = Color.White, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                modifier = Modifier.testTag("btn_cancel_reset_all")
            ) {
                Text(text = Localization.strings.btnCancel)
            }
        }
    )
}
