package com.example.monetization

import android.app.Activity
import android.content.Context
import android.util.Log
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class BillingManager(
    private val context: Context,
    initialAdsRemoved: Boolean,
    private val onAdsRemovedStateChanged: (Boolean) -> Unit
) : PurchasesUpdatedListener {

    companion object {
        private const val TAG = "BillingManager"
        const val PRODUCT_REMOVE_ADS = "remove_ads"
    }

    private val _isAdsRemoved = MutableStateFlow(initialAdsRemoved)
    val isAdsRemoved: StateFlow<Boolean> = _isAdsRemoved.asStateFlow()

    private val _formattedPrice = MutableStateFlow<String?>("6,99 lei")
    val formattedPrice: StateFlow<String?> = _formattedPrice.asStateFlow()

    private val _billingStatusMessage = MutableStateFlow<String?>(null)
    val billingStatusMessage: StateFlow<String?> = _billingStatusMessage.asStateFlow()

    private var productDetails: ProductDetails? = null

    private val billingClient: BillingClient = BillingClient.newBuilder(context)
        .setListener(this)
        .enablePendingPurchases()
        .build()



    init {
        startConnection()
    }

    fun startConnection() {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    Log.d(TAG, "Billing setup finished successfully.")
                    queryProductDetails()
                    queryPurchases()
                } else {
                    Log.w(TAG, "Billing setup failed: ${billingResult.debugMessage}")
                }
            }

            override fun onBillingServiceDisconnected() {
                Log.w(TAG, "Billing service disconnected.")
            }
        })
    }

    private fun queryProductDetails() {
        val productList = listOf(
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId(PRODUCT_REMOVE_ADS)
                .setProductType(BillingClient.ProductType.INAPP)
                .build()
        )

        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(productList)
            .build()

        billingClient.queryProductDetailsAsync(params) { billingResult, productDetailsList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && productDetailsList.isNotEmpty()) {
                val details = productDetailsList[0]
                productDetails = details
                val price = details.oneTimePurchaseOfferDetails?.formattedPrice
                if (price != null) {
                    _formattedPrice.value = price
                }
                Log.d(TAG, "Product details fetched. Price: $price")
            } else {
                Log.w(TAG, "queryProductDetailsAsync failed: ${billingResult.debugMessage}")
            }
        }
    }

    fun queryPurchases() {
        if (!billingClient.isReady) return

        val queryPurchasesParams = QueryPurchasesParams.newBuilder()
            .setProductType(BillingClient.ProductType.INAPP)
            .build()

        billingClient.queryPurchasesAsync(queryPurchasesParams) { billingResult, purchases ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                var foundAdsRemoved = false
                for (purchase in purchases) {
                    if (purchase.products.contains(PRODUCT_REMOVE_ADS) &&
                        purchase.purchaseState == Purchase.PurchaseState.PURCHASED
                    ) {
                        foundAdsRemoved = true
                        handlePurchase(purchase)
                    }
                }
                if (foundAdsRemoved) {
                    _isAdsRemoved.value = true
                    onAdsRemovedStateChanged(true)
                }
            } else {
                Log.w(TAG, "queryPurchasesAsync failed: ${billingResult.debugMessage}")
            }
        }
    }

    fun launchBillingFlow(activity: Activity): Boolean {
        if (!billingClient.isReady) {
            _billingStatusMessage.value = "Google Play Billing nu este conectat încă."
            startConnection()
            return false
        }

        val details = productDetails
        if (details == null) {
            _billingStatusMessage.value = "Produsul 'remove_ads' nu a fost găsit în Google Play Console."
            return false
        }

        val productDetailsParamsList = listOf(
            BillingFlowParams.ProductDetailsParams.newBuilder()
                .setProductDetails(details)
                .build()
        )

        val billingFlowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(productDetailsParamsList)
            .build()

        val result = billingClient.launchBillingFlow(activity, billingFlowParams)
        return result.responseCode == BillingClient.BillingResponseCode.OK
    }

    override fun onPurchasesUpdated(billingResult: BillingResult, purchases: MutableList<Purchase>?) {
        when (billingResult.responseCode) {
            BillingClient.BillingResponseCode.OK -> {
                if (purchases != null) {
                    for (purchase in purchases) {
                        handlePurchase(purchase)
                    }
                }
            }
            BillingClient.BillingResponseCode.USER_CANCELED -> {
                Log.d(TAG, "User canceled the purchase.")
            }
            BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED -> {
                _isAdsRemoved.value = true
                onAdsRemovedStateChanged(true)
                _billingStatusMessage.value = "Reclamele sunt deja eliminate!"
            }
            else -> {
                Log.e(TAG, "Purchase error: ${billingResult.debugMessage}")
                _billingStatusMessage.value = "Eroare achiziție: ${billingResult.debugMessage}"
            }
        }
    }

    private fun handlePurchase(purchase: Purchase) {
        if (purchase.products.contains(PRODUCT_REMOVE_ADS) &&
            purchase.purchaseState == Purchase.PurchaseState.PURCHASED
        ) {
            _isAdsRemoved.value = true
            onAdsRemovedStateChanged(true)
            _billingStatusMessage.value = "Mulțumim! Reclamele au fost eliminate permanent."

            if (!purchase.isAcknowledged) {
                val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                    .setPurchaseToken(purchase.purchaseToken)
                    .build()
                billingClient.acknowledgePurchase(acknowledgePurchaseParams) { result ->
                    if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                        Log.d(TAG, "Purchase acknowledged successfully.")
                    }
                }
            }
        }
    }

    fun clearStatusMessage() {
        _billingStatusMessage.value = null
    }
}
