package com.jiaying.mediatablet.net.state.stateswitch;

import android.softfan.dataCenter.DataCenterClientService;
import android.softfan.dataCenter.DataCenterRun;
import android.softfan.dataCenter.task.DataCenterTaskCmd;
import android.softfan.util.textUnit;

import com.jiaying.mediatablet.entity.DeviceEntity;
import com.jiaying.mediatablet.entity.DonorEntity;
import com.jiaying.mediatablet.entity.ServerTime;
import com.jiaying.mediatablet.net.signal.RecSignal;
import com.jiaying.mediatablet.net.state.RecoverState.RecordState;
import com.jiaying.mediatablet.net.thread.ObservableZXDCSignalListenerThread;

import java.util.HashMap;

/**
 * Created by hipil on 2016/5/11.
 */
public class AuthPassTimeoutState extends AbstractState {
    private static AuthPassTimeoutState ourInstance = new AuthPassTimeoutState();

    public static AuthPassTimeoutState getInstance() {
        return ourInstance;
    }

    private AuthPassTimeoutState() {
    }

    @Override
    void handleMessage(RecordState recordState, ObservableZXDCSignalListenerThread listenerThread,
                       DataCenterRun dataCenterRun, DataCenterTaskCmd cmd, RecSignal recSignal, TabletStateContext tabletStateContext) {
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

            case CANCLEAUTHPASS:

                //记录状态
                recordState.recCheckOver();

                //获取数据

                //切换状态
                tabletStateContext.setCurrentState(WaitingForDonorState.getInstance());

                //发送信号
                listenerThread.notifyObservers(RecSignal.CHECKOVER);
                break;

            case REAUTHPASS:
                //记录状态

                //获取数据

                //切换状态
                tabletStateContext.setCurrentState(WaitingForSerZxdcResState.getInstance());

                //发送信号
                sendAuthPassCmd();
                listenerThread.notifyObservers(RecSignal.AUTHPASS);

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

    private void sendAuthPassCmd() {
        DataCenterClientService clientService = ObservableZXDCSignalListenerThread.getClientService();
        DataCenterTaskCmd retcmd = new DataCenterTaskCmd();
        retcmd.setCmd("authentication_donor");
        retcmd.setHasResponse(true);
        retcmd.setLevel(2);
        HashMap<String, Object> values = new HashMap<>();
        values.put("donorId", DonorEntity.getInstance().getIdentityCard().getId());
        values.put("deviceId", DeviceEntity.getInstance().getAp());
        retcmd.setValues(values);
        clientService.getApDataCenter().addSendCmd(retcmd);
    }
}
