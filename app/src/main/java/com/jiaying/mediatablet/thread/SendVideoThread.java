package com.jiaying.mediatablet.thread;

import java.io.File;
import java.io.IOException;

import android.util.Log;

import com.jiaying.mediatablet.activity.MainActivity;
import com.jiaying.mediatablet.net.serveraddress.VideoServer;
import com.jiaying.mediatablet.net.softfan.FtpSenderFile;
import com.jiaying.mediatablet.net.softfan.SoftFanFTPException;
import com.jiaying.mediatablet.utils.SelfFile;


public class SendVideoThread extends Thread {

    String lPath, rPath;
    String resultStr;

    public SendVideoThread(String localPath, String remotePath) {
        this.lPath = localPath;
        this.rPath = remotePath;


    }

    @Override
    public void run() {
        super.run();
        long start = System.currentTimeMillis();

        File file = new File(this.lPath);
        while (true) {
            boolean b = file.exists();
            if (b) {
                Log.e("ERROR", "视频文件存在");
                break;
            } else {
                if ((System.currentTimeMillis() - start) < 2 * 60 * 1000) {
                    Log.e("ERROR", "视频文件不存在");
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


        FtpSenderFile sender = new FtpSenderFile(VideoServer.getInstance().getIp(), VideoServer.getInstance().getPort());

        try {

            resultStr = sender.send(lPath, rPath);


        } catch (SoftFanFTPException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        } catch (Exception e) {
        }

        long end = System.currentTimeMillis();
        Log.e("ERROR", "传送视频耗时" + (end - start));


        if ("传送成功".equals(resultStr)) {
            Log.e("ERROR", resultStr);

            // success and delete the video file.
            SelfFile.delFile(lPath);
        } else {
            Log.e("ERROR", "传送失败");
            // save the video if failure.
            File srcFile = SelfFile.createNewFile(SelfFile.generateLocalVideoName());
            File destFile = SelfFile.createNewFile(SelfFile.generateLocalBackupVideoName());
            try {
                SelfFile.copyFile(srcFile, destFile);
                SelfFile.delFile(lPath);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
            }
        }
    }

}

