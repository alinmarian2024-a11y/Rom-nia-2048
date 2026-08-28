import re

with open("app/src/main/java/com/example/ui/strings/StringsEn.kt", "r") as f:
    content = f.read()

old_desc = """    override fun getAchievementDesc(id: String) = when(id) {
        "1" -> "Discover tile 4 (Pie)"
        "2" -> "Discover tile 8 (Soup)"
        "3" -> "Discover tile 16 (Polenta)"
        "4" -> "Discover tile 32 (Mici)"
        "5" -> "Discover tile 64 (Sarmale)"
        "6" -> "Discover tile 128 (Cozonac)"
        "7" -> "Discover tile 256 (Coffee)"
        "8" -> "Discover tile 512 (Castle)"
        "9" -> "Discover tile 1024 (Athenaeum)"
        "10" -> "Discover tile 2048 (Romania)"
        "11" -> "Discover tile 4096 (Michael the Brave)"
        "12" -> "Discover tile 8192 (Dacian Wolf)"
        "13" -> "Discover tile 16384 (Royal Crown)"
        "14" -> "Get the rarest possible tile!\""""

new_desc = """    override fun getAchievementDesc(id: String) = when(id) {
        "1" -> "Discover tile 2 (Pretzel)"
        "2" -> "Discover tile 4 (Pie)"
        "3" -> "Discover tile 8 (Tripe Soup)"
        "4" -> "Discover tile 16 (Polenta with Cheese)"
        "5" -> "Discover tile 32 (Mici with Mustard)"
        "6" -> "Discover tile 64 (Sarmale)"
        "7" -> "Discover tile 128 (Cozonac)"
        "8" -> "Discover tile 256 (Ibrik Coffee)"
        "9" -> "Discover tile 512 (Bran Castle)"
        "10" -> "Discover tile 1024 (Romanian Athenaeum)"
        "11" -> "Discover tile 2048 (Romania 2048)"
        "12" -> "Discover tile 4096 (Michael the Brave)"
        "13" -> "Discover tile 8192 (Dacian Wolf)"
        "14" -> "Get the rarest possible tile!\""""

if old_desc in content:
    print("Found exact match!")
    content = content.replace(old_desc, new_desc)
    with open("app/src/main/java/com/example/ui/strings/StringsEn.kt", "w") as f:
        f.write(content)
else:
    print("Did not find exact match, checking manually.")

