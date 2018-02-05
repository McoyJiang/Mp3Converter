package com.example.danny_jiang.sample;

import android.app.Application;

import cn.jpush.im.android.api.JMessageClient;

/**
 * Created by Danny on 18/2/5.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // You can enable debug mode in developing state. You should close debug mode when release.
        JMessageClient.setDebugMode(true);

        JMessageClient.init(this);
    }
}
