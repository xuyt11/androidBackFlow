package cn.ytxu.androidbackflow;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import cn.ytxu.androidbackflow.sample.App;

public class BaseActivity extends AppCompatActivity {
    private final String tag = this.getClass().getSimpleName();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(tag, "init-->2current process name:" + ((App) getApplication()).getCurProcessName(this));
        Log.i(tag, "init-->2taskId:" + getTaskId());
    }

    public <T extends View> T $(@IdRes int id) {
        return (T) findViewById(id);
    }

    @Override
    public void startActivity(Intent intent) {
        startActivityForResult(intent, BackFlow.REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(tag, "onActivityResult");
        if (BackFlow.handle(this, resultCode, data)) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
