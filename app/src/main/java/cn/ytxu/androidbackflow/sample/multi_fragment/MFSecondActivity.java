package cn.ytxu.androidbackflow.sample.multi_fragment;

import android.os.Bundle;
import android.view.View;

import cn.ytxu.androidbackflow.BackFlow;
import cn.ytxu.androidbackflow.BaseActivity;
import cn.ytxu.androidbackflow.R;
import cn.ytxu.androidbackflow.sample.multi_fragment.letter.MFLetterAFragment;
import cn.ytxu.androidbackflow.sample.multi_fragment.letter.MFLetterBFragment;
import cn.ytxu.androidbackflow.sample.multi_fragment.letter.MFLetterCFragment;
import cn.ytxu.androidbackflow.sample.multi_fragment.letter.MFLetterDFragment;

public class MFSecondActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multi_fragment_second_activity);

        setTitle(getClass().getSimpleName());

        $(R.id.mf2_finish_app_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackFlow.finishApp(MFSecondActivity.this);
            }
        });

        $(R.id.mf2_back_2_a_fragment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackFlow.requestF(MFSecondActivity.this, MFLetterAFragment.class);
            }
        });
        $(R.id.mf2_back_2_b_fragment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackFlow.requestF(MFSecondActivity.this, MFLetterBFragment.class);
            }
        });
        $(R.id.mf2_back_2_c_fragment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackFlow.requestF(MFSecondActivity.this, MFLetterCFragment.class);
            }
        });
        $(R.id.mf2_back_2_d_fragment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackFlow.requestF(MFSecondActivity.this, MFLetterDFragment.class);
            }
        });

    }
}
