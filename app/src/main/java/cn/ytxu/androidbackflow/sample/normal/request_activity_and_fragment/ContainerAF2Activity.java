package cn.ytxu.androidbackflow.sample.normal.request_activity_and_fragment;

import android.os.Bundle;

import cn.ytxu.androidbackflow.BaseBackFlowActivity;
import cn.ytxu.androidbackflow.BaseBackFlowFragment;
import cn.ytxu.androidbackflow.R;

/**
 * Created by ytxu on 16/12/31.
 */
public class ContainerAF2Activity extends BaseBackFlowActivity {

    /**
     * sample: XXXFragment.class.getName()
     */
    public final static String PARAM_CLASSNAME = "className";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container);

        BaseBackFlowFragment fragment = getFragment();
        if (fragment == null) {
//            finish();
            return;
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commitAllowingStateLoss();
    }

    private BaseBackFlowFragment getFragment() {
        String className = getIntent().getStringExtra(PARAM_CLASSNAME);
        BaseBackFlowFragment fragment = null;
        if (className != null && !className.isEmpty()) {
            try {
                fragment = (BaseBackFlowFragment) getClassLoader().loadClass(className).newInstance();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return fragment;
    }

}
