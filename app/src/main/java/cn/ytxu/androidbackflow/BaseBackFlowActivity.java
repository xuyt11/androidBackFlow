package cn.ytxu.androidbackflow;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import cn.ytxu.androidbackflow.sample.App;

public class BaseBackFlowActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BackFlow.Logger.log(TAG, "taskId:" + getTaskId()
                + ", obj:" + Integer.toHexString(hashCode())
                + ", myPid:" + android.os.Process.myPid()  //获取当前进程的id
                + ", process:" + ((App) getApplication()).getCurProcessName(this));
    }

    public <T extends View> T $(@IdRes int id) {
        return (T) findViewById(id);
    }


    //******************** start activity replace method ********************
    public void startActivity4NonBackFlow(Intent intent) {
        startActivityForResult(intent, BackFlow.REQUEST_CODE_4_NON_BACK_FLOW);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void startActivity4NonBackFlow(Intent intent, @Nullable Bundle options) {
        startActivityForResult(intent, BackFlow.REQUEST_CODE_4_NON_BACK_FLOW, options);
    }


    //******************** back flow ********************
    @Override
    public void startActivity(Intent intent) {
        startActivityForResult(intent, BackFlow.REQUEST_CODE);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void startActivity(Intent intent, @Nullable Bundle options) {
        startActivityForResult(intent, BackFlow.REQUEST_CODE, options);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (BackFlow.handle(this, getSupportFragmentManager().getFragments(), requestCode, resultCode, data)) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        BackFlow.Logger.log("onDestroy", this.getClass().getSimpleName());
        super.onDestroy();
    }
}
