package cn.ytxu.androidbackflow.sample.request_activity_and_fragment.letter;

import cn.ytxu.androidbackflow.sample.request_activity_and_fragment.BaseLetterAFFragment;
import cn.ytxu.androidbackflow.sample.request_activity_and_fragment.ContainerAF1Activity;

/**
 * Created by newchama on 17/1/3.
 */
public class LetterAFFFragment extends BaseLetterAFFragment {

    @Override
    protected void initView() {
        super.initView();


        if (getActivity() instanceof ContainerAF1Activity) {
            setRollbackFlow(ContainerAF1Activity.class, LetterAFFFragment.class,
                    "由于根activity是MainActivity，没有Fragment，\n所以只会退到MainActivity，\n不会变成finish_app的效果");
        }
    }
}
