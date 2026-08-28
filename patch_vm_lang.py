import re

with open("app/src/main/java/com/example/viewmodel/GameViewModel.kt", "r") as f:
    content = f.read()

# Add to init
content = content.replace("loadSavedState()", "Localization.language = repository.getLanguage()\n        loadSavedState()")

# Add toggleLanguage method
toggle_method = """
    fun toggleLanguage() {
        val newLang = if (Localization.language == Language.RO) Language.EN else Language.RO
        Localization.language = newLang
        repository.saveLanguage(newLang)
    }
"""
content = content.replace("fun selectGameMode(mode: GameMode) {", toggle_method + "\n    fun selectGameMode(mode: GameMode) {")

# Add Language import if missing
if "import com.example.ui.strings.Language" not in content:
    content = content.replace("import com.example.ui.strings.Localization", "import com.example.ui.strings.Localization\nimport com.example.ui.strings.Language")

with open("app/src/main/java/com/example/viewmodel/GameViewModel.kt", "w") as f:
    f.write(content)
