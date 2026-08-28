import wave
import struct

def create_silent_wav(filename):
    with wave.open(filename, 'wb') as wav_file:
        wav_file.setnchannels(1) # mono
        wav_file.setsampwidth(2) # 16-bit
        wav_file.setframerate(44100)
        # 1 second of silence
        data = struct.pack('<h', 0) * 44100
        wav_file.writeframes(data)

create_silent_wav('app/src/main/res/raw/game_theme_1.mp3')
create_silent_wav('app/src/main/res/raw/game_theme_2.mp3')
create_silent_wav('app/src/main/res/raw/game_theme_3.mp3')
create_silent_wav('app/src/main/res/raw/menu_theme.mp3')
print("Replaced with silent WAV")
