import re

with open("app/src/main/java/com/example/ui/screens/GameScreen.kt", "r") as f:
    content = f.read()

# Add imports
content = content.replace("import androidx.compose.foundation.background", "import androidx.compose.foundation.background\nimport androidx.compose.foundation.clickable\nimport androidx.compose.foundation.interaction.MutableInteractionSource")

# Add isPaused state collection
content = content.replace("var showQuickMenu by remember { mutableStateOf(false) }", "var showQuickMenu by remember { mutableStateOf(false) }\n    val isPaused by viewModel.showPauseModal.collectAsState()")

# Add the overlay at the end of the Box
overlay_code = """
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
"""

content = content.replace("        }\n    }\n}", "        }\n" + overlay_code)

with open("app/src/main/java/com/example/ui/screens/GameScreen.kt", "w") as f:
    f.write(content)
