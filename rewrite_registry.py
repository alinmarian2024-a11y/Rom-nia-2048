import re

with open("app/src/main/java/com/example/model/GameModels.kt", "r") as f:
    content = f.read()

# Replace TileRegistry items
new_registry = """    val items = mapOf(
        2 to RomaniaItem(2, "🥨", { Localization.strings.reg2name }, { Localization.strings.reg2desc }, 1, Color(0xFFFEF9C3), Color(0xFF713F12), { Localization.strings.reg2badge }),
        4 to RomaniaItem(4, "🥧", { Localization.strings.reg4name }, { Localization.strings.reg4desc }, 1, Color(0xFFFDE68A), Color(0xFF78350F), { Localization.strings.reg4badge }),
        8 to RomaniaItem(8, "🥣", { Localization.strings.reg8name }, { Localization.strings.reg8desc }, 1, Color(0xFFFED7AA), Color(0xFF7C2D12), { Localization.strings.reg8badge }),
        16 to RomaniaItem(16, "🧀", { Localization.strings.reg16name }, { Localization.strings.reg16desc }, 1, Color(0xFFFDBA74), Color(0xFF7C2D12), { Localization.strings.reg16badge }),
        32 to RomaniaItem(32, "🍗", { Localization.strings.reg32name }, { Localization.strings.reg32desc }, 1, Color(0xFFFECACA), Color(0xFF7F1D1D), { Localization.strings.reg32badge }),
        64 to RomaniaItem(64, "🥘", { Localization.strings.reg64name }, { Localization.strings.reg64desc }, 1, Color(0xFFFCA5A5), Color(0xFF7F1D1D), { Localization.strings.reg64badge }),
        128 to RomaniaItem(128, "🍲", { Localization.strings.reg128name }, { Localization.strings.reg128desc }, 1, Color(0xFF93C5FD), Color(0xFF1E3A8A), { Localization.strings.reg128badge }),
        256 to RomaniaItem(256, "☕", { Localization.strings.reg256name }, { Localization.strings.reg256desc }, 2, Color(0xFF60A5FA), Color(0xFFFFFFFF), { Localization.strings.reg256badge }),
        512 to RomaniaItem(512, "🏰", { Localization.strings.reg512name }, { Localization.strings.reg512desc }, 3, Color(0xFF3B82F6), Color(0xFFFFFFFF), { Localization.strings.reg512badge }),
        1024 to RomaniaItem(1024, "🏛️", { Localization.strings.reg1024name }, { Localization.strings.reg1024desc }, 4, Color(0xFF1D4ED8), Color(0xFFFFFFFF), { Localization.strings.reg1024badge }),
        2048 to RomaniaItem(2048, "🇷🇴", { Localization.strings.reg2048name }, { Localization.strings.reg2048desc }, 5, Color(0xFF002B7F), Color(0xFFFFFFFF), { Localization.strings.reg2048badge }),
        4096 to RomaniaItem(4096, "🌟", { Localization.strings.reg4096name }, { Localization.strings.reg4096desc }, 6, Color(0xFF7E22CE), Color(0xFFFFFFFF), { Localization.strings.reg4096badge }),
        8192 to RomaniaItem(8192, "🦅", { Localization.strings.reg8192name }, { Localization.strings.reg8192desc }, 7, Color(0xFF4338CA), Color(0xFFFFFFFF), { Localization.strings.reg8192badge }),
        16384 to RomaniaItem(16384, "👑", { Localization.strings.reg16384name }, { Localization.strings.reg16384desc }, 7, Color(0xFF991B1B), Color(0xFFFFFFFF), { Localization.strings.reg16384badge })
    )
"""
content = re.sub(r'    val items = mapOf\([\s\S]*?    fun getItem', new_registry + '\n    fun getItem', content)

# Replace getItem fallback
new_fallback = """    fun getItem(value: Int): RomaniaItem {
        return items[value] ?: RomaniaItem(
            value = value,
            emoji = "✨",
            nameKey = { Localization.strings.fallbackName + value },
            descriptionKey = { Localization.strings.fallbackDesc },
            levelUnlocked = 7,
            backgroundColor = Color(0xFF311B92),
            textColor = Color(0xFFFFFFFF),
            badgeKey = { Localization.strings.fallbackBadge }
        )
    }"""
content = re.sub(r'    fun getItem\(value: Int\): RomaniaItem \{[\s\S]*?    \}', new_fallback, content)

# Replace GAME_LEVELS
new_levels = """val GAME_LEVELS = listOf(
    GameLevel(1, { Localization.strings.gameLevel1Name }, 8, 100, { Localization.strings.gameLevel1Reward }, 100),
    GameLevel(2, { Localization.strings.gameLevel2Name }, 16, 250, { Localization.strings.gameLevel2Reward }, 250),
    GameLevel(3, { Localization.strings.gameLevel3Name }, 32, 500, { Localization.strings.gameLevel3Reward }, 500),
    GameLevel(4, { Localization.strings.gameLevel4Name }, 64, 1000, { Localization.strings.gameLevel4Reward }, 1000),
    GameLevel(5, { Localization.strings.gameLevel5Name }, 128, 2000, { Localization.strings.gameLevel5Reward }, 1500),
    GameLevel(6, { Localization.strings.gameLevel6Name }, 256, 4000, { Localization.strings.gameLevel6Reward }, 2500),
    GameLevel(7, { Localization.strings.gameLevel7Name }, 512, 8000, { Localization.strings.gameLevel7Reward }, 4000),
    GameLevel(8, { Localization.strings.gameLevel8Name }, 1024, 15000, { Localization.strings.gameLevel8Reward }, 7000),
    GameLevel(9, { Localization.strings.gameLevel9Name }, 2048, 30000, { Localization.strings.gameLevel9Reward }, 10000),
    GameLevel(10, { Localization.strings.gameLevel10Name }, 4096, 60000, { Localization.strings.gameLevel10Reward }, 20000),
    GameLevel(11, { Localization.strings.gameLevel11Name }, 8192, 120000, { Localization.strings.gameLevel11Reward }, 35000),
    GameLevel(12, { Localization.strings.gameLevel12Name }, 16384, 250000, { Localization.strings.gameLevel12Reward }, 50000)
)"""
content = re.sub(r'val GAME_LEVELS = listOf\([\s\S]*?\)\n\ndata class', new_levels + '\n\ndata class', content)

with open("app/src/main/java/com/example/model/GameModels.kt", "w") as f:
    f.write(content)
