with open("app/src/main/java/com/example/ui/screens/SettingsScreen.kt", "r") as f:
    lines = f.readlines()

new_lines = []
skip = False
for line in lines:
    if "// Monetization / Remove Ads Section" in line:
        skip = True
    if skip and "        // Reset Data Button" in line:
        skip = False
    
    if not skip:
        new_lines.append(line)

with open("app/src/main/java/com/example/ui/screens/SettingsScreen.kt", "w") as f:
    f.writelines(new_lines)
