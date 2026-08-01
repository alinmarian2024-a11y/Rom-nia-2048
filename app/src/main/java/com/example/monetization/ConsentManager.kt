package com.example.monetization

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ConsentManager(context: Context) {

    companion object {
        private const val TAG = "ConsentManager"
    }

    private val consentInformation: ConsentInformation =
        UserMessagingPlatform.getConsentInformation(context)

    private val _isPrivacyOptionsRequired = MutableStateFlow(false)
    val isPrivacyOptionsRequired: StateFlow<Boolean> =
        _isPrivacyOptionsRequired.asStateFlow()

    /**
     * Requests fresh consent information on every app launch, then shows a consent form if
     * Google UMP determines one is required. Ads are initialized only when canRequestAds is true.
     */
    fun requestConsent(activity: Activity, onAdsAllowed: () -> Unit) {
        val params = ConsentRequestParameters.Builder().build()

        consentInformation.requestConsentInfoUpdate(
            activity,
            params,
            {
                updatePrivacyOptionsState()

                // A previous-session decision may already allow requests.
                if (consentInformation.canRequestAds()) {
                    onAdsAllowed()
                }

                UserMessagingPlatform.loadAndShowConsentFormIfRequired(activity) { formError ->
                    if (formError != null) {
                        Log.w(TAG, "Consent form error: ${formError.message}")
                    }
                    updatePrivacyOptionsState()
                    if (consentInformation.canRequestAds()) {
                        onAdsAllowed()
                    }
                }
            },
            { requestError ->
                Log.w(TAG, "Consent info update failed: ${requestError.message}")
                updatePrivacyOptionsState()

                // UMP may still have a valid decision from a previous session.
                if (consentInformation.canRequestAds()) {
                    onAdsAllowed()
                }
            }
        )
    }

    fun showPrivacyOptions(activity: Activity, onAdsAllowed: () -> Unit) {
        UserMessagingPlatform.showPrivacyOptionsForm(activity) { formError ->
            if (formError != null) {
                Log.w(TAG, "Privacy options form error: ${formError.message}")
            }
            updatePrivacyOptionsState()
            if (consentInformation.canRequestAds()) {
                onAdsAllowed()
            }
        }
    }

    private fun updatePrivacyOptionsState() {
        _isPrivacyOptionsRequired.value =
            consentInformation.privacyOptionsRequirementStatus ==
                ConsentInformation.PrivacyOptionsRequirementStatus.REQUIRED
    }
}
