package com.jiaying.mediatablet.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;


import com.jiaying.mediatablet.R;
import com.jiaying.mediatablet.utils.MyLog;
import com.jiaying.mediatablet.utils.WifiAdmin;

public class LaunchActivity extends Activity {

    //wifi自动连接begin


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
    }

    @Override
    protected void onResume() {
        super.onResume();
        autoWifiConnect();
    }

    //自动连接wifi
    private void autoWifiConnect() {
        ConnectWifiThread connectWifiThread = new ConnectWifiThread("JiaYing_ZXDC", "jyzxdcarm", 3, this);
        connectWifiThread.start();
    }

    private class ConnectWifiThread extends Thread {
        private boolean wifiIsOk = false;
        private String SSID = null;
        private String PWD = null;
        private int TYPE = 0;
        private WifiAdmin wifiAdmin = null;

        public ConnectWifiThread(String SSID, String PWD, int TYPE, Context context) {
            this.SSID = SSID;
            this.PWD = PWD;
            this.TYPE = TYPE;
            wifiAdmin = new WifiAdmin(context);
        }

        @Override
        public void run() {
            super.run();
            while (true) {
                //判断wifi是否已经打开
                if (wifiAdmin.checkState() == WifiManager.WIFI_STATE_ENABLED) {
                    //连接网络
                    wifiIsOk = wifiAdmin.addNetwork(wifiAdmin.CreateWifiInfo(SSID, PWD, TYPE));
                    //判断wifi是否已经连接上
                    if (wifiIsOk) {
                        //界面跳转
                        goToMainActivity();
                        break;
                    }
                } else {
                    wifiAdmin.openWifi();
                }
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private void goToMainActivity() {
            LaunchActivity.this.startActivity(new Intent(LaunchActivity.this, MainActivity.class));
        }
    }
}


