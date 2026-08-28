import re

with open("app/src/main/java/com/example/model/GameModels.kt", "r") as f:
    content = f.read()

# Add import
if "com.example.ui.strings.Localization" not in content:
    content = content.replace("import androidx.compose.ui.graphics.Color", "import androidx.compose.ui.graphics.Color\nimport com.example.ui.strings.Localization")

# RomaniaItem
content = content.replace(
    "val name: String,",
    "val nameKey: () -> String,"
).replace(
    "val description: String,",
    "val descriptionKey: () -> String,"
).replace(
    "val badge: String",
    "val badgeKey: () -> String\n) {\n    val name: String get() = nameKey()\n    val description: String get() = descriptionKey()\n    val badge: String get() = badgeKey()\n}"
)

# GameLevel
content = content.replace(
    "val title: String,",
    "val titleKey: () -> String,"
).replace(
    "val rewardName: String = \"\",",
    "val rewardNameKey: () -> String = { \"\" },"
)
content = re.sub(
    r'val rewardBonus: Int = 0\n\)',
    'val rewardBonus: Int = 0\n) {\n    val title: String get() = titleKey()\n    val rewardName: String get() = rewardNameKey()\n}',
    content
)

# Achievement
content = content.replace(
    "val title: String,",
    "val titleKey: () -> String,"
).replace(
    "val description: String,",
    "val descriptionKey: () -> String,"
)
content = re.sub(
    r'val maxProgress: Int = 1\n\)',
    'val maxProgress: Int = 1\n) {\n    val title: String get() = titleKey()\n    val description: String get() = descriptionKey()\n}',
    content
)

with open("app/src/main/java/com/example/model/GameModels.kt", "w") as f:
    f.write(content)
