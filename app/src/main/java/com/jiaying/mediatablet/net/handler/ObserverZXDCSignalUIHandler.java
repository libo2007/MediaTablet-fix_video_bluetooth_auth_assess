package com.jiaying.mediatablet.net.handler;

import android.os.Message;
import android.util.Log;


import com.jiaying.mediatablet.activity.MainActivity;
import com.jiaying.mediatablet.net.signal.RecSignal;

import java.lang.ref.SoftReference;
import java.util.Observable;

/**
 * Created by Administrator on 2015/9/13 0013.
 */
public class ObserverZXDCSignalUIHandler extends android.os.Handler implements java.util.Observer {

    private SoftReference<MainActivity> srMActivity;
    private MainActivity hmainActivity;

    public ObserverZXDCSignalUIHandler(SoftReference<MainActivity> mActivity) {
        this.srMActivity = mActivity;
        Log.e("ERROR", "在ObserverZXDCSignalUIHandler中 MainActivity = " + mActivity.get().toString());
    }




    @Override
    public void handleMessage(Message msg) {

        super.handleMessage(msg);

        switch ((RecSignal) msg.obj) {

            case TIMESTAMP:
                dealSignalTimestamp(this);
                break;

            case LOWPOWER:
                dealSignalLowPower(this);
                break;

            // The nurse make sure the info of the donor is right.
            case CONFIRM:
                dealSignalConfirm(this);
                break;


            case AUTHPASS:
                dealSignalAuthPass(this);
                break;

            case AUTHRESTIMEOUT:
                dealSignalAuthResTimeout(this);
                break;

            case SERAUTHRES:
                dealSignalSerAuthRes(this);
                break;

            case ZXDCAUTHRES:
                dealSignalZxdcAuthRes(this);
                break;

            case AUTHRESOK:
                dealSignalAuthResOK(this);
                break;

            case COMPRESSINON:

                dealSignalComprssion(this);
                break;

            // The nurse punctuate the donor.
            case PUNCTURE:

                dealSignalPuncture(this);
                break;
            // Start the collection of plasma.
            case START:

                dealSignalStart(this);
                break;

            // Start the play the video collection of plasma.
            case STARTCOLLECTIONVIDEO:

                dealSignalStartCollcetionVideo(this);
                break;

            case TOVIDEOLIST:
                dealSignalToVideo(this);
                break;

            case TOVIDEOCATEGORY:
                dealSignalToVideoCategory(this);
                break;

            case TOSURF:
                dealSignalToSurf(this);
                break;

            case TOSUGGEST:
                dealSignalToSuggest(this);
                break;


            case TOAPPOINT:
                dealSignalToAppoint(this);
                break;

            case PLASMAWEIGHT:
                dealSignalPlasmaWeight(this);
                break;

            // The pressure is not enough, recommend the donor to make a fist.
            case PIPELOW:
                dealSignalStartFist(this);
                break;

            case PIPENORMAL:
                dealSignalStopFist(this);
                break;


            case VIDEOTOMAIN:
                dealSignalVideoFinish(this);
                break;

            case BACKTOVIDEOLIST:
                dealSignalBackToVideoList(this);
                break;

            case STARTVIDEO:
                dealSignalStartVideo(this);
                break;

            // The collection is over.
            case END:

                dealSignalEnd(this);
                break;

            case CHECKSTART:
                dealSignalCheckStart(this);
                break;

            case CHECKOVER:
                dealSignalCheckOver(this);
                break;


            case RESTART:
                dealSignalReStart(this);
                break;

            case SETTINGS:
                dealSignalSettings(this);
                break;

            default:
        }
    }

    @Override
    public void update(Observable observable, Object data) {
        Message msg = Message.obtain();
        switch ((RecSignal) data) {
            case WAITING:
                msg.obj = RecSignal.WAITING;
                sendMessage(msg);
                break;

            case TIMESTAMP:
                msg.obj = RecSignal.TIMESTAMP;
                sendMessage(msg);
                break;

            case LOWPOWER:
                msg.obj = RecSignal.LOWPOWER;
                sendMessage(msg);
                break;

            case CONFIRM:
                msg.obj = RecSignal.CONFIRM;
                sendMessage(msg);
                break;

            case AUTHPASS:
                msg.obj = RecSignal.AUTHPASS;
                sendMessage(msg);
                break;

            case AUTHRESTIMEOUT:
                msg.obj = RecSignal.AUTHRESTIMEOUT;
                sendMessage(msg);
                break;

            case SERAUTHRES:
                msg.obj = RecSignal.SERAUTHRES;
                sendMessage(msg);
                break;

            case ZXDCAUTHRES:
                msg.obj = RecSignal.ZXDCAUTHRES;
                sendMessage(msg);
                break;

            case AUTHRESOK:
                msg.obj = RecSignal.AUTHRESOK;
                sendMessage(msg);
                break;


            case COMPRESSINON:
                msg.obj = RecSignal.COMPRESSINON;
                sendMessage(msg);
                break;

            case PUNCTURE:
                msg.obj = RecSignal.PUNCTURE;
                sendMessage(msg);
                break;

            case STARTPUNTUREVIDEO:
                msg.obj = RecSignal.STARTPUNTUREVIDEO;
                sendMessage(msg);
                break;

            case START:
                msg.obj = RecSignal.START;
                sendMessage(msg);
                break;

            case STARTCOLLECTIONVIDEO:
                msg.obj = RecSignal.STARTCOLLECTIONVIDEO;
                sendMessage(msg);
                break;

            case TOVIDEOLIST:
                msg.obj = RecSignal.TOVIDEOLIST;
                sendMessage(msg);
                break;

            case TOVIDEOCATEGORY:
                msg.obj = RecSignal.TOVIDEOCATEGORY;
                sendMessage(msg);
                break;

            case TOSURF:
                msg.obj = RecSignal.TOSURF;
                sendMessage(msg);
                break;

            case TOSUGGEST:
                msg.obj = RecSignal.TOSUGGEST;
                sendMessage(msg);
                break;

            case CLICKSUGGESTION:
                msg.obj = RecSignal.CLICKSUGGESTION;
                sendMessage(msg);
                break;

            case CLICKEVALUATION:
                msg.obj = RecSignal.CLICKEVALUATION;
                sendMessage(msg);
                break;

            case TOAPPOINT:
                msg.obj = RecSignal.TOAPPOINT;
                sendMessage(msg);
                break;

            case CLICKAPPOINTMENT:
                msg.obj = RecSignal.CLICKAPPOINTMENT;
                sendMessage(msg);
                break;

            case PIPELOW:
                msg.obj = RecSignal.PIPELOW;
                sendMessage(msg);
                break;

            case PIPENORMAL:
                msg.obj = RecSignal.PIPENORMAL;
                sendMessage(msg);
                break;

            case SAVEAPPOINTMENT:
                msg.obj = RecSignal.SAVEAPPOINTMENT;
                sendMessage(msg);
                break;

            case SAVESUGGESTION:
                msg.obj = RecSignal.SAVESUGGESTION;
                sendMessage(msg);
                break;

            case SAVEEVALUATION:
                msg.obj = RecSignal.SAVEEVALUATION;
                sendMessage(msg);
                break;
            case BACKTOVIDEOLIST:
                msg.obj = RecSignal.BACKTOVIDEOLIST;
                sendMessage(msg);
                break;

            case STARTVIDEO:
                msg.obj = RecSignal.STARTVIDEO;
                sendMessage(msg);
                break;

            case PLASMAWEIGHT:
                msg.obj = RecSignal.PLASMAWEIGHT;
                sendMessage(msg);
                break;

            case END:
                msg.obj = RecSignal.END;
                sendMessage(msg);
                break;

            case CHECKSTART:
                msg.obj = RecSignal.CHECKSTART;
                sendMessage(msg);
                break;

            case CHECKOVER:
                msg.obj = RecSignal.CHECKOVER;
                sendMessage(msg);
                break;

            case RESTART:
                msg.obj = RecSignal.RESTART;
                sendMessage(msg);
                break;

            case SETTINGS:
                msg.obj = RecSignal.SETTINGS;
                sendMessage(msg);
                break;


            default:
                break;
        }
    }

    private void dealSignalTimestamp(ObserverZXDCSignalUIHandler observerMainHandler) {
        MainActivity mainActivity = observerMainHandler.srMActivity.get();
        mainActivity.dealTime();
    }

    private void dealSignalConfirm(ObserverZXDCSignalUIHandler observerMainHandler) {

        MainActivity mainActivity = observerMainHandler.srMActivity.get();
        mainActivity.dealConfirm();

    }

    private void dealSignalLowPower(ObserverZXDCSignalUIHandler observerMainHandler) {
        MainActivity mainActivity = observerMainHandler.srMActivity.get();
        mainActivity.dealLowPower();
    }


    private void dealSignalAuthPass(ObserverZXDCSignalUIHandler observerMainHandler) {

        MainActivity mainActivity = observerMainHandler.srMActivity.get();
        mainActivity.dealAuthPass();
    }

    private void dealSignalAuthResTimeout(ObserverZXDCSignalUIHandler observerMainHandler) {
        MainActivity mainActivity = observerMainHandler.srMActivity.get();
        mainActivity.dealAuthResTimeout();
    }

    private void dealSignalSerAuthRes(ObserverZXDCSignalUIHandler observerMainHandler) {
        MainActivity mainActivity = observerMainHandler.srMActivity.get();
        mainActivity.dealSerAuthRes();
    }

    private void dealSignalZxdcAuthRes(ObserverZXDCSignalUIHandler observerMainHandler) {
        MainActivity mainActivity = observerMainHandler.srMActivity.get();
        mainActivity.dealZxdcAuthRes();
    }

    private void dealSignalAuthResOK(ObserverZXDCSignalUIHandler observerMainHandler) {
        MainActivity mainActivity = observerMainHandler.srMActivity.get();
        mainActivity.dealAuthResOk();
    }

    private void dealSignalComprssion(ObserverZXDCSignalUIHandler observerMainHandler) {

        MainActivity mainActivity = observerMainHandler.srMActivity.get();
        mainActivity.dealCompression();
    }

    private void dealSignalPuncture(ObserverZXDCSignalUIHandler observerMainHandler) {

        MainActivity mainActivity = observerMainHandler.srMActivity.get();
        mainActivity.dealPuncture();
    }


    private void dealSignalStart(ObserverZXDCSignalUIHandler observerMainHandler) {

        MainActivity mainActivity = observerMainHandler.srMActivity.get();
        mainActivity.dealStart();
    }

    private void dealSignalStartCollcetionVideo(ObserverZXDCSignalUIHandler observerMainHandler) {

        MainActivity mainActivity = observerMainHandler.srMActivity.get();
        mainActivity.dealStartCollcetionVideo("/sdcard/kindness.mp4");
    }

    private void dealSignalToVideo(ObserverZXDCSignalUIHandler observerMainHandler) {

        MainActivity mainActivity = observerMainHandler.srMActivity.get();
        mainActivity.dealToVideoList();
    }

    private void dealSignalToVideoCategory(ObserverZXDCSignalUIHandler observerMainHandler) {
        MainActivity mainActivity = observerMainHandler.srMActivity.get();
        mainActivity.dealToVideoCategory();
    }
    private void dealSignalToSurf(ObserverZXDCSignalUIHandler observerMainHandler) {

        MainActivity mainActivity = observerMainHandler.srMActivity.get();
        mainActivity.dealToSurf();
    }

    private void dealSignalToSuggest(ObserverZXDCSignalUIHandler observerMainHandler) {

        MainActivity mainActivity = observerMainHandler.srMActivity.get();
        mainActivity.dealToAdvice();
    }

    private void dealSignalToAppoint(ObserverZXDCSignalUIHandler observerMainHandler) {

        MainActivity mainActivity = observerMainHandler.srMActivity.get();
        mainActivity.dealToAppointment();
    }


    private void dealSignalStartFist(ObserverZXDCSignalUIHandler observerMainHandler) {

        MainActivity mainActivity = observerMainHandler.srMActivity.get();
        mainActivity.dealStartFist();
    }

    private void dealSignalStopFist(ObserverZXDCSignalUIHandler observerMainHandler) {

        MainActivity mainActivity = observerMainHandler.srMActivity.get();
        mainActivity.dealStopFist();
    }

    private void dealSignalVideoFinish(ObserverZXDCSignalUIHandler observerMainHandler) {
        MainActivity mainActivity = observerMainHandler.srMActivity.get();
        mainActivity.dealBackToVideoList();
    }

    private void dealSignalBackToVideoList(ObserverZXDCSignalUIHandler observerMainHandler) {
        MainActivity mainActivity = observerMainHandler.srMActivity.get();
        mainActivity.dealBackToVideoList();
    }

    private void dealSignalStartVideo(ObserverZXDCSignalUIHandler observerMainHandler) {
        MainActivity mainActivity = observerMainHandler.srMActivity.get();
        mainActivity.dealStartVideo();
    }

    private void dealSignalEnd(ObserverZXDCSignalUIHandler observerMainHandler) {

        MainActivity mainActivity = observerMainHandler.srMActivity.get();
        mainActivity.dealEnd();
    }

    private void dealSignalCheckStart(ObserverZXDCSignalUIHandler observerMainHandler) {
        MainActivity mainActivity = observerMainHandler.srMActivity.get();
        mainActivity.dealCheckStart();
    }

    private void dealSignalCheckOver(ObserverZXDCSignalUIHandler observerMainHandler) {

        MainActivity mainActivity = observerMainHandler.srMActivity.get();
        mainActivity.dealCheckOver();
    }


    private void dealSignalReStart(ObserverZXDCSignalUIHandler observerMainHandler) {
        MainActivity mainActivity = observerMainHandler.srMActivity.get();
        mainActivity.dealReStart();
    }

    private void dealSignalSettings(ObserverZXDCSignalUIHandler observerMainHandler) {
        MainActivity mainActivity = observerMainHandler.srMActivity.get();
        mainActivity.dealSettings();
    }

    private void dealSignalPlasmaWeight(ObserverZXDCSignalUIHandler observerMainHandler) {
        MainActivity mainActivity = observerMainHandler.srMActivity.get();
        mainActivity.dealPlasmaWeight();
    }

}


