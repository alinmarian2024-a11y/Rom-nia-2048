import re

with open("app/src/main/java/com/example/viewmodel/GameViewModel.kt", "r") as f:
    content = f.read()

new_achievements = """    val allAchievementsList = listOf(
        Achievement("1", "🥨", { Localization.strings.getAchievementTitle("1") }, { Localization.strings.getAchievementDesc("1") }, maxProgress = 1),
        Achievement("2", "🥧", { Localization.strings.getAchievementTitle("2") }, { Localization.strings.getAchievementDesc("2") }, maxProgress = 1),
        Achievement("3", "🥣", { Localization.strings.getAchievementTitle("3") }, { Localization.strings.getAchievementDesc("3") }, maxProgress = 1),
        Achievement("4", "🧀", { Localization.strings.getAchievementTitle("4") }, { Localization.strings.getAchievementDesc("4") }, maxProgress = 1),
        Achievement("5", "🍗", { Localization.strings.getAchievementTitle("5") }, { Localization.strings.getAchievementDesc("5") }, maxProgress = 1),
        Achievement("6", "🥘", { Localization.strings.getAchievementTitle("6") }, { Localization.strings.getAchievementDesc("6") }, maxProgress = 1),
        Achievement("7", "🍲", { Localization.strings.getAchievementTitle("7") }, { Localization.strings.getAchievementDesc("7") }, maxProgress = 1),
        Achievement("8", "☕", { Localization.strings.getAchievementTitle("8") }, { Localization.strings.getAchievementDesc("8") }, maxProgress = 1),
        Achievement("9", "🏰", { Localization.strings.getAchievementTitle("9") }, { Localization.strings.getAchievementDesc("9") }, maxProgress = 1),
        Achievement("10", "🏛️", { Localization.strings.getAchievementTitle("10") }, { Localization.strings.getAchievementDesc("10") }, maxProgress = 1),
        Achievement("11", "🇷🇴", { Localization.strings.getAchievementTitle("11") }, { Localization.strings.getAchievementDesc("11") }, maxProgress = 1),
        Achievement("12", "🌟", { Localization.strings.getAchievementTitle("12") }, { Localization.strings.getAchievementDesc("12") }, maxProgress = 1),
        Achievement("13", "🦅", { Localization.strings.getAchievementTitle("13") }, { Localization.strings.getAchievementDesc("13") }, maxProgress = 1),
        Achievement("14", "🔥", { Localization.strings.getAchievementTitle("14") }, { Localization.strings.getAchievementDesc("14") }, maxProgress = 1),
        Achievement("15", "💪", { Localization.strings.getAchievementTitle("15") }, { Localization.strings.getAchievementDesc("15") }, maxProgress = 3),
        Achievement("16", "🧠", { Localization.strings.getAchievementTitle("16") }, { Localization.strings.getAchievementDesc("16") }, maxProgress = 10),
        Achievement("17", "🏆", { Localization.strings.getAchievementTitle("17") }, { Localization.strings.getAchievementDesc("17") }, maxProgress = 10000),
        Achievement("18", "🥇", { Localization.strings.getAchievementTitle("18") }, { Localization.strings.getAchievementDesc("18") }, maxProgress = 25000),
        Achievement("19", "👑", { Localization.strings.getAchievementTitle("19") }, { Localization.strings.getAchievementDesc("19") }, maxProgress = 50000),
        Achievement("20", "🗺", { Localization.strings.getAchievementTitle("20") }, { Localization.strings.getAchievementDesc("20") }, maxProgress = 5),
        Achievement("21", "🎨", { Localization.strings.getAchievementTitle("21") }, { Localization.strings.getAchievementDesc("21") }, maxProgress = 8),
        Achievement("22", "⚡", { Localization.strings.getAchievementTitle("22") }, { Localization.strings.getAchievementDesc("22") }, maxProgress = 50),
        Achievement("23", "🔄", { Localization.strings.getAchievementTitle("23") }, { Localization.strings.getAchievementDesc("23") }, maxProgress = 1),
        Achievement("24", "🌙", { Localization.strings.getAchievementTitle("24") }, { Localization.strings.getAchievementDesc("24") }, maxProgress = 1),
        Achievement("25", "🇷🇴", { Localization.strings.getAchievementTitle("25") }, { Localization.strings.getAchievementDesc("25") }, maxProgress = 7)
    )"""

content = re.sub(r'    val allAchievementsList = listOf\([\s\S]*?maxProgress = 7\)\n    \)', new_achievements, content)

with open("app/src/main/java/com/example/viewmodel/GameViewModel.kt", "w") as f:
    f.write(content)

