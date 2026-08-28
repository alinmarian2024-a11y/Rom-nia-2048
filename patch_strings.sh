cat << 'INNER_EOF' > /tmp/add_strings.txt
    val contentDescBack: String
    val contentDescPlay: String
    val contentDescPurchased: String
    val contentDescLocked: String
    val contentDescUnlocked: String
    val contentDescQuickMenu: String
    val contentDescPause: String
    val unknownTile: String
    fun undoFreeRemaining(remaining: Int, total: Int): String
    val undoAdTooltipPurchased: String
    val undoAdTooltipAd: String
INNER_EOF
sed -i '/val statusLocked: String/r /tmp/add_strings.txt' app/src/main/java/com/example/ui/strings/AppStrings.kt

cat << 'INNER_EOF' > /tmp/add_strings_en.txt
    override val contentDescBack = "Back"
    override val contentDescPlay = "Play"
    override val contentDescPurchased = "Purchased"
    override val contentDescLocked = "Locked"
    override val contentDescUnlocked = "Unlocked"
    override val contentDescQuickMenu = "Quick Menu"
    override val contentDescPause = "Pause"
    override val unknownTile = "???"
    override fun undoFreeRemaining(remaining: Int, total: Int) = "UNDO ($remaining/$total)"
    override val undoAdTooltipPurchased = "UNDO (+1)"
    override val undoAdTooltipAd = "UNDO 🎬"
INNER_EOF
sed -i '/override val statusLocked = "LOCKED"/r /tmp/add_strings_en.txt' app/src/main/java/com/example/ui/strings/StringsEn.kt

cat << 'INNER_EOF' > /tmp/add_strings_ro.txt
    override val contentDescBack = "Înapoi"
    override val contentDescPlay = "Joacă"
    override val contentDescPurchased = "Achiziționat"
    override val contentDescLocked = "Blocat"
    override val contentDescUnlocked = "Deblocat"
    override val contentDescQuickMenu = "Meniu Rapid"
    override val contentDescPause = "Pauză"
    override val unknownTile = "???"
    override fun undoFreeRemaining(remaining: Int, total: Int) = "UNDO ($remaining/$total)"
    override val undoAdTooltipPurchased = "UNDO (+1)"
    override val undoAdTooltipAd = "UNDO 🎬"
INNER_EOF
sed -i '/override val statusLocked = "BLOCAT"/r /tmp/add_strings_ro.txt' app/src/main/java/com/example/ui/strings/StringsRo.kt

echo "Added"
