import re
with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

# I see:
# class MainActivity : ComponentActivity() {
#                     newBase
#                 }
#             } else {
#                 newBase
#             }
#         )
#     }
#     private var gameViewModel: GameViewModel? = null

# I need to clean it up to just be:
# class MainActivity : ComponentActivity() {
#     private var gameViewModel: GameViewModel? = null

content = re.sub(r"class MainActivity : ComponentActivity\(\) \{.*?private var gameViewModel:", "class MainActivity : ComponentActivity() {\n\n    private var gameViewModel:", content, flags=re.DOTALL)

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)
