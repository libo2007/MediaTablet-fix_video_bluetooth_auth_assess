package com.jiaying.mediatablet.app;

import android.app.Application;
import android.graphics.Bitmap;

import com.iflytek.cloud.SpeechUtility;
import com.jiaying.mediatablet.constants.Constants;

/**
 * 作者：lenovo on 2016/4/3 13:59
 * 邮箱：353510746@qq.com
 * 功能：application
 */
public class MediatabletApp extends Application{
    @Override
    public void onCreate() {
        SpeechUtility.createUtility(MediatabletApp.this, Constants.FLYTEK_APP_ID);
        super.onCreate();
    }

}
