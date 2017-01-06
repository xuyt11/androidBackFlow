package cn.ytxu.androidbackflow.sample.fragment_contains_sub_fragment.first_level;

/**
 * Created by ytxu on 17/1/6.
 */
public class FcsfBFragment extends ContainsSFFragment {

    @Override
    protected void initView() {
        super.initView();
        jump2NextView(FcsfCFragment.class);
    }
}
