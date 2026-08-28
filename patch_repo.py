import re

with open("app/src/main/java/com/example/data/GameRepository.kt", "r") as f:
    content = f.read()

# Add import
if "com.example.ui.strings.Language" not in content:
    content = content.replace("import com.example.model.GameState", "import com.example.model.GameState\nimport com.example.ui.strings.Language")

content = content.replace('private const val KEY_GAME_MODE = "key_game_mode"', 'private const val KEY_GAME_MODE = "key_game_mode"\n        private const val KEY_LANGUAGE = "key_language"')

# Add language methods
methods = """    
    fun saveLanguage(language: Language) {
        prefs.edit().putString(KEY_LANGUAGE, language.code).apply()
    }
    
    fun getLanguage(): Language {
        val code = prefs.getString(KEY_LANGUAGE, Language.RO.code) ?: Language.RO.code
        return if (code == Language.EN.code) Language.EN else Language.RO
    }
"""
content = content.replace("fun saveGameState(state: GameState) {", methods + "\n    fun saveGameState(state: GameState) {")

with open("app/src/main/java/com/example/data/GameRepository.kt", "w") as f:
    f.write(content)
