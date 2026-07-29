import re

with open("app/src/main/java/com/example/viewmodel/GameViewModel.kt", "r") as f:
    content = f.read()

# Let's extract the content before "    // Settings actions" and after the messed up block.
match = re.search(r"(.*?)    // Settings actions.*?    fun setVibration\(enabled: Boolean\) \{(.*?)$", content, flags=re.DOTALL)

if match:
    pre = match.group(1)
    post = match.group(2)
    
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

    fun setVibration(enabled: Boolean) {"""
    
    # We notice post might contain duplicate isVibrationEnabled lines, let's fix that.
    # We'll just replace the whole Settings Actions block manually by finding the start of Theme.
    
match = re.search(r"(.*?)    // Settings actions.*?    fun setTheme\(theme: String\) \{(.*?)$", content, flags=re.DOTALL)
if match:
    pre = match.group(1)
    post = match.group(2)
    
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
    }

    fun setTheme(theme: String) {"""
    
    with open("app/src/main/java/com/example/viewmodel/GameViewModel.kt", "w") as f:
        f.write(pre + settings_block + post)
