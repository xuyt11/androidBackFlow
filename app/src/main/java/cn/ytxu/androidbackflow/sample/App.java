package cn.ytxu.androidbackflow.sample;

import android.app.Application;

import cn.ytxu.androidbackflow.BackFlow;

/**
 * Created by ytxu on 17/1/4.
 */
public class App extends Application {

    private static final String TAG = App.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        BackFlow.Logger.log(TAG, "init app, process:" + BackFlow.getCurProcessName(this));
    }

}
