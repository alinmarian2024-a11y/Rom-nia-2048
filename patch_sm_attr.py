import re

with open("app/src/main/java/com/example/audio/SoundManager.kt", "r") as f:
    content = f.read()

# Replace the context initialization
attr_pattern = re.compile(r"private val context: Context = if \(Build\.VERSION\.SDK_INT >= Build\.VERSION_CODES\.R\) \{.*?} else \{\s*baseContext\s*\}", flags=re.DOTALL)
content = attr_pattern.sub("private val context: Context = baseContext", content)

with open("app/src/main/java/com/example/audio/SoundManager.kt", "w") as f:
    f.write(content)
