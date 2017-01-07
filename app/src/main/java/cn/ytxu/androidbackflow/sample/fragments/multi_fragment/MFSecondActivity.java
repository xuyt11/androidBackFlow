package cn.ytxu.androidbackflow.sample.fragments.multi_fragment;

import android.os.Bundle;
import android.view.View;

import cn.ytxu.androidbackflow.BackFlow;
import cn.ytxu.androidbackflow.BaseActivity;
import cn.ytxu.androidbackflow.R;
import cn.ytxu.androidbackflow.sample.fragments.multi_fragment.letter.MFLetterAFragment;
import cn.ytxu.androidbackflow.sample.fragments.multi_fragment.letter.MFLetterBFragment;
import cn.ytxu.androidbackflow.sample.fragments.multi_fragment.letter.MFLetterCFragment;
import cn.ytxu.androidbackflow.sample.fragments.multi_fragment.letter.MFLetterDFragment;

public class MFSecondActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multi_fragment_second_activity);

        setTitle(getClass().getSimpleName());

        $(R.id.mf2_finish_task_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackFlow.finishTask(MFSecondActivity.this);
            }
        });

        $(R.id.mf2_back_2_a_fragment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackFlow.request(MFSecondActivity.this, MFLetterAFragment.class);
//                BackFlow.builder(BackFlowType.back_to_fragments, MFSecondActivity.this).setFragments(MFLetterAFragment.class).create().request();
            }
        });
        $(R.id.mf2_back_2_b_fragment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackFlow.request(MFSecondActivity.this, MFLetterBFragment.class);
            }
        });
        $(R.id.mf2_back_2_c_fragment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackFlow.request(MFSecondActivity.this, MFLetterCFragment.class);
            }
        });
        $(R.id.mf2_back_2_d_fragment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackFlow.request(MFSecondActivity.this, MFLetterDFragment.class);
            }
        });

    }
}
