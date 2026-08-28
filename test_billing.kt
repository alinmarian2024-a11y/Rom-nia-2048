import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.ProductDetailsResponseListener

fun test(client: BillingClient, params: QueryProductDetailsParams) {
    client.queryProductDetailsAsync(params, ProductDetailsResponseListener { result, list ->
        println(list)
    })
}
