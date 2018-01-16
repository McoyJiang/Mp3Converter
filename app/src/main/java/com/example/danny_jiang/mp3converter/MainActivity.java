package com.example.danny_jiang.mp3converter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        TextView tv = (TextView) findViewById(R.id.sample_text);
        tv.setText(LameUtils.stringFromJNI());
    }

    public void init(View view) {
        LameUtils.init(Constant.LameBehaviorChannelNumber, Constant.RecordSampleRate,
                Constant.BehaviorSampleRate, Constant.LameBehaviorBitRate, Constant.LameMp3Quality);

        //LameUtils.encodeFile(firstDecodedPcm, transformMusicUrl);
    }
}
