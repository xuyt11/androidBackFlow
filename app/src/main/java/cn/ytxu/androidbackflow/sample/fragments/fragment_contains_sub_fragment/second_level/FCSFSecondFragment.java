package cn.ytxu.androidbackflow.sample.fragments.fragment_contains_sub_fragment.second_level;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import cn.ytxu.androidbackflow.BackFlow;
import cn.ytxu.androidbackflow.BaseFragment;
import cn.ytxu.androidbackflow.R;

public class FCSFSecondFragment extends BaseFragment {
    private static final String TAG = FCSFSecondFragment.class.getSimpleName();

    private TextView tipTxt;
    protected Button requestBtn;

    public FCSFSecondFragment() {
        BackFlow.Logger.log(TAG, "init " + this.getClass().getSimpleName());
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_csf_second_fragment, container, false);
        setRoot(view);

        tipTxt = $(R.id.fcsf2_tip_txt);
        tipTxt.setText(getClass().getSimpleName());

        requestBtn = $(R.id.fcsf2_request_btn);
        requestBtn.setVisibility(View.INVISIBLE);

        $(R.id.fcsf2_finish_task_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackFlow.finishTask(FCSFSecondFragment.this);
            }
        });

        initView();
        return view;
    }

    protected void initView() {
    }

}
