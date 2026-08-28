sed -i 's/selected = themePref == Localization.strings.themeLight/selected = themePref == "LIGHT"/g' app/src/main/java/com/example/ui/screens/SettingsScreen.kt
sed -i 's/onClick = { viewModel.setTheme(Localization.strings.themeLight) }/onClick = { viewModel.setTheme("LIGHT") }/g' app/src/main/java/com/example/ui/screens/SettingsScreen.kt

sed -i 's/selected = themePref == Localization.strings.themeDark/selected = themePref == "DARK"/g' app/src/main/java/com/example/ui/screens/SettingsScreen.kt
sed -i 's/onClick = { viewModel.setTheme(Localization.strings.themeDark) }/onClick = { viewModel.setTheme("DARK") }/g' app/src/main/java/com/example/ui/screens/SettingsScreen.kt

sed -i 's/selected = themePref == Localization.strings.themeSystem/selected = themePref == "SYSTEM"/g' app/src/main/java/com/example/ui/screens/SettingsScreen.kt
sed -i 's/onClick = { viewModel.setTheme(Localization.strings.themeSystem) }/onClick = { viewModel.setTheme("SYSTEM") }/g' app/src/main/java/com/example/ui/screens/SettingsScreen.kt

echo "Fixed SettingsScreen themes"
