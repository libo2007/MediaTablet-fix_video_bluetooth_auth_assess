package com.jiaying.mediatablet.net.state.stateswitch;

import android.softfan.dataCenter.DataCenterRun;
import android.softfan.dataCenter.task.DataCenterTaskCmd;
import android.softfan.util.textUnit;
import android.util.Log;

import com.jiaying.mediatablet.entity.PlasmaWeightEntity;
import com.jiaying.mediatablet.entity.ServerTime;
import com.jiaying.mediatablet.net.signal.RecSignal;
import com.jiaying.mediatablet.net.state.RecoverState.RecordState;
import com.jiaying.mediatablet.net.thread.ObservableZXDCSignalListenerThread;

import java.util.Random;

/**
 * Created by hipil on 2016/4/13.
 */
public class CollectionState extends AbstractState {
    private static CollectionState collectionState = null;

    private CollectionState() {
    }

    public static CollectionState getInstance() {
        if (collectionState == null) {
            collectionState = new CollectionState();
        }
        return collectionState;
    }

    @Override
    public synchronized void handleMessage(RecordState recordState, ObservableZXDCSignalListenerThread listenerThread,
                                           DataCenterRun dataCenterRun, DataCenterTaskCmd cmd, RecSignal recSignal,TabletStateContext tabletStateContext) {
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

            case STARTCOLLECTIONVIDEO:

                //记录状态

                //获取数据

                //切换状态

                //发送信号
                listenerThread.notifyObservers(RecSignal.STARTCOLLECTIONVIDEO);
                break;

            case PIPELOW:

                //记录状态

                //获取数据

                //切换状态

                //发送信号
                listenerThread.notifyObservers(RecSignal.PIPELOW);
                break;

            case PIPENORMAL:

                //记录状态

                //获取数据

                //切换状态

                //发送信号
                listenerThread.notifyObservers(RecSignal.PIPENORMAL);
                break;

            case TOVIDEOLIST:

                //记录状态

                //获取数据

                //切换状态

                //发送信号
                listenerThread.notifyObservers(RecSignal.TOVIDEOLIST);
                break;

            case TOVIDEOCATEGORY:

                //记录状态

                //获取数据

                //切换状态

                //发送信号
                listenerThread.notifyObservers(RecSignal.TOVIDEOCATEGORY);
                break;

            case TOSURF:

                //记录状态

                //获取数据

                //切换状态

                //发送信号
                listenerThread.notifyObservers(RecSignal.TOSURF);
                break;

            case TOSUGGEST:

                //记录状态

                //获取数据

                //切换状态

                //发送信号
                listenerThread.notifyObservers(RecSignal.TOSUGGEST);
                break;

            case TOAPPOINT:

                //记录状态

                //获取数据

                //切换状态

                //发送信号
                listenerThread.notifyObservers(RecSignal.TOAPPOINT);
                break;

            case CLICKAPPOINTMENT:

                //记录状态

                //获取数据

                //切换状态

                //发送信号
                listenerThread.notifyObservers(RecSignal.CLICKAPPOINTMENT);
                break;

            case CLICKSUGGESTION:

                //记录状态

                //获取数据

                //切换状态

                //发送信号
                listenerThread.notifyObservers(RecSignal.CLICKSUGGESTION);
                break;

            case CLICKEVALUATION:

                //记录状态

                //获取数据

                //切换状态

                //发送信号
                listenerThread.notifyObservers(RecSignal.CLICKEVALUATION);
                break;

            case SAVEAPPOINTMENT:

                //记录状态

                //获取数据

                //切换状态

                //发送信号
                listenerThread.notifyObservers(RecSignal.SAVEAPPOINTMENT);
                break;

            case SAVESUGGESTION:

                //记录状态

                //获取数据

                //切换状态

                //发送信号
                listenerThread.notifyObservers(RecSignal.SAVESUGGESTION);
                break;

            case SAVEEVALUATION:

                //记录状态

                //获取数据

                //切换状态

                //发送信号
                listenerThread.notifyObservers(RecSignal.SAVEEVALUATION);
                break;
            case VIDEOTOMAIN:

                //记录状态

                //获取数据

                //切换状态

                //发送信号
                listenerThread.notifyObservers(RecSignal.VIDEOTOMAIN);
                break;

            case BACKTOVIDEOLIST:
                listenerThread.notifyObservers(RecSignal.BACKTOVIDEOLIST);
                break;

            case STARTVIDEO:

                //记录状态

                //获取数据

                //切换状态

                //发送信号
                listenerThread.notifyObservers(RecSignal.STARTVIDEO);
                break;

            case AUTOTRANFUSIONSTART:

                //记录状态

                //获取数据

                //切换状态

                //发送信号
                listenerThread.notifyObservers(RecSignal.AUTOTRANFUSIONSTART);

                break;
            case AUTOTRANFUSIONEND:

                //记录状态

                //获取数据

                //切换状态

                //发送信号
                listenerThread.notifyObservers(RecSignal.AUTOTRANFUSIONEND);
                Log.e("error", "还输结束");
                break;
            case PLASMAWEIGHT:

                //记录状态

                //获取数据

                //切换状态

                //发送信号
                if (cmd != null) {
                    PlasmaWeightEntity.getInstance().setCurWeight(Integer.parseInt(textUnit.ObjToString(cmd.getValue("current_weight"))));

                    PlasmaWeightEntity.getInstance().setSettingWeight(600);

                } else {
                    PlasmaWeightEntity.getInstance().setCurWeight(new Random().nextInt(600));
                    PlasmaWeightEntity.getInstance().setSettingWeight(600);
                }
                listenerThread.notifyObservers(RecSignal.PLASMAWEIGHT);

                break;

            case END:

                //记录状态
                recordState.recEnd();

                //获取数据

                //切换状态
                tabletStateContext.setCurrentState(EndState.getInstance());

                //发送信号
                listenerThread.notifyObservers(RecSignal.END);

                break;

            case RESTART:
                //记录状态

                //获取数据

                //切换状态

                //发送信号
                listenerThread.notifyObservers(recSignal);

                break;
        }
    }


}
