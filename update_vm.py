import re

with open("app/src/main/java/com/example/viewmodel/GameViewModel.kt", "r") as f:
    content = f.read()

# Add language flow
lang_flow = """    val themePreference = MutableStateFlow(repository.getThemePreference())
    val isRomanianTheme = MutableStateFlow(repository.isRomanianTheme())

    private val _currentLanguage = MutableStateFlow(repository.getLanguage())
    val currentLanguage: StateFlow<Language> = _currentLanguage.asStateFlow()"""

content = re.sub(r'    val themePreference = MutableStateFlow\(repository\.getThemePreference\(\)\)\n    val isRomanianTheme = MutableStateFlow\(repository\.isRomanianTheme\(\)\)', lang_flow, content)

# Change init block to not set Localization directly
init_block_old = """    init {
        Localization.language = repository.getLanguage()
        loadSavedState()
        syncAudioSettings()
    }"""
init_block_new = """    init {
        loadSavedState()
        syncAudioSettings()
    }"""
content = content.replace(init_block_old, init_block_new)

# Update toggleLanguage
toggle_old = """    fun toggleLanguage() {
        val newLang = if (Localization.language == Language.RO) Language.EN else Language.RO
        Localization.language = newLang
        repository.saveLanguage(newLang)
    }"""
toggle_new = """    fun toggleLanguage() {
        val newLang = if (_currentLanguage.value == Language.RO) Language.EN else Language.RO
        _currentLanguage.value = newLang
        repository.saveLanguage(newLang)
    }"""
content = content.replace(toggle_old, toggle_new)

with open("app/src/main/java/com/example/viewmodel/GameViewModel.kt", "w") as f:
    f.write(content)
