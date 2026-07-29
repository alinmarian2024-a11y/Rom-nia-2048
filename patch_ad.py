import re

with open("app/src/main/java/com/example/monetization/AdManager.kt", "r") as f:
    content = f.read()

old_ctx = "class AdManager(private val context: Context) {"
new_ctx = """import android.os.Build

class AdManager(private val baseContext: Context) {
    private val context: Context = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        baseContext.createAttributionContext("default")
    } else {
        baseContext
    }"""
content = content.replace(old_ctx, new_ctx)

with open("app/src/main/java/com/example/monetization/AdManager.kt", "w") as f:
    f.write(content)
