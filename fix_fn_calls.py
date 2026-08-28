import re

# Dialogs.kt
with open("app/src/main/java/com/example/ui/components/Dialogs.kt", "r") as f:
    d = f.read()
d = d.replace("Localization.strings.gameOverScore.format(score, highScore)", "Localization.strings.gameOverScore(score, highScore)")
with open("app/src/main/java/com/example/ui/components/Dialogs.kt", "w") as f:
    f.write(d)

# AchievementsScreen.kt
with open("app/src/main/java/com/example/ui/screens/AchievementsScreen.kt", "r") as f:
    a = f.read()
a = a.replace("Localization.strings.achievementsSubtitle.format(allAchievements.size)", "Localization.strings.achievementsSubtitle(allAchievements.size)")
with open("app/src/main/java/com/example/ui/screens/AchievementsScreen.kt", "w") as f:
    f.write(a)

# CollectionScreen.kt
with open("app/src/main/java/com/example/ui/screens/CollectionScreen.kt", "r") as f:
    c = f.read()
c = c.replace("Localization.strings.collectionLockedDesc.format(item.value)", "Localization.strings.collectionLockedDesc(item.value)")
with open("app/src/main/java/com/example/ui/screens/CollectionScreen.kt", "w") as f:
    f.write(c)

