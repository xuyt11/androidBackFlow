package cn.ytxu.androidbackflow.sample.letter;

import android.os.Bundle;

import cn.ytxu.androidbackflow.sample.letter.base.BaseLetterActivity;

public class LetterOActivity extends BaseLetterActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRollbackFlow(LetterIActivity.class);
    }

}
