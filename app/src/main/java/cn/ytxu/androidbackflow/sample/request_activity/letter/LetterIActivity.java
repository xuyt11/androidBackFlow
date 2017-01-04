package cn.ytxu.androidbackflow.sample.request_activity.letter;

import android.os.Bundle;

import cn.ytxu.androidbackflow.sample.request_activity.base.BaseLetterActivity;

public class LetterIActivity extends BaseLetterActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRollbackFlow(LetterDActivity.class);
    }

}
