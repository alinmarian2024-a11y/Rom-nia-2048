import re

with open("app/src/main/java/com/example/audio/SoundManager.kt", "r") as f:
    content = f.read()

new_ctx = "private val context: Context = baseContext"
old_ctx = """private val context: Context = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        baseContext.createAttributionContext("default")
    } else {
        baseContext
    }"""
content = content.replace(old_ctx, new_ctx)

with open("app/src/main/java/com/example/audio/SoundManager.kt", "w") as f:
    f.write(content)
