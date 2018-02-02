package com.example.danny_jiang.sample;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.danny_jiang.mp3converter.utils.AudioProcessTask;
import com.example.danny_jiang.mp3converter.utils.Mp3Recorder;
import com.example.danny_jiang.mp3converter.utils.SoundTouchUtils;
import com.example.danny_jiang.mp3converter.utils.WavRecorder;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private Mp3Recorder mRecorder = new Mp3Recorder(new File(Environment.getExternalStorageDirectory(),"test.mp3"));

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);
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
            AudioProcessTask task = new AudioProcessTask();
            AudioProcessTask.Parameters params = task.new Parameters();
            // parse processing parameters
            params.inFileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "leftTempCut.wav";
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

    public void process(View view) {
        process();
    }

    private WavRecorder wavRecorder;
    public void startWavRecord(View view) {
        wavRecorder = new WavRecorder();

        wavRecorder.setOutputFile(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "temp_wav.wav");
        wavRecorder.prepare();

        wavRecorder.start();
    }

    public void stopWavRecord(View view) {
        wavRecorder.stop();
    }

}

