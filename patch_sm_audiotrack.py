import re

with open("app/src/main/java/com/example/audio/SoundManager.kt", "r") as f:
    content = f.read()

old_ctx = "val audioTrack = AudioTrack.Builder()"
new_ctx = """val audioTrack = AudioTrack.Builder()"""

# Wait, if we just use .apply { if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) setContext(context) }
# wait, what's API 34 constant? Build.VERSION_CODES.UPSIDE_DOWN_CAKE is 34.
# But it might be in Android 31 (S)?
