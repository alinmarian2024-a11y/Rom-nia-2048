import os
import glob
import re

for filepath in glob.glob("app/src/main/java/com/example/**/*.kt", recursive=True):
    with open(filepath, "r") as f:
        content = f.read()

    # Remove all instances of "import com.example.ui.strings.Localization\n" and "import com.example.ui.strings.Language\n"
    content = content.replace("import com.example.ui.strings.Localization\n", "")
    content = content.replace("import com.example.ui.strings.Language\n", "")

    # Now add just one at the top of imports if needed
    if "Localization." in content or "Language." in content or " Language" in content:
        # Find the first import
        match = re.search(r'^import ', content, re.MULTILINE)
        if match:
            pos = match.start()
            imports = "import com.example.ui.strings.Localization\nimport com.example.ui.strings.Language\n"
            content = content[:pos] + imports + content[pos:]

    with open(filepath, "w") as f:
        f.write(content)
