from pydub import AudioSegment
import argparse

parser = argparse.ArgumentParser(description="reverse audio")
parser.add_argument("--prefix", type=str, help="path prefix")
parser.add_argument("--input_file", type=str, help="file name of audio")
parser.add_argument("--output_file", type=str, help="output name")
args = parser.parse_args()

audio = AudioSegment.from_wav(args.prefix+args.input_file)
audio.reverse().export(args.prefix+args.output_file, format="wav")