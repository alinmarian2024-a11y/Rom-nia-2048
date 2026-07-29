import re

with open("app/src/main/java/com/example/viewmodel/GameViewModel.kt", "r") as f:
    content = f.read()

settings_block = """    // Settings actions
    fun setMusicVolume(vol: Float) {
        isMusicEnabled.value = true
        repository.setMusicEnabled(true)
        soundManager.isMusicEnabled = true
        musicVolume.value = vol
        repository.setMusicVolume(vol)
        soundManager.musicVolume = vol
        soundManager.updateMusicVolume()
    }

    fun setSfxVolume(vol: Float) {
        isSfxEnabled.value = true
        repository.setSfxEnabled(true)
        soundManager.isSfxEnabled = true
        sfxVolume.value = vol
        repository.setSfxVolume(vol)
        soundManager.sfxVolume = vol
    }

    fun setVibration(enabled: Boolean) {
        isVibrationEnabled.value = enabled
        repository.setVibrationEnabled(enabled)
        syncAudioSettings()
    }"""

# Use regex to replace the messed up block
content = re.sub(r"    // Settings actions.*?fun setVibration\(enabled: Boolean\) \{", settings_block + "\n    fun setVibration(enabled: Boolean) {", content, flags=re.DOTALL)

# Ensure setVibration is not duplicated
content = content.replace(settings_block + "\n    fun setVibration(enabled: Boolean) {\n        isVibrationEnabled.value = enabled\n        repository.setVibrationEnabled(enabled)\n        syncAudioSettings()\n    }\n        isVibrationEnabled.value = enabled", settings_block)

# Wait, let's just do a clean regex replacement up to setVibration
content = re.sub(r"    // Settings actions.*?    fun setVibration\(enabled: Boolean\) \{", settings_block.replace("    fun setVibration(enabled: Boolean) {", "") + "    fun setVibration(enabled: Boolean) {", content, flags=re.DOTALL)

with open("app/src/main/java/com/example/viewmodel/GameViewModel.kt", "w") as f:
    f.write(content)

