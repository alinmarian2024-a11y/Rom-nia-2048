package com.example.ui.strings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

enum class Language(val code: String) { RO("RO"), EN("EN") }

interface AppStrings {
    val dpadUp: String
    val dpadDown: String
    val dpadLeft: String
    val dpadRight: String
    val appTitle: String
    val appSubtitle: String
    val playNow: String
    val record: String
    val currentLevelTitle: String
    val levelPrefix: String
    val collectionTitle: String
    val btnLevels: String
    val btnAchievements: String
    val btnSettings: String
    val btnAbout: String
    val removeAdsTitle: String
    val adsRemovedMsg: String
    val removeAdsDesc: String
    val removeAdsBtn: String
    val removeAdsDisclaimer: String
    val restorePurchaseBtn: String
    val languageSelector: String

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

    val levelsTitle: String
    val levelsSubtitle: String
    val levelObjectivePrefix: String
    val levelRewardPrefix: String
    val levelBtnPlay: String
    val levelBtnReplay: String

    val collectionHeaderTitle: String
    val collectionSubtitle: String
    val statusUnlocked: String
    val statusLocked: String
    val contentDescBack: String
    val billingNotConnected: String
    val billingProductNotFound: String
    val billingAdsAlreadyRemoved: String
    fun billingError(msg: String): String
    val billingSuccess: String
    val billingRestoredSuccess: String
    val billingNoPurchaseFound: String
    val adNotReady: String
    val pointsSuffix: String
    val contentDescPlay: String
    val contentDescPurchased: String
    val contentDescLocked: String
    val contentDescUnlocked: String
    val contentDescQuickMenu: String
    val contentDescPause: String
    val unknownTile: String
    fun undoFreeRemaining(remaining: Int, total: Int): String
    val undoAdTooltipPurchased: String
    val undoAdTooltipAd: String
    fun collectionLockedDesc(value: Int): String

    val achievementsTitle: String
    fun achievementsSubtitle(count: Int): String

    val aboutTitle: String
    val aboutDescription: String
    val version: String
    val copyright: String

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

    val achievementUnlocked: String

    val btnCancel: String
    val restartDialogTitle: String
    val restartDialogDesc: String
    val restartDialogConfirm: String
    val pauseDialogTitle: String
    val victoryTitle: String
    val victoryDesc: String
    val btnContinuePlaying: String
    val btnPlayAgain: String
    val btnHome: String
    val gameOverTitle: String
    val gameOverDesc: String
    val gameOverNewRecord: String
    fun gameOverScore(score: Int, highScore: Int): String
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

    fun getLevelName(level: Int): String
    val infiniteModeTop: String
    val score: String
    val recordUpper: String
    val restartBtn: String
    val undoBtn: String
    fun objectiveAdventure(level: Int, emoji: String, name: String, val_: Int): String
    val objectiveInfinite: String
    fun objectiveAdventureDesc(name: String): String
    val objectiveInfiniteDesc: String
    val gamePaused: String
    val resumeGameBtn: String

    val fallbackName: String
    val fallbackDesc: String
    val fallbackBadge: String
    
    val gameLevel1Name: String
    val gameLevel1Reward: String
    val gameLevel2Name: String
    val gameLevel2Reward: String
    val gameLevel3Name: String
    val gameLevel3Reward: String
    val gameLevel4Name: String
    val gameLevel4Reward: String
    val gameLevel5Name: String
    val gameLevel5Reward: String
    val gameLevel6Name: String
    val gameLevel6Reward: String
    val gameLevel7Name: String
    val gameLevel7Reward: String
    val gameLevel8Name: String
    val gameLevel8Reward: String
    val gameLevel9Name: String
    val gameLevel9Reward: String
    val gameLevel10Name: String
    val gameLevel10Reward: String
    val gameLevel11Name: String
    val gameLevel11Reward: String
    val gameLevel12Name: String
    val gameLevel12Reward: String
    
    val reg2name: String
    val reg2desc: String
    val reg2badge: String
    val reg4name: String
    val reg4desc: String
    val reg4badge: String
    val reg8name: String
    val reg8desc: String
    val reg8badge: String
    val reg16name: String
    val reg16desc: String
    val reg16badge: String
    val reg32name: String
    val reg32desc: String
    val reg32badge: String
    val reg64name: String
    val reg64desc: String
    val reg64badge: String
    val reg128name: String
    val reg128desc: String
    val reg128badge: String
    val reg256name: String
    val reg256desc: String
    val reg256badge: String
    val reg512name: String
    val reg512desc: String
    val reg512badge: String
    val reg1024name: String
    val reg1024desc: String
    val reg1024badge: String
    val reg2048name: String
    val reg2048desc: String
    val reg2048badge: String
    val reg4096name: String
    val reg4096desc: String
    val reg4096badge: String
    val reg8192name: String
    val reg8192desc: String
    val reg8192badge: String
    val reg16384name: String
    val reg16384desc: String
    val reg16384badge: String

    fun getAchievementTitle(id: String): String
    fun getAchievementDesc(id: String): String
}

object Localization {
    var language by mutableStateOf(Language.EN)
    val strings: AppStrings
        get() = if (language == Language.RO) StringsRo else StringsEn
}
