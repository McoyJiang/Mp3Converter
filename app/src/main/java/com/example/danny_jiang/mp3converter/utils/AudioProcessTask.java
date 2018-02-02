package com.example.danny_jiang.mp3converter.utils;

import android.os.AsyncTask;
import android.util.Log;


/**
 * Created by Danny on 18/1/30.
 */

public class AudioProcessTask extends AsyncTask<AudioProcessTask.Parameters, Integer, Long> {
    /// Helper class to store the SoundTouch file processing parameters
    public final class Parameters
    {
        public String inFileName;
        public String outFileName;
        public float tempo;
        public float pitch;
    }



    /// Function that does the SoundTouch processing
    public final long doSoundTouchProcessing(Parameters params)
    {

        SoundTouchUtils st = new SoundTouchUtils();
        st.setTempo(params.tempo);
        st.setPitchSemiTones(params.pitch);
        Log.i("SoundTouch", "process file " + params.inFileName);
        long startTime = System.currentTimeMillis();
        int res = st.processFile(params.inFileName, params.outFileName);
        long endTime = System.currentTimeMillis();
        float duration = (endTime - startTime) * 0.001f;

        Log.i("SoundTouch", "process file done, duration = " + duration);

        return 0L;
    }



    /// Overloaded function that get called by the system to perform the background processing
    @Override
    protected Long doInBackground(Parameters... aparams)
    {
        return doSoundTouchProcessing(aparams[0]);
    }
}
