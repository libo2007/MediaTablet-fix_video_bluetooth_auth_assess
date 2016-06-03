package com.jiaying.mediatablet.net.state.stateswitch;

import android.softfan.dataCenter.DataCenterRun;
import android.softfan.dataCenter.task.DataCenterTaskCmd;
import android.softfan.util.textUnit;

import com.jiaying.mediatablet.entity.ServerTime;
import com.jiaying.mediatablet.net.signal.RecSignal;
import com.jiaying.mediatablet.net.state.RecoverState.RecordState;
import com.jiaying.mediatablet.net.thread.ObservableZXDCSignalListenerThread;

/**
 * Created by hipil on 2016/4/16.
 */
//
public class WaitingForCheckOverState extends AbstractState {
    private static WaitingForCheckOverState waitingForCheckOverState = null;

    private WaitingForCheckOverState() {
    }

    public static WaitingForCheckOverState getInstance() {
        if (waitingForCheckOverState == null) {
            waitingForCheckOverState = new WaitingForCheckOverState();
        }
        return waitingForCheckOverState;
    }

    @Override
    void handleMessage(RecordState recordState, ObservableZXDCSignalListenerThread listenerThread, DataCenterRun dataCenterRun,
                       DataCenterTaskCmd cmd, RecSignal recSignal,TabletStateContext tabletStateContext) {
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

            case CHECKOVER:
                //记录状态
                recordState.recCheckOver();

                //获取数据

                //切换状态
                tabletStateContext.setCurrentState(WaitingForDonorState.getInstance());

                //发送信号
                listenerThread.notifyObservers(RecSignal.CHECKOVER);
                break;

            case SETTINGS:

                //记录状态

                //获取数据

                //切换状态

                tabletStateContext.setCurrentState(SettingState.getInstance());


                //发送信号
                listenerThread.notifyObservers(RecSignal.SETTINGS);
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
