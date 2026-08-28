import re

content = """package com.example.ui.strings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

enum class Language(val code: String) { RO("RO"), EN("EN") }

interface AppStrings {
    val languageName: String
    val languageSelector: String

    // Home Screen
    val appTitle: String
    val appSubtitle: String
    val playNow: String
    val record: String
    val currentLevelTitle: String
    val collectionTitle: String
    
    val btnLevels: String
    val btnAchievements: String
    val btnCollection: String
    val btnSettings: String
    val btnAbout: String
    
    val removeAdsTitle: String
    val adsRemovedMsg: String
    val removeAdsDesc: String
    val removeAdsBtn: String
    val removeAdsDisclaimer: String
    val restorePurchaseBtn: String

    // Mode Selection
    val modeSelectionTitle: String
    val modeSelectionDesc: String
    val modeAdventureTitle: String
    val modeAdventureDesc: String
    val modeAdventureLongDesc: String
    val unlockedLevelsPrefix: String
    val viewLevelsBtn: String
    val modeInfiniteTitle: String
    val modeInfiniteDesc: String
    val modeInfiniteLongDesc: String

    // Levels Screen
    val levelsTitle: String
    val levelsSubtitle: String
    val levelObjectivePrefix: String
    val levelRewardPrefix: String
    val levelBtnPlay: String
    val levelBtnReplay: String

    // Collection Screen
    val collectionHeaderTitle: String
    val collectionSubtitle: String
    val collectionLockedDesc: String
    val statusUnlocked: String
    val statusLocked: String

    // Achievements Screen
    val achievementsTitle: String
    val achievementsSubtitle: String

    // About Screen
    val aboutTitle: String
    val aboutDescription: String
    val version: String
    val copyright: String

    // Settings Screen
    val settingsTitle: String
    val audioSection: String
    val visualSection: String
    val gamePrefsSection: String
    val resetSection: String
    val musicVol: String
    val sfxVol: String
    val vibration: String
    val themeLight: String
    val themeDark: String
    val themeSystem: String
    val romanianTheme: String
    val animations: String
    val confirmRestartPref: String

    // Game Screen
    val infiniteModeTop: String
    val score: String
    val recordUpper: String
    val undoBtn: String
    val restartBtn: String
    val gamePaused: String
    val resumeGameBtn: String
    
    // Dialogs
    val btnCancel: String
    
    val restartDialogTitle: String
    val restartDialogDesc: String
    val restartDialogConfirm: String

    val pauseDialogTitle: String

    val victoryTitle: String
    val victoryDesc: String

    val gameOverTitle: String
    val gameOverDesc: String
    val gameOverNewRecord: String
    val gameOverScore: String
    val continueGameBtn: String
    val continueAdBtn: String

    val extraUndoTitle: String
    val extraUndoBtnFree: String
    val extraUndoBtnAd: String
    val extraUndoDescAd: String
    val extraUndoDescFree: String

    val levelCompleteTitle: String
    val rewardUnlocked: String

    val resetProgressTitle: String
    val resetProgressDesc: String
    val resetProgressConfirm: String
    
    val btnHome: String
    val btnContinuePlaying: String
    val btnPlayAgain: String
    
    val achievementUnlocked: String
    
    fun getLevelName(number: Int): String
    fun getLevelTitle(number: Int, title: String): String
    
    val gameLevel1Name: String
    val gameLevel2Name: String
    val gameLevel3Name: String
    val gameLevel4Name: String
    val gameLevel5Name: String
    val gameLevel6Name: String
    val gameLevel7Name: String
    val gameLevel8Name: String
    val gameLevel9Name: String
    val gameLevel10Name: String
    val gameLevel11Name: String
    val gameLevel12Name: String

    val gameLevel1Reward: String
    val gameLevel2Reward: String
    val gameLevel3Reward: String
    val gameLevel4Reward: String
    val gameLevel5Reward: String
    val gameLevel6Reward: String
    val gameLevel7Reward: String
    val gameLevel8Reward: String
    val gameLevel9Reward: String
    val gameLevel10Reward: String
    val gameLevel11Reward: String
    val gameLevel12Reward: String
    
    // Registry names and descs
    val reg2name: String
    val reg2desc: String
    val reg4name: String
    val reg4desc: String
    val reg8name: String
    val reg8desc: String
    val reg16name: String
    val reg16desc: String
    val reg32name: String
    val reg32desc: String
    val reg64name: String
    val reg64desc: String
    val reg128name: String
    val reg128desc: String
    val reg256name: String
    val reg256desc: String
    val reg512name: String
    val reg512desc: String
    val reg1024name: String
    val reg1024desc: String
    val reg2048name: String
    val reg2048desc: String
    val reg4096name: String
    val reg4096desc: String
    val reg8192name: String
    val reg8192desc: String
    val reg16384name: String
    val reg16384desc: String
    
    val reg2badge: String
    val reg4badge: String
    val reg8badge: String
    val reg16badge: String
    val reg32badge: String
    val reg64badge: String
    val reg128badge: String
    val reg256badge: String
    val reg512badge: String
    val reg1024badge: String
    val reg2048badge: String
    val reg4096badge: String
    val reg8192badge: String
    val reg16384badge: String
    
    val fallbackName: String
    val fallbackDesc: String
    val fallbackBadge: String

    // Objectives
    fun objectiveAdventure(level: Int, targetEmoji: String, targetName: String, targetTile: Int): String
    fun objectiveAdventureDesc(targetName: String): String
    val objectiveInfinite: String
    val objectiveInfiniteDesc: String
}

object StringsRo : AppStrings {
    override val languageName = "RO"
    override val languageSelector = "Limba: RO"

    override val appTitle = "ROMÂNIA 2048"
    override val appSubtitle = "Construiește România, piesă cu piesă!"
    override val playNow = "JOACĂ ACUM"
    override val record = "RECORD"
    override val currentLevelTitle = "NIVEL ACTUAL"
    override val levelPrefix = "Nivel "
    override val collectionTitle = "COLECȚIE"
    
    override val btnLevels = "NIVELURI"
    override val btnAchievements = "REALIZĂRI"
    override val btnCollection = "COLECȚIE"
    override val btnSettings = "SETĂRI"
    override val btnAbout = "DESPRE JOC"
    
    override val removeAdsTitle = "Joacă fără reclame"
    override val adsRemovedMsg = "Reclamele interstițiale sunt dezactivate"
    override val removeAdsDesc = "Elimină reclamele interstițiale și bucură-te de o experiență de joc mai plăcută."
    override val removeAdsBtn = "ELIMINĂ RECLAMELE — 6,99 lei"
    override val removeAdsDisclaimer = "Achiziție unică. Reclamele recompensate pentru Undo și Continuă după Game Over rămân disponibile."
    override val restorePurchaseBtn = "RESTAUREAZĂ ACHIZIȚIA"

    override val modeSelectionTitle = "🎮 ALEGE MODUL DE JOC"
    override val modeSelectionDesc = "Selectează experiența de joc pe care o dorești:"
    override val modeAdventureTitle = "MOD AVENTURĂ"
    override val modeAdventureDesc = "Progres pe niveluri"
    override val modeAdventureLongDesc = "Descoperă preparatele românești, completează colecția și deblochează noi niveluri pas cu pas."
    override val unlockedLevelsPrefix = "Niveluri deblocate: "
    override val viewLevelsBtn = "VEZI NIVELURILE"
    override val modeInfiniteTitle = "MOD INFINIT"
    override val modeInfiniteDesc = "Clasic fără sfârșit"
    override val modeInfiniteLongDesc = "Joacă fără limită, combină cât mai multe piese și încearcă să-ți depășești recordul personal."

    override val levelsTitle = "🗺 NIVELURI"
    override val levelsSubtitle = "Progresează prin nivelurile României combinând piese și atingând obiectivele stabilite!"
    override val levelObjectivePrefix = "Obiectiv: Descoperă Piesa "
    override val levelRewardPrefix = "Recompensă: "
    override val levelBtnPlay = "JOACĂ"
    override val levelBtnReplay = "REJOACĂ"

    override val collectionHeaderTitle = "🎨 COLECȚIE"
    override val collectionSubtitle = "Toate piesele românești pe care le-ai creat în joc "
    override val collectionLockedDesc = "Atinge piesa %d în joc pentru a debloca acest obiect tradițional!"
    override val statusUnlocked = "DEBLOCAT"
    override val statusLocked = "BLOCAT"

    override val achievementsTitle = "🏆 REALIZĂRI"
    override val achievementsSubtitle = "Deblochează toate cele %d de realizări "

    override val aboutTitle = "ℹ DESPRE JOC"
    override val aboutDescription = "„ROMÂNIA 2048 este un joc casual inspirat de România, de lucrurile mici și mari care ne definesc și de plăcerea de a construi ceva pas cu pas.”"
    override val version = "Versiunea 1.0.0"
    override val copyright = "© România 2048"

    override val settingsTitle = "⚙ SETĂRI"
    override val audioSection = "AUDIO & VIBRAȚII"
    override val visualSection = "🌙 TEMĂ VIZUALĂ"
    override val gamePrefsSection = "⚡ PREFERINȚE JOC"
    override val resetSection = "🗑️ RESETARE PROGRES"
    override val musicVol = "🎵 VOLUM MUZICĂ: "
    override val sfxVol = "🔊 VOLUM EFECTE SONORE: "
    override val vibration = "🔔 VIBRAȚII"
    override val themeLight = "LIGHT"
    override val themeDark = "DARK"
    override val themeSystem = "SYSTEM"
    override val romanianTheme = "🎨 TEMĂ ROMÂNEASCĂ"
    override val animations = "⚡ ANIMAȚII"
    override val confirmRestartPref = "CONFIRMĂ RESTART"

    override val infiniteModeTop = "♾️ MOD INFINIT"
    override val score = "SCOR"
    override val recordUpper = "RECORD"
    override val undoBtn = "UNDO"
    override val restartBtn = "RESTART"
    override val gamePaused = "JOC ÎN PAUZĂ"
    override val resumeGameBtn = "CONTINUĂ JOCUL"
    
    override val btnCancel = "ANULEAZĂ"
    
    override val restartDialogTitle = "🔄 Joc Nou"
    override val restartDialogDesc = "Ești sigur că vrei să începi un joc nou? Progresul jocului curent va fi resetat."
    override val restartDialogConfirm = "DA, RESTART"

    override val pauseDialogTitle = "⏸ JOC PUS PE PAUZĂ"

    override val victoryTitle = "FELICITĂRI!"
    override val victoryDesc = "„Ai construit România, piesă cu piesă!”"

    override val gameOverTitle = "GAME OVER"
    override val gameOverDesc = "Nu mai ai mutări disponibile."
    override val gameOverNewRecord = "🏆 RECORD NOU! "
    override val gameOverScore = "Scor final: %d\nRecord: %d"
    override val continueGameBtn = "▶ CONTINUĂ JOCUL"
    override val continueAdBtn = "🎬 CONTINUĂ (RECLAMĂ)"

    override val extraUndoTitle = "Undo Suplimentar"
    override val extraUndoBtnFree = "▶ PRIMEȘTE UNDO"
    override val extraUndoBtnAd = "🎬 VEZI RECLAMĂ (+1 UNDO)"
    override val extraUndoDescAd = "Folosește o șansă pentru a-ți corecta ultima mutare urmărind o scurtă reclamă."
    override val extraUndoDescFree = "Folosește o șansă pentru a-ți corecta ultima mutare gratuit."

    override val levelCompleteTitle = "NIVEL COMPLETAT!"
    override val rewardUnlocked = "🎁 Recompensă deblocată:"

    override val resetProgressTitle = "⚠️ Resetare Progres"
    override val resetProgressDesc = "Ești absolut sigur că vrei să ștergi TOT progresul, nivelurile și realizările? Această acțiune nu poate fi anulată!"
    override val resetProgressConfirm = "DA, RESETEAZĂ TOT"
    
    override val btnHome = "🏠 MENIU PRINCIPAL"
    override val btnContinuePlaying = "▶ CONTINUĂ"
    override val btnPlayAgain = "🔄 JOACĂ DIN NOU"
    
    override val achievementUnlocked = "🏆 REALIZARE DEBLOCATĂ!"
    
    override fun getLevelName(number: Int): String = "Nivelul $number"
    override fun getLevelTitle(number: Int, title: String): String = title
    
    override val gameLevel1Name = "Nivelul 1: Mămăliga Aurie"
    override val gameLevel2Name = "Nivelul 2: Brânza de Burduf"
    override val gameLevel3Name = "Nivelul 3: Copane la Grătar"
    override val gameLevel4Name = "Nivelul 4: Sarmale Tradiționale"
    override val gameLevel5Name = "Nivelul 5: Ciorba Rădăuțeană"
    override val gameLevel6Name = "Nivelul 6: Cafea la Ibric"
    override val gameLevel7Name = "Nivelul 7: Castelul Peleș"
    override val gameLevel8Name = "Nivelul 8: Palatul Parlamentului"
    override val gameLevel9Name = "Nivelul 9: Marea Unire 2048"
    override val gameLevel10Name = "Nivelul 10: Poezia Luceafărului"
    override val gameLevel11Name = "Nivelul 11: Vulturul Carpaților"
    override val gameLevel12Name = "Nivelul 12: Coroana Regală"

    override val gameLevel1Reward = "Insignă Mămăligă"
    override val gameLevel2Reward = "Insignă Brânză"
    override val gameLevel3Reward = "Insignă Copane"
    override val gameLevel4Reward = "Insignă Sarmale"
    override val gameLevel5Reward = "Insignă Ciorbă"
    override val gameLevel6Reward = "Insignă Cafea"
    override val gameLevel7Reward = "Insignă Castel"
    override val gameLevel8Reward = "Insignă Palat"
    override val gameLevel9Reward = "Insignă Patriot"
    override val gameLevel10Reward = "Insignă Geniu"
    override val gameLevel11Reward = "Insignă Legendă"
    override val gameLevel12Reward = "Insignă Supremă"

    override val reg2name = "Covrig"
    override val reg2desc = "Covrigul – combustibilul oficial al românului grăbit."
    override val reg4name = "Plăcintă"
    override val reg4desc = "Plăcinta caldă cu brânză, desertul nostalgic al copilăriei."
    override val reg8name = "Mămăligă"
    override val reg8desc = "Mămăliga aurie, companionul de nelipsit de pe masa tradițională."
    override val reg16name = "Brânză"
    override val reg16desc = "Brânză de burduf sau telemea, savoare pur carpatică."
    override val reg32name = "Copane"
    override val reg32desc = "Copane perpelite la grătar, bucuria duminicilor în familie."
    override val reg64name = "Sarmale"
    override val reg64desc = "Sarmalele în foi de varză, regina incontestabilă a sărbătorilor."
    override val reg128name = "Ciorbă"
    override val reg128desc = "Ciorba rădăuțeană sau de burtă, leacul suprem românesc."
    override val reg256name = "Cafea"
    override val reg256desc = "Cafeaua la ibric, secretul conversațiilor lungi cu prietenii."
    override val reg512name = "Castel"
    override val reg512desc = "Castelul Peleș sau Bran, mândria arhitecturală a Carpaților."
    override val reg1024name = "Palat"
    override val reg1024desc = "Palatul Parlamentului, monument gigantic al istoriei moderne."
    override val reg2048name = "România"
    override val reg2048desc = "România – țara tuturor posibilităților și a spiritului unit!"
    override val reg4096name = "Luceafărul"
    override val reg4096desc = "Luceafărul – geniul eminescian și aspirația spre infinit."
    override val reg8192name = "Vulturul"
    override val reg8192desc = "Vulturul Carpaților – simbolul libertății și al măreției."
    override val reg16384name = "Coroana"
    override val reg16384desc = "Coroana Regală – simbol al nobleței și rezistenței în timp."

    override val reg2badge = "Gustare"
    override val reg4badge = "Desert"
    override val reg8badge = "Tradiție"
    override val reg16badge = "Deliciu"
    override val reg32badge = "Mâncare"
    override val reg64badge = "Sărbătoare"
    override val reg128badge = "Ospăț"
    override val reg256badge = "Energie"
    override val reg512badge = "Istorie"
    override val reg1024badge = "Monument"
    override val reg2048badge = "Glorie"
    override val reg4096badge = "Mister"
    override val reg8192badge = "Legendă"
    override val reg16384badge = "Măreție"

    override val fallbackName = "Piesă "
    override val fallbackDesc = "O piesă de valoare impresionantă în construcția României!"
    override val fallbackBadge = "Suprem"

    override fun objectiveAdventure(level: Int, targetEmoji: String, targetName: String, targetTile: Int) = 
        "🎯 OBIECTIV NIVEL $level: Descoperă $targetEmoji $targetName ($targetTile)"
    override fun objectiveAdventureDesc(targetName: String) = 
        "„Combină două piese identice pentru a descoperi $targetName!”"
    override val objectiveInfinite = "🎯 OBIECTIV: Ajunge la piesa 2048!"
    override val objectiveInfiniteDesc = "„Combină două piese identice pentru a construi România, piesă cu piesă!”"
}

object StringsEn : AppStrings {
    override val languageName = "EN"
    override val languageSelector = "Language: EN"

    override val appTitle = "ROMANIA 2048"
    override val appSubtitle = "Build Romania, tile by tile!"
    override val playNow = "PLAY NOW"
    override val record = "RECORD"
    override val currentLevelTitle = "CURRENT LEVEL"
    override val levelPrefix = "Level "
    override val collectionTitle = "COLLECTION"
    
    override val btnLevels = "LEVELS"
    override val btnAchievements = "ACHIEVEMENTS"
    override val btnCollection = "COLLECTION"
    override val btnSettings = "SETTINGS"
    override val btnAbout = "ABOUT"
    
    override val removeAdsTitle = "Play Ad-Free"
    override val adsRemovedMsg = "Interstitial ads are disabled"
    override val removeAdsDesc = "Remove interstitial ads and enjoy a smoother gaming experience."
    override val removeAdsBtn = "REMOVE ADS — $1.49"
    override val removeAdsDisclaimer = "One-time purchase. Rewarded ads for Undo and Continue remain available."
    override val restorePurchaseBtn = "RESTORE PURCHASE"

    override val modeSelectionTitle = "🎮 CHOOSE GAME MODE"
    override val modeSelectionDesc = "Select the gaming experience you want:"
    override val modeAdventureTitle = "ADVENTURE MODE"
    override val modeAdventureDesc = "Level-based progression"
    override val modeAdventureLongDesc = "Discover Romanian foods, complete your collection, and unlock new levels step by step."
    override val unlockedLevelsPrefix = "Levels unlocked: "
    override val viewLevelsBtn = "VIEW LEVELS"
    override val modeInfiniteTitle = "INFINITE MODE"
    override val modeInfiniteDesc = "Endless classic"
    override val modeInfiniteLongDesc = "Play without limits, combine as many tiles as you can and try to beat your personal record."

    override val levelsTitle = "🗺 LEVELS"
    override val levelsSubtitle = "Progress through Romania's levels by combining tiles and reaching goals!"
    override val levelObjectivePrefix = "Goal: Discover Tile "
    override val levelRewardPrefix = "Reward: "
    override val levelBtnPlay = "PLAY"
    override val levelBtnReplay = "REPLAY"

    override val collectionHeaderTitle = "🎨 COLLECTION"
    override val collectionSubtitle = "All the Romanian tiles you created in the game "
    override val collectionLockedDesc = "Reach tile %d in the game to unlock this traditional item!"
    override val statusUnlocked = "UNLOCKED"
    override val statusLocked = "LOCKED"

    override val achievementsTitle = "🏆 ACHIEVEMENTS"
    override val achievementsSubtitle = "Unlock all %d achievements "

    override val aboutTitle = "ℹ ABOUT THE GAME"
    override val aboutDescription = "\"ROMANIA 2048 is a casual game inspired by Romania, by the small and big things that define us, and by the joy of building something step by step.\""
    override val version = "Version 1.0.0"
    override val copyright = "© Romania 2048"

    override val settingsTitle = "⚙ SETTINGS"
    override val audioSection = "AUDIO & VIBRATION"
    override val visualSection = "🌙 VISUAL THEME"
    override val gamePrefsSection = "⚡ GAME PREFERENCES"
    override val resetSection = "🗑️ RESET PROGRESS"
    override val musicVol = "🎵 MUSIC VOLUME: "
    override val sfxVol = "🔊 SFX VOLUME: "
    override val vibration = "🔔 VIBRATIONS"
    override val themeLight = "LIGHT"
    override val themeDark = "DARK"
    override val themeSystem = "SYSTEM"
    override val romanianTheme = "🎨 ROMANIAN THEME"
    override val animations = "⚡ ANIMATIONS"
    override val confirmRestartPref = "CONFIRM RESTART"

    override val infiniteModeTop = "♾️ INFINITE MODE"
    override val score = "SCORE"
    override val recordUpper = "RECORD"
    override val undoBtn = "UNDO"
    override val restartBtn = "RESTART"
    override val gamePaused = "GAME PAUSED"
    override val resumeGameBtn = "RESUME GAME"
    
    override val btnCancel = "CANCEL"
    
    override val restartDialogTitle = "🔄 New Game"
    override val restartDialogDesc = "Are you sure you want to start a new game? Current progress will be reset."
    override val restartDialogConfirm = "YES, RESTART"

    override val pauseDialogTitle = "⏸ GAME PAUSED"

    override val victoryTitle = "CONGRATULATIONS!"
    override val victoryDesc = "\"You built Romania, tile by tile!\""

    override val gameOverTitle = "GAME OVER"
    override val gameOverDesc = "No more moves available."
    override val gameOverNewRecord = "🏆 NEW RECORD! "
    override val gameOverScore = "Final Score: %d\nRecord: %d"
    override val continueGameBtn = "▶ CONTINUE GAME"
    override val continueAdBtn = "🎬 CONTINUE (AD)"

    override val extraUndoTitle = "Extra Undo"
    override val extraUndoBtnFree = "▶ GET UNDO"
    override val extraUndoBtnAd = "🎬 WATCH AD (+1 UNDO)"
    override val extraUndoDescAd = "Use a chance to correct your last move by watching a short ad."
    override val extraUndoDescFree = "Use a chance to correct your last move for free."

    override val levelCompleteTitle = "LEVEL COMPLETED!"
    override val rewardUnlocked = "🎁 Reward unlocked:"

    override val resetProgressTitle = "⚠️ Reset Progress"
    override val resetProgressDesc = "Are you absolutely sure you want to delete ALL progress, levels, and achievements? This action cannot be undone!"
    override val resetProgressConfirm = "YES, RESET EVERYTHING"
    
    override val btnHome = "🏠 MAIN MENU"
    override val btnContinuePlaying = "▶ CONTINUE"
    override val btnPlayAgain = "🔄 PLAY AGAIN"
    
    override val achievementUnlocked = "🏆 ACHIEVEMENT UNLOCKED!"
    
    override fun getLevelName(number: Int): String = "Level $number"
    override fun getLevelTitle(number: Int, title: String): String {
        return title.replace("Nivelul", "Level").replace("Mămăliga Aurie", "Golden Polenta").replace("Brânza de Burduf", "Burduf Cheese").replace("Copane la Grătar", "Grilled Chicken Legs").replace("Sarmale Tradiționale", "Traditional Sarmale").replace("Ciorba Rădăuțeană", "Rădăuți Soup").replace("Cafea la Ibric", "Turkish Coffee").replace("Castelul Peleș", "Peleș Castle").replace("Palatul Parlamentului", "Palace of Parliament").replace("Marea Unire 2048", "Great Union 2048").replace("Poezia Luceafărului", "Poem of Luceafărul").replace("Vulturul Carpaților", "Carpathian Eagle").replace("Coroana Regală", "Royal Crown")
    }
    
    override val gameLevel1Name = "Level 1: Golden Polenta"
    override val gameLevel2Name = "Level 2: Burduf Cheese"
    override val gameLevel3Name = "Level 3: Grilled Chicken Legs"
    override val gameLevel4Name = "Level 4: Traditional Sarmale"
    override val gameLevel5Name = "Level 5: Rădăuți Soup"
    override val gameLevel6Name = "Level 6: Turkish Coffee"
    override val gameLevel7Name = "Level 7: Peleș Castle"
    override val gameLevel8Name = "Level 8: Palace of Parliament"
    override val gameLevel9Name = "Level 9: Great Union 2048"
    override val gameLevel10Name = "Level 10: Poem of Luceafărul"
    override val gameLevel11Name = "Level 11: Carpathian Eagle"
    override val gameLevel12Name = "Level 12: Royal Crown"

    override val gameLevel1Reward = "Polenta Badge"
    override val gameLevel2Reward = "Cheese Badge"
    override val gameLevel3Reward = "Chicken Legs Badge"
    override val gameLevel4Reward = "Sarmale Badge"
    override val gameLevel5Reward = "Soup Badge"
    override val gameLevel6Reward = "Coffee Badge"
    override val gameLevel7Reward = "Castle Badge"
    override val gameLevel8Reward = "Palace Badge"
    override val gameLevel9Reward = "Patriot Badge"
    override val gameLevel10Reward = "Genius Badge"
    override val gameLevel11Reward = "Legend Badge"
    override val gameLevel12Reward = "Supreme Badge"

    override val reg2name = "Covrig"
    override val reg2desc = "Covrig (Pretzel) – the official fuel of the Romanian in a hurry."
    override val reg4name = "Plăcintă"
    override val reg4desc = "Warm cheese pie, the nostalgic dessert of childhood."
    override val reg8name = "Mămăligă"
    override val reg8desc = "Golden polenta, the indispensable companion of the traditional table."
    override val reg16name = "Brânză"
    override val reg16desc = "Burduf cheese or telemea, pure Carpathian flavor."
    override val reg32name = "Copane"
    override val reg32desc = "Grilled chicken legs, the joy of family Sundays."
    override val reg64name = "Sarmale"
    override val reg64desc = "Stuffed cabbage rolls, the undisputed queen of holidays."
    override val reg128name = "Ciorbă"
    override val reg128desc = "Rădăuți or tripe soup, the ultimate Romanian cure."
    override val reg256name = "Cafea"
    override val reg256desc = "Coffee made in ibric, the secret of long conversations with friends."
    override val reg512name = "Castel"
    override val reg512desc = "Peleș or Bran Castle, the architectural pride of the Carpathians."
    override val reg1024name = "Palat"
    override val reg1024desc = "Palace of Parliament, gigantic monument of modern history."
    override val reg2048name = "România"
    override val reg2048desc = "Romania – the land of all possibilities and united spirit!"
    override val reg4096name = "Luceafărul"
    override val reg4096desc = "Luceafărul – the genius of Eminescu and aspiration for the infinite."
    override val reg8192name = "Vulturul"
    override val reg8192desc = "Carpathian Eagle – symbol of freedom and greatness."
    override val reg16384name = "Coroana"
    override val reg16384desc = "Royal Crown – symbol of nobility and endurance over time."

    override val reg2badge = "Snack"
    override val reg4badge = "Dessert"
    override val reg8badge = "Tradition"
    override val reg16badge = "Delight"
    override val reg32badge = "Food"
    override val reg64badge = "Holiday"
    override val reg128badge = "Feast"
    override val reg256badge = "Energy"
    override val reg512badge = "History"
    override val reg1024badge = "Monument"
    override val reg2048badge = "Glory"
    override val reg4096badge = "Mystery"
    override val reg8192badge = "Legend"
    override val reg16384badge = "Greatness"

    override val fallbackName = "Tile "
    override val fallbackDesc = "A tile of impressive value in building Romania!"
    override val fallbackBadge = "Supreme"

    override fun objectiveAdventure(level: Int, targetEmoji: String, targetName: String, targetTile: Int) = 
        "🎯 LEVEL $level GOAL: Discover $targetEmoji $targetName ($targetTile)"
    override fun objectiveAdventureDesc(targetName: String) = 
        "\"Combine two identical tiles to discover $targetName!\""
    override val objectiveInfinite = "🎯 GOAL: Reach the 2048 tile!"
    override val objectiveInfiniteDesc = "\"Combine two identical tiles to build Romania, tile by tile!\""
}

object Localization {
    var language by mutableStateOf(Language.RO)
    val strings: AppStrings get() = if (language == Language.RO) StringsRo else StringsEn
}
