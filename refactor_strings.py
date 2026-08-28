import re

def process_file(filepath, lang):
    with open(filepath, 'r') as f:
        content = f.read()

    # Step 1: Update regXnames to exact requests
    if lang == "ro":
        names = {
            "2": "Covrig",
            "4": "Plăcintă",
            "8": "Ciorbă",
            "16": "Brânză",
            "32": "Copane",
            "64": "Sarmale",
            "128": "Tocăniță",
            "256": "Cafea la Ibric",
            "512": "Castelul Bran",
            "1024": "Ateneul Român",
            "2048": "România 2048",
            "4096": "Mihai Viteazul",
            "8192": "Vultur",
            "16384": "Coroana Regală"
        }
    else:
        names = {
            "2": "Pretzel",
            "4": "Pie",
            "8": "Tripe Soup",
            "16": "Cheese",
            "32": "Chicken Drumsticks",
            "64": "Cabbage Rolls",
            "128": "Stew",
            "256": "Ibrik Coffee",
            "512": "Bran Castle",
            "1024": "Romanian Athenaeum",
            "2048": "Romania 2048",
            "4096": "Michael the Brave",
            "8192": "Eagle",
            "16384": "Royal Crown"
        }
        
    for k, v in names.items():
        pattern = rf'(override val reg{k}name\s*=\s*)".*?"'
        if re.search(pattern, content):
            content = re.sub(pattern, rf'\1"{v}"', content)
            
    # Step 2: Update gameLevelXReward to use property getters
    # Levels target: 1->8, 2->16, 3->32, 4->64, 5->128, 6->256, 7->512, 8->1024, 9->2048, 10->4096, 11->8192, 12->16384
    level_targets = {
        1: 8, 2: 16, 3: 32, 4: 64, 5: 128, 6: 256, 7: 512, 8: 1024, 9: 2048, 10: 4096, 11: 8192, 12: 16384
    }
    
    for lvl, target in level_targets.items():
        pattern = rf'override val gameLevel{lvl}Reward\s*=\s*".*?"'
        replacement = f'override val gameLevel{lvl}Reward: String get() = "$reg{target}name ({target})"'
        content = re.sub(pattern, replacement, content)
        
    # Step 3: Update getAchievementDesc for 1..13
    achievement_targets = {
        1: 2, 2: 4, 3: 8, 4: 16, 5: 32, 6: 64, 7: 128, 8: 256, 9: 512, 10: 1024, 11: 2048, 12: 4096, 13: 8192
    }
    
    def repl_achievement(m):
        block = m.group(1)
        for ach, target in achievement_targets.items():
            # e.g. "1" -> "Descoperă piesa 2 (...)"
            if lang == "ro":
                repl = f'"{ach}" -> "Descoperă piesa {target} ($reg{target}name)"'
            else:
                repl = f'"{ach}" -> "Discover tile {target} ($reg{target}name)"'
                
            block = re.sub(rf'"{ach}"\s*->\s*".*?"', repl, block)
        return f"override fun getAchievementDesc(id: String) = when(id) {{{block}}}"
        
    content = re.sub(r'override fun getAchievementDesc\(id: String\) = when\(id\) \{(.*?)\}', repl_achievement, content, flags=re.DOTALL)

    with open(filepath, 'w') as f:
        f.write(content)

process_file("app/src/main/java/com/example/ui/strings/StringsRo.kt", "ro")
process_file("app/src/main/java/com/example/ui/strings/StringsEn.kt", "en")
print("Done!")
