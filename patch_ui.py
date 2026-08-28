import re
import os

def replace_in_file(filepath, replacements):
    with open(filepath, "r") as f:
        content = f.read()
    
    # Add import if missing
    if "import com.example.ui.strings.Localization" not in content and "import com.example.ui.strings.Language" not in content:
        content = content.replace("import androidx.compose", "import com.example.ui.strings.Localization\nimport androidx.compose")
    
    for old, new in replacements.items():
        content = content.replace(old, new)
        
    with open(filepath, "w") as f:
        f.write(content)

# 1. ModeSelectionScreen
replace_in_file("app/src/main/java/com/example/ui/screens/ModeSelectionScreen.kt", {
    '"🎮 ALEGE MODUL DE JOC"': 'Localization.strings.modeSelectionTitle',
    '"Selectează experiența de joc pe care o dorești:"': 'Localization.strings.modeSelectionDesc',
    '"MOD AVENTURĂ"': 'Localization.strings.modeAdventureTitle',
    '"Progres pe niveluri"': 'Localization.strings.modeAdventureDesc',
    '"Descoperă preparatele românești, completează colecția și deblochează noi niveluri pas cu pas."': 'Localization.strings.modeAdventureLongDesc',
    '"Niveluri deblocate: ${gameState.unlockedLevels.size} / 12"': 'Localization.strings.unlockedLevelsPrefix + "${gameState.unlockedLevels.size} / 12"',
    '"VEZI NIVELURILE"': 'Localization.strings.viewLevelsBtn',
    '"MOD INFINIT"': 'Localization.strings.modeInfiniteTitle',
    '"Clasic fără sfârșit"': 'Localization.strings.modeInfiniteDesc',
    '"Joacă fără limită, combină cât mai multe piese și încearcă să-ți depășești recordul personal."': 'Localization.strings.modeInfiniteLongDesc',
    '"Record: ${gameState.highScore} pct"': 'Localization.strings.record + ": ${gameState.highScore}"',
    '"JOACĂ ACUM"': 'Localization.strings.playNow',
})

# 2. LevelsScreen
replace_in_file("app/src/main/java/com/example/ui/screens/LevelsScreen.kt", {
    '"🗺 NIVELURI"': 'Localization.strings.levelsTitle',
    '"Progresează prin nivelurile României combinând piese și atingând obiectivele stabilite!"': 'Localization.strings.levelsSubtitle',
    '"Obiectiv: Descoperă Piesa ${level.targetTile} (${targetItem.name})"': 'Localization.strings.levelObjectivePrefix + "${level.targetTile} (${targetItem.name})"',
    '"Recompensă: ${level.rewardName}"': 'Localization.strings.levelRewardPrefix + level.rewardName',
    'if (isCompleted) "REJOACĂ" else "JOACĂ"': 'if (isCompleted) Localization.strings.levelBtnReplay else Localization.strings.levelBtnPlay'
})

# 3. CollectionScreen
replace_in_file("app/src/main/java/com/example/ui/screens/CollectionScreen.kt", {
    '"🎨 COLECȚIE"': 'Localization.strings.collectionHeaderTitle',
    '"Toate piesele românești pe care le-ai creat în joc (${gameState.unlockedCollectionValues.size}/${allItemsList.size}):"': 'Localization.strings.collectionSubtitle + "(${gameState.unlockedCollectionValues.size}/${allItemsList.size}):"',
    'if (isUnlocked) "DEBLOCAT" else "BLOCAT"': 'if (isUnlocked) Localization.strings.statusUnlocked else Localization.strings.statusLocked',
    '"Atinge piesa ${item.value} în joc pentru a debloca acest obiect tradițional!"': 'Localization.strings.collectionLockedDesc.format(item.value)'
})

# 4. AchievementsScreen
replace_in_file("app/src/main/java/com/example/ui/screens/AchievementsScreen.kt", {
    '"🏆 REALIZĂRI"': 'Localization.strings.achievementsTitle',
    '"Deblochează toate cele ${allAchievements.size} de realizări (${gameState.unlockedAchievementIds.size}/${allAchievements.size}):"': 'Localization.strings.achievementsSubtitle.format(allAchievements.size) + "(${gameState.unlockedAchievementIds.size}/${allAchievements.size}):"',
})

# 5. AboutScreen
replace_in_file("app/src/main/java/com/example/ui/screens/AboutScreen.kt", {
    '"ℹ DESPRE JOC"': 'Localization.strings.aboutTitle',
    '"ROMÂNIA 2048"': 'Localization.strings.appTitle',
    '"„ROMÂNIA 2048 este un joc casual inspirat de România, de lucrurile mici și mari care ne definesc și de plăcerea de a construi ceva pas cu pas.”"': 'Localization.strings.aboutDescription',
    '"Versiunea 1.0.0"': 'Localization.strings.version',
    '"© România 2048"': 'Localization.strings.copyright'
})

# 6. SettingsScreen
replace_in_file("app/src/main/java/com/example/ui/screens/SettingsScreen.kt", {
    '"⚙ SETĂRI"': 'Localization.strings.settingsTitle',
    '"AUDIO & VIBRAȚII"': 'Localization.strings.audioSection',
    '"🌙 TEMĂ VIZUALĂ"': 'Localization.strings.visualSection',
    '"⚡ PREFERINȚE JOC"': 'Localization.strings.gamePrefsSection',
    '"🗑️ RESETARE PROGRES"': 'Localization.strings.resetSection',
    '"🎵 VOLUM MUZICĂ: ${ (musicVol * 100).toInt() }%"': 'Localization.strings.musicVol + "${ (musicVol * 100).toInt() }%"',
    '"🔊 VOLUM EFECTE SONORE: ${ (sfxVol * 100).toInt() }%"': 'Localization.strings.sfxVol + "${ (sfxVol * 100).toInt() }%"',
    '"🔔 VIBRAȚII"': 'Localization.strings.vibration',
    '"LIGHT"': 'Localization.strings.themeLight',
    '"DARK"': 'Localization.strings.themeDark',
    '"SYSTEM"': 'Localization.strings.themeSystem',
    '"🎨 TEMĂ ROMÂNEASCĂ"': 'Localization.strings.romanianTheme',
    '"⚡ ANIMAȚII"': 'Localization.strings.animations',
    '"CONFIRMĂ RESTART"': 'Localization.strings.confirmRestartPref'
})

# 7. AchievementToast
replace_in_file("app/src/main/java/com/example/ui/components/AchievementToast.kt", {
    '"🏆 REALIZARE DEBLOCATĂ!"': 'Localization.strings.achievementUnlocked'
})

# 8. Dialogs
replace_in_file("app/src/main/java/com/example/ui/components/Dialogs.kt", {
    '"ANULEAZĂ"': 'Localization.strings.btnCancel',
    '"🔄 Joc Nou"': 'Localization.strings.restartDialogTitle',
    '"Ești sigur că vrei să începi un joc nou? Progresul jocului curent va fi resetat."': 'Localization.strings.restartDialogDesc',
    '"DA, RESTART"': 'Localization.strings.restartDialogConfirm',
    '"⏸ JOC PUS PE PAUZĂ"': 'Localization.strings.pauseDialogTitle',
    '"FELICITĂRI!"': 'Localization.strings.victoryTitle',
    '"„Ai construit România, piesă cu piesă!”"': 'Localization.strings.victoryDesc',
    '"▶ CONTINUĂ"': 'Localization.strings.btnContinuePlaying',
    '"🔄 JOACĂ DIN NOU"': 'Localization.strings.btnPlayAgain',
    '"🏠 MENIU PRINCIPAL"': 'Localization.strings.btnHome',
    '"GAME OVER"': 'Localization.strings.gameOverTitle',
    '"Nu mai ai mutări disponibile."': 'Localization.strings.gameOverDesc',
    '"🏆 RECORD NOU! $score"': 'Localization.strings.gameOverNewRecord + "$score"',
    '"Scor final: $score\\nRecord: $highScore"': 'Localization.strings.gameOverScore.format(score, highScore)',
    'if (isAdsRemoved) "▶ CONTINUĂ JOCUL" else "🎬 CONTINUĂ (RECLAMĂ)"': 'if (isAdsRemoved) Localization.strings.continueGameBtn else Localization.strings.continueAdBtn',
    '"↩ Undo Suplimentar"': 'Localization.strings.extraUndoTitle',
    '"🎬 Undo Suplimentar"': 'Localization.strings.extraUndoTitle',
    'if (isAdsRemoved) "▶ PRIMEȘTE UNDO" else "🎬 VEZI RECLAMĂ (+1 UNDO)"': 'if (isAdsRemoved) Localization.strings.extraUndoBtnFree else Localization.strings.extraUndoBtnAd',
    '"Folosește o șansă pentru a-ți corecta ultima mutare urmărind o scurtă reclamă."': 'Localization.strings.extraUndoDescAd',
    '"Folosește o șansă pentru a-ți corecta ultima mutare gratuit."': 'Localization.strings.extraUndoDescFree',
    '"NIVEL COMPLETAT!"': 'Localization.strings.levelCompleteTitle',
    '"🎁 Recompensă deblocată:"': 'Localization.strings.rewardUnlocked',
    '"⚠️ Resetare Progres"': 'Localization.strings.resetProgressTitle',
    '"Ești absolut sigur că vrei să ștergi TOT progresul, nivelurile și realizările? Această acțiune nu poate fi anulată!"': 'Localization.strings.resetProgressDesc',
    '"DA, RESETEAZĂ TOT"': 'Localization.strings.resetProgressConfirm'
})

# 9. GameScreen
replace_in_file("app/src/main/java/com/example/ui/screens/GameScreen.kt", {
    'if (isAdventure) "🗺️ NIVEL ${gameState.currentLevel}" else "♾️ MOD INFINIT"': 'if (isAdventure) "🗺️ " + Localization.strings.getLevelName(gameState.currentLevel) else Localization.strings.infiniteModeTop',
    '"SCOR"': 'Localization.strings.score',
    '"RECORD"': 'Localization.strings.recordUpper',
    '"RESTART"': 'Localization.strings.restartBtn',
    '"UNDO"': 'Localization.strings.undoBtn',
    '"🎯 OBIECTIV NIVEL ${gameState.currentLevel}: Descoperă ${targetItem.emoji} ${targetItem.name} (${targetItem.value})"': 'Localization.strings.objectiveAdventure(gameState.currentLevel, targetItem.emoji, targetItem.name, targetItem.value)',
    '"🎯 OBIECTIV: Ajunge la piesa 2048!"': 'Localization.strings.objectiveInfinite',
    '"„Combină două piese identice pentru a descoperi ${targetItem.name}!”"': 'Localization.strings.objectiveAdventureDesc(targetItem.name)',
    '"„Combină două piese identice pentru a construi România, piesă cu piesă!”"': 'Localization.strings.objectiveInfiniteDesc',
    '"JOC ÎN PAUZĂ"': 'Localization.strings.gamePaused',
    '"CONTINUĂ JOCUL"': 'Localization.strings.resumeGameBtn'
})

print("Patched UI strings!")
