cat << 'INNER_EOF' > /tmp/add_billing.txt
    val billingNotConnected: String
    val billingProductNotFound: String
    val billingAdsAlreadyRemoved: String
    fun billingError(msg: String): String
    val billingSuccess: String
INNER_EOF
sed -i '/val contentDescBack: String/r /tmp/add_billing.txt' app/src/main/java/com/example/ui/strings/AppStrings.kt

cat << 'INNER_EOF' > /tmp/add_billing_en.txt
    override val billingNotConnected = "Google Play Billing is not connected yet."
    override val billingProductNotFound = "Product 'remove_ads' not found in Google Play Console."
    override val billingAdsAlreadyRemoved = "Ads are already removed!"
    override fun billingError(msg: String) = "Purchase error: $msg"
    override val billingSuccess = "Thank you! Ads have been permanently removed."
INNER_EOF
sed -i '/override val contentDescBack = "Back"/r /tmp/add_billing_en.txt' app/src/main/java/com/example/ui/strings/StringsEn.kt

cat << 'INNER_EOF' > /tmp/add_billing_ro.txt
    override val billingNotConnected = "Google Play Billing nu este conectat încă."
    override val billingProductNotFound = "Produsul 'remove_ads' nu a fost găsit în Google Play Console."
    override val billingAdsAlreadyRemoved = "Reclamele sunt deja eliminate!"
    override fun billingError(msg: String) = "Eroare achiziție: $msg"
    override val billingSuccess = "Mulțumim! Reclamele au fost eliminate permanent."
INNER_EOF
sed -i '/override val contentDescBack = "Înapoi"/r /tmp/add_billing_ro.txt' app/src/main/java/com/example/ui/strings/StringsRo.kt

sed -i 's/"Google Play Billing nu este conectat încă."/com.example.ui.strings.Localization.strings.billingNotConnected/g' app/src/main/java/com/example/monetization/BillingManager.kt
sed -i 's/"Produsul '"'remove_ads'"' nu a fost găsit în Google Play Console."/com.example.ui.strings.Localization.strings.billingProductNotFound/g' app/src/main/java/com/example/monetization/BillingManager.kt
sed -i 's/"Reclamele sunt deja eliminate!"/com.example.ui.strings.Localization.strings.billingAdsAlreadyRemoved/g' app/src/main/java/com/example/monetization/BillingManager.kt
sed -i 's/"Eroare achiziție: ${billingResult.debugMessage}"/com.example.ui.strings.Localization.strings.billingError(billingResult.debugMessage)/g' app/src/main/java/com/example/monetization/BillingManager.kt
sed -i 's/"Mulțumim! Reclamele au fost eliminate permanent."/com.example.ui.strings.Localization.strings.billingSuccess/g' app/src/main/java/com/example/monetization/BillingManager.kt

echo "Added billing strings"
