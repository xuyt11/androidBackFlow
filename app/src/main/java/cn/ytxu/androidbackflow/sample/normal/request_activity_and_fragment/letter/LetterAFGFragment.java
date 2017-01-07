package cn.ytxu.androidbackflow.sample.normal.request_activity_and_fragment.letter;

import cn.ytxu.androidbackflow.sample.normal.request_activity_and_fragment.BaseLetterAFFragment;
import cn.ytxu.androidbackflow.sample.normal.request_activity_and_fragment.ContainerAF1Activity;
import cn.ytxu.androidbackflow.sample.normal.request_activity_and_fragment.ContainerAF2Activity;

/**
 * Created by newchama on 17/1/3.
 */
public class LetterAFGFragment extends BaseLetterAFFragment {

    @Override
    protected void initView() {
        super.initView();

        if (getActivity() instanceof ContainerAF2Activity) {
            setRollbackFlow(ContainerAF1Activity.class, LetterAFFFragment.class);
        }
    }
}
