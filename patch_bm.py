with open("app/src/main/java/com/example/monetization/BillingManager.kt", "r") as f:
    content = f.read()

old_ctx = "    private val context: Context,"
new_ctx = "    private val baseContext: Context,"

content = content.replace(old_ctx, new_ctx)

old_init = "    init {"
new_init = """    private val context: Context = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
        baseContext.createAttributionContext("default")
    } else {
        baseContext
    }

    init {"""

content = content.replace(old_init, new_init)

with open("app/src/main/java/com/example/monetization/BillingManager.kt", "w") as f:
    f.write(content)
