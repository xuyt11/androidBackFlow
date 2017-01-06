package cn.ytxu.androidbackflow.sample.fragment_contains_sub_fragment.second_level;

import android.view.View;

import cn.ytxu.androidbackflow.BackFlow;
import cn.ytxu.androidbackflow.sample.fragment_contains_sub_fragment.first_level.FcsfAFragment;
import cn.ytxu.androidbackflow.sample.fragment_contains_sub_fragment.first_level.FcsfDFragment;

/**
 * Created by ytxu on 17/1/5.
 */
public class FCSFSecondDFragment extends FCSFSecondFragment {


    @Override
    protected void initView() {
        super.initView();

        if (getParentFragment() instanceof FcsfDFragment) {
            requestBtn.setVisibility(View.VISIBLE);
            requestBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BackFlow.request(FCSFSecondDFragment.this, FcsfAFragment.class, FCSFSecondAFragment.class);
                }
            });
            requestBtn.setText("request back flow to :\n" + FcsfAFragment.class.getSimpleName()
                    + "\n" + FCSFSecondAFragment.class.getSimpleName());
        } else {

        }
    }
}
