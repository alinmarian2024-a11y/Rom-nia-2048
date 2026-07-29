with open("app/src/main/AndroidManifest.xml", "r") as f:
    content = f.read()

content = content.replace('    <attribution android:tag="default" android:label="@string/app_name" />\n', '')

with open("app/src/main/AndroidManifest.xml", "w") as f:
    f.write(content)
