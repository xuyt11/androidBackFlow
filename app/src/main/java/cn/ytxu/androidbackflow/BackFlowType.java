package cn.ytxu.androidbackflow;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;

enum BackFlowType {
    /**
     * 所有错误的type，都将返回该类型，且所有这些都不会处理
     */
    error(0){
        @Override
        public void requestBackFlow(Activity activity, String activityClassName, String fragmentClassName) {
            throw new IllegalArgumentException("error back flow type");
        }

        @Override
        public boolean handleBackFlow(Activity activity, int resultCode, Intent data) {
            Log.w(BackFlowType.class.getSimpleName(), new Throwable("error back flow type"));
            return false;
        }

        @Override
        public boolean handleBackFlow(Fragment fragment, int resultCode, Intent data) {
            Log.w(BackFlowType.class.getSimpleName(), new Throwable("error back flow type"));
            return false;
        }
    },
    /**
     * 结束整个App的所有的activity
     */
    finish_app(1) {
        @Override
        public void requestBackFlow(Activity activity, String activityClassName, String fragmentClassName) {
            Intent data = initData();
            requestBackFlowInner(activity, data);
        }

        @Override
        public boolean handleBackFlow(Activity activity, int resultCode, Intent data) {
            requestBackFlowInner(activity, data);// request again
            return true;
        }

        @Override
        public boolean handleBackFlow(Fragment fragment, int resultCode, Intent data) {// 多余的，在activity中就会调用finishApp，不会传递到fragment
            throw new RuntimeException("在activity中就会处理finishApp，不会传递到fragment");
        }
    },
    /**
     * 返回到指定的activity
     */
    back_to_activity(2) {
        @Override
        public void requestBackFlow(Activity activity, String activityClassName, String fragmentClassName) {
            Intent data = initData().putExtra(BACK_TO_ACTIVITY, activityClassName);
            requestBackFlowInner(activity, data);
        }

        @Override
        public boolean handleBackFlow(Activity activity, int resultCode, Intent data) {
            String currActivityClassName = activity.getClass().getName();
            String targetActivityClassName = data.getStringExtra(BACK_TO_ACTIVITY);

            if (!isMatch(currActivityClassName, targetActivityClassName)) {// not arrived target activity
                requestBackFlowInner(activity, data);// request again
            }
            return true;
        }

        @Override
        public boolean handleBackFlow(Fragment fragment, int resultCode, Intent data) {
            throw new RuntimeException("在activity中就会处理，不会传递到fragment");
        }
    },
    /**
     * 返回到指定的fragment
     */
    back_to_fragment(3) {
        @Override
        public void requestBackFlow(Activity activity, String activityClassName, String fragmentClassName) {
            Intent data = initData().putExtra(BACK_TO_FRAGMENT, fragmentClassName);
            requestBackFlowInner(activity, data);
        }

        @Override
        public boolean handleBackFlow(Activity activity, int resultCode, Intent data) {
            return false;
        }

        @Override
        public boolean handleBackFlow(Fragment fragment, int resultCode, Intent data) {
            String currFragmentClassName = fragment.getClass().getName();
            String targetFragmentClassName = data.getStringExtra(BACK_TO_FRAGMENT);

            if (!isMatch(currFragmentClassName, targetFragmentClassName)) {// not arrived target fragment
                requestBackFlowInner(fragment.getActivity(), data);// request again
            }
            return true;
        }
    },
    /**
     * 返回到activity和fragment都相同的位置
     */
    back_to_activity_fragment(4) {
        @Override
        public void requestBackFlow(Activity activity, String activityClassName, String fragmentClassName) {
            Intent data = initData().putExtra(BACK_TO_ACTIVITY, activityClassName)
                    .putExtra(BACK_TO_FRAGMENT, fragmentClassName);
            requestBackFlowInner(activity, data);
        }

        @Override
        public boolean handleBackFlow(Activity activity, int resultCode, Intent data) {
            return false;
        }

        @Override
        public boolean handleBackFlow(Fragment fragment, int resultCode, Intent data) {
            String currFragmentClassName = fragment.getClass().getName();
            String currActivityClassName = fragment.getActivity().getClass().getName();

            String targetActivityClassName = data.getStringExtra(BACK_TO_ACTIVITY);
            String targetFragmentClassName = data.getStringExtra(BACK_TO_FRAGMENT);

            // must arrived target activity and fragment at the same time
            if (!isMatch(currFragmentClassName, targetFragmentClassName)
                    || !isMatch(currActivityClassName, targetActivityClassName)) {
                requestBackFlowInner(fragment.getActivity(), data);// request again
            }
            return true;
        }
    };

    public static final String BACK_FLOW_TYPE = "back_flow_type";
    public static final int ERROR_BACK_FLOW_TYPE = -1;
    /**
     * 返回到指定的activity
     * type is String
     */
    public static final String BACK_TO_ACTIVITY = "back_to_activity";
    /**
     * 返回到指定的fragment
     * type is String
     */
    public static final String BACK_TO_FRAGMENT = "back_to_fragment";


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
     * @param fragmentClassName 回退到该fragment
     */
    public abstract void requestBackFlow(Activity activity, String activityClassName, String fragmentClassName);

    protected Intent initData() {
        return new Intent().putExtra(BACK_FLOW_TYPE, type);
    }

    protected void requestBackFlowInner(Activity activity, Intent data) {
        activity.setResult(BackFlow.RESULT_CODE, data);
        activity.finish();
    }

    /**
     * @return handled 是否处理了；
     * true:不需要再次分发给BaseActivity去处理，否则继续分发给BaseActivity，
     */
    public abstract boolean handleBackFlow(Activity activity, int resultCode, Intent data);

    /**
     * @return handled 是否已处理
     * true：已经处理了，不需要再次分发给该activity或fragment去处理；
     * false：不能处理，需要继续分发；
     */
    public abstract boolean handleBackFlow(Fragment fragment, int resultCode, Intent data);

    protected boolean isMatch(String currClassName, String targetClassName) {
        return currClassName.equals(targetClassName);
    }

    public static BackFlowType get(Intent data) {
        return BackFlowType.get(data.getIntExtra(BACK_FLOW_TYPE, ERROR_BACK_FLOW_TYPE));
    }

    public static BackFlowType get(int type) {
        for (BackFlowType backFlowType : BackFlowType.values()) {
            if (backFlowType.type == type) {
                return backFlowType;
            }
        }
        return error;
    }

}