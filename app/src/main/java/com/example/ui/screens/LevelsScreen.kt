package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.GAME_LEVELS
import com.example.model.GameLevel
import com.example.model.GameState
import com.example.model.TileRegistry
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.TricolorBlue
import com.example.ui.theme.TricolorRed
import com.example.viewmodel.AppScreen

@Composable
fun LevelsScreen(
    gameState: GameState,
    onNavigate: (AppScreen) -> Unit,
    onSelectLevel: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // Top Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(
                onClick = { onNavigate(AppScreen.HOME) },
                modifier = Modifier.testTag("btn_back_home_from_levels")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Înapoi",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "🗺 NIVELURI",
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Progresează prin nivelurile României combinând piese și atingând obiectivele stabilite!",
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(GAME_LEVELS) { level ->
                val isUnlocked = gameState.unlockedLevels.contains(level.levelNumber)
                val isCompleted = gameState.highestTileAchieved >= level.targetTile || gameState.highScore >= level.minScore
                val targetItem = TileRegistry.getItem(level.targetTile)

                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isUnlocked) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = if (isUnlocked) 6.dp else 1.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = if (isCompleted) 2.dp else 1.dp,
                            color = when {
                                isCompleted -> GoldAccent
                                isUnlocked -> TricolorBlue
                                else -> Color.Transparent
                            },
                            shape = RoundedCornerShape(18.dp)
                        )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        // Emoji icon badge
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(56.dp)
                                .background(
                                    color = if (isUnlocked) targetItem.backgroundColor else Color.Gray.copy(alpha = 0.3f),
                                    shape = RoundedCornerShape(14.dp)
                                )
                        ) {
                            Text(
                                text = if (isUnlocked) targetItem.emoji else "🔒",
                                fontSize = 28.sp
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = level.title,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isUnlocked) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(2.dp))

                            Text(
                                text = "Obiectiv: Piesa ${level.targetTile} (${targetItem.name}) sau ${level.minScore} pct",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Text(
                                text = "Recompensă: ${level.rewardName}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = GoldAccent
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Column(horizontalAlignment = Alignment.End) {
                            when {
                                isCompleted -> {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .background(GoldAccent.copy(alpha = 0.2f), CircleShape)
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = null,
                                            tint = GoldAccent,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "COMPLETAT",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = GoldAccent
                                        )
                                    }
                                }
                                isUnlocked -> {
                                    Button(
                                        onClick = {
                                            onSelectLevel(level.levelNumber)
                                            onNavigate(AppScreen.GAME)
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = TricolorBlue),
                                        shape = RoundedCornerShape(10.dp)
                                    ) {
                                        Text("JOACĂ", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                                else -> {
                                    Icon(
                                        imageVector = Icons.Default.Lock,
                                        contentDescription = "Blocat",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
