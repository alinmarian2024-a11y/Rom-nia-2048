import re
import os

def remove_attribution(filepath):
    with open(filepath, "r") as f:
        content = f.read()

    # Revert AdManager.kt
    if "AdManager.kt" in filepath:
        content = content.replace('import android.os.Build\n\n', '')
        content = re.sub(r'class AdManager\(private val baseContext: Context\) \{\n    private val context: Context = if \(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R\) \{\n        baseContext.createAttributionContext\("default"\)\n    \} else \{\n        baseContext\n    \}', 'class AdManager(private val context: Context) {', content)
        
    # Revert BillingManager.kt
    if "BillingManager.kt" in filepath:
        content = re.sub(r'private val baseContext: Context', 'private val context: Context', content)
        content = re.sub(r'    private val context: Context = if \(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R\) \{\n        baseContext.createAttributionContext\("default"\)\n    \} else \{\n        baseContext\n    \}\n\n', '', content)

    # Revert SoundManager.kt
    if "SoundManager.kt" in filepath:
        content = re.sub(r'class SoundManager\(private val baseContext: Context\) \{\n    private val context: Context = if \(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R\) \{\n        baseContext.createAttributionContext\("default"\)\n    \} else \{\n        baseContext\n    \}', 'class SoundManager(private val baseContext: Context) {\n    private val context: Context = baseContext', content)

    with open(filepath, "w") as f:
        f.write(content)

remove_attribution("app/src/main/java/com/example/monetization/AdManager.kt")
remove_attribution("app/src/main/java/com/example/monetization/BillingManager.kt")
remove_attribution("app/src/main/java/com/example/audio/SoundManager.kt")
