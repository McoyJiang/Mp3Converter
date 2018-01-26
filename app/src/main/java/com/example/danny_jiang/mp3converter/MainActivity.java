package com.example.danny_jiang.mp3converter;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.danny_jiang.mp3converter.utils.Mp3Recorder;
import com.example.danny_jiang.mp3converter.utils.SoundTouchUtils;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private Mp3Recorder mRecorder = new Mp3Recorder(new File(Environment.getExternalStorageDirectory(),"test.mp3"));

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button startButton = (Button) findViewById(R.id.StartButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mRecorder.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        Button stopButton = (Button) findViewById(R.id.StopButton);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecorder.stop();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRecorder.stop();
    }

    public void setTempo(View view) {
        SoundTouchUtils soundTouchUtils = new SoundTouchUtils();

        soundTouchUtils.setTempo(10);
    }

    public void getVersionString(View view) {
        String versionString = SoundTouchUtils.getVersionString();

        TextView textView = (TextView) findViewById(R.id.sample_text);

        textView.setText(versionString);
    }

    /// process a file with SoundTouch. Do the processing using a background processing
    /// task to avoid hanging of the UI
    protected void process()
    {
        try
        {
            ProcessTask task = new ProcessTask();
            ProcessTask.Parameters params = task.new Parameters();
            // parse processing parameters
            params.inFileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "leftTempCut.wav";
            params.outFileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "fff.mp3";
            params.tempo = 0.01f * Float.parseFloat("1");
            params.pitch = Float.parseFloat("1");

            Toast.makeText(this, "Starting to process file " + params.inFileName + "...", Toast.LENGTH_SHORT).show();

            // start SoundTouch processing in a background thread
            task.execute(params);
//			task.doSoundTouchProcessing(params);	// this would run processing in main thread

        }
        catch (Exception exp)
        {
            exp.printStackTrace();
        }

    }

    public void process(View view) {
        process();
    }

    /// Helper class that will execute the SoundTouch processing. As the processing may take
    /// some time, run it in background thread to avoid hanging of the UI.
    protected class ProcessTask extends AsyncTask<ProcessTask.Parameters, Integer, Long>
    {
        /// Helper class to store the SoundTouch file processing parameters
        public final class Parameters
        {
            String inFileName;
            String outFileName;
            float tempo;
            float pitch;
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

}
