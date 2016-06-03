package com.jiaying.mediatablet.net.signal;

/**
 * Created by hipil on 2016/4/2.
 */
public enum RecSignal {

    // The signals received from the plasma.

// 服务器来的时间戳信号
    TIMESTAMP,

    //服务器推送了浆员信息过来
    CONFIRM,

    //人脸识别长时间不通过，录制献浆员视频
    RECORDDONORVIDEO,

    //人脸识别长时间不通过，录制护士视频
    RECORDNURSEVIDEO,

    //人脸识别长时间不通过，录制视频完成
    RECORDOVER,

    //人脸识别通过
    AUTHPASS,

    //人脸识别通过后，向服务器发送认证通过信号后，得到了服务器应答
    SERAUTHRES,

    //人脸识别通过后，向服务器发送认证通过信号后，得到了ZXDC应答
    ZXDCAUTHRES,

    //人脸识别通过后，向服务器发送认证通过信号后，得到了全部应答
    AUTHRESOK,

    //人脸识别通过后，向服务器发送认证通过信号后，得到应答超时
    AUTHRESTIMEOUT,

    //得到应答超时后，重发人脸识别通过信号
    REAUTHPASS,

    //得到应答超时后，取消发送人脸识别通过信号
    CANCLEAUTHPASS,

    //得到服务器传送来的，机器开始加压信号
    COMPRESSINON,

    //得到服务器传送来的，机器开始穿刺信号
    PUNCTURE,

    //得到服务器传来的，机器开始采集信号
    START,


    AUTOTRANFUSIONSTART,
    AUTOTRANFUSIONEND,
    PLASMAWEIGHT,
    PIPELOW,
    PIPENORMAL,
    PAUSED,
    END,

    //
    LOWPOWER,
    CHECKSTART,
    CHECKOVER,
    AVAILABLERES,
    WAITING,
    STARTPUNTUREVIDEO,
    STARTCOLLECTIONVIDEO,


    //
    SETTINGS,
    RESTART,


    // 这个是视频列表
    TOVIDEOLIST,
    //视频分类列表
    TOVIDEOCATEGORY,

    // Switch between the tabs

    TOSURF,
    TOSUGGEST,
    TOAPPOINT,

    //between activity and fragment
    VIDEOTOMAIN,
    CLICKSUGGESTION,
    CLICKEVALUATION,
    CLICKAPPOINTMENT,
    SAVEAPPOINTMENT,
    SAVESUGGESTION,
    SAVEEVALUATION,
    AUTH,
    STARTVIDEO,

    //back button
    BACKTOVIDEOLIST,
    BACKTOADVICE,
    BACKTOAPPOINTMENT,

    //
    NOTHING,

    //The three physical keys
    POWEROFF,
    RECENT,
    HOME

}
