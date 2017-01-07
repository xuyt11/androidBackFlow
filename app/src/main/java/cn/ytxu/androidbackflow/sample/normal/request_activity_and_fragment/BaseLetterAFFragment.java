package cn.ytxu.androidbackflow.sample.normal.request_activity_and_fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import cn.ytxu.androidbackflow.BackFlow;
import cn.ytxu.androidbackflow.BaseFragment;
import cn.ytxu.androidbackflow.R;
import cn.ytxu.androidbackflow.sample.normal.request_activity_and_fragment.letter.LetterAFAFragment;

public class BaseLetterAFFragment extends BaseFragment {

    private TextView tipTxt;
    private Button jumpBtn, rollbackFlowBtn;


    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.container_letter_fragment, container, false);
        setRoot(view);

        tipTxt = $(R.id.letter_tip_txt);
        jumpBtn = $(R.id.letter_jump_btn);
        rollbackFlowBtn = $(R.id.letter_back_flow_btn);
        rollbackFlowBtn.setVisibility(View.GONE);
        tipTxt.setText(getActivity().getClass().getSimpleName() + "\n" + getClass().getSimpleName());

        jumpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() instanceof ContainerAF1Activity) {
                    if (gotoAF1()) {
                        return;
                    }
                    gotoAF2Header();
                } else {
                    gotoAF2();
                }

            }
        });

        finishApp();

        initView();
        return view;
    }

    private boolean gotoAF1() {
        try {
            Intent intent = new Intent(getActivity(), ContainerAF1Activity.class);
            intent.putExtra(ContainerAF1Activity.PARAM_CLASSNAME, LetterAFFragmentType.getNextFragmentName(BaseLetterAFFragment.this));
            startActivity(intent);
            return true;
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void gotoAF2Header() {
        Intent intent = new Intent(getActivity(), ContainerAF2Activity.class);
        intent.putExtra(ContainerAF2Activity.PARAM_CLASSNAME, LetterAFAFragment.class.getName());
        startActivity(intent);
    }

    private boolean gotoAF2() {
        try {
            Intent intent = new Intent(getActivity(), ContainerAF2Activity.class);
            intent.putExtra(ContainerAF2Activity.PARAM_CLASSNAME, LetterAFFragmentType.getNextFragmentName(BaseLetterAFFragment.this));
            startActivity(intent);
            return true;
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            return false;
        }
    }

    protected void initView() {
    }

    protected void setRollbackFlow(final Class<? extends Activity> atyClass, final Class<? extends Fragment> fragmentClass) {
        setRollbackFlow(atyClass, fragmentClass,
                "rollback :" + atyClass.getSimpleName() + ", " + fragmentClass.getSimpleName());
    }

    protected void setRollbackFlow(final Class<? extends Activity> atyClass, final Class<? extends Fragment> fragmentClass, String text) {
        rollbackFlowBtn.setVisibility(View.VISIBLE);
        rollbackFlowBtn.setText(text);
        rollbackFlowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackFlow.request(BaseLetterAFFragment.this, atyClass, fragmentClass);
            }
        });
    }

    private void finishApp() {
        $(R.id.letter_finish_task_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackFlow.finishTask(BaseLetterAFFragment.this);
            }
        });
    }
}
