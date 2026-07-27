package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BoardBackgroundDark
import com.example.ui.theme.BoardBackgroundLight
import com.example.ui.theme.TricolorBlue
import com.example.ui.theme.TricolorRed
import com.example.ui.theme.TricolorYellow
import com.example.viewmodel.Direction
import kotlin.math.abs

@Composable
fun BoardView(
    grid: List<List<Int>>,
    isDarkTheme: Boolean = true,
    onMove: (Direction) -> Unit,
    modifier: Modifier = Modifier
) {
    var totalDragX by remember { mutableFloatStateOf(0f) }
    var totalDragY by remember { mutableFloatStateOf(0f) }

    val boardBg = if (isDarkTheme) BoardBackgroundDark else BoardBackgroundLight
    val boardBorder = if (isDarkTheme) Color(0xFF334155) else Color(0xFFCBD5E1)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {
        // 4x4 Grid Board
        Surface(
            shadowElevation = 4.dp,
            shape = RoundedCornerShape(24.dp),
            color = boardBg,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(24.dp))
                .border(
                    width = 2.dp,
                    color = boardBorder,
                    shape = RoundedCornerShape(24.dp)
                )
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = {
                            totalDragX = 0f
                            totalDragY = 0f
                        },
                        onDragEnd = {
                            val minSwipeDistance = 40f
                            if (abs(totalDragX) > abs(totalDragY)) {
                                if (abs(totalDragX) > minSwipeDistance) {
                                    if (totalDragX > 0) onMove(Direction.RIGHT) else onMove(Direction.LEFT)
                                }
                            } else {
                                if (abs(totalDragY) > minSwipeDistance) {
                                    if (totalDragY > 0) onMove(Direction.DOWN) else onMove(Direction.UP)
                                }
                            }
                        },
                        onDrag = { change, dragAmount ->
                            change.consume()
                            totalDragX += dragAmount.x
                            totalDragY += dragAmount.y
                        }
                    )
                }
                .padding(10.dp)
                .testTag("board_grid")
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(2.dp)
            ) {
                for (r in 0..3) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        for (c in 0..3) {
                            TileView(
                                value = grid[r][c],
                                isDarkTheme = isDarkTheme,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tactile D-Pad Controls for Mobile Touch
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(8.dp)
            ) {
                FilledIconButton(
                    onClick = { onMove(Direction.UP) },
                    shape = RoundedCornerShape(12.dp),
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ),
                    modifier = Modifier
                        .size(44.dp)
                        .shadow(elevation = 2.dp, shape = RoundedCornerShape(12.dp))
                        .testTag("dpad_up")
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowUpward,
                        contentDescription = "Sus"
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FilledIconButton(
                        onClick = { onMove(Direction.LEFT) },
                        shape = RoundedCornerShape(12.dp),
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            contentColor = MaterialTheme.colorScheme.onSurface
                        ),
                        modifier = Modifier
                            .size(44.dp)
                            .shadow(elevation = 2.dp, shape = RoundedCornerShape(12.dp))
                            .testTag("dpad_left")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Stânga"
                        )
                    }

                    Spacer(modifier = Modifier.width(20.dp))

                    FilledIconButton(
                        onClick = { onMove(Direction.DOWN) },
                        shape = RoundedCornerShape(12.dp),
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            contentColor = MaterialTheme.colorScheme.onSurface
                        ),
                        modifier = Modifier
                            .size(44.dp)
                            .shadow(elevation = 2.dp, shape = RoundedCornerShape(12.dp))
                            .testTag("dpad_down")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowDownward,
                            contentDescription = "Jos"
                        )
                    }

                    Spacer(modifier = Modifier.width(20.dp))

                    FilledIconButton(
                        onClick = { onMove(Direction.RIGHT) },
                        shape = RoundedCornerShape(12.dp),
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            contentColor = MaterialTheme.colorScheme.onSurface
                        ),
                        modifier = Modifier
                            .size(44.dp)
                            .shadow(elevation = 2.dp, shape = RoundedCornerShape(12.dp))
                            .testTag("dpad_right")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Dreapta"
                        )
                    }
                }
            }
        }
    }
}
