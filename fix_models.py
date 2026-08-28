with open("app/src/main/java/com/example/model/GameModels.kt", "r") as f:
    content = f.read()
content = content.replace("val badge: String get() = badgeKey()\n})", "val badge: String get() = badgeKey()\n}")
with open("app/src/main/java/com/example/model/GameModels.kt", "w") as f:
    f.write(content)
