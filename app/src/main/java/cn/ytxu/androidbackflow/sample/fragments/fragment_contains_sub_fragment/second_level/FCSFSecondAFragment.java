package cn.ytxu.androidbackflow.sample.fragments.fragment_contains_sub_fragment.second_level;

import android.view.View;

import cn.ytxu.androidbackflow.BackFlow;
import cn.ytxu.androidbackflow.sample.fragments.fragment_contains_sub_fragment.first_level.FcsfAFragment;

/**
 * Created by ytxu on 17/1/5.
 */
public class FCSFSecondAFragment extends FCSFSecondFragment {

    @Override
    protected void initView() {
        super.initView();

        if (getParentFragment() instanceof FcsfAFragment) {
            requestBtn.setVisibility(View.VISIBLE);
            requestBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BackFlow.request(FCSFSecondAFragment.this, FcsfAFragment.class, FCSFSecondAFragment.class);
                }
            });
            requestBtn.setText("unmatch this request back flow :\n"
                    + FcsfAFragment.class.getSimpleName() + "," + FCSFSecondAFragment.class.getSimpleName()
                    + "\n convert to finish_task back flow type");
        } else {

        }
    }
}
