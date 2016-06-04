package com.jiaying.mediatablet.constants;


import android.os.Environment;

import java.io.File;


/**
 * 作者：lenovo on 2016/4/3 14:00
 * 邮箱：353510746@qq.com
 * 功能：
 */
public class Constants {

    //讯飞语音id
    public static final String FLYTEK_APP_ID = "5700a6bb";
    //公益视频的路径
//    public static final String VIDEO_PATH_GONGYI = Environment.getExternalStorageDirectory().getPath() + File.separator + "2016";
    public static final String VIDEO_PATH_GONGYI = Environment.getExternalStorageDirectory().getPath() + File.separator + "jiaying/publicwelfare";
    //献浆知识的路径
    public static final String VIDEO_PATH_XIANJIANG = Environment.getExternalStorageDirectory().getPath() + File.separator + "jiaying/donation";
    //娱乐视频的路径
    public static final String VIDEO_PATH_YULE = Environment.getExternalStorageDirectory().getPath() + File.separator + "jiaying/entertainment";
    //视频备份的目录
    public static final String VIDEO_PATH_BACKUP = Environment.getExternalStorageDirectory().getPath() + File.separator + "backup";

}
