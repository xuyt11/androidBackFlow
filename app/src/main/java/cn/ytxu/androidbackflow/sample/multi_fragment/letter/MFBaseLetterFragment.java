package cn.ytxu.androidbackflow.sample.multi_fragment.letter;

import android.content.Intent;
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
import cn.ytxu.androidbackflow.sample.multi_fragment.MFSecondActivity;

public class MFBaseLetterFragment extends BaseFragment {
    private static final String TAG = MFBaseLetterFragment.class.getSimpleName();

    private TextView tipTxt;
    private Button jumpBtn;

    public MFBaseLetterFragment() {
        BackFlow.Logger.log(TAG, "init " + this.getClass().getSimpleName());
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.multi_fragment_letter_fragment, container, false);
        setRoot(view);

        tipTxt = $(R.id.mf_letter_tip_txt);
        jumpBtn = $(R.id.mf_letter_jump_btn);
        tipTxt.setText(getClass().getSimpleName());

        jumpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MFSecondActivity.class));
            }
        });

        $(R.id.mf_letter_finish_app_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackFlow.finishTask(MFBaseLetterFragment.this);
            }
        });

        initView();
        return view;
    }

    protected void initView() {
    }

}
