package com.jiaying.mediatablet.activity;

import android.app.Activity;
import android.os.Bundle;


/**
 * activity基类
 */
public abstract class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVariables();
        initView();
        loadData();
    }

    //初始化变量，包括Intent带的数据和Activity内的变量
    protected abstract void initVariables();

    //加载layout布局文件，初始化控件，为控件挂上时间方法
    protected abstract void initView();

    // 调用服务器API加载数据
    protected abstract void loadData();

}
