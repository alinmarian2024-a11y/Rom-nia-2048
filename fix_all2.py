import re

def update_strings(filepath, lang):
    with open(filepath, "r") as f:
        content = f.read()

    if lang == "en":
        # Titles
        content = re.sub(r'override fun getAchievementTitle\(id: String\) = when\(id\) \{(.*?)\}', 
            lambda m: m.group(0)
                .replace('"4" -> "Discover tile 16 (Cheese)"', '"4" -> "Authentic Romanian"')
                .replace('"5" -> "Discover tile 32 (Chicken Drumsticks)"', '"5" -> "Grill Master"')
                .replace('"6" -> "Discover tile 64 (Cabbage Rolls)"', '"6" -> "Family Dinner"')
                .replace('"7" -> "Discover tile 128 (Mici)"', '"7" -> "Holiday Scent"')
                .replace('"13" -> "Discover tile 8192 (Eagle)"', '"13" -> "Dacian Spirit"'),
            content, flags=re.DOTALL)
        
        # 128 values
        content = re.sub(r'override val gameLevel5Reward = "Mici \(128\)"', 'override val gameLevel5Reward = "Stew (128)"', content)
        content = re.sub(r'override val reg128name = "Mici"', 'override val reg128name = "Stew"', content)
        content = re.sub(r'"7" -> "Discover tile 128 \(Mici\)"', '"7" -> "Discover tile 128 (Stew)"', content)
        
    else:
        # Titles
        content = re.sub(r'override fun getAchievementTitle\(id: String\) = when\(id\) \{(.*?)\}', 
            lambda m: m.group(0)
                .replace('"4" -> "Descoperă piesa 16 (Brânză)"', '"4" -> "Autentic Românesc"')
                .replace('"5" -> "Descoperă piesa 32 (Copane)"', '"5" -> "Grătaragiu"')
                .replace('"6" -> "Descoperă piesa 64 (Sarmale)"', '"6" -> "Masa în Familie"')
                .replace('"7" -> "Descoperă piesa 128 (Mici)"', '"7" -> "Miros de Sărbătoare"')
                .replace('"13" -> "Descoperă piesa 8192 (Vultur)"', '"13" -> "Spirit Dacic"'),
            content, flags=re.DOTALL)
        
        # 128 values
        content = re.sub(r'override val gameLevel5Reward = "Mici \(128\)"', 'override val gameLevel5Reward = "Tocăniță (128)"', content)
        content = re.sub(r'override val reg128name = "Mici"', 'override val reg128name = "Tocăniță"', content)
        content = re.sub(r'"7" -> "Descoperă piesa 128 \(Mici\)"', '"7" -> "Descoperă piesa 128 (Tocăniță)"', content)

    with open(filepath, "w") as f:
        f.write(content)

update_strings("app/src/main/java/com/example/ui/strings/StringsEn.kt", "en")
update_strings("app/src/main/java/com/example/ui/strings/StringsRo.kt", "ro")
print("Updated successfully!")
