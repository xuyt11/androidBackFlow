package cn.ytxu.androidbackflow.sample.fragment_contains_sub_fragment.first_level;

import android.content.Intent;

/**
 * Created by ytxu on 17/1/6.
 */
public class FcsfAFragment extends ContainsSFFragment {

    @Override
    protected void initView() {
        super.initView();
        jump2NextView(FcsfBFragment.class);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
