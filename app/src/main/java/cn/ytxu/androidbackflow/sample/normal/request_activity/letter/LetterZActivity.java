package cn.ytxu.androidbackflow.sample.normal.request_activity.letter;

import android.os.Bundle;

import cn.ytxu.androidbackflow.sample.normal.request_activity.base.BaseLetterActivity;

public class LetterZActivity extends BaseLetterActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRollbackFlow(LetterOActivity.class);
    }

}
