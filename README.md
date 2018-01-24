# Mp3Converter
this is a library which can convert PCM or WAV source into Mp3 without lossy

## Mp3 Compiler
Mp3Converter uses [LameMp3](http://lame.sourceforge.net/) library which is a high quality MPEG Audio Layer III (MP3) encoder licensed under the LGP
<br>

On Samsung's official website, there is a [lamemp3 post](http://developer.samsung.com/technical-doc/view.do?v=T000000090) describes how to integrate it and use it

### Details
Java Layer:
there is a <font color='#FF0000' size='5'>LameUtils.java</font> which you can use to call functions implemented in C++ layer.
<br>
there are two main methods in this class
<br>

`init`
this method takes 5 params, follow are the instructions:

     * @param inChannel
     *            number of channels in input stream.
     * @param inSamplerate
     *            input sample rate in Hz.
     * @param outSamplerate
     *            output sample rate in Hz.
     * @param outBitrate
     *            brate compression ratio in KHz.
     * @param quality
     *            quality=0..9. 0=best (very slow). 9=worst.<br />
     *            recommended:<br />
     *            2 near-best quality, not too slow<br />
     *            5 good quality, fast<br />
     *            7 ok quality, really fast
     public native static int encode(short[] bufferLeft, short[] bufferRight,
                                         int samples, byte[] mp3buf);

`encodeFile`
this method takes 2 params

    * @param sourceFile    should be .pcm format
    * @param targetFile    out put .mp3 format file
    public native static int encodeFile(String sourceFile, String targetFile);

both of these two methods are <font color='#FF0000'><b>native</b></font> method,
so the real implementation is in <font size='6'>`wrapper.c`</font>

<br>
according to the JNI criteria, their declaration should be like this:

```
void Java_com_example_danny_1jiang_mp3converter_LameUtils_init
       (JNIEnv *env, jclass jc, jint in_num_channels, jint in_sample_rate,
       jint out_sample_rate, jint in_brate, jint in_quality){
    ...
}
```
and
```
jint Java_com_example_danny_1jiang_mp3converter_LameUtils_encodeFile(JNIEnv *env,
		jobject jobj, jstring in_source_path, jstring in_target_path) {
	...
}
```

## Recorder
