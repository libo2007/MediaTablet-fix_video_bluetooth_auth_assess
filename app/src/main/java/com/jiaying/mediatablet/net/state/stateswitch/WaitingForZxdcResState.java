package com.jiaying.mediatablet.net.state.stateswitch;

import android.softfan.dataCenter.DataCenterRun;
import android.softfan.dataCenter.task.DataCenterTaskCmd;
import android.softfan.util.textUnit;

import com.jiaying.mediatablet.entity.ServerTime;
import com.jiaying.mediatablet.net.signal.RecSignal;
import com.jiaying.mediatablet.net.state.RecoverState.RecordState;
import com.jiaying.mediatablet.net.thread.ObservableZXDCSignalListenerThread;

/**
 * Created by hipil on 2016/5/11.
 */
public class WaitingForZxdcResState extends AbstractState {
    private static WaitingForZxdcResState ourInstance = new WaitingForZxdcResState();

    public static WaitingForZxdcResState getInstance() {
        return ourInstance;
    }

    private WaitingForZxdcResState() {
    }

    @Override
    void handleMessage(RecordState recordState, ObservableZXDCSignalListenerThread listenerThread, DataCenterRun dataCenterRun,
                       DataCenterTaskCmd cmd, RecSignal recSignal, TabletStateContext tabletStateContext) {
        switch (recSignal) {
            //记录状态

            //获取数据

            //切换状态

            //发送信号
            case TIMESTAMP:
                //记录状态

                //获取数据
                if ("timestamp".equals(cmd.getCmd())) {
                    ServerTime.curtime = Long.parseLong(textUnit.ObjToString(cmd.getValue("t")));
                }
                //切换状态

                //发送信号
                listenerThread.notifyObservers(RecSignal.TIMESTAMP);
                break;

            case ZXDCAUTHRES:
                //记录状态
                recordState.recAuth();
                //获取数据

                //切换状态
                tabletStateContext.setCurrentState(WaitingForCompressionState.getInstance());

                //发送信号
                listenerThread.notifyObservers(RecSignal.AUTHRESOK);
                break;

            case AUTHRESTIMEOUT:
                //记录状态

                //获取数据

                //切换状态
                tabletStateContext.setCurrentState(AuthPassTimeoutState.getInstance());

                //发送信号
                listenerThread.notifyObservers(RecSignal.AUTHRESTIMEOUT);
                break;

            case RESTART:
                //记录状态

                //获取数据

                //切换状态

                //发送信号
                listenerThread.notifyObservers(RecSignal.RESTART);
                break;


        }
    }
}
