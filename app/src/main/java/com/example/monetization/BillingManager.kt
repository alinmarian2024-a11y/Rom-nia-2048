package com.example.monetization

import android.app.Activity
import android.content.Context
import android.util.Log
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.PendingPurchasesParams
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

    private val _formattedPrice = MutableStateFlow<String?>(null)
    val formattedPrice: StateFlow<String?> = _formattedPrice.asStateFlow()

    private val _billingStatusMessage = MutableStateFlow<String?>(null)
    val billingStatusMessage: StateFlow<String?> = _billingStatusMessage.asStateFlow()

    private var productDetails: ProductDetails? = null
    private var isConnecting = false
    private var restoreRequestedWhileConnecting = false

    private val billingClient: BillingClient = BillingClient.newBuilder(context)
        .setListener(this)
        .enablePendingPurchases(
            PendingPurchasesParams.newBuilder()
                .enableOneTimeProducts()
                .build()
        )
        .enableAutoServiceReconnection()
        .build()

    init {
        startConnection()
    }

    fun startConnection() {
        if (billingClient.isReady || isConnecting) return
        isConnecting = true

        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                isConnecting = false
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    Log.d(TAG, "Billing setup finished successfully.")
                    queryProductDetails()
                    val showRestoreResult = restoreRequestedWhileConnecting
                    restoreRequestedWhileConnecting = false
                    queryPurchases(showResultMessage = showRestoreResult)
                } else {
                    Log.w(TAG, "Billing setup failed: ${billingResult.debugMessage}")
                    _billingStatusMessage.value = "Google Play Billing nu este disponibil momentan."
                }
            }

            override fun onBillingServiceDisconnected() {
                isConnecting = false
                Log.w(TAG, "Billing service disconnected; automatic reconnection is enabled.")
            }
        })
    }

    private fun queryProductDetails() {
        if (!billingClient.isReady) return

        val productList = listOf(
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId(PRODUCT_REMOVE_ADS)
                .setProductType(BillingClient.ProductType.INAPP)
                .build()
        )

        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(productList)
            .build()

        billingClient.queryProductDetailsAsync(params) { billingResult, queryResult ->
            val detailsList = queryResult.productDetailsList
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK &&
                detailsList.isNotEmpty()
            ) {
                val details = detailsList.first()
                productDetails = details
                _formattedPrice.value = details.oneTimePurchaseOfferDetails?.formattedPrice
                Log.d(TAG, "Product details fetched. Price: ${_formattedPrice.value}")
            } else {
                productDetails = null
                _formattedPrice.value = null
                Log.w(
                    TAG,
                    "queryProductDetailsAsync failed: ${billingResult.debugMessage}; " +
                        "unfetched=${queryResult.unfetchedProductList.size}"
                )
            }
        }
    }

    fun restorePurchases() {
        _billingStatusMessage.value = "Se verifică achizițiile..."
        if (!billingClient.isReady) {
            restoreRequestedWhileConnecting = true
            startConnection()
            return
        }
        queryPurchases(showResultMessage = true)
    }

    private fun queryPurchases(showResultMessage: Boolean) {
        if (!billingClient.isReady) return

        val params = QueryPurchasesParams.newBuilder()
            .setProductType(BillingClient.ProductType.INAPP)
            .build()

        billingClient.queryPurchasesAsync(params) { billingResult, purchases ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                val ownedPurchase = purchases.firstOrNull { purchase ->
                    purchase.products.contains(PRODUCT_REMOVE_ADS) &&
                        purchase.purchaseState == Purchase.PurchaseState.PURCHASED
                }

                if (ownedPurchase != null) {
                    handlePurchase(ownedPurchase)
                    if (showResultMessage) {
                        _billingStatusMessage.value = "Achiziția a fost restaurată."
                    }
                } else {
                    updateAdsRemoved(false)
                    if (showResultMessage) {
                        _billingStatusMessage.value = "Nu a fost găsită o achiziție activă."
                    }
                }
            } else {
                Log.w(TAG, "queryPurchasesAsync failed: ${billingResult.debugMessage}")
                if (showResultMessage) {
                    _billingStatusMessage.value = "Nu am putut verifica achizițiile. Încearcă din nou."
                }
            }
        }
    }

    fun launchBillingFlow(activity: Activity): Boolean {
        _billingStatusMessage.value = null

        if (!billingClient.isReady) {
            _billingStatusMessage.value = "Google Play Billing se conectează. Încearcă din nou în câteva secunde."
            startConnection()
            return false
        }

        val details = productDetails
        if (details == null) {
            _billingStatusMessage.value =
                "Produsul remove_ads nu este încă activ în Google Play Console."
            queryProductDetails()
            return false
        }

        val productDetailsParams = BillingFlowParams.ProductDetailsParams.newBuilder()
            .setProductDetails(details)
            .build()

        val billingFlowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(listOf(productDetailsParams))
            .build()

        val result = billingClient.launchBillingFlow(activity, billingFlowParams)
        if (result.responseCode != BillingClient.BillingResponseCode.OK) {
            _billingStatusMessage.value = "Achiziția nu a putut fi pornită: ${result.debugMessage}"
            return false
        }
        return true
    }

    override fun onPurchasesUpdated(
        billingResult: BillingResult,
        purchases: MutableList<Purchase>?
    ) {
        when (billingResult.responseCode) {
            BillingClient.BillingResponseCode.OK -> {
                purchases.orEmpty().forEach(::handlePurchase)
            }

            BillingClient.BillingResponseCode.USER_CANCELED -> {
                Log.d(TAG, "User canceled the purchase.")
                _billingStatusMessage.value = "Achiziția a fost anulată."
            }

            BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED -> {
                restorePurchases()
            }

            else -> {
                Log.e(TAG, "Purchase error: ${billingResult.debugMessage}")
                _billingStatusMessage.value = "Eroare la achiziție: ${billingResult.debugMessage}"
            }
        }
    }

    private fun handlePurchase(purchase: Purchase) {
        if (!purchase.products.contains(PRODUCT_REMOVE_ADS)) return

        when (purchase.purchaseState) {
            Purchase.PurchaseState.PURCHASED -> {
                updateAdsRemoved(true)
                _billingStatusMessage.value =
                    "Mulțumim! Reclamele interstițiale au fost eliminate permanent."

                if (!purchase.isAcknowledged) {
                    val params = AcknowledgePurchaseParams.newBuilder()
                        .setPurchaseToken(purchase.purchaseToken)
                        .build()

                    billingClient.acknowledgePurchase(params) { result ->
                        if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                            Log.d(TAG, "Purchase acknowledged successfully.")
                        } else {
                            Log.w(TAG, "Purchase acknowledgement failed: ${result.debugMessage}")
                        }
                    }
                }
            }

            Purchase.PurchaseState.PENDING -> {
                _billingStatusMessage.value =
                    "Plata este în așteptare. Reclamele vor fi eliminate după confirmarea plății."
            }

            else -> Unit
        }
    }

    private fun updateAdsRemoved(removed: Boolean) {
        _isAdsRemoved.value = removed
        onAdsRemovedStateChanged(removed)
    }

    fun clearStatusMessage() {
        _billingStatusMessage.value = null
    }

    fun release() {
        if (billingClient.isReady) {
            billingClient.endConnection()
        }
    }
}
