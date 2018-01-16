# Mp3Converter
this is a library which can convert PCM or WAV source into Mp3 without lossy

## Introduction
Mp3Converter uses [LameMp3](http://lame.sourceforge.net/) library which is a high quality MPEG Audio Layer III (MP3) encoder licensed under the LGP
<br>

On Samsung's official website, there is a [lamemp3 post](http://developer.samsung.com/technical-doc/view.do?v=T000000090) describes how to integrate it and use it

## Details
Java Layer:
there is a LameUtils.java which you can use to call functions implemented in C++ layer.
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

`encodeFile`
this method takes 2 params

    * @param sourceFile    should be .pcm format
    * @param targetFile    out put .mp3 format file