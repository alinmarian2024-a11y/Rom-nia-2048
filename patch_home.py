import re

with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "r") as f:
    content = f.read()

# Add import
if "import com.example.ui.strings.Localization" not in content:
    content = content.replace("import androidx.compose", "import com.example.ui.strings.Localization\nimport com.example.ui.strings.Language\nimport androidx.compose")

# Strings replacement
replacements = {
    '"ROMÂNIA 2048"': 'Localization.strings.appTitle',
    '"Construiește România, piesă cu piesă!"': 'Localization.strings.appSubtitle',
    '"JOACĂ ACUM"': 'Localization.strings.playNow',
    '"RECORD"': 'Localization.strings.record',
    '"NIVEL ACTUAL"': 'Localization.strings.currentLevelTitle',
    '"Nivel ${gameState.currentLevel}"': 'Localization.strings.levelPrefix + "${gameState.currentLevel}"',
    '"COLECȚIE"': 'Localization.strings.collectionTitle',
    '"NIVELURI"': 'Localization.strings.btnLevels',
    '"REALIZĂRI"': 'Localization.strings.btnAchievements',
    '"SETĂRI"': 'Localization.strings.btnSettings',
    '"DESPRE JOC"': 'Localization.strings.btnAbout',
    '"Joacă fără reclame"': 'Localization.strings.removeAdsTitle',
    '"Reclamele interstițiale sunt dezactivate"': 'Localization.strings.adsRemovedMsg',
    '"Elimină reclamele interstițiale și bucură-te de o experiență de joc mai plăcută."': 'Localization.strings.removeAdsDesc',
    '"ELIMINĂ RECLAMELE — 6,99 lei"': 'Localization.strings.removeAdsBtn',
    '"Achiziție unică. Reclamele recompensate pentru Undo și Continuă după Game Over rămân disponibile."': 'Localization.strings.removeAdsDisclaimer',
    '"RESTAUREAZĂ ACHIZIȚIA"': 'Localization.strings.restorePurchaseBtn',
}

for old, new in replacements.items():
    content = content.replace(old, new)

# Add Language Selector
lang_selector = """
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            androidx.compose.material3.TextButton(
                onClick = { 
                    Localization.language = if (Localization.language == Language.RO) Language.EN else Language.RO 
                }
            ) {
                Text(Localization.strings.languageSelector, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
"""

content = content.replace("Spacer(modifier = Modifier.height(12.dp))", lang_selector, 1)

with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "w") as f:
    f.write(content)
