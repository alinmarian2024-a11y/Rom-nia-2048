with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

content = content.replace("import android.content.Context\nimport android.os.Build\n", "import android.os.Build\n")
content = content.replace("import android.content.Context\n", "")
content = content.replace("import android.os.Build\n", "import android.content.Context\nimport android.os.Build\n")

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)
