with open("app/src/main/java/com/example/monetization/BillingManager.kt", "r") as f:
    content = f.read()

# Remove context from bottom
context_block = """    private val context: Context = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
        baseContext.createAttributionContext("default")
    } else {
        baseContext
    }"""
content = content.replace(context_block, "")

# Insert it at the top
target = """    companion object {
        private const val TAG = "BillingManager"
        const val PRODUCT_REMOVE_ADS = "remove_ads"
    }"""

new_target = target + "\n\n" + context_block

content = content.replace(target, new_target)

with open("app/src/main/java/com/example/monetization/BillingManager.kt", "w") as f:
    f.write(content)
