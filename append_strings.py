import re

with open("app/src/main/java/com/example/ui/strings/AppStrings.kt", "r") as f:
    content = f.read()

# Clean up any leftover fun getAchievementDesc lines at the end of the file if present
content = re.sub(r'    fun getAchievementTitle\(id: String\): String\n    fun getAchievementDesc\(id: String\): String\n}\n', '}\n', content)

# Add to interface
interface_addition = """
    fun getAchievementTitle(id: String): String
    fun getAchievementDesc(id: String): String
"""
content = content.replace("val objectiveInfiniteDesc: String\n}", "val objectiveInfiniteDesc: String\n" + interface_addition + "\n}")

# Add to StringsRo
ro_addition = """
    override fun getAchievementTitle(id: String): String = when (id) {
        "1" -> "PRIMUL COVRIG"
        "2" -> "POFTA VINE MÂNCÂND"
        "3" -> "MĂMĂLIGAR DEVOTAT"
        "4" -> "BRÂNZĂ BUNA"
        "5" -> "MAESTRU AL COPANELOR"
        "6" -> "REGELE SARMALELOR"
        "7" -> "MASTERCHEF ROMÂN"
        "8" -> "CAFEAUA DE DIMINEAȚĂ"
        "9" -> "TURIST ÎN ROMÂNIA"
        "10" -> "BOIER MODERN"
        "11" -> "PATRIOT"
        "12" -> "LUCEAFĂRUL JOCULUI"
        "13" -> "VULTURUL CARPAȚILOR"
        "14" -> "NU MĂ OPRESC"
        "15" -> "ÎNCĂ O DATĂ"
        "16" -> "STRATEG"
        "17" -> "MAESTRU 2048"
        "18" -> "LEGENDĂ RURALĂ"
        "19" -> "REGELE ROMÂNIEI"
        "20" -> "EXPLORATOR"
        else -> "REALIZARE"
    }

    override fun getAchievementDesc(id: String): String = when (id) {
        "1" -> "Obține prima piesă de nivel 2."
        "2" -> "Obține piesa 4."
        "3" -> "Obține piesa 8."
        "4" -> "Obține piesa 16."
        "5" -> "Obține piesa 32."
        "6" -> "Obține piesa 64."
        "7" -> "Obține piesa 128."
        "8" -> "Obține piesa 256."
        "9" -> "Obține piesa 512."
        "10" -> "Obține piesa 1024."
        "11" -> "Ajunge la piesa 2048."
        "12" -> "Ajunge la piesa 4096."
        "13" -> "Ajunge la piesa 8192."
        "14" -> "Continuă jocul după atingerea 2048."
        "15" -> "Folosește UNDO de 3 ori într-un joc."
        "16" -> "Realizează 10 mutări valide."
        "17" -> "Obține un scor peste 10.000 de puncte."
        "18" -> "Obține un scor peste 25.000 de puncte."
        "19" -> "Obține un scor peste 50.000 de puncte."
        "20" -> "Deblochează cel puțin 5 niveluri."
        else -> "Realizare secretă."
    }
"""
content = content.replace('val objectiveInfiniteDesc = "„Combină două piese identice pentru a construi România, piesă cu piesă!”"\n}', 'val objectiveInfiniteDesc = "„Combină două piese identice pentru a construi România, piesă cu piesă!”"\n' + ro_addition + '\n}')

# Add to StringsEn
en_addition = """
    override fun getAchievementTitle(id: String): String = when (id) {
        "1" -> "FIRST PRETZEL"
        "2" -> "APPETITE COMES WITH EATING"
        "3" -> "DEVOTED POLENTA FAN"
        "4" -> "GOOD CHEESE"
        "5" -> "MASTER OF CHICKEN LEGS"
        "6" -> "KING OF SARMALE"
        "7" -> "ROMANIAN MASTERCHEF"
        "8" -> "MORNING COFFEE"
        "9" -> "TOURIST IN ROMANIA"
        "10" -> "MODERN BOYAR"
        "11" -> "PATRIOT"
        "12" -> "GAME LUCEAFĂR"
        "13" -> "CARPATHIAN EAGLE"
        "14" -> "I WON'T STOP"
        "15" -> "ONE MORE TIME"
        "16" -> "STRATEGIST"
        "17" -> "2048 MASTER"
        "18" -> "RURAL LEGEND"
        "19" -> "KING OF ROMANIA"
        "20" -> "EXPLORER"
        else -> "ACHIEVEMENT"
    }

    override fun getAchievementDesc(id: String): String = when (id) {
        "1" -> "Get the first level 2 tile."
        "2" -> "Get the 4 tile."
        "3" -> "Get the 8 tile."
        "4" -> "Get the 16 tile."
        "5" -> "Get the 32 tile."
        "6" -> "Get the 64 tile."
        "7" -> "Get the 128 tile."
        "8" -> "Get the 256 tile."
        "9" -> "Get the 512 tile."
        "10" -> "Get the 1024 tile."
        "11" -> "Reach the 2048 tile."
        "12" -> "Reach the 4096 tile."
        "13" -> "Reach the 8192 tile."
        "14" -> "Continue playing after reaching 2048."
        "15" -> "Use UNDO 3 times in one game."
        "16" -> "Make 10 valid moves."
        "17" -> "Get a score over 10,000 points."
        "18" -> "Get a score over 25,000 points."
        "19" -> "Get a score over 50,000 points."
        "20" -> "Unlock at least 5 levels."
        else -> "Secret achievement."
    }
"""
content = content.replace('val objectiveInfiniteDesc = "\\"Combine two identical tiles to build Romania, tile by tile!\\""\n}', 'val objectiveInfiniteDesc = "\\"Combine two identical tiles to build Romania, tile by tile!\\""\n' + en_addition + '\n}')

with open("app/src/main/java/com/example/ui/strings/AppStrings.kt", "w") as f:
    f.write(content)
