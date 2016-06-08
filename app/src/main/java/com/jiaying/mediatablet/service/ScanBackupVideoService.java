package com.jiaying.mediatablet.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;


import com.jiaying.mediatablet.constants.Constants;

import com.jiaying.mediatablet.db.DataPreference;
import com.jiaying.mediatablet.entity.DonorEntity;
import com.jiaying.mediatablet.entity.VideoEntity;
import com.jiaying.mediatablet.net.serveraddress.LogServer;
import com.jiaying.mediatablet.net.serveraddress.VideoServer;
import com.jiaying.mediatablet.net.softfan.FtpSenderFile;
import com.jiaying.mediatablet.net.softfan.SoftFanFTPException;

import com.jiaying.mediatablet.utils.MyLog;
import com.jiaying.mediatablet.utils.SelfFile;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * 作者：lenovo on 2016/5/29 17:16
 * 邮箱：353510746@qq.com
 * 功能：扫描本地未上传到服务器的本地视频，定时扫描传送到服务器
 */
public class ScanBackupVideoService extends Service {
    private static final String TAG = "ScanBackupVideoService";
    private Handler mHandler = null;
    private HandlerThread mHandlerThread = null;
    private DealBackupVideoTask mDealBackupVideoTask = null;

    //立即扫描
    private static final int MSG_SCAN_NOW = 1001;

    //等5分钟扫描
    private static final int MSG_SCAN_DELAY = 1002;

    private static final int TIME_5_MINUTE = 5 * 60 * 1000;
    private static final int TIME_3_SECOND = 3 * 1000;

    //传送结果
    private String resultStr;

    @Override
    public void onCreate() {
        super.onCreate();
        MyLog.e(TAG, "开始启动 ScanBackupVideoService");
        stopHandlerThread();
        mHandlerThread = new HandlerThread("scanbackthread");
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case MSG_SCAN_DELAY:
                        mHandler.postDelayed(mDealBackupVideoTask, TIME_5_MINUTE);
                        break;

                    case MSG_SCAN_NOW:
                        mHandler.postDelayed(mDealBackupVideoTask, TIME_3_SECOND);
                        break;

                    default:
                        break;
                }
            }
        };
        mDealBackupVideoTask = new DealBackupVideoTask();
        mHandler.post(mDealBackupVideoTask);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        MyLog.e(TAG, "销毁 ScanBackupVideoService");
        super.onDestroy();
        Intent it = new Intent(this, ScanBackupVideoService.class);
        startService(it);
    }

    private void stopHandlerThread() {
        if (mHandler != null && mDealBackupVideoTask != null) {
            mHandler.removeCallbacks(mDealBackupVideoTask);
            mHandler = null;
            mDealBackupVideoTask = null;
        }
        if (mHandlerThread != null) {
            mHandlerThread.quit();
            mHandlerThread = null;
        }
    }

    private class DealBackupVideoTask implements Runnable {

        @Override
        public void run() {
            //扫描backup目录是否有视频文件

            List<VideoEntity> backupFileList = SelfFile.getLocalVideoList(Constants.VIDEO_PATH_BACKUP);
            if (backupFileList == null || backupFileList.isEmpty()) {
                //等5分钟再次发送扫描请求
                MyLog.e(TAG, "backup目录下不存在视频文件");
                mHandler.sendEmptyMessage(MSG_SCAN_DELAY);
            } else {
                //向服务器传送文件
                String localFilePath = backupFileList.get(0).getPlay_url();
                String remoteFilePath = generateRemoteVideoNameByBackupVideoName(backupFileList.get(0).getName());
                MyLog.e(TAG, "backup目录下存在视频文件名：" + localFilePath + "\n远程文件名：" + remoteFilePath);
                upLoadVideo(localFilePath, remoteFilePath);
            }
        }
    }

    //本地backup中文件名转换为远程的文件名
    // [ShareFtp]jzVideo/年/月/日/浆员卡号[HH-mm-ss][HH-mm-ss].mp4
    private String generateRemoteVideoNameByBackupVideoName(String backupVideoName) {
        if (backupVideoName.length() >= 10) {
            String backupVideoDate = backupVideoName.substring(0, 10);
            String remoteVideoDate = backupVideoDate.replace("_", "/");
            backupVideoName = remoteVideoDate + backupVideoName.substring(10);
        }
        return SelfFile.getRemoteVideoNamePrefix() + "/" + backupVideoName + "/" + SelfFile.fileEx;
    }

    private void upLoadVideo(String lPath, String rPath) {
        long start = System.currentTimeMillis();

        File file = new File(lPath);
        while (true) {
            boolean b = file.exists();
            if (b) {
                Log.e(TAG, "视频文件存在");
                break;
            } else {
                if ((System.currentTimeMillis() - start) < 2 * 60 * 1000) {
                    Log.e(TAG, "视频文件不存在...");
                    mHandler.sendEmptyMessage(MSG_SCAN_DELAY);
                    return;
                }
            }
            synchronized (this) {
                try {
                    this.wait(500);
                } catch (InterruptedException e) {
                }
            }
        }

        MyLog.e(TAG, ",开始向服务器发送back中的视频文件");

        VideoServer.getInstance().setIdataPreference(new DataPreference(this));
        FtpSenderFile sender = new FtpSenderFile(VideoServer.getInstance().getIp(), VideoServer.getInstance().getPort());
        try {
            resultStr = sender.send(lPath, rPath);
        } catch (SoftFanFTPException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        } catch (Exception e) {
        } finally {
            Log.e(TAG, "传送视频 finally");
        }

        long end = System.currentTimeMillis();
        Log.e(TAG, "传送视频耗时" + (end - start));


        if ("传送成功".equals(resultStr)) {
            Log.e(TAG, resultStr);

            // success and delete the video file.
            SelfFile.delFile(lPath);
            mHandler.sendEmptyMessage(MSG_SCAN_NOW);

        } else {
            Log.e(TAG, "传送失败");
            mHandler.sendEmptyMessage(MSG_SCAN_NOW);
        }
    }
}
