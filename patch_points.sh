cat << 'INNER_EOF' > /tmp/add_points.txt
    val pointsSuffix: String
INNER_EOF
sed -i '/val contentDescBack: String/r /tmp/add_points.txt' app/src/main/java/com/example/ui/strings/AppStrings.kt

cat << 'INNER_EOF' > /tmp/add_points_en.txt
    override val pointsSuffix = " pts"
INNER_EOF
sed -i '/override val contentDescBack = "Back"/r /tmp/add_points_en.txt' app/src/main/java/com/example/ui/strings/StringsEn.kt

cat << 'INNER_EOF' > /tmp/add_points_ro.txt
    override val pointsSuffix = " puncte"
INNER_EOF
sed -i '/override val contentDescBack = "Înapoi"/r /tmp/add_points_ro.txt' app/src/main/java/com/example/ui/strings/StringsRo.kt

sed -i 's/"${level.rewardName} (+${level.rewardBonus} puncte)"/"${level.rewardName} (+${level.rewardBonus}" + Localization.strings.pointsSuffix + ")"/g' app/src/main/java/com/example/ui/components/Dialogs.kt
echo "Added pointsSuffix"
