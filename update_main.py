import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

# Add observation in MainActivity
main_old = """            val themePref by vm.themePreference.collectAsState()
            val isRomanianTheme by vm.isRomanianTheme.collectAsState()"""
main_new = """            val themePref by vm.themePreference.collectAsState()
            val isRomanianTheme by vm.isRomanianTheme.collectAsState()
            val currentLang by vm.currentLanguage.collectAsState()
            
            // Sync Localization state with ViewModel state
            com.example.ui.strings.Localization.language = currentLang"""

content = content.replace(main_old, main_new)

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)
