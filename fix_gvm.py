import re

with open("app/src/main/java/com/example/viewmodel/GameViewModel.kt", "r") as f:
    content = f.read()

content = content.replace("private fun Localization.language = repository.getLanguage()\n        loadSavedState() {", "private fun loadSavedState() {")

with open("app/src/main/java/com/example/viewmodel/GameViewModel.kt", "w") as f:
    f.write(content)
