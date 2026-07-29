package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.ResetConfirmDialog
import androidx.compose.ui.platform.LocalContext
import android.app.Activity
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.TricolorRed
import com.example.viewmodel.AppScreen
import com.example.viewmodel.GameViewModel

@Composable
fun SettingsScreen(
    viewModel: GameViewModel,
    onNavigate: (AppScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    val musicVol by viewModel.musicVolume.collectAsState()
    val sfxVol by viewModel.sfxVolume.collectAsState()
    val isVibration by viewModel.isVibrationEnabled.collectAsState()
    val themePref by viewModel.themePreference.collectAsState()
    val isRomanianTheme by viewModel.isRomanianTheme.collectAsState()
    val isAnimations by viewModel.isAnimationsEnabled.collectAsState()
    val isConfirmRestart by viewModel.isConfirmRestart.collectAsState()
    val showResetConfirm by viewModel.showResetConfirmDialog.collectAsState()

    val context = LocalContext.current
    val activity = context as? Activity

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // Top Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(
                onClick = { onNavigate(AppScreen.HOME) },
                modifier = Modifier.testTag("btn_back_home_from_settings")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Înapoi",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "⚙ SETĂRI",
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Audio & Haptics Section
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "AUDIO & VIBRAȚII",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(12.dp))

                Column(modifier = Modifier.fillMaxWidth()) {
                    Text("🎵 VOLUM MUZICĂ: ${ (musicVol * 100).toInt() }%", fontWeight = FontWeight.Medium)
                    Slider(
                        value = musicVol,
                        onValueChange = { viewModel.setMusicVolume(it) },
                        valueRange = 0f..1f,
                        modifier = Modifier.testTag("slider_music")
                    )
                }

                Column(modifier = Modifier.fillMaxWidth()) {
                    Text("🔊 VOLUM EFECTE SONORE: ${ (sfxVol * 100).toInt() }%", fontWeight = FontWeight.Medium)
                    Slider(
                        value = sfxVol,
                        onValueChange = { viewModel.setSfxVolume(it) },
                        valueRange = 0f..1f,
                        modifier = Modifier.testTag("slider_sfx")
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("🔔 VIBRAȚII", fontWeight = FontWeight.Medium)
                    Switch(
                        checked = isVibration,
                        onCheckedChange = { viewModel.setVibration(it) },
                        modifier = Modifier.testTag("switch_vibration")
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Theme Section
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "🌙 TEMĂ VIZUALĂ",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    FilterChip(
                        selected = themePref == "LIGHT",
                        onClick = { viewModel.setTheme("LIGHT") },
                        label = { Text("LIGHT") },
                        modifier = Modifier.weight(1f).testTag("chip_theme_light")
                    )
                    FilterChip(
                        selected = themePref == "DARK",
                        onClick = { viewModel.setTheme("DARK") },
                        label = { Text("DARK") },
                        modifier = Modifier.weight(1f).testTag("chip_theme_dark")
                    )
                    FilterChip(
                        selected = themePref == "SYSTEM",
                        onClick = { viewModel.setTheme("SYSTEM") },
                        label = { Text("SYSTEM") },
                        modifier = Modifier.weight(1f).testTag("chip_theme_system")
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("🎨 TEMĂ ROMÂNEASCĂ", fontWeight = FontWeight.Medium)
                    Switch(
                        checked = isRomanianTheme,
                        onCheckedChange = { viewModel.setRomanianTheme(it) },
                        modifier = Modifier.testTag("switch_romanian_theme")
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Gameplay Options
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "⚡ PREFERINȚE JOC",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("⚡ ANIMAȚII", fontWeight = FontWeight.Medium)
                    Switch(
                        checked = isAnimations,
                        onCheckedChange = { viewModel.setAnimations(it) },
                        modifier = Modifier.testTag("switch_animations")
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("CONFIRMĂ RESTART", fontWeight = FontWeight.Medium)
                    Switch(
                        checked = isConfirmRestart,
                        onCheckedChange = { viewModel.setConfirmRestartPref(it) },
                        modifier = Modifier.testTag("switch_confirm_restart")
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Reset Data Button
        Button(
            onClick = { viewModel.requestResetData() },
            colors = ButtonDefaults.buttonColors(containerColor = TricolorRed),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("btn_reset_progress")
        ) {
            Text(
                text = "🗑️ RESETARE PROGRES",
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
    }

    if (showResetConfirm) {
        ResetConfirmDialog(
            onConfirm = { viewModel.confirmResetData() },
            onDismiss = { viewModel.cancelResetData() }
        )
    }
}
