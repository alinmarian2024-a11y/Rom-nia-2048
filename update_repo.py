import re

with open("app/src/main/java/com/example/data/GameRepository.kt", "r") as f:
    content = f.read()

content = content.replace("prefs.edit().putString(KEY_LANGUAGE, language.code).apply()", "prefs.edit().putString(KEY_LANGUAGE, language.code).commit()")

with open("app/src/main/java/com/example/data/GameRepository.kt", "w") as f:
    f.write(content)
