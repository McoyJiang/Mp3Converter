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

## Mp3 Encoder
Mp3Converter uses 
<br>

On Samsung's official website, there is a [lamemp3 post](http://developer.samsung.com/technical-doc/view.do?v=T000000090) describes how to integrate it and use it

### Widget


## Recorder: Mp3Recorder.java
Mp3Recorder is a encapsulated Android AudioRecorder. you can just call start() method to start an
audio recording, and call stop() method to stop it.

#### Use
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

