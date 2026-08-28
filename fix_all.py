import re

def update_strings(filepath, lang):
    with open(filepath, "r") as f:
        content = f.read()
    
    # Update reg*name variables
    if lang == "en":
        reg_updates = {
            "reg16name": "Cheese",
            "reg32name": "Chicken Drumsticks",
            "reg64name": "Cabbage Rolls",
            "reg128name": "Mici",
            "reg8192name": "Eagle"
        }
    else:
        reg_updates = {
            "reg16name": "Brânză",
            "reg32name": "Copane",
            "reg64name": "Sarmale",
            "reg128name": "Mici",
            "reg8192name": "Vultur"
        }
    
    for key, val in reg_updates.items():
        pattern = rf'override val {key}\s*=\s*".*?"'
        replacement = f'override val {key} = "{val}"'
        content = re.sub(pattern, replacement, content)
        
    # Update getAchievementDesc
    if lang == "en":
        desc_updates = {
            "4": "Discover tile 16 (Cheese)",
            "5": "Discover tile 32 (Chicken Drumsticks)",
            "6": "Discover tile 64 (Cabbage Rolls)",
            "7": "Discover tile 128 (Mici)",
            "13": "Discover tile 8192 (Eagle)"
        }
    else:
        desc_updates = {
            "4": "Descoperă piesa 16 (Brânză)",
            "5": "Descoperă piesa 32 (Copane)",
            "6": "Descoperă piesa 64 (Sarmale)",
            "7": "Descoperă piesa 128 (Mici)",
            "13": "Descoperă piesa 8192 (Vultur)"
        }
        
    for key, val in desc_updates.items():
        # Match the specific case in the when block
        pattern = rf'"{key}"\s*->\s*".*?"'
        replacement = f'"{key}" -> "{val}"'
        content = re.sub(pattern, replacement, content)
        
    with open(filepath, "w") as f:
        f.write(content)

update_strings("app/src/main/java/com/example/ui/strings/StringsEn.kt", "en")
update_strings("app/src/main/java/com/example/ui/strings/StringsRo.kt", "ro")
print("Updated successfully!")
