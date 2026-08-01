import urllib.request
import zipfile
import io

url = "https://services.gradle.org/distributions/gradle-9.3.1-bin.zip"
print(f"Downloading {url}...")

with urllib.request.urlopen(url) as response:
    with zipfile.ZipFile(io.BytesIO(response.read())) as z:
        for name in z.namelist():
            if name.endswith('gradle-wrapper.jar'):
                print(f"Found {name}")
                with open('gradle/wrapper/gradle-wrapper.jar', 'wb') as f:
                    f.write(z.read(name))
                break
print("Done.")
