package com.example.monetization

import android.app.Activity
import android.content.Context
import android.os.SystemClock
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AdManager(private val context: Context) {

    companion object {
        private const val TAG = "AdManager"

        // Official Google test IDs. Replace both with your own AdMob ad-unit IDs before publishing.
        const val TEST_REWARDED_AD_UNIT_ID = "ca-app-pub-3940256099942544/5224354917"
        const val TEST_INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712"

        private const val MIN_INTERSTITIAL_INTERVAL_MS = 12 * 60 * 1000L
    }

    private var mobileAdsInitialized = false

    private var rewardedAd: RewardedAd? = null
    private var isRewardedLoading = false

    private var interstitialAd: InterstitialAd? = null
    private var isInterstitialLoading = false
    private var lastInterstitialShownAtMs = SystemClock.elapsedRealtime()

    private val _isAdReady = MutableStateFlow(false)
    val isAdReady: StateFlow<Boolean> = _isAdReady.asStateFlow()

    private val _isInterstitialReady = MutableStateFlow(false)
    val isInterstitialReady: StateFlow<Boolean> = _isInterstitialReady.asStateFlow()

    /**
     * Call only after UMP says ads may be requested. The method is idempotent.
     */
    fun initialize() {
        if (mobileAdsInitialized) return
        mobileAdsInitialized = true

        try {
            MobileAds.initialize(context) {
                Log.d(TAG, "Google Mobile Ads initialized.")
                loadRewardedAd()
                loadInterstitialAd()
            }
        } catch (e: Exception) {
            mobileAdsInitialized = false
            Log.e(TAG, "Error initializing Google Mobile Ads", e)
        }
    }

    fun loadRewardedAd() {
        if (!mobileAdsInitialized || isRewardedLoading || rewardedAd != null) return
        isRewardedLoading = true

        RewardedAd.load(
            context,
            TEST_REWARDED_AD_UNIT_ID,
            AdRequest.Builder().build(),
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedAd) {
                    Log.d(TAG, "Rewarded ad loaded.")
                    rewardedAd = ad
                    isRewardedLoading = false
                    _isAdReady.value = true
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    Log.w(TAG, "Rewarded ad failed to load: ${error.message}")
                    rewardedAd = null
                    isRewardedLoading = false
                    _isAdReady.value = false
                }
            }
        )
    }

    fun showRewardedAd(
        activity: Activity,
        onRewarded: () -> Unit,
        onClosedOrFailed: () -> Unit
    ) {
        val ad = rewardedAd
        if (ad == null) {
            Log.w(TAG, "Rewarded ad not ready.")
            loadRewardedAd()
            onClosedOrFailed()
            return
        }

        rewardedAd = null
        _isAdReady.value = false
        var rewardGranted = false
        var completionSent = false

        fun finishWithoutReward() {
            if (!completionSent && !rewardGranted) {
                completionSent = true
                onClosedOrFailed()
            }
        }

        ad.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                Log.d(TAG, "Rewarded ad dismissed.")
                finishWithoutReward()
                loadRewardedAd()
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                Log.w(TAG, "Rewarded ad failed to show: ${adError.message}")
                finishWithoutReward()
                loadRewardedAd()
            }
        }

        try {
            ad.show(activity) { rewardItem ->
                if (!rewardGranted) {
                    rewardGranted = true
                    completionSent = true
                    Log.d(TAG, "Reward earned: ${rewardItem.type} x${rewardItem.amount}")
                    onRewarded()
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error showing rewarded ad", e)
            finishWithoutReward()
            loadRewardedAd()
        }
    }

    fun loadInterstitialAd() {
        if (!mobileAdsInitialized || isInterstitialLoading || interstitialAd != null) return
        isInterstitialLoading = true

        InterstitialAd.load(
            context,
            TEST_INTERSTITIAL_AD_UNIT_ID,
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    Log.d(TAG, "Interstitial ad loaded.")
                    interstitialAd = ad
                    isInterstitialLoading = false
                    _isInterstitialReady.value = true
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    Log.w(TAG, "Interstitial ad failed to load: ${error.message}")
                    interstitialAd = null
                    isInterstitialLoading = false
                    _isInterstitialReady.value = false
                }
            }
        )
    }

    /**
     * Shows an interstitial only at a caller-selected safe transition and only if at least
     * 12 minutes passed since the previous interstitial opportunity that resulted in a show.
     */
    fun showInterstitialIfEligible(
        activity: Activity,
        adsRemoved: Boolean,
        onFinished: () -> Unit
    ) {
        if (adsRemoved) {
            onFinished()
            return
        }

        val elapsed = SystemClock.elapsedRealtime() - lastInterstitialShownAtMs
        if (elapsed < MIN_INTERSTITIAL_INTERVAL_MS) {
            onFinished()
            return
        }

        val ad = interstitialAd
        if (ad == null) {
            loadInterstitialAd()
            onFinished()
            return
        }

        interstitialAd = null
        _isInterstitialReady.value = false
        var finished = false

        fun finishOnce() {
            if (!finished) {
                finished = true
                onFinished()
            }
        }

        ad.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdShowedFullScreenContent() {
                lastInterstitialShownAtMs = SystemClock.elapsedRealtime()
                Log.d(TAG, "Interstitial ad shown.")
            }

            override fun onAdDismissedFullScreenContent() {
                Log.d(TAG, "Interstitial ad dismissed.")
                loadInterstitialAd()
                finishOnce()
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                Log.w(TAG, "Interstitial ad failed to show: ${adError.message}")
                loadInterstitialAd()
                finishOnce()
            }
        }

        try {
            ad.show(activity)
        } catch (e: Exception) {
            Log.e(TAG, "Error showing interstitial ad", e)
            loadInterstitialAd()
            finishOnce()
        }
    }
}
