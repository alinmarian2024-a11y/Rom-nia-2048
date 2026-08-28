import re
with open("app/src/main/java/com/example/ui/strings/AppStrings.kt", "r") as f:
    c = f.read()
    
c = c.replace("val btnCancel: String", "val btnCancel: String\n    val dpadUp: String\n    val dpadDown: String\n    val dpadLeft: String\n    val dpadRight: String")
c = c.replace("override val btnCancel = \"ANULEAZĂ\"", "override val btnCancel = \"ANULEAZĂ\"\n    override val dpadUp = \"Sus\"\n    override val dpadDown = \"Jos\"\n    override val dpadLeft = \"Stânga\"\n    override val dpadRight = \"Dreapta\"")
c = c.replace("override val btnCancel = \"CANCEL\"", "override val btnCancel = \"CANCEL\"\n    override val dpadUp = \"Up\"\n    override val dpadDown = \"Down\"\n    override val dpadLeft = \"Left\"\n    override val dpadRight = \"Right\"")

with open("app/src/main/java/com/example/ui/strings/AppStrings.kt", "w") as f:
    f.write(c)

with open("app/src/main/java/com/example/ui/components/BoardView.kt", "r") as f:
    b = f.read()

if "import com.example.ui.strings.Localization" not in b:
    b = b.replace("import androidx.compose", "import com.example.ui.strings.Localization\nimport androidx.compose")

b = b.replace('"Sus"', 'Localization.strings.dpadUp')
b = b.replace('"Stânga"', 'Localization.strings.dpadLeft')
b = b.replace('"Jos"', 'Localization.strings.dpadDown')
b = b.replace('"Dreapta"', 'Localization.strings.dpadRight')

with open("app/src/main/java/com/example/ui/components/BoardView.kt", "w") as f:
    f.write(b)

