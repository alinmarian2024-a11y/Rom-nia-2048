import re

with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "r") as f:
    content = f.read()

content = content.replace(
    "onRestorePurchases: () -> Unit = {},",
    "onRestorePurchases: () -> Unit = {},\n    onToggleLanguage: () -> Unit = {},"
)

content = content.replace(
    "Localization.language = if (Localization.language == Language.RO) Language.EN else Language.RO",
    "onToggleLanguage()"
)

with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "w") as f:
    f.write(content)

# Patch MainActivity
with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content2 = f.read()

content2 = content2.replace(
    "onRestorePurchases = { viewModel.restorePurchases() }",
    "onRestorePurchases = { viewModel.restorePurchases() },\n                onToggleLanguage = { viewModel.toggleLanguage() }"
)

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content2)
