package com.example.danny_jiang.mp3converter;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
        String versionString = SoundTouchUtils.getVersionString();

        TextView textView = (TextView) findViewById(R.id.sample_text);

        textView.setText(versionString);
    }
}
