package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.TileRegistry
import com.example.ui.theme.EmptyCellDark
import com.example.ui.theme.EmptyCellLight

@Composable
fun TileView(
    value: Int,
    isDarkTheme: Boolean = true,
    modifier: Modifier = Modifier
) {
    if (value == 0) {
        Box(
            modifier = modifier
                .aspectRatio(1f)
                .clip(RoundedCornerShape(14.dp))
                .background(if (isDarkTheme) EmptyCellDark else EmptyCellLight)
        )
        return
    }

    val item = TileRegistry.getItem(value)
    val scale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = spring(dampingRatio = 0.65f, stiffness = 450f),
        label = "tileScale"
    )

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .scale(scale)
            .shadow(elevation = 3.dp, shape = RoundedCornerShape(14.dp))
            .clip(RoundedCornerShape(14.dp))
            .background(item.backgroundColor)
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.5f),
                shape = RoundedCornerShape(14.dp)
            )
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = item.emoji,
                fontSize = if (value >= 1000) 20.sp else 24.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(1.dp))

            Text(
                text = value.toString(),
                fontSize = when {
                    value >= 10000 -> 11.sp
                    value >= 1000 -> 13.sp
                    else -> 16.sp
                },
                fontWeight = FontWeight.Black,
                color = item.textColor,
                textAlign = TextAlign.Center
            )

            Text(
                text = item.name,
                fontSize = 9.sp,
                fontWeight = FontWeight.SemiBold,
                color = item.textColor.copy(alpha = 0.8f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
        }
    }
}
