package cn.ytxu.androidbackflow;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import cn.ytxu.androidbackflow.sample.App;

public class BaseActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BackFlow.Logger.log(TAG, "taskId:" + getTaskId()
                + ", obj:" + Integer.toHexString(hashCode())
                + ", process:" + ((App) getApplication()).getCurProcessName(this));
    }

    public <T extends View> T $(@IdRes int id) {
        return (T) findViewById(id);
    }

    @Override
    public void startActivity(Intent intent) {
        // 可以放开，查看该标签的对BackFlow的影响
        // 有该标签，会立即调用当前的onActivityResult(requestCode, 0, null)方法
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivityForResult(intent, BackFlow.REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (BackFlow.handle(this, getSupportFragmentManager().getFragments(), requestCode, resultCode, data)) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
