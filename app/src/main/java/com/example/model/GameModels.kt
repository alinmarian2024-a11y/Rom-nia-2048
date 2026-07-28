package com.example.model

import androidx.compose.ui.graphics.Color

data class Tile(
    val id: Long,
    val value: Int,
    val row: Int,
    val col: Int,
    val isNew: Boolean = false,
    val isMerged: Boolean = false
)

data class RomaniaItem(
    val value: Int,
    val emoji: String,
    val name: String,
    val description: String,
    val levelUnlocked: Int,
    val backgroundColor: Color,
    val textColor: Color,
    val badge: String
)

object TileRegistry {
    val items = mapOf(
        2 to RomaniaItem(
            value = 2,
            emoji = "🥨",
            name = "Covrig",
            description = "Covrigul – combustibilul oficial al românului grăbit.",
            levelUnlocked = 1,
            backgroundColor = Color(0xFFFEF9C3),
            textColor = Color(0xFF713F12),
            badge = "Gustare"
        ),
        4 to RomaniaItem(
            value = 4,
            emoji = "🥧",
            name = "Plăcintă",
            description = "Plăcinta caldă cu brânză, desertul nostalgic al copilăriei.",
            levelUnlocked = 1,
            backgroundColor = Color(0xFFFDE68A),
            textColor = Color(0xFF78350F),
            badge = "Desert"
        ),
        8 to RomaniaItem(
            value = 8,
            emoji = "🥣",
            name = "Mămăligă",
            description = "Mămăliga aurie, companionul de nelipsit de pe masa tradițională.",
            levelUnlocked = 1,
            backgroundColor = Color(0xFFFED7AA),
            textColor = Color(0xFF7C2D12),
            badge = "Tradiție"
        ),
        16 to RomaniaItem(
            value = 16,
            emoji = "🧀",
            name = "Brânză",
            description = "Brânză de burduf sau telemea, savoare pur carpatică.",
            levelUnlocked = 1,
            backgroundColor = Color(0xFFFDBA74),
            textColor = Color(0xFF7C2D12),
            badge = "Deliciu"
        ),
        32 to RomaniaItem(
            value = 32,
            emoji = "🍗",
            name = "Copane",
            description = "Copane perpelite la grătar, bucuria duminicilor în familie.",
            levelUnlocked = 1,
            backgroundColor = Color(0xFFFECACA),
            textColor = Color(0xFF7F1D1D),
            badge = "Mâncare"
        ),
        64 to RomaniaItem(
            value = 64,
            emoji = "🥘",
            name = "Sarmale",
            description = "Sarmalele în foi de varză, regina incontestabilă a sărbătorilor.",
            levelUnlocked = 1,
            backgroundColor = Color(0xFFFCA5A5),
            textColor = Color(0xFF7F1D1D),
            badge = "Sărbătoare"
        ),
        128 to RomaniaItem(
            value = 128,
            emoji = "🍲",
            name = "Ciorbă",
            description = "Ciorba rădăuțeană sau de burtă, leacul suprem românesc.",
            levelUnlocked = 1,
            backgroundColor = Color(0xFF93C5FD),
            textColor = Color(0xFF1E3A8A),
            badge = "Ospăț"
        ),
        256 to RomaniaItem(
            value = 256,
            emoji = "☕",
            name = "Cafea",
            description = "Cafeaua la ibric, secretul conversațiilor lungi cu prietenii.",
            levelUnlocked = 2,
            backgroundColor = Color(0xFF60A5FA),
            textColor = Color(0xFFFFFFFF),
            badge = "Energie"
        ),
        512 to RomaniaItem(
            value = 512,
            emoji = "🏰",
            name = "Castel",
            description = "Castelul Peleș sau Bran, mândria arhitecturală a Carpaților.",
            levelUnlocked = 3,
            backgroundColor = Color(0xFF3B82F6),
            textColor = Color(0xFFFFFFFF),
            badge = "Istorie"
        ),
        1024 to RomaniaItem(
            value = 1024,
            emoji = "🏛️",
            name = "Palat",
            description = "Palatul Parlamentului, monument gigantic al istoriei moderne.",
            levelUnlocked = 4,
            backgroundColor = Color(0xFF1D4ED8),
            textColor = Color(0xFFFFFFFF),
            badge = "Monument"
        ),
        2048 to RomaniaItem(
            value = 2048,
            emoji = "🇷🇴",
            name = "România",
            description = "România – țara tuturor posibilităților și a spiritului unit!",
            levelUnlocked = 5,
            backgroundColor = Color(0xFF002B7F),
            textColor = Color(0xFFFFFFFF),
            badge = "Glorie"
        ),
        4096 to RomaniaItem(
            value = 4096,
            emoji = "🌟",
            name = "Luceafărul",
            description = "Luceafărul – geniul eminescian și aspirația spre infinit.",
            levelUnlocked = 6,
            backgroundColor = Color(0xFF7E22CE),
            textColor = Color(0xFFFFFFFF),
            badge = "Mister"
        ),
        8192 to RomaniaItem(
            value = 8192,
            emoji = "🦅",
            name = "Vulturul",
            description = "Vulturul Carpaților – simbolul libertății și al măreției.",
            levelUnlocked = 7,
            backgroundColor = Color(0xFF4338CA),
            textColor = Color(0xFFFFFFFF),
            badge = "Legendă"
        ),
        16384 to RomaniaItem(
            value = 16384,
            emoji = "👑",
            name = "Coroana",
            description = "Coroana Regală – simbol al nobleței și rezistenței în timp.",
            levelUnlocked = 7,
            backgroundColor = Color(0xFF991B1B),
            textColor = Color(0xFFFFFFFF),
            badge = "Măreție"
        )
    )

    fun getItem(value: Int): RomaniaItem {
        return items[value] ?: RomaniaItem(
            value = value,
            emoji = "✨",
            name = "Piesă $value",
            description = "O piesă de valoare impresionantă în construcția României!",
            levelUnlocked = 7,
            backgroundColor = Color(0xFF311B92),
            textColor = Color(0xFFFFFFFF),
            badge = "Suprem"
        )
    }
}

enum class GameMode {
    INFINITE,
    ADVENTURE
}

data class GameLevel(
    val levelNumber: Int,
    val title: String,
    val targetTile: Int,
    val minScore: Int = 0,
    val rewardName: String = "",
    val rewardBonus: Int = 0
)

val GAME_LEVELS = listOf(
    GameLevel(1, "Nivelul 1: Mămăliga Aurie", 8, 100, "Insignă Mămăligă", 100),
    GameLevel(2, "Nivelul 2: Brânza de Burduf", 16, 250, "Insignă Brânză", 250),
    GameLevel(3, "Nivelul 3: Copane la Grătar", 32, 500, "Insignă Copane", 500),
    GameLevel(4, "Nivelul 4: Sarmale Tradiționale", 64, 1000, "Insignă Sarmale", 1000),
    GameLevel(5, "Nivelul 5: Ciorba Rădăuțeană", 128, 2000, "Insignă Ciorbă", 1500),
    GameLevel(6, "Nivelul 6: Cafea la Ibric", 256, 4000, "Insignă Cafea", 2500),
    GameLevel(7, "Nivelul 7: Castelul Peleș", 512, 8000, "Insignă Castel", 4000),
    GameLevel(8, "Nivelul 8: Palatul Parlamentului", 1024, 15000, "Insignă Palat", 7000),
    GameLevel(9, "Nivelul 9: Marea Unire 2048", 2048, 30000, "Insignă Patriot", 10000),
    GameLevel(10, "Nivelul 10: Poezia Luceafărului", 4096, 60000, "Insignă Geniu", 20000),
    GameLevel(11, "Nivelul 11: Vulturul Carpaților", 8192, 120000, "Insignă Legendă", 35000),
    GameLevel(12, "Nivelul 12: Coroana Regală", 16384, 250000, "Insignă Supremă", 50000)
)

data class Achievement(
    val id: String,
    val emoji: String,
    val title: String,
    val description: String,
    val isUnlocked: Boolean = false,
    val progress: Int = 0,
    val maxProgress: Int = 1
)

data class GridSnapshot(
    val grid: List<List<Int>>,
    val score: Int
)

data class GameState(
    val gameMode: GameMode = GameMode.INFINITE,
    val grid: List<List<Int>> = List(4) { List(4) { 0 } },
    val tiles: List<Tile> = emptyList(),
    val score: Int = 0,
    val highScore: Int = 0,
    val currentLevel: Int = 1,
    val highestTileAchieved: Int = 0,
    val movesCount: Int = 0,
    val undoCount: Int = 0,
    val isGameOver: Boolean = false,
    val isWon: Boolean = false,
    val keepPlayingPast2048: Boolean = false,
    val undoStack: List<GridSnapshot> = emptyList(),
    val unlockedLevels: Set<Int> = setOf(1),
    val completedLevels: Set<Int> = emptySet(),
    val unlockedCollectionValues: Set<Int> = setOf(2),
    val unlockedAchievementIds: Set<Int> = emptySet()
)
