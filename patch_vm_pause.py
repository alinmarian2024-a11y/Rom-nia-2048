import re

with open("app/src/main/java/com/example/viewmodel/GameViewModel.kt", "r") as f:
    content = f.read()

toggle_pause_pattern = re.compile(r"fun togglePause\(\) \{\s*soundManager\.playTap\(\)\s*_showPauseModal\.value = !_showPauseModal\.value\s*\}")
new_toggle_pause = """fun togglePause() {
        soundManager.playTap()
        val willPause = !_showPauseModal.value
        _showPauseModal.value = willPause
        soundManager.setMusicPausedState(willPause)
    }
    
    fun resumeFromPause() {
        soundManager.playTap()
        _showPauseModal.value = false
        soundManager.setMusicPausedState(false)
    }"""
content = toggle_pause_pattern.sub(new_toggle_pause, content)

with open("app/src/main/java/com/example/viewmodel/GameViewModel.kt", "w") as f:
    f.write(content)
