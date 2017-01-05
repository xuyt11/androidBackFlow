package cn.ytxu.androidbackflow;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.util.List;

/**
 * tip:
 * 1、若在回退链中间有任何一个TempActivity消耗过onActivityResult方法，<br>
 * 则会停留在该TempActivity，不能继续回退 <br>
 * 2、现在发现的消耗onActivityResult方法的情况有：<br>
 * a: 切换task；<br>
 * b: 切换process；<br>
 * c: 在startActivity时，调用了intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);<br>
 */
enum BackFlowType {
    /**
     * 所有错误的type，都将返回该类型，且都不会处理
     */
    error(BackFlowExtra.ERROR_BACK_FLOW_TYPE) {
        @Override
        public <A extends Activity, F extends Fragment> void requestBackFlow(Activity activity, @Nullable Class<A> atyClass, @NonNull List<Class<F>> fragmentClazzs) {
            throw new IllegalArgumentException("error back flow type");
        }

        @Override
        public boolean handleBackFlow(Activity activity, List<Fragment> fragments, int requestCode, int resultCode, Intent data) {
            Log.w(TAG, new Throwable("error back flow type"));
            return false;
        }
    },
    /**
     * 结束App功能：结束App中所有的activity（准确的说是：finish该task中所有的activity）
     */
    finish_app(1) {
        @Override
        public <A extends Activity, F extends Fragment> void requestBackFlow(Activity activity, @Nullable Class<A> atyClass, @NonNull List<Class<F>> fragmentClazzs) {
            Intent data = initData();
            requestBackFlowInner(activity, data);
        }

        @Override
        public boolean handleBackFlow(Activity activity, List<Fragment> fragments, int requestCode, int resultCode, Intent data) {
            requestBackFlowInner(activity, data);// request again
            return true;
        }
    },
    /**
     * 返回到指定的activity（回退到指定的activity）
     */
    back_to_activity(2) {
        @Override
        public <A extends Activity, F extends Fragment> void requestBackFlow(Activity activity, @Nullable Class<A> atyClass, @NonNull List<Class<F>> fragmentClazzs) {
            Intent data = BackFlowExtra.putActivity(initData(), atyClass);
            requestBackFlowInner(activity, data);
        }

        @Override
        public boolean handleBackFlow(Activity activity, List<Fragment> fragments, int requestCode, int resultCode, Intent data) {
            String currActivityClassName = activity.getClass().getName();
            String targetActivityClassName = BackFlowExtra.getActivity(data);

            if (!isMatch(currActivityClassName, targetActivityClassName)) {// not arrived target activity
                requestBackFlowInner(activity, data);// request again
            }
            return true;
        }
    },
    /**
     * 返回到指定的fragment列（回退到包含了指定fragment列的activity）
     */
    back_to_fragment(3) {
        @Override
        public <A extends Activity, F extends Fragment> void requestBackFlow(Activity activity, @Nullable Class<A> atyClass, @NonNull List<Class<F>> fragmentClazzs) {
            Intent data = BackFlowExtra.putFragments(initData(), fragmentClazzs);
            requestBackFlowInner(activity, data);
        }

        @Override
        public boolean handleBackFlow(Activity activity, List<Fragment> fragments, int requestCode, int resultCode, Intent data) {
            List<String> fragmentClassNames = BackFlowExtra.getFragments(data);
            try {
                Fragment fragment = BackFlowFindTargetFragmentHelper.findTargetFragment(fragments, fragmentClassNames.listIterator());
                fragment.onActivityResult(requestCode, resultCode, data);
                return true;
            } catch (BackFlowFindTargetFragmentHelper.NotFindTargetFragmentException e) {
                requestBackFlowInner(activity, data);// send request again
                return true;
            }
        }
    },

    /**
     * 返回到activity和fragment列都一致的activity
     */
    back_to_activity_fragment(4) {
        @Override
        public <A extends Activity, F extends Fragment> void requestBackFlow(Activity activity, @Nullable Class<A> atyClass, @NonNull List<Class<F>> fragmentClazzs) {
            Intent data = BackFlowExtra.putActivity(initData(), atyClass);
            BackFlowExtra.putFragments(data, fragmentClazzs);
            requestBackFlowInner(activity, data);
        }

        @Override
        public boolean handleBackFlow(Activity activity, List<Fragment> fragments, int requestCode, int resultCode, Intent data) {
            String currActivityClassName = activity.getClass().getName();
            String targetActivityClassName = BackFlowExtra.getActivity(data);
            if (!isMatch(currActivityClassName, targetActivityClassName)) {
                requestBackFlowInner(activity, data);// send request again
                return true;
            }

            List<String> fragmentClassNames = BackFlowExtra.getFragments(data);
            try {
                Fragment fragment = BackFlowFindTargetFragmentHelper.findTargetFragment(fragments, fragmentClassNames.listIterator());
                fragment.onActivityResult(requestCode, resultCode, data);
                return true;
            } catch (BackFlowFindTargetFragmentHelper.NotFindTargetFragmentException e) {
                requestBackFlowInner(activity, data);// send request again
                return true;
            }
        }
    };

    public static final String TAG = BackFlowType.class.getSimpleName();


    protected final int type;

    BackFlowType(int type) {
        this.type = type;
    }

    /**
     * 1、若activityClassName!=null，则会回退到该activity；
     * 若有多个activity实例，则只会回退到第一个匹配；
     * 2、若fragmentClassName!=null，则会回退到该fragment；
     * 若有多个fragment实例，则只会回退到第一个匹配；
     * 3、若activityClassName!=null && fragmentClassName!=null，则会回退到包含了该fragment的activity；
     * 但，activity只能包含单个fragment
     * 4、若在整个回退流程流程中，没有匹配目标，则相当于finish_app的功能。
     *
     * @param activityClassName 回退到该activity
     * @param fragmentClazzs    回退到该fragment的顺序列表
     */
    public abstract <A extends Activity, F extends Fragment> void requestBackFlow(Activity activity, @Nullable Class<A> atyClass, @NonNull List<Class<F>> fragmentClazzs);

    protected Intent initData() {
        return BackFlowExtra.init(type);
    }

    protected void requestBackFlowInner(Activity activity, Intent data) {
        activity.setResult(BackFlow.RESULT_CODE, data);
        activity.finish();
    }

    /**
     * @return handled 是否处理了；
     * true:不需要再次分发给BaseActivity去处理，否则继续分发给BaseActivity，
     * 若找到目标组，则停止分发；
     * 且若目标为fragment，会调用onActivityResult(resultCode, resultCode, data)方法
     */
    public abstract boolean handleBackFlow(Activity activity, List<Fragment> fragments, int requestCode, int resultCode, Intent data);

    protected boolean isMatch(String currClassName, String targetClassName) {
        return currClassName.equals(targetClassName);
    }

    public static BackFlowType get(Intent data) {
        int type = BackFlowExtra.getType(data);
        for (BackFlowType backFlowType : BackFlowType.values()) {
            if (backFlowType.type == type) {
                return backFlowType;
            }
        }
        return error;
    }


    public static boolean isBackFlowType(Intent data) {
        return BackFlowExtra.isBackFlowType(data);
    }
}


