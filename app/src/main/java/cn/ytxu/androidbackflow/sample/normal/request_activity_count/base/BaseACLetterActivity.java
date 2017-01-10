package cn.ytxu.androidbackflow.sample.normal.request_activity_count.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import cn.ytxu.androidbackflow.BackFlow;
import cn.ytxu.androidbackflow.BackFlowType;
import cn.ytxu.androidbackflow.BaseBackFlowActivity;
import cn.ytxu.androidbackflow.R;

public class BaseACLetterActivity extends BaseBackFlowActivity {

    private Button jumpBtn, rollbackFlowBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_letter);

        jumpBtn = $(R.id.letter_jump_btn);
        rollbackFlowBtn = $(R.id.letter_back_flow_btn);
        rollbackFlowBtn.setVisibility(View.GONE);

        setTitle(getClass().getSimpleName());
        jumpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivity(new Intent(BaseACLetterActivity.this, ActivityCountLetterType.getNextActivity(BaseACLetterActivity.this)));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            }
        });

        finishApp();
    }

    protected void setRollbackFlow(final Class<? extends Activity> atyClass) {
        setRollbackFlow(atyClass, "rollback :" + atyClass.getSimpleName());
    }

    protected void setRollbackFlow(final Class<? extends Activity> atyClass, String text) {
        rollbackFlowBtn.setVisibility(View.VISIBLE);
        rollbackFlowBtn.setText(text);

        final int currPosition = ActivityCountLetterType.getCurrPosition(this);
        final int targetPosition = ActivityCountLetterType.getCurrPosition(atyClass);

        rollbackFlowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackFlow.builder(BackFlowType.back_activity_count, BaseACLetterActivity.this)
                        .setBackActivityCount(currPosition, targetPosition).create().request();
            }
        });
    }

    private void finishApp() {
        $(R.id.letter_finish_task_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackFlow.finishTask(BaseACLetterActivity.this);
            }
        });
    }
}
