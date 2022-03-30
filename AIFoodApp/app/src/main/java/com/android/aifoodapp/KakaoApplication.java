package com.android.aifoodapp;

import android.app.Application;
import com.kakao.sdk.common.KakaoSdk;

public class KakaoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        KakaoSdk.init(this, "ee925720d00642f3e5c08066b8883de5");
    }
}