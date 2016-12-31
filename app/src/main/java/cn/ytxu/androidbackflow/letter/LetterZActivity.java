package cn.ytxu.androidbackflow.letter;

import android.os.Bundle;

import cn.ytxu.androidbackflow.BackFlow;
import cn.ytxu.androidbackflow.letter.base.BaseLetterActivity;

public class LetterZActivity extends BaseLetterActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRollbackFlow(LetterOActivity.class);
    }

}
