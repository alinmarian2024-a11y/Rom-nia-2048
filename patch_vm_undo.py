import re

with open("app/src/main/java/com/example/viewmodel/GameViewModel.kt", "r") as f:
    content = f.read()

handle_undo_pattern = re.compile(r"fun handleUndoClick\(activity: Activity\?\)\ \{\s*val current = _gameState\.value")
new_handle_undo = """fun handleUndoClick(activity: Activity?) {
        if (_showPauseModal.value) return
        val current = _gameState.value"""
content = handle_undo_pattern.sub(new_handle_undo, content)

with open("app/src/main/java/com/example/viewmodel/GameViewModel.kt", "w") as f:
    f.write(content)
