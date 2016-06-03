package com.jiaying.mediatablet.activity;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.KeyguardManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jiaying.mediatablet.R;


import com.jiaying.mediatablet.constants.IntentAction;
import com.jiaying.mediatablet.constants.IntentExtra;

import com.jiaying.mediatablet.db.DataPreference;

import com.jiaying.mediatablet.entity.CurrentDate;
import com.jiaying.mediatablet.entity.DeviceEntity;
import com.jiaying.mediatablet.entity.DonorEntity;
import com.jiaying.mediatablet.entity.PlasmaWeightEntity;
import com.jiaying.mediatablet.entity.ServerTime;

import com.jiaying.mediatablet.entity.VideoPathEntity;
import com.jiaying.mediatablet.fragment.ServerSettingFragment;
import com.jiaying.mediatablet.fragment.authentication.AuthFragment;
import com.jiaying.mediatablet.fragment.authentication.AuthPreviewFragment;

import com.jiaying.mediatablet.fragment.BlankFragment;
import com.jiaying.mediatablet.fragment.collection.CollectionPreviewFragment;
import com.jiaying.mediatablet.fragment.check.CheckFragment;

import com.jiaying.mediatablet.fragment.collection.PlayVideoFragment;
import com.jiaying.mediatablet.fragment.collection.VideoCategorizeFragment;

import com.jiaying.mediatablet.fragment.collection.VideoListFragment;
import com.jiaying.mediatablet.fragment.end.EndFragment;
import com.jiaying.mediatablet.fragment.authentication.WaitingForDonorFragment;
import com.jiaying.mediatablet.net.handler.ObserverZXDCSignalUIHandler;

import com.jiaying.mediatablet.net.serveraddress.LogServer;
import com.jiaying.mediatablet.net.serveraddress.SignalServer;
import com.jiaying.mediatablet.net.serveraddress.VideoServer;
import com.jiaying.mediatablet.net.signal.RecSignal;
import com.jiaying.mediatablet.net.state.RecoverState.StateIndex;
import com.jiaying.mediatablet.net.state.stateswitch.TabletStateContext;
import com.jiaying.mediatablet.net.state.stateswitch.WaitingForCheckOverState;
import com.jiaying.mediatablet.net.state.stateswitch.WaitingForTimestampState;
import com.jiaying.mediatablet.net.thread.ObservableZXDCSignalListenerThread;
import com.jiaying.mediatablet.net.state.RecoverState.RecordState;

import com.jiaying.mediatablet.service.ScanBackupVideoService;

import com.jiaying.mediatablet.service.TimeService;

import com.jiaying.mediatablet.thread.AniThread;
import com.jiaying.mediatablet.fragment.AdviceFragment;
import com.jiaying.mediatablet.fragment.AppointmentFragment;
import com.jiaying.mediatablet.fragment.collection.CollectionFragment;
import com.jiaying.mediatablet.fragment.collection.JCPlayVideoFragment;
import com.jiaying.mediatablet.fragment.pression.PressingFragment;
import com.jiaying.mediatablet.fragment.collection.SurfInternetFragment;
import com.jiaying.mediatablet.fragment.authentication.WelcomeFragment;

import com.jiaying.mediatablet.utils.AppInfoUtils;

import com.jiaying.mediatablet.utils.BrightnessTools;

import com.jiaying.mediatablet.utils.MyLog;

import com.jiaying.mediatablet.utils.WifiAdmin;
import com.jiaying.mediatablet.widget.HorizontalProgressBar;
import com.jiaying.mediatablet.widget.VerticalProgressBar;


import java.lang.ref.SoftReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


/**
 * 主界面
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {


    private RecordState recordState;
    private FragmentManager fragmentManager;
    private AniThread startFist;
    private View title_bar_view;//标题栏
    private RadioGroup mGroup;
    private ImageView overflow_image;//弹出功能
    private PopupWindow mPopupWindow;
    private View mPopView;
    private View mParentView;
    private ImageView ivStartFistHint;
    private ImageView ivLogoAndBack;
    private TextView fun_txt;//功能设置
    private TextView server_txt;//参数设置
    private TextView restart_txt;//软件重启
    private TextView net_state_txt;//网络链接状态
    private TextView wifi_not_txt;
    private TextView title_txt;//标题
    private VerticalProgressBar battery_pb;//剩余电量
    private View left_hint_view;//采浆过程状态显示
    private View call_view;//呼叫
    private TextView battery_not_connect_txt;//电源未连接提示
    private ProgressDialog mDialog = null;
    private TextView time_txt;//当前时间
    private HorizontalProgressBar collect_pb;//采集进度
    private View dlg_call_service_view;//电话服务view
    private CollectionPreviewFragment collectionPreviewFragment;
    private LinearLayout ll_cl;

    private PowerManager.WakeLock mWakelock;
    private KeyguardManager km;
    private PowerManager pm;

    //wifi自动连接begin
    private WifiAdmin wifiAdmin = null;
    private static final String SSID = "JiaYing_ZXDC";
    private static final String PWD = "jyzxdcarm";
    private static final int TYPE = 3;
    //wifi自动连接end

    //告警电量值

    private static final int WARNING_BATTERY_VALUE = 5;
    //电量检查是否通过
    private boolean batteryIsOk = false;


    private DeviceEntity deviceEntity;

    private TabletStateContext tabletStateContext;

    private ProgressDialog allocDevDialog = null;
    private AlertDialog failAllocDialog = null;

    private ObserverZXDCSignalUIHandler observerZXDCSignalUIHandler;
    private ObservableZXDCSignalListenerThread observableZXDCSignalListenerThread = null;

    private Integer onSaveInstanceState = new Integer(1);
    private Boolean onSaveInstanceStateBoolean = true;
    //避免fragment的相关操作在onSaveInstanceState之后执行。
    //todo还未在所有需要互斥的函数里添加

    public TabletStateContext getTabletStateContext() {
        return this.tabletStateContext;
    }

    public ObservableZXDCSignalListenerThread getObservableZXDCSignalListenerThread() {
        return observableZXDCSignalListenerThread;
    }

    public RecordState getRecordState() {
        return recordState;
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {

            //判断电量
            String action = intent.getAction();
            if (Intent.ACTION_BATTERY_CHANGED.equals(action)) {
                int batteryLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);

                //获取最大电量，如未获取到具体数值，则默认为100
                int batteryScale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 100);

                //显示电量
                battery_pb.setMax(batteryScale);
                battery_pb.setProgress(batteryLevel);

                int status = intent.getIntExtra("status", BatteryManager.BATTERY_STATUS_UNKNOWN);


                //如果没有充电的状态下，判断电量是否充足
                String state = recordState.getState();
                boolean isCheckBattery = false;
                if (TextUtils.isEmpty(state)) {
                    isCheckBattery = true;
                } else {
                    if (state.equals(StateIndex.WAITINGFORDONOR) || state.equals(StateIndex.WAITINGFORCHECKOVER)) {
                        isCheckBattery = true;
                    }
                }
                MyLog.e("ERROR", "recordState " + state + ",isCheckBattery " + isCheckBattery);
                if (isCheckBattery == true && batteryLevel <= WARNING_BATTERY_VALUE) {
                    MyLog.e("ERROR", "正在检查状态");
                    battery_not_connect_txt.setVisibility(View.VISIBLE);
                    battery_not_connect_txt.setText(getString(R.string.battery_low));
                    batteryIsOk = false;
                    tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.LOWPOWER);
                } else if (isCheckBattery == true && batteryLevel > WARNING_BATTERY_VALUE) {
                    battery_not_connect_txt.setVisibility(View.GONE);
                    batteryIsOk = true;
                } else if (isCheckBattery != true && batteryLevel <= WARNING_BATTERY_VALUE) {
                    batteryIsOk = false;
                } else if (isCheckBattery == true && batteryLevel > WARNING_BATTERY_VALUE) {
                    batteryIsOk = true;
                }


            } else if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo mobNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                NetworkInfo wifiNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//                if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
//                    net_state_txt.setVisibility(View.VISIBLE);
//                    wifi_not_txt.setVisibility(View.VISIBLE);
//                } else {
                net_state_txt.setVisibility(View.GONE);
                wifi_not_txt.setVisibility(View.GONE);
//                }
            } else if (IntentAction.ACTION_UPDATE_TIME.equals(action)) {


                long sysTime = intent.getLongExtra(IntentExtra.EXTRA_TIME, System.currentTimeMillis());

                time_txt.setText(timeStamp2Date(sysTime + "")); //更新时间

            } else if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())) {
                //wifi状态改变

//                int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
//                if (wifiState == WifiManager.WIFI_STATE_DISABLING) {
//                    MyLog.e("ERROR", "wifi正在关闭");
//                } else if (wifiState == WifiManager.WIFI_STATE_ENABLING) {
//                    MyLog.e("ERROR", "wifi正在打开");
//                } else if (wifiState == WifiManager.WIFI_STATE_DISABLED) {
//                    MyLog.e("ERROR", "wifi已经关闭");
//                    autoWifiConnect();
//                } else if (wifiState == WifiManager.WIFI_STATE_ENABLED) {
//                    MyLog.e("ERROR", "wifi已经打开");
//                } else {
//                    MyLog.e("ERROR", "wifi未知状态");
//                }

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);

        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(IntentAction.ACTION_UPDATE_TIME);
        registerReceiver(receiver, filter);

//        启动北京时间service
        startTimeService();

//        启动扫描backup文件夹视频文件
        startScanBackupVideoService();

        forbidLockScreen();
    }

    @Override
    protected void initVariables() {
        Log.e("ERROR", "开始执行MainActivity中的onCreate()函数");
        wifiAdmin = new WifiAdmin(this);
        fragmentManager = getFragmentManager();
        tabletStateContext = new TabletStateContext();

        //记录现场
        recordState = RecordState.getInstance(this);

        allocDevDialog = new ProgressDialog(this);

        //初始化网络
        LogServer.getInstance().setIdataPreference(new DataPreference(this));
        SignalServer.getInstance().setIdataPreference(new DataPreference(this));
        VideoServer.getInstance().setIdataPreference(new DataPreference(this));


        //初始化设备
        DeviceEntity.getInstance().setDataPreference(new DataPreference(this));

        //初始化献浆员
        DonorEntity.getInstance().setDataPreference(new DataPreference(this));

        //开机后处于等待时间信号
        tabletStateContext.setCurrentState(WaitingForTimestampState.getInstance());


        // 观察者模式
        // Observer Pattern: ObservableZXDCSignalListenerThread(Observer),ObserverZXDCSignalUIHandler(Observer),
        // ObservableZXDCSignalListenerThread(Observable)
        observableZXDCSignalListenerThread = new ObservableZXDCSignalListenerThread(recordState, tabletStateContext);
        observerZXDCSignalUIHandler = new ObserverZXDCSignalUIHandler(new SoftReference<>(this));

        // Add the observers into the observable object.
        observableZXDCSignalListenerThread.addObserver(observerZXDCSignalUIHandler);
    }


    @Override
    protected void initView() {
        Log.e("ERROR", "initView");

        setContentView(R.layout.activity_main);

        //左侧提示栏
        initLeftView();

        //顶部标题栏
        initTitleBar();

        //选择按钮
        initTabGroup();

        //主内容区
        initMainUI();
    }

    private void initLeftView() {

        //屏幕左侧的提示栏
        left_hint_view = findViewById(R.id.left_view_container);

        //握拳提示图片
        ivStartFistHint = (ImageView) this.findViewById(R.id.iv_start_fist);

        //服务请求
        call_view = findViewById(R.id.call_view);
        call_view.setOnClickListener(this);


    }

    //标题栏初始化
    private void initTitleBar() {

        //屏幕顶端的标题栏
        title_bar_view = findViewById(R.id.title_bar_view);

        //标题栏内部左侧的标题栏名字
        title_txt = (TextView) findViewById(R.id.title_txt);
        title_txt.setText(R.string.fragment_wait_check_title);

        //标题栏内部左侧的图标
        ivLogoAndBack = (ImageView) findViewById(R.id.logo_or_back);
        ivLogoAndBack.setEnabled(true);
        ivLogoAndBack.setImageResource(R.mipmap.ic_launcher);
        ivLogoAndBack.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.SETTINGS);
                return false;
            }
        });
        mParentView = getLayoutInflater().inflate(R.layout.activity_main,
                null);
        mPopView = getLayoutInflater().inflate(R.layout.popupwin_main, null);

        //标题栏内部中间的采集进度
        ll_cl = (LinearLayout) findViewById(R.id.ll_cl);
        collect_pb = (HorizontalProgressBar) findViewById(R.id.collect_pb);
        collect_pb.setProgress(0);

        //标题栏内部右侧的北京时间
        time_txt = (TextView) findViewById(R.id.time_txt);

        //标题栏内部右侧的电量、网络信号。
        battery_pb = (VerticalProgressBar) findViewById(R.id.battery_pb);
        wifi_not_txt = (TextView) findViewById(R.id.wifi_not_txt);
        net_state_txt = (TextView) findViewById(R.id.net_state_txt);
        net_state_txt.setOnClickListener(this);

        //标题栏内部右侧的选择功能设置，服务器地址设置以及重启
        // TODO: 2016/4/29 参数配置及应用重启的功能还未能实现。
        overflow_image = (ImageView) findViewById(R.id.overflow_image);
        overflow_image.setOnClickListener(this);
//        fun_txt = (TextView) mPopView.findViewById(R.id.fun_txt);
//        fun_txt.setOnClickListener(this);

        restart_txt = (TextView) mPopView.findViewById(R.id.restart_txt);
        restart_txt.setOnClickListener(this);
    }

    //初始化tab选择
    private void initTabGroup() {
        mGroup = (RadioGroup) findViewById(R.id.tab_group);
        mGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.btn_video:
                        //视频列表 改为视频分类列表
//                        tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.TOVIDEOLIST);
                        tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.TOVIDEOCATEGORY);

                        break;

                    case R.id.btn_surfinternet:
                        //上网
                        tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.TOSURF);
                        break;
                }
            }
        });
    }

    //中间部分的ui初始化
    private void initMainUI() {

        dlg_call_service_view = findViewById(R.id.dlg_call_service_view);
        View dlg_call_service_cancle_view = findViewById(R.id.dlg_call_service_view);
        dlg_call_service_cancle_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg_call_service_view.setVisibility(View.GONE);
            }
        });

        switchUiComponent(fragmentManager, R.id.fragment_container, new CheckFragment());


        battery_not_connect_txt = (TextView) findViewById(R.id.battery_not_connect_txt);

    }

    @Override
    public void loadData() {
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("ERROR", "开始执行MainActivity中的onRestart()函数");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("ERROR", "开始执行MainActivity中的onStart()函数");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("ERROR", "开始执行MainActivity中的onResume()函数");

        test();
        //启动联网
        observableZXDCSignalListenerThread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.e("ERROR", "开始执行MainActivity中的onPause()函数" + this.toString());
        long start = System.currentTimeMillis();
        tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.POWEROFF);
        StartMainActivityAgain();
        long end = System.currentTimeMillis();
        Log.e("ERROR", "结束执行MainActivity中的onPause()函数，耗时：" + (end - start) / 1000.0);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("ERROR", "开始执行MainActivity中的onStop()函数");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("ERROR", "onDestroy");
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
    }

    @Override
    protected synchronized void onSaveInstanceState(Bundle outState) {
        synchronized (onSaveInstanceState) {
            onSaveInstanceStateBoolean = false;
            super.onSaveInstanceState(outState);
        }
    }

    public void dealTime() {

        Log.e("ERROR", "开始--处理时间戳信号" + this.toString());
        startTimeService();
        Log.e("ERROR", "结束--处理时间戳信号");
    }

    //收到浆员信息后，认证浆员信息
    public synchronized void dealConfirm() {
        Log.e("ERROR", "开始--处理确认信号" + fragmentManager.toString());

        setBrightnessNormal();

        //设置显示状态
        showUiComponent(false, true, false, false);

        //界面切换：
        // 调整出身份证信息和档案信息
        AuthFragment authFragment = new AuthFragment();
        switchUiComponent(fragmentManager, R.id.fragment_container, authFragment);

        //调整出认证预览界面
        AuthPreviewFragment authPreviewFragment = new AuthPreviewFragment();
        switchUiComponent(fragmentManager, R.id.fragment_auth_container, authPreviewFragment);

        //设置文字内容
        title_txt.setText(R.string.auth);

        //设置logo按钮事件
        ivLogoAndBack.setEnabled(true);
        ivLogoAndBack.setImageResource(R.mipmap.ic_launcher);

        ivLogoAndBack.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
//                tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.RECORDDONORVIDEO);
//                // TODO: 2016/5/20 录制献浆员视频和护士视频的模块做好后，调整为发送RECORDDONORVIDEO命令
                tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.AUTHPASS);
                return false;
            }
        });


        Log.e("ERROR", "结束--处理确认信号");
    }

    public synchronized void dealLowPower() {
        Log.e("ERROR", "开始--处理电量低");


        if (onSaveInstanceStateBoolean) {

            //设置显示状态
            showUiComponent(false, true, false, false);

            //界面切换：
            //调出企业文化界面
            switchUiComponent(fragmentManager, R.id.fragment_container, new CheckFragment());

            //设置文字内容
            title_txt.setText(R.string.check1);

            //设置logo按钮事件
            ivLogoAndBack.setEnabled(true);
            ivLogoAndBack.setImageResource(R.mipmap.ic_launcher);
            ivLogoAndBack.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.SETTINGS);
                    return false;
                }
            });


            checkdBattery();
        }


        Log.e("ERROR", "结束--处理电量低");
    }


    public synchronized void dealRecordDonorVideo() {

    }

    public synchronized void dealRecordNurseVideo() {

    }


    //认证通过后，等待服务器接收到通过信号
    public synchronized void dealAuthPass() {

        Log.e("ERROR", "开始--处理认证通过信号" + fragmentManager.toString());


        if (onSaveInstanceStateBoolean) {

            //设置显示状态
            showUiComponent(false, true, false, false);

            //界面切换：

            //设置文字内容
            title_txt.setText(R.string.authres);

            //设置logo按钮事件
            ivLogoAndBack.setEnabled(false);
            ivLogoAndBack.setImageResource(R.mipmap.ic_launcher);

            //启动相关动作
            showProgress("认证通过，等待应答！", "服务器（**）\n单采机（**）");

            CountDownTimer countDownTimer = new CountDownTimer(11000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    disAllocDevDialog();
                    tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.AUTHRESTIMEOUT);
                }
            };
            countDownTimer.start();
        }

        Log.e("ERROR", "结束--处理认证通过信号" + fragmentManager.toString());
    }

    public synchronized void dealAuthResTimeout() {

        Log.e("ERROR", "开始--处理人证应答超时" + fragmentManager.toString());
        if (onSaveInstanceStateBoolean) {
            //设置显示状态
            showUiComponent(false, true, false, false);

            //界面切换：
            showTimeoutDialog("应答失败！", "超时");

            //设置文字内容
            title_txt.setText(R.string.authres);

            //设置logo按钮事件
            ivLogoAndBack.setEnabled(false);
            ivLogoAndBack.setImageResource(R.mipmap.ic_launcher);

            //启动相关动作
        }


    }

    public synchronized void dealSerAuthRes() {

        //设置显示状态
        showUiComponent(false, true, false, false);

        //界面切换：
        //调整应答
        showProgress("认证通过，等待应答！", "服务器（应答）\n单采机（**）");
        //设置文字内容
        title_txt.setText(R.string.authres);

        //设置logo按钮事件
        ivLogoAndBack.setEnabled(false);
        ivLogoAndBack.setImageResource(R.mipmap.ic_launcher);

        //启动相关动作

    }

    public synchronized void dealZxdcAuthRes() {

        //设置显示状态
        showUiComponent(false, true, false, false);

        //界面切换：
        showProgress("认证通过，等待应答！", "服务器（**）\n单采机（应答）");

        //设置文字内容
        title_txt.setText(R.string.authres);

        //设置logo按钮事件
        ivLogoAndBack.setEnabled(false);
        ivLogoAndBack.setImageResource(R.mipmap.ic_launcher);

        //启动相关动作
    }

    public synchronized void dealAuthResOk() {

        Log.e("ERROR", "开始--处理认证通过信号" + fragmentManager.toString());
        //设置显示状态
        showUiComponent(false, true, false, false);

        //界面切换：
        String name = DonorEntity.getInstance().getIdentityCard().getName();
        String sloganone = MainActivity.this.getString(R.string.sloganoneabove);
        WelcomeFragment welcomeFragment = WelcomeFragment.newInstance(name, sloganone);
        switchUiComponent(fragmentManager, R.id.fragment_container, welcomeFragment);
        //隐藏认证预览界面
        BlankFragment blankFragment = new BlankFragment();
        switchUiComponent(fragmentManager, R.id.fragment_auth_container, blankFragment);

        showProgress("认证通过，等待应答！", "服务器（应答）\n单采机（应答）");
        disAllocDevDialog();

        //设置文字内容
        title_txt.setText(R.string.fragment_welcome_plasm_title);

        //设置logo按钮事件
        ivLogoAndBack.setEnabled(false);
        ivLogoAndBack.setImageResource(R.mipmap.ic_launcher);

        Log.e("ERROR", "结束--处理认证通过信号" + fragmentManager.toString());
    }

    //    ***********************对话框***************************************
    //显示认证应答进度框
    private void showProgress(String title, String msg) {
        if (isFinishing()) {
            return;
        }
        if (allocDevDialog == null) {
            allocDevDialog = new ProgressDialog(this);
        }
        allocDevDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        allocDevDialog.setTitle(title);
        allocDevDialog.setMessage(msg);
        allocDevDialog.setIcon(R.mipmap.ic_launcher);


//        设置点击进度对话框外的区域对话框不消失
        allocDevDialog.setCanceledOnTouchOutside(false);
        allocDevDialog.setIndeterminate(false);
        allocDevDialog.setCancelable(false);
        allocDevDialog.show();
    }

    //关闭分配中对话框
    private void disAllocDevDialog() {
        if (!isFinishing()) {
            if (allocDevDialog != null) {
                allocDevDialog.dismiss();
                allocDevDialog = null;
            }
        }
    }

    //超时对话框
    private void showTimeoutDialog(String title, String msg) {
        if (isFinishing()) {
            return;
        }
        AlertDialog.Builder failAllocDialogBuilder = new AlertDialog.Builder(this);
        failAllocDialogBuilder.setIcon(R.mipmap.ic_launcher);
        failAllocDialogBuilder.setTitle(title);
        failAllocDialogBuilder.setMessage(msg);


        failAllocDialogBuilder.setPositiveButton("重发", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.REAUTHPASS);
            }
        });
        failAllocDialogBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.CANCLEAUTHPASS);
            }
        });
        if (failAllocDialog == null) {
            failAllocDialog = failAllocDialogBuilder.create();
        }

        failAllocDialog.setCanceledOnTouchOutside(false);
        failAllocDialog.setCancelable(false);
        failAllocDialog.show();
    }
    //    ***********************对话框***************************************

    //收到加压信号，进入等待穿刺状态
    public synchronized void dealCompression() {
        Log.e("ERROR", "开始--处理加压信号");
        //设置显示状态
        showUiComponent(true, true, false, true);

        //界面切换：
        //调出企业文化界面
        //播报加压提示
        PressingFragment pressingFragment = new PressingFragment();
        switchUiComponent(fragmentManager, R.id.fragment_container, pressingFragment);


        //调出采集过程中预览画面
        collectionPreviewFragment = CollectionPreviewFragment.newInstance("", "");
        switchUiComponent(fragmentManager, R.id.fragment_record_container, collectionPreviewFragment);

        //设置文字内容
        title_txt.setText(R.string.fragment_pressing_title);

        //设置logo按钮事件
        ivLogoAndBack.setImageResource(R.mipmap.ic_launcher);
        ivLogoAndBack.setEnabled(false);

        //启动相关动作
        collect_pb.setProgress(0);
        collect_pb.setMax(600);
        Log.e("ERROR", "结束--处理加压信号");

    }

    //             处理穿刺
    public synchronized void dealPuncture() {

        Log.e("ERROR", "开始---处理穿刺信号");
        //设置显示状态
        showUiComponent(true, true, false, true);

        //界面切换：
        //调出企业文化界面
        //开始播放采集视频
        JCPlayVideoFragment playVideoFragment = JCPlayVideoFragment.newInstance("/sdcard/donation.mp4", "PunctureVideo");
//        PlayVideoFragment playVideoFragment = PlayVideoFragment.newInstance("/sdcard/donation.mp4", "PunctureVideo");
        switchUiComponent(fragmentManager, R.id.fragment_container, playVideoFragment);
        //如果加压信号跳过了，需要调出采集中预览画面
        if (collectionPreviewFragment == null) {
            collectionPreviewFragment = CollectionPreviewFragment.newInstance("", "");
            switchUiComponent(fragmentManager, R.id.fragment_record_container, collectionPreviewFragment);
        }

        //设置文字内容
        title_txt.setText(R.string.fragment_puncture_video);

        //设置logo按钮事件
        ivLogoAndBack.setImageResource(R.mipmap.ic_launcher);
        ivLogoAndBack.setEnabled(false);

        //启动相关动作
        collect_pb.setProgress(0);
        collect_pb.setMax(600);
        Log.e("ERROR", "结束---处理穿刺信号");
    }


    //处理开始采集信号
    public synchronized void dealStart() {

        Log.e("ERROR", "开始————处理开采信号");
        //设置显示状态
        showUiComponent(true, true, false, true);

        //界面切换：
        //播放采集提示
        switchUiComponent(fragmentManager, R.id.fragment_container, new CollectionFragment());
        //如果加压信号跳过了，需要调出采集中预览画面
        if (collectionPreviewFragment == null) {
            collectionPreviewFragment = CollectionPreviewFragment.newInstance("", "");
            switchUiComponent(fragmentManager, R.id.fragment_record_container, collectionPreviewFragment);
        }
        //设置文字内容
        title_txt.setText(R.string.fragment_collect_title);
        collect_pb.setProgress(PlasmaWeightEntity.getInstance().getCurWeight());
        collect_pb.setMax(PlasmaWeightEntity.getInstance().getSettingWeight());
        //设置logo按钮事件
        ivLogoAndBack.setImageResource(R.mipmap.ic_launcher);
        ivLogoAndBack.setEnabled(false);

        Log.e("ERROR", "结束————处理开采信号");
    }

    public synchronized void dealStartCollcetionVideo(String path) {

        Log.e("ERROR", "开始--播放采集开始视频");

        //设置显示状态
        showUiComponent(true, true, false, true);

        //界面切换：
        //播放默认的采集视频
        switchUiComponent(fragmentManager, R.id.fragment_container, new VideoListFragment());
        JCPlayVideoFragment playVideoFragment = JCPlayVideoFragment.newInstance(path, "StartCollcetionVideo");
//        PlayVideoFragment playVideoFragment = PlayVideoFragment.newInstance(path, "StartCollcetionVideo");
        switchUiComponent(fragmentManager, R.id.fragment_container, playVideoFragment);

        //设置文字内容
        title_txt.setText(R.string.play_video);

        //设置logo按钮事件
        ivLogoAndBack.setEnabled(true);
        ivLogoAndBack.setImageResource(R.mipmap.jiantou_press);

        //给返回按钮设置回掉函数
        ivLogoAndBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.TOVIDEOCATEGORY);

            }
        });

        //启动相关动作
        collect_pb.setProgress(PlasmaWeightEntity.getInstance().getCurWeight());
        collect_pb.setMax(PlasmaWeightEntity.getInstance().getSettingWeight());
        Log.e("ERROR", "结束--播放采集开始视频");
    }

    public synchronized void dealStartVideo() {

        Log.e("ERROR", "开始--播放视频");
        //设置显示状态
        showUiComponent(true, true, false, true);
        //界面切换：
        JCPlayVideoFragment playVideoFragment = JCPlayVideoFragment.newInstance(VideoPathEntity.videoPath, "selectVideo");
//        PlayVideoFragment playVideoFragment =PlayVideoFragment.newInstance(VideoPathEntity.videoPath, "selectVideo");
        switchUiComponent(fragmentManager, R.id.fragment_container, playVideoFragment);

        //设置文字内容
        title_txt.setText(R.string.play_video);

        //设置logo按钮事件
        ivLogoAndBack.setEnabled(true);
        ivLogoAndBack.setImageResource(R.mipmap.jiantou_press);
        ivLogoAndBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.TOVIDEOLIST);
            }
        });
        //启动相关动作
        Log.e("ERROR", "开始--播放视频");
    }

    //观看影片
    public synchronized void dealToVideoList() {
        Log.e("ERROR", "开始--返回播放列表");
        showUiComponent(true, true, false, true);
        ivLogoAndBack.setImageResource(R.mipmap.ic_launcher);

        title_txt.setText(R.string.video_list);

        //设置logo按钮事件
        ivLogoAndBack.setEnabled(true);
        ivLogoAndBack.setImageResource(R.mipmap.jiantou_press);
        ivLogoAndBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.TOVIDEOCATEGORY);

            }
        });

        switchUiComponent(fragmentManager, R.id.fragment_container, new VideoListFragment());
        Log.e("ERROR", "结束--返回播放列表");
    }

    //影片分类
    public synchronized void dealToVideoCategory() {
        Log.e("ERROR", "开始--返回播放分类");
        showUiComponent(true, true, true, true);

        ivLogoAndBack.setImageResource(R.mipmap.ic_launcher);
        title_txt.setText(R.string.watch_film);
        ivLogoAndBack.setEnabled(false);

        switchUiComponent(fragmentManager, R.id.fragment_container, new VideoCategorizeFragment());
        Log.e("ERROR", "结束--返回播放分类");
    }

    //上网娱乐
    public synchronized void dealToSurf() {

        title_txt.setText(R.string.surf_internet);
        switchUiComponent(fragmentManager, R.id.fragment_container, new SurfInternetFragment());

    }

    //建议评价
    public synchronized void dealToAdvice() {

        title_txt.setText(R.string.advice);
        switchUiComponent(fragmentManager, R.id.fragment_container, new AdviceFragment());
    }

    //预约服务
    public synchronized void dealToAppointment() {

        title_txt.setText(R.string.appointment);
        switchUiComponent(fragmentManager, R.id.fragment_container, new AppointmentFragment());
    }

    //握拳
    public synchronized void dealStartFist() {

        if (ivStartFistHint.getVisibility() != View.VISIBLE) {
            ivStartFistHint.setVisibility(View.VISIBLE);

            startFist = new AniThread(this, ivStartFistHint, "startfist.gif", 150);
            startFist.startAni();
        }

    }

    //停止握拳
    public void dealStopFist() {
        if (startFist != null) {
            startFist.finishAni();
        }
        ivStartFistHint.setVisibility(View.INVISIBLE);
    }

    //从视频播放界面返回视频列表
    public void dealBackToVideoList() {
        showUiComponent(true, true, true, true);
        ivLogoAndBack.setImageResource(R.mipmap.ic_launcher);
        title_txt.setText(R.string.watch_film);
        ivLogoAndBack.setEnabled(false);

        //switch
        switchUiComponent(fragmentManager, R.id.fragment_container, new VideoListFragment());
    }

    //处理采浆结束信号
    public void dealEnd() {

        Log.e("ERROR", "开始--处理结束信号");
        //设置显示状态
        showUiComponent(false, false, false, false);

        //界面切换：
        switchUiComponent(fragmentManager, R.id.fragment_record_container, new BlankFragment());
        switchUiComponent(fragmentManager, R.id.fragment_container, new EndFragment());
        PlasmaWeightEntity.getInstance().setCurWeight(0);
        //设置文字内容

        //设置logo按钮事件

        //启动相关动作

        //有可能在结束的时候还未关闭握拳提示，如果不关闭则会在后台一直运行该线程。
        if (startFist != null) {
            startFist.finishAni();
        }

        Log.e("ERROR", "结束--处理结束信号");
    }


    public void dealCheckStart() {
        Log.e("ERROR", "开始--处理开始检查信号");

        //设置显示状态
        showUiComponent(false, true, false, false);

        //界面切换：
        switchUiComponent(fragmentManager, R.id.fragment_container, new CheckFragment());

        //设置文字内容
        title_txt.setText(R.string.check2);

        //设置logo按钮事件
        ivLogoAndBack.setEnabled(true);
        ivLogoAndBack.setImageResource(R.mipmap.ic_launcher);
        ivLogoAndBack.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.SETTINGS);
                return false;
            }
        });

        //启动相关动作

        checkdBattery();

        Log.e("ERROR", "结束--处理开始检查信号");
    }

    //检查电量和wifi是否已经检测通过

    private void checkdBattery() {
//        batteryIsOk = false;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {

                    if (batteryIsOk) {
                        tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.CHECKOVER);

                        break;
                    }
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }


    public void dealCheckOver() {
        Log.e("ERROR", "开始--处理检查通过信号" + recordState.getState().toString() + " " + fragmentManager.toString());

        setBrightnessLow();

        //设置显示状态
        showUiComponent(false, true, false, false);

        //界面切换：

        //切换
        WaitingForDonorFragment waitingForDonorFragment = WaitingForDonorFragment.newInstance(getString(R.string.general_welcome), "");
        switchUiComponent(fragmentManager, R.id.fragment_container, waitingForDonorFragment);

        BlankFragment blankFragment = new BlankFragment();
        switchUiComponent(fragmentManager, R.id.fragment_auth_container, blankFragment);

        //设置文字内容
        title_txt.setText(R.string.fragment_wait_plasm_title);

        //设置logo按钮事件
        ivLogoAndBack.setEnabled(true);
        ivLogoAndBack.setImageResource(R.mipmap.ic_launcher);
        ivLogoAndBack.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.SETTINGS);
                return false;
            }
        });

        //启动相关动作
        Log.e("ERROR", "结束--处理检查通过信号" + fragmentManager.toString());
    }


    public void dealSettings() {

        //设置显示状态
        setBrightnessNormal();
        showUiComponent(false, true, false, false);

        //界面切换：
        switchUiComponent(fragmentManager, R.id.fragment_container, new ServerSettingFragment());

        //设置文字内容
        title_txt.setText(R.string.fragment_wait_plasm_title);

        //设置logo按钮事件
        ivLogoAndBack.setEnabled(false);
        ivLogoAndBack.setImageResource(R.mipmap.ic_launcher);

        //启动相关动作

    }

    public void dealReStart() {

        //设置显示状态


        //界面切换：


        //设置文字内容


        //设置logo按钮事件


        //启动相关动作
        recordState.recTimeStamp();
        recordState.commit();
        tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.POWEROFF);
        StartMainActivityAgain();
    }

    public void dealPlasmaWeight() {
        Log.e("ERROR", "dealPlasmaWeight");

        //设置显示状态
//        showUiComponent(true, true, true, true);

        //界面切换：


        //设置文字内容
//        title_txt.setText(R.string.fragment_collect_title);

        //设置logo按钮事件
//        ivLogoAndBack.setImageResource(R.mipmap.ic_launcher);
//        ivLogoAndBack.setEnabled(false);

        //启动相关动作
        collect_pb.setProgress(PlasmaWeightEntity.getInstance().getCurWeight());
        collect_pb.setMax(PlasmaWeightEntity.getInstance().getSettingWeight());
    }

    private void showUiComponent(boolean leftHint, boolean titleBar, boolean tabGroup, boolean collection) {
        if (leftHint) {
            left_hint_view.setVisibility(View.VISIBLE);
        } else {
            left_hint_view.setVisibility(View.GONE);
        }

        if (titleBar) {
            title_bar_view.setVisibility(View.VISIBLE);
        } else {
            title_bar_view.setVisibility(View.GONE);
        }

        if (tabGroup) {
            mGroup.setVisibility(View.VISIBLE);
        } else {
            mGroup.setVisibility(View.GONE);
        }
        if (collection) {

            ll_cl.setVisibility(View.INVISIBLE);
        } else {
            ll_cl.setVisibility(View.INVISIBLE);
//            根据在金堂的使用，采集进度的显示会影响献浆员感受。
//            ll_cl.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onClick(View v) {
        Intent it = null;
        switch (v.getId()) {

            case R.id.restart_txt:
                //重启
                tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.RESTART);
                break;
            case R.id.overflow_image:
                showPopWindow();
                break;
            case R.id.net_state_txt:
                //检测网络和检查服务器配置
//                recordState
                StartMainActivityAgain();
                break;
            case R.id.call_view:
                //呼叫护士提供服务
                dlg_call_service_view.setVisibility(View.VISIBLE);
                break;

        }
        if (it != null) {
            startActivity(it);
        }
    }


    private void showPopWindow() {
        View view = findViewById(R.id.ll_test);
        view.setVisibility(View.VISIBLE);
        if (mPopupWindow == null) {
            mPopupWindow = new PopupWindow(mPopView,
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,
                    false);
            mPopupWindow.setHeight(AppInfoUtils.dip2px(MainActivity.this, 100));
            mPopupWindow.setWidth(AppInfoUtils.dip2px(MainActivity.this, 210));
            mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.setFocusable(true);

        }
        mPopupWindow
                .setOnDismissListener(new PopupWindow.OnDismissListener() {

                    @Override
                    public void onDismiss() {
                    }
                });
        mPopupWindow.setAnimationStyle(R.style.popwin_anim_style);
        mPopupWindow.showAtLocation(mParentView, Gravity.RIGHT
                        | Gravity.TOP, AppInfoUtils.dip2px(MainActivity.this, 2),
                AppInfoUtils.dip2px(MainActivity.this, 76));
    }

    private void showCallDialog() {
        if (mDialog == null) {
            mDialog = new ProgressDialog(this);
        }
        mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mDialog.setCancelable(true);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.show();
        mDialog.setContentView(R.layout.dlg_call_service);
    }

    //禁止返回按钮
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {
            // The donor can't use the BACK button to close the APP.
            case KeyEvent.KEYCODE_BACK:
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    private void switchUiComponent(FragmentManager fragmentManager, int containerViewId, Fragment fragment) {


        if (!isFinishing()) {
            fragmentManager.beginTransaction().replace(containerViewId, fragment).commitAllowingStateLoss();
        }


    }

    // 重新开启一个MainActivity
    private void StartMainActivityAgain() {
        Intent intentToNewMainActivity = new Intent(MainActivity.this, MainActivity.class);
        intentToNewMainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intentToNewMainActivity);
        MainActivity.this.finish();
    }

    private void startTimeService() {
        Intent it = new Intent(MainActivity.this, TimeService.class);
        it.putExtra("currenttime", ServerTime.curtime);
        startService(it);
    }


    private void startScanBackupVideoService() {
        Intent it = new Intent(MainActivity.this, ScanBackupVideoService.class);
        startService(it);
    }

    private String timeStamp2Date(String seconds) {
        if (TextUtils.isEmpty(seconds)) {
            return "";
        }
        String format = "HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));

        CurrentDate.curDate = new Date(Long.valueOf(seconds + ""));
        return sdf.format(CurrentDate.curDate);
    }


    //禁止屏幕休眠，但长时间无献浆消息推送过来，屏幕处于低亮度。
    private void forbidLockScreen() {

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        pm = (PowerManager) getSystemService(POWER_SERVICE);
        mWakelock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "SimpleTimer");

        km = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);

        KeyguardManager.KeyguardLock kl = km.newKeyguardLock("unLock");
        kl.disableKeyguard();  //解锁
        mWakelock.acquire();//点亮
    }

    private void test() {
        //收到浆员信息
        Button btn_send_donor_info = (Button) this.findViewById(R.id.btn_send_donor_info);
        btn_send_donor_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.CONFIRM);
            }
        });

        //认证通过
        Button btn_auth_pass = (Button) this.findViewById(R.id.btn_auth_pass);
        btn_auth_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.AUTHPASS);
                tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.ZXDCAUTHRES);
                tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.SERAUTHRES);

            }
        });


        //加压
        Button btn_compression = (Button) this.findViewById(R.id.btn_compression);
        btn_compression.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.COMPRESSINON);

                MyLog.e("SCR", "screen:" + BrightnessTools.getScreenBrightness(MainActivity.this));
            }
        });

        //穿刺
        Button btn_puncture = (Button) this.findViewById(R.id.btn_puncture);
        btn_puncture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.PUNCTURE);
            }
        });

        //采集开始
        Button btn_collection_start = (Button) this.findViewById(R.id.btn_collection_start);
        btn_collection_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.START);
            }
        });

        //血浆重量
        Button btn_plasma_weight = (Button) this.findViewById(R.id.btn_plasma_weight);
        btn_plasma_weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.PLASMAWEIGHT);
            }
        });

        //管压过低
        Button btn_pipe_low = (Button) this.findViewById(R.id.btn_pipe_low);
        btn_pipe_low.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.PIPELOW);
            }
        });

        //管压正常
        Button btn_pipe_normal = (Button) this.findViewById(R.id.btn_pipe_normal);
        btn_pipe_normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.PIPENORMAL);
            }
        });

        //采集结束
        Button btn_collection_end = (Button) this.findViewById(R.id.btn_collection_end);
        btn_collection_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.END);
            }
        });

        //设备可用
        Button btn_check_pass = (Button) this.findViewById(R.id.btn_device_available);
        btn_check_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.CHECKOVER);
            }
        });

        //设备不可用
        Button btn_device_unavailable = (Button) this.findViewById(R.id.btn_device_unavailable);
        btn_device_unavailable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabletStateContext.handleMessge(recordState, observableZXDCSignalListenerThread, null, null, RecSignal.LOWPOWER);
            }
        });

    }

    //调低屏幕亮度
    private void setBrightnessLow() {
        BrightnessTools.stopAutoBrightness(this);
        BrightnessTools.setBrightness(this, 2);
    }

    //调正常屏幕亮度
    private void setBrightnessNormal() {
        BrightnessTools.startAutoBrightness(this);
    }

}

