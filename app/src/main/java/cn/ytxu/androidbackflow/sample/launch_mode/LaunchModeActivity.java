package cn.ytxu.androidbackflow.sample.launch_mode;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import cn.ytxu.androidbackflow.BackFlow;
import cn.ytxu.androidbackflow.BackFlowType;
import cn.ytxu.androidbackflow.BaseActivity;
import cn.ytxu.androidbackflow.R;

/**
 * Created by ytxu on 2017/1/8.
 */
public class LaunchModeActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launch_mode_activity);

        setTitle(this.getClass().getSimpleName());

        $(R.id.launch_mode_finish_task).setOnClickListener(this);

        $(R.id.launch_mode_standard).setOnClickListener(this);
        $(R.id.launch_mode_single_task).setOnClickListener(this);
        $(R.id.launch_mode_single_top).setOnClickListener(this);
        $(R.id.launch_mode_single_instance).setOnClickListener(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        BackFlow.Logger.log("onNewIntent", this.getClass().getSimpleName());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.launch_mode_finish_task: {
                BackFlow.builder(BackFlowType.finish_task, this).create().request();
            }
            break;
            case R.id.launch_mode_standard: {
                startActivity(new Intent(this, LaunchModeActivity.class));
            }
            break;
            case R.id.launch_mode_single_task: {
                startActivity(new Intent(this, SingleTaskActivity.class));
            }
            break;
            case R.id.launch_mode_single_top: {
                startActivity(new Intent(this, SingleTopActivity.class));
            }
            break;
            case R.id.launch_mode_single_instance: {
                startActivity(new Intent(this, SingleInstanceActivity.class));
            }
            break;
        }
    }
}
