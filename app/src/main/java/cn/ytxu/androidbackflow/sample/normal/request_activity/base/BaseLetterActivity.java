package cn.ytxu.androidbackflow.sample.normal.request_activity.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import cn.ytxu.androidbackflow.BackFlow;
import cn.ytxu.androidbackflow.BaseActivity;
import cn.ytxu.androidbackflow.R;

public class BaseLetterActivity extends BaseActivity {

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
                    startActivity(new Intent(BaseLetterActivity.this, LetterType.getNextActivity(BaseLetterActivity.this)));
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
        rollbackFlowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackFlow.request(BaseLetterActivity.this, atyClass);
            }
        });
    }

    private void finishApp() {
        $(R.id.letter_finish_task_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackFlow.finishTask(BaseLetterActivity.this);
            }
        });
    }
}
