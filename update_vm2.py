import re

with open("app/src/main/java/com/example/viewmodel/GameViewModel.kt", "r") as f:
    content = f.read()

# Add language flow
lang_flow = """    val themePreference = MutableStateFlow(repository.getThemePreference())
    val isRomanianTheme = MutableStateFlow(repository.isRomanianThemeEnabled())

    private val _currentLanguage = MutableStateFlow(repository.getLanguage())
    val currentLanguage: StateFlow<Language> = _currentLanguage.asStateFlow()"""

content = re.sub(r'    val themePreference = MutableStateFlow\(repository\.getThemePreference\(\)\)\n    val isRomanianTheme = MutableStateFlow\(repository\.isRomanianThemeEnabled\(\)\)', lang_flow, content)

with open("app/src/main/java/com/example/viewmodel/GameViewModel.kt", "w") as f:
    f.write(content)
