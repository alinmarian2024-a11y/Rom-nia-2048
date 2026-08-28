import re

with open("app/src/main/java/com/example/ui/strings/StringsRo.kt", "r") as f:
    ro = f.read()

ro = ro.replace('"20" -> "Călător"\n        else -> "Necunoscut"', '"20" -> "Călător"\n        "21" -> "COLECȚIONAR HARNIC"\n        "22" -> "VITEZĂ SUPREMĂ"\n        "23" -> "REÎNCEPUT PROSPER"\n        "24" -> "NOPȚI ALBE"\n        "25" -> "INIMĂ DE ROMÂN"\n        else -> "Necunoscut"')

ro = ro.replace('"20" -> "Joacă 5 jocuri în Modul Infinit"\n        else -> "Necunoscut"', '"20" -> "Joacă 5 jocuri în Modul Infinit"\n        "21" -> "Deblochează 8 piese în Colecție."\n        "22" -> "Efectuează 50 de mutări."\n        "23" -> "Începe un joc nou după ce ai obținut un scor bun."\n        "24" -> "Comută aplicația în modul Dark."\n        "25" -> "Completează toate nivelurile din joc."\n        else -> "Necunoscut"')

with open("app/src/main/java/com/example/ui/strings/StringsRo.kt", "w") as f:
    f.write(ro)


with open("app/src/main/java/com/example/ui/strings/StringsEn.kt", "r") as f:
    en = f.read()

en = en.replace('"20" -> "Traveler"\n        else -> "Unknown"', '"20" -> "Traveler"\n        "21" -> "Avid Collector"\n        "22" -> "Supreme Speed"\n        "23" -> "Prosperous Restart"\n        "24" -> "White Nights"\n        "25" -> "Romanian Heart"\n        else -> "Unknown"')

en = en.replace('"20" -> "Play 5 games in Infinite Mode"\n        else -> "Unknown"', '"20" -> "Play 5 games in Infinite Mode"\n        "21" -> "Unlock 8 tiles in the Collection."\n        "22" -> "Make 50 moves."\n        "23" -> "Start a new game after getting a good score."\n        "24" -> "Switch the app to Dark mode."\n        "25" -> "Complete all levels in the game."\n        else -> "Unknown"')

with open("app/src/main/java/com/example/ui/strings/StringsEn.kt", "w") as f:
    f.write(en)
