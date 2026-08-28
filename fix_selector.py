import re

with open("app/src/main/java/com/example/ui/strings/StringsRo.kt", "r") as f:
    ro = f.read()

ro = ro.replace('override val languageSelector = "Limba: RO"', 'override val languageSelector = "🌐 Limba: RO"')
with open("app/src/main/java/com/example/ui/strings/StringsRo.kt", "w") as f:
    f.write(ro)

with open("app/src/main/java/com/example/ui/strings/StringsEn.kt", "r") as f:
    en = f.read()

en = en.replace('override val languageSelector = "Language: EN"', 'override val languageSelector = "🌐 Language: EN"')
with open("app/src/main/java/com/example/ui/strings/StringsEn.kt", "w") as f:
    f.write(en)
