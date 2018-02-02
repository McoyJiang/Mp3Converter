# Mp3Converter
this is a library which contains a lot of audio processing function, like:

    1、 convert a local PCM or WAV source into Mp3 without lossy
    
    2、 adjust the tempo、pitch、rate of an local audio
    
    3、 record an audio, transform the original PCM data into Mp3 file
    
    4、 record an audio, and dynamically setting tempo、pitch、rate

# Dependency library
### **_LameMp3_**
[LameMp3](http://lame.sourceforge.net/) library which is a high quality MPEG Audio Layer III (MP3) encoder licensed under the LGP.

### **_SoundTouch_**
[SoundTouch](https://www.surina.net/soundtouch/) is an open-source audio processing library for changing the Tempo, Pitch and Playback Rates of audio streams or audio files. The library additionally supports estimating stable beats-per-minute rates for audio tracks.

# Widget
### LameUtils.java
LameUtils is an utils class, which you can use to call C++ native code, before use a specific method, you need to load the .so
library into memory: `System.loadLibrary("mp3lame");` . The main function within this class is to transfer the original PCM stream into a .mp3 file

### SoundTouchUtils.java
SoundTouchUtils is another utils class, which you can use to adjust tempo、pitch、rate of an locally existed audio file

### Mp3Recorder.java
Mp3Recorder is a encapsulated Android AudioRecorder. you can just call start() method to start an
audio recording, and call stop() method to stop it.
<br>
1. declare and initialize a instance of Mp3Recorder
```
Mp3Recorder mRecorder = new Mp3Recorder(new File(Environment.getExternalStorageDirectory(),"test.mp3"));
```

as you can see in Mp3Recorder.java, i use default params for sample_rate and bit_rate

2. call Mp3Recorder.start() method to start the audio recording
```
try {
    mRecorder.start();
} catch (IOException e) {
    e.printStackTrace();
}
```
### WavRecorder.java
WavRecorder is another type of recording compared with Mp3Recorder.java。 The output file should be .wav format.

```
// initialize WavRecorder
WavRecorder wavRecorder = new WavRecorder();

// set output path, need to be the absolute path, like storage/sdcard0/Downloads/XXX.wav
wavRecorder.setOutputFile(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "temp_wav.wav");

// prepare audio
wavRecorder.prepare();

// start recording
wavRecorder.start();
```
<b><i>in the duration of recording, you need to call stop() method to stop recording</i></b>
