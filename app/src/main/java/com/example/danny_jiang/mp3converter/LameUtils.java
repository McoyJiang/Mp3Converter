package com.example.danny_jiang.mp3converter;

/**
 * Created by Danny on 18/1/16.
 */

public class LameUtils {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("mp3lame");
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native static String stringFromJNI();

    /**
     * Initialize LAME.
     *
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
     */
    public native static void init(int inChannel, int inSamplerate,
                                   int outSamplerate, int outBitrate, int quality);

    /**
     * Encode buffer to mp3.
     *
     * @param bufferLeft
     *            PCM data for left channel.
     * @param bufferRight
     *            PCM data for right channel.
     * @param samples
     *            number of samples per channel.
     * @param mp3buf
     *            result encoded MP3 stream. You must specified
     *            "7200 + (1.25 * buffer_l.length)" length array.
     * @return number of bytes output in mp3buf. Can be 0.<br />
     *         -1: mp3buf was too small<br />
     *         -2: malloc() problem<br />
     *         -3: lame_init_params() not called<br />
     *         -4: psycho acoustic problems
     */
    public native static int encode(short[] bufferLeft, short[] bufferRight,
                                    int samples, byte[] mp3buf);

    /**
     * Convert a PCM file into Mp3 file
     *
     * @param sourceFile    should be .pcm format
     * @param targetFile    out put .mp3 format file
     * @return              if convert successful
     */
    public native static int encodeFile(String sourceFile, String targetFile);
}
