with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

content = content.replace("""    override fun getAttributionTag(): String? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) "default" else super.getAttributionTag()
    }
    
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                base.createAttributionContext("default")
            } else {
                base
            }
        )
    }""", "")

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)

with open("app/src/main/java/com/example/MainApplication.kt", "w") as f:
    f.write("""package com.example

import android.app.Application

class MainApplication : Application()
""")
