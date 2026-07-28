import math
import struct
import wave
import random

def create_test_wav(filename="test.wav"):
    sample_rate = 44100
    duration = 2.0 # seconds
    num_samples = int(sample_rate * duration)
    
    with wave.open(filename, 'w') as wav_file:
        wav_file.setnchannels(2) # Stereo
        wav_file.setsampwidth(2) # 16-bit
        wav_file.setframerate(sample_rate)
        
        frames = bytearray()
        for i in range(num_samples):
            t = i / sample_rate
            # 440 Hz tone with sine
            left = math.sin(2 * math.pi * 440 * t) * 0.3
            right = math.sin(2 * math.pi * 440 * t) * 0.3
            
            left_int = int(max(-32768, min(32767, left * 32767)))
            right_int = int(max(-32768, min(32767, right * 32767)))
            
            frames.extend(struct.pack('<hh', left_int, right_int))
            
        wav_file.writeframes(frames)
    print("Saved test.wav")

create_test_wav()
