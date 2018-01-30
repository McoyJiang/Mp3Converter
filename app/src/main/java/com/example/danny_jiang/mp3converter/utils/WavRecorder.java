package com.example.danny_jiang.mp3converter.utils;

import android.media.AudioRecord;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import static android.media.AudioFormat.CHANNEL_IN_MONO;
import static android.media.AudioFormat.ENCODING_PCM_16BIT;
import static android.media.MediaRecorder.AudioSource.MIC;

/**
 * Created by Danny on 17/11/30.
 * <p>
 * current function added by time
 * record an audio of .wav format 30/11/2017
 */
public class WavRecorder {
    private static final String TAG = "WavRecorder";

    /**
     * INITIALIZING : recorder is initializing;
     * READY : recorder has been initialized, recorder not yet started
     * RECORDING : recording
     * ERROR : reconstruction needed
     * STOPPED: reset needed
     */
    public enum State {
        INITIALIZING, READY, RECORDING, ERROR, STOPPED
    }

    ;

    public static final boolean RECORDING_UNCOMPRESSED = true;
    public static final boolean RECORDING_COMPRESSED = false;

    // The interval in which the recorded samples are output to the file
    // Used only in uncompressed mode
    private static final int TIMER_INTERVAL = 120;

    // Recorder used for uncompressed recording
    private AudioRecord aRecorder = null;

    // Stores current amplitude (only in uncompressed mode)
    private int cAmplitude = 0;
    // Output file path
    private String outPath = null;

    // Recorder state; see State
    private State state;

    // File writer (only in uncompressed mode)
    private RandomAccessFile fWriter;

    // Number of channels, sample rate, sample size(size in bits), buffer size, audio source, sample size(see AudioFormat)
    private short nChannels;
    private int sRate;
    private short bSamples;
    private int bufferSize;
    private int aSource;
    private int aFormat;

    // Number of frames written to file on each output(only in uncompressed mode)
    private int framePeriod;

    // Buffer for output(only in uncompressed mode)
    private byte[] buffer;

    // Number of bytes written to file after header(only in uncompressed mode)
    // after stop() is called, this size is written to the header/data chunk in the wave file
    private int payloadSize;

    /**
     * Returns the state of the recorder in a RehearsalAudioRecord.State typed object.
     * Useful, as no exceptions are thrown.
     *
     * @return recorder state
     */
    public State getState() {
        return state;
    }

    /*
     *
     * Method used for recording.
     *
     */
    private AudioRecord.OnRecordPositionUpdateListener updateListener = new AudioRecord.OnRecordPositionUpdateListener() {
        public void onPeriodicNotification(AudioRecord recorder) {
            Log.e(TAG, "onPeriodicNotification: ");
            if (State.STOPPED == state) {
                Log.d(WavRecorder.this.getClass().getName(), "recorder stopped");
                return;
            }

            Log.e(TAG, "onPeriodicNotification: buffer is " + buffer.length);

            int bufferReadResult = aRecorder.read(buffer, 0, buffer.length);// Fill buffer

            if(AudioRecord.ERROR_INVALID_OPERATION != bufferReadResult){
                    try {
                        if (bSamples == 16) {
                            for (int i = 0; i < buffer.length / 2; i++) { // 16bit sample size
                                short curSample = getShort(buffer[i * 2], buffer[i * 2 + 1]);
                                if (curSample > cAmplitude) { // Check amplitude
                                    cAmplitude = curSample;
                                }
                            }
                        } else { // 8bit sample size
                            for (int i = 0; i < buffer.length; i++) {
                                if (buffer[i] > cAmplitude) { // Check amplitude
                                    cAmplitude = buffer[i];
                                }
                            }
                        }

                        fWriter.write(buffer); // Write buffer to file
                        payloadSize += buffer.length;

                    } catch (IOException e) {
                        Log.e(TAG, "Error occured in updateListener, recording is aborted : " + e.getMessage());
                        stop();
                    }
            }
        }

        public void onMarkerReached(AudioRecord recorder) {
            // NOT USED
        }
    };

    /**
     * use default param to instantiate an WavRecorder
     */
    public WavRecorder() {
        this(MIC, 44100, CHANNEL_IN_MONO, ENCODING_PCM_16BIT, 3);
    }

    /**
     * constructor an WavRecorder with specified params
     *
     * @param audioSource   the source of audio
     *                      AudioSource.MIC
     *                      AudioSource.DEFAULT
     *                      AudioSource.CAMCORDER
     * @param sampleRate    sample rate of the recorded audio
     *                      44100, 22050, 11025, 8000
     * @param channelConfig the channel config
     *                      AudioFormat.CHANNEL_IN_MONO
     *                      AudioFormat.CHANNEL_IN_STEREO
     * @param audioFormat   the format of recorded audio
     *                      AudioFormat.ENCODING_PCM_16BIT
     *                      AudioFormat.ENCODING_PCM_8BIT
     *
     * @param duration      the automatic duration of audio recording, default is 3 seconds
     */
    public WavRecorder(int audioSource, int sampleRate, int channelConfig,
                       int audioFormat, int duration) {
        try {
            // RECORDING_UNCOMPRESSED
            if (audioFormat == ENCODING_PCM_16BIT) {
                bSamples = 16;
            } else {
                bSamples = 8;
            }

            if (channelConfig == CHANNEL_IN_MONO) {
                nChannels = 1;
            } else {
                nChannels = 2;
            }

            aSource = audioSource;
            sRate = sampleRate;
            aFormat = audioFormat;

            framePeriod = sampleRate * TIMER_INTERVAL / 1000;
            bufferSize = framePeriod * 2 * bSamples * nChannels / 8;
            if (bufferSize < AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat)) { // Check to make sure buffer size is not smaller than the smallest allowed one
                bufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat);
                // Set frame period and timer interval accordingly
                framePeriod = bufferSize / (2 * bSamples * nChannels / 8);
                Log.w(TAG, "Increasing buffer size to " + Integer.toString(bufferSize));
            }

            aRecorder = new AudioRecord(audioSource, sampleRate, channelConfig, audioFormat, bufferSize);
            if (aRecorder.getState() != AudioRecord.STATE_INITIALIZED)
                throw new Exception("AudioRecord initialization failed");
            aRecorder.setRecordPositionUpdateListener(updateListener);
            aRecorder.setPositionNotificationPeriod(framePeriod);

            cAmplitude = 0;
            outPath = null;

            if (duration > 0) {
            }

            state = State.INITIALIZING;
        } catch (Exception e) {
            if (e.getMessage() != null) {
                Log.e(TAG, e.getMessage());
            } else {
                Log.e(TAG, "Unknown error occured while initializing recording");
            }
            state = State.ERROR;
        }
    }

    /**
     * Sets output file path, call directly after construction/reset.
     *
     * @param outPath file path
     */
    public void setOutputFile(String outPath) {
        try {
            if (state == State.INITIALIZING) {
                this.outPath = outPath;
            }
        } catch (Exception e) {
            if (e.getMessage() != null) {
                Log.e(TAG, e.getMessage());
            } else {
                Log.e(TAG, "Unknown error occured while setting output path");
            }
            state = State.ERROR;
        }
    }

    /**
     * Returns the largest amplitude sampled since the last call to this method.
     *
     * @return returns the largest amplitude since the last call, or 0 when not in recording state.
     */
    public int getMaxAmplitude() {
        if (state == State.RECORDING) {
            int result = cAmplitude;
            cAmplitude = 0;
            return result;
        } else {
            return 0;
        }
    }


    /**
     * Prepares the recorder for recording, in case the recorder is not in the INITIALIZING state and the file path was not set
     * the recorder is set to the ERROR state, which makes a reconstruction necessary.
     * In case uncompressed recording is toggled, the header of the wave file is written.
     * In case of an exception, the state is changed to ERROR
     */
    public void prepare() {
        try {
            if (state == State.INITIALIZING) {
                if ((aRecorder.getState() == AudioRecord.STATE_INITIALIZED) & (outPath != null)) {
                    // write file header

                    fWriter = new RandomAccessFile(outPath, "rw");

                    fWriter.setLength(0); // Set file length to 0, to prevent unexpected behavior in case the file already existed
                    fWriter.writeBytes("RIFF");
                    fWriter.writeInt(0); // Final file size not known yet, write 0
                    fWriter.writeBytes("WAVE");
                    fWriter.writeBytes("fmt ");
                    fWriter.writeInt(Integer.reverseBytes(16)); // Sub-chunk size, 16 for PCM
                    fWriter.writeShort(Short.reverseBytes((short) 1)); // AudioFormat, 1 for PCM
                    fWriter.writeShort(Short.reverseBytes(nChannels));// Number of channels, 1 for mono, 2 for stereo
                    fWriter.writeInt(Integer.reverseBytes(sRate)); // Sample rate
                    fWriter.writeInt(Integer.reverseBytes(sRate * bSamples * nChannels / 8)); // Byte rate, SampleRate*NumberOfChannels*BitsPerSample/8
                    fWriter.writeShort(Short.reverseBytes((short) (nChannels * bSamples / 8))); // Block align, NumberOfChannels*BitsPerSample/8
                    fWriter.writeShort(Short.reverseBytes(bSamples)); // Bits per sample
                    fWriter.writeBytes("data");
                    fWriter.writeInt(0); // Data chunk size not known yet, write 0

                    buffer = new byte[framePeriod * bSamples / 8 * nChannels];
                    state = State.READY;
                } else {
                    Log.e(TAG, "prepare() method called on uninitialized recorder");
                    state = State.ERROR;
                }
            } else {
                Log.e(TAG, "prepare() method called on illegal state");
                release();
                state = State.ERROR;
            }
        } catch (Exception e) {
            if (e.getMessage() != null) {
                Log.e(TAG, e.getMessage());
            } else {
                Log.e(TAG, "Unknown error occured in prepare()");
            }
            state = State.ERROR;
        }
    }

    /**
     * Releases the resources associated with this class, and removes the unnecessary files, when necessary
     */
    public void release() {
        if (state == State.RECORDING) {
            stop();
        } else {
            if ((state == State.READY)) {
                try {
                    fWriter.close(); // Remove prepared file
                } catch (IOException e) {
                    Log.e(TAG, "I/O exception occured while closing output file");
                }
                (new File(outPath)).delete();
            }
        }

        if (aRecorder != null) {
            aRecorder.release();
        }
    }

    /**
     * Resets the recorder to the INITIALIZING state, as if it was just created.
     * In case the class was in RECORDING state, the recording is stopped.
     * In case of exceptions the class is set to the ERROR state.
     */
    public void reset() {
        try {
            if (state != State.ERROR) {
                release();
                outPath = null; // Reset file path
                cAmplitude = 0; // Reset amplitude
                aRecorder = new AudioRecord(aSource, sRate, nChannels + 1, aFormat, bufferSize);
                state = State.INITIALIZING;
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            state = State.ERROR;
        }
    }

    /**
     * Starts the recording, and sets the state to RECORDING.
     * Call after prepare().
     */
    public void start() {
        if (state == State.READY) {
            payloadSize = 0;
            aRecorder.startRecording();
            aRecorder.read(buffer, 0, buffer.length);

            state = State.RECORDING;
        } else {
            Log.e(TAG, "start() called on illegal state");
            state = State.ERROR;
        }
    }

    /**
     * Stops the recording, and sets the state to STOPPED.
     * In case of further usage, a reset is needed.
     * Also finalizes the wave file in case of uncompressed recording.
     */
    public void stop() {
        Log.e(TAG, "WavRecorder stop: ");
        if (state == State.RECORDING) {
            aRecorder.stop();

            try {
                fWriter.seek(4); // Write size to RIFF header
                fWriter.writeInt(Integer.reverseBytes(36 + payloadSize));

                fWriter.seek(40); // Write size to Subchunk2Size field
                fWriter.writeInt(Integer.reverseBytes(payloadSize));

                fWriter.close();

                process();
            } catch (IOException e) {
                Log.e(TAG, "I/O exception occured while closing output file");
                state = State.ERROR;
            }

            aRecorder.setRecordPositionUpdateListener(null);

            aRecorder = null;

            state = State.STOPPED;
        } else {
            Log.e(TAG, "stop() called on illegal state");
            state = State.ERROR;
        }
    }

    /// process a file with SoundTouch. Do the processing using a background processing
    /// task to avoid hanging of the UI
    protected void process()
    {
        try
        {
            AudioProcessTask task = new AudioProcessTask();
            AudioProcessTask.Parameters params = task.new Parameters();
            // parse processing parameters
            params.inFileName = outPath;
            params.outFileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "processed_mp3.mp3";
            params.tempo = 1f * Float.parseFloat("1");
            params.pitch = Float.parseFloat("8.5");

            // start SoundTouch processing in a background thread
            task.execute(params);
        }
        catch (Exception exp)
        {
            exp.printStackTrace();
        }

    }

    /*
     *
     * Converts a byte[2] to a short, in LITTLE_ENDIAN format
     *
     */
    private short getShort(byte argB1, byte argB2) {
        return (short) (argB1 | (argB2 << 8));
    }
}
