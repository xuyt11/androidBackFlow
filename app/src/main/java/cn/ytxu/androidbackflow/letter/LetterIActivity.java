package cn.ytxu.androidbackflow.letter;

import android.os.Bundle;

import cn.ytxu.androidbackflow.letter.base.BaseLetterActivity;

public class LetterIActivity extends BaseLetterActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRollbackFlow(LetterDActivity.class);
    }

}
