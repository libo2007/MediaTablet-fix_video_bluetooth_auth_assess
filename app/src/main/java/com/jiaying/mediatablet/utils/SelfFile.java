package com.jiaying.mediatablet.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;


import java.util.ArrayList;
import java.util.List;

import java.util.TimeZone;


import android.graphics.Bitmap;

import android.text.TextUtils;

import com.jiaying.mediatablet.entity.DonorEntity;
import com.jiaying.mediatablet.entity.VideoEntity;

/**
 * Created by Administrator on 2015/9/27 0027.
 */
public class SelfFile {

    private static final String TAG = "SelfFile";
    private static Integer videoScanAndCopyLocked = new Integer(1);


    public static void createDir(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public static void delDir(String path) {
        File dir = new File(path);
        if (dir.exists()) {
            File[] tmp = dir.listFiles();
            for (int i = 0; i < tmp.length; i++) {
                if (tmp[i].isDirectory()) {
                    delDir(path + "/" + tmp[i].getName());
                } else {
                    tmp[i].delete();
                }
            }
            dir.delete();
        }
    }

    public static void delFile(String filename) {
        File file = new File(filename);
        if (file.exists() && file.isFile())
            file.delete();
    }

    public static File createNewFile(String fileDirectoryAndName) {
        File myFile = null;
        try {
            String fileName = fileDirectoryAndName;
            //创建File对象，参数为String类型，表示目录名
            myFile = new File(fileName);
            //判断文件是否存在，如果不存在则调用createNewFile()方法创建新目录，否则跳至异常处理代码
            if (!myFile.exists())
                myFile.createNewFile();
            else  //如果不存在则扔出异常
                throw new Exception("The new file already exists!");

        } catch (Exception ex) {
            System.out.println("无法创建新文件！");

        }
        return myFile;
    }

    public static void copyFile(File src, File dest) throws Exception {

        synchronized (videoScanAndCopyLocked) {
            FileInputStream input = null;
            FileOutputStream outstrem = null;
            try {
                input = new FileInputStream(src);
                outstrem = new FileOutputStream(dest);
                outstrem.getChannel().transferFrom(input.getChannel(), 0, input.available());
            } catch (Exception e) {
                throw e;
            } finally {
                outstrem.flush();
                outstrem.close();
                input.close();
            }
        }
    }

    public static String fileEx = ".mp4";


    public static String generateRemoteVideoName() {

        // [ShareFtp]jzVideo/年/月/日/浆员卡号[HH-mm-ss][HH-mm-ss].mp4

        SimpleDateFormat dfs1 = new SimpleDateFormat("/yyyy/MM/dd/" + DonorEntity.getInstance().getIdentityCard().getId() + "[HH-mm-ss]");
        SimpleDateFormat dfs2 = new SimpleDateFormat("[HH-mm-ss]");

        dfs1.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
        dfs2.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));


        return getRemoteVideoNamePrefix() + dfs1.format(TimeRecord.getInstance().getStartDate()) + dfs2.format(TimeRecord.getInstance().getEndDate()) + fileEx;
    }

    public static String getRemoteVideoNamePrefix() {
        return "[ShareFtp]jzVideo";
    }

    public static String generateLocalBackupVideoName() {
        SimpleDateFormat dfs1 = new SimpleDateFormat("yyyy_MM_dd_" + DonorEntity.getInstance().getIdentityCard().getId() + "[HH-mm-ss]");
        SimpleDateFormat dfs2 = new SimpleDateFormat("[HH-mm-ss]");

        dfs1.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
        dfs2.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));


        return generateLocalBackupVideoDIR() + dfs1.format(TimeRecord.getInstance().getStartDate()) + dfs2.format(TimeRecord.getInstance().getEndDate()) + fileEx;
    }

    public static String generateLocalBackupVideoDIR() {
        return "/sdcard/backup/";
    }

    public static String generateLocalVideoName() {
        // /sdcard/年/月/日/浆员卡号[HH-mm-ss][HH-mm-ss].mp4
        SimpleDateFormat dfs2 = new SimpleDateFormat("[HH-mm-ss]");

        dfs2.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
        String folder = generateLocalVideoDIR();
        File tempFile = new File(folder);
        if (!tempFile.exists()) {
            tempFile.mkdirs();
        }

        return folder + DonorEntity.getInstance().getIdentityCard().getId() + dfs2.format(TimeRecord.getInstance().getStartDate()) + fileEx;
    }

    public static String generateLocalVideoDIR() {
        SimpleDateFormat dfs1 = new SimpleDateFormat("yyyy/MM/dd/");

        dfs1.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
        return "/sdcard/" + dfs1.format(TimeRecord.getInstance().getStartDate());
    }

    public static void saveBitmapToFile(Bitmap bitmap, String _file)
            throws IOException {
        BufferedOutputStream os = null;
        try {
            File file = new File(_file);
            // String _filePath_file.replace(File.separatorChar +
            // file.getName(), "");
            int end = _file.lastIndexOf(File.separator);
            String _filePath = _file.substring(0, end);
            File filePath = new File(_filePath);
            if (!filePath.exists()) {
                filePath.mkdirs();
            }
            file.createNewFile();
            os = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                }
            }
        }
    }

    public static List<VideoEntity> getLocalVideoList(String path) {
        MyLog.e(TAG, "scan path:" + path);
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        List<VideoEntity> fileList = new ArrayList<>();
        synchronized (videoScanAndCopyLocked) {
            getFileList(new File(path), fileList);
        }
        return fileList;
    }

    /**
     * @param path
     * @param fileList 注意的是并不是所有的文件夹都可以进行读取的，权限问题
     */
    private static void getFileList(File path, List<VideoEntity> fileList) {
        //如果是文件夹的话
        if (path.isDirectory()) {
            //返回文件夹中有的数据
            File[] files = path.listFiles();
            //先判断下有没有权限，如果没有权限的话，就不执行了
            if (null == files)
                return;

            for (int i = 0; i < files.length; i++) {
                getFileList(files[i], fileList);
            }
        }
        //如果是文件的话直接加入
        else {
            if (!TextUtils.isEmpty(path.getName())) {
                if (path.getName().endsWith("3gp") || path.getName().endsWith("mp4")
                        || path.getName().endsWith("avi") || path.getName().endsWith("wmv")) {
                    if (!TextUtils.isEmpty(path.getAbsolutePath())) {
                        MyLog.e(TAG, "path:" + path.getAbsolutePath());
                        //文件名
                        String filePath = path.getAbsolutePath();
                        String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
                        VideoEntity videoEntity = new VideoEntity();
                        videoEntity.setPlay_url(path.getAbsolutePath());
                        videoEntity.setName(fileName.substring(0, fileName.length() - 4));
                        fileList.add(videoEntity);
                    }
                }
            }
        }
    }
}

