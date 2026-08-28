import re

def update_file(filepath, titles, descs):
    with open(filepath, "r") as f:
        content = f.read()
    
    title_pattern = r'override fun getAchievementTitle\(id: String\) = when\(id\) \{.*?\n    \}'
    desc_pattern = r'override fun getAchievementDesc\(id: String\) = when\(id\) \{.*?\n    \}'
    
    title_replacement = 'override fun getAchievementTitle(id: String) = when(id) {\n'
    for k, v in titles.items():
        title_replacement += f'        "{k}" -> "{v}"\n'
    title_replacement += '        else -> "Unknown"\n    }'
    
    desc_replacement = 'override fun getAchievementDesc(id: String) = when(id) {\n'
    for k, v in descs.items():
        desc_replacement += f'        "{k}" -> "{v}"\n'
    desc_replacement += '        else -> "Unknown"\n    }'
    
    if "Necunoscut" in content:
        title_replacement = title_replacement.replace('"Unknown"', '"Necunoscut"')
        desc_replacement = desc_replacement.replace('"Unknown"', '"Necunoscut"')
    
    content = re.sub(title_pattern, title_replacement, content, flags=re.DOTALL)
    content = re.sub(desc_pattern, desc_replacement, content, flags=re.DOTALL)
    
    with open(filepath, "w") as f:
        f.write(content)

en_titles = {
    "1": "First Snack",
    "2": "Something Sweet",
    "3": "Soup Time",
    "4": "Authentic Romanian",
    "5": "Grill Master",
    "6": "Family Dinner",
    "7": "Holiday Scent",
    "8": "Coffee Break",
    "9": "Tourist in Transylvania",
    "10": "Man of Culture",
    "11": "True Patriot",
    "12": "National Hero",
    "13": "Dacian Spirit",
    "14": "Beyond the Limit",
    "15": "Second Chances",
    "16": "Warming Up",
    "17": "Beginner",
    "18": "Amateur",
    "19": "Professional",
    "20": "Traveler",
    "21": "Avid Collector",
    "22": "Supreme Speed",
    "23": "Prosperous Restart",
    "24": "White Nights",
    "25": "Romanian Heart"
}

en_descs = {
    "1": "Discover tile 2 (Pretzel)",
    "2": "Discover tile 4 (Pie)",
    "3": "Discover tile 8 (Tripe Soup)",
    "4": "Discover tile 16 (Polenta with Cheese)",
    "5": "Discover tile 32 (Mici with Mustard)",
    "6": "Discover tile 64 (Sarmale)",
    "7": "Discover tile 128 (Cozonac)",
    "8": "Discover tile 256 (Ibrik Coffee)",
    "9": "Discover tile 512 (Bran Castle)",
    "10": "Discover tile 1024 (Romanian Athenaeum)",
    "11": "Discover tile 2048 (Romania 2048)",
    "12": "Discover tile 4096 (Michael the Brave)",
    "13": "Discover tile 8192 (Dacian Wolf)",
    "14": "Keep playing after reaching 2048!",
    "15": "Use the Undo feature 3 times.",
    "16": "Make your first 10 moves.",
    "17": "Reach a score of 10,000 pts.",
    "18": "Reach a score of 25,000 pts.",
    "19": "Reach a score of 50,000 pts.",
    "20": "Unlock 5 levels in Adventure Mode.",
    "21": "Unlock 8 tiles in the Collection.",
    "22": "Make 50 moves in a single game.",
    "23": "Start a new game after scoring over 100.",
    "24": "Switch the app to Dark mode.",
    "25": "Unlock 7 levels in Adventure Mode."
}

ro_titles = {
    "1": "Prima Gustare",
    "2": "Ceva Dulce",
    "3": "La Ciorbă",
    "4": "Autentic Românesc",
    "5": "Grătaragiu",
    "6": "Masa în Familie",
    "7": "Miros de Sărbătoare",
    "8": "Pauza de Cafea",
    "9": "Turist în Transilvania",
    "10": "Om de Cultură",
    "11": "Patriot Adevărat",
    "12": "Erou Național",
    "13": "Spirit Dacic",
    "14": "Dincolo de Limite",
    "15": "A Doua Șansă",
    "16": "Încălzirea",
    "17": "Începător",
    "18": "Amator",
    "19": "Profesionist",
    "20": "Călător",
    "21": "Colecționar Harnic",
    "22": "Viteză Supremă",
    "23": "Reînceput Prosper",
    "24": "Nopți Albe",
    "25": "Inimă de Român"
}

ro_descs = {
    "1": "Descoperă piesa 2 (Covrig)",
    "2": "Descoperă piesa 4 (Plăcintă)",
    "3": "Descoperă piesa 8 (Ciorbă)",
    "4": "Descoperă piesa 16 (Mămăligă cu Brânză)",
    "5": "Descoperă piesa 32 (Mici)",
    "6": "Descoperă piesa 64 (Sarmale)",
    "7": "Descoperă piesa 128 (Cozonac)",
    "8": "Descoperă piesa 256 (Cafea la Ibric)",
    "9": "Descoperă piesa 512 (Castelul Bran)",
    "10": "Descoperă piesa 1024 (Ateneul Român)",
    "11": "Descoperă piesa 2048 (România 2048)",
    "12": "Descoperă piesa 4096 (Mihai Viteazul)",
    "13": "Descoperă piesa 8192 (Lupul Dacic)",
    "14": "Continuă jocul după ce ai atins 2048!",
    "15": "Folosește funcția Undo de 3 ori.",
    "16": "Efectuează primele 10 mutări.",
    "17": "Atinge un scor de 10.000 pct.",
    "18": "Atinge un scor de 25.000 pct.",
    "19": "Atinge un scor de 50.000 pct.",
    "20": "Deblochează 5 niveluri în Modul Aventură.",
    "21": "Deblochează 8 piese în Colecție.",
    "22": "Efectuează 50 de mutări într-un joc.",
    "23": "Începe un joc nou după ce ai depășit 100 pct.",
    "24": "Comută aplicația în modul Dark.",
    "25": "Deblochează 7 niveluri în Modul Aventură."
}

update_file("app/src/main/java/com/example/ui/strings/StringsEn.kt", en_titles, en_descs)
update_file("app/src/main/java/com/example/ui/strings/StringsRo.kt", ro_titles, ro_descs)
print("Updated successfully!")
