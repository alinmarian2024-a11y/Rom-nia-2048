import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

main_old = """            val currentLang by vm.currentLanguage.collectAsState()
                
            // Sync Localization state with ViewModel state
            com.example.ui.strings.Localization.language = currentLang"""
main_new = """            val currentLang by vm.currentLanguage.collectAsState()
                
            androidx.compose.runtime.LaunchedEffect(currentLang) {
                com.example.ui.strings.Localization.language = currentLang
            }"""

content = content.replace(main_old, main_new)

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)
