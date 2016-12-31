package cn.ytxu.androidbackflow;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;

/**
 * Created by ytxu on 16/12/30.
 * 回退流程，回退到制定的activity或fragment上，在这中间的activity都将finish
 * tip: 需要有BaseActivity与BaseFragment两个基础类，用于处理
 */
public class BackFlow {
    public static final String TAG = BackFlow.class.getName();
    /**
     * 这是回退功能的核心结构，其他的操作的resultCode不能与其一样，否则会有错误
     */
    public static final int RESULT_CODE = Integer.MAX_VALUE;
    /**
     * startActivity方法调用的，防止不能触发onActivityResult方法
     * 其他的requestCode，不能与其一样，否则会出错
     * Can only use lower 16 bits for requestCode
     */
    public static final int REQUEST_CODE = 0x0000ffff;


    //********************* execute back *********************
    public static void finishApp(Activity activity) {
        BackFlowType.finish_app.requestBackFlow(activity, null, null);
    }

    public static void finishApp(Fragment fragment) {
        BackFlowType.finish_app.requestBackFlow(fragment.getActivity(), null, null);
    }

    public static <A extends Activity> void request(Activity activity, @NonNull Class<A> atyClass) {
        BackFlowType.back_to_activity.requestBackFlow(activity, atyClass.getName(), null);
    }

    public static <A extends Activity> void request(Fragment fragment, @NonNull Class<A> atyClass) {
        BackFlowType.back_to_activity.requestBackFlow(fragment.getActivity(), atyClass.getName(), null);
    }

    public static <F extends Fragment> void requestF(Activity activity, @NonNull Class<F> fragmentClass) {
        BackFlowType.back_to_fragment.requestBackFlow(activity, null, fragmentClass.getName());
    }

    public static <F extends Fragment> void requestF(Fragment fragment, @NonNull Class<F> fragmentClass) {
        BackFlowType.back_to_fragment.requestBackFlow(fragment.getActivity(), null, fragmentClass.getName());
    }

    public static <A extends Activity, F extends Fragment> void request(Activity activity, @NonNull Class<A> atyClass, @NonNull Class<F> fragmentClass) {
        BackFlowType.back_to_activity_fragment.requestBackFlow(activity, atyClass.getName(), fragmentClass.getName());
    }

    public static <A extends Activity, F extends Fragment> void request(Fragment fragment, @NonNull Class<A> atyClass, @NonNull Class<F> fragmentClass) {
        BackFlowType.back_to_activity_fragment.requestBackFlow(fragment.getActivity(), atyClass.getName(), fragmentClass.getName());
    }


    //********************* handle back *********************

    /**
     * @return 不需要再次分发给BaseActivity去处理，否则继续分发给BaseActivity，
     */
    public static boolean handle(Activity activity, int resultCode, Intent data) {
        Log.e(TAG, "ytxu handle:" + activity.getClass().getName());
        printlog(resultCode, data);
        if (!canHandle(resultCode, data)) {
            return false;
        }

        BackFlowType backFlowType = BackFlowType.get(data.getIntExtra(BackFlowType.BACK_FLOW_TYPE, 0));
        return backFlowType.handleBackFlow(activity, resultCode, data);
    }

    /**
     * activity 已经处理了finishApp，到了这就证明，不是finishApp，所以不需要处理finishApp
     */
    public static boolean handle(Fragment fragment, int resultCode, Intent data) {
        Log.e(TAG, "ytxu handle:" + fragment.getClass().getName());

        printlog(resultCode, data);
        if (!canHandle(resultCode, data)) {
            return false;
        }

        BackFlowType backFlowType = BackFlowType.get(data.getIntExtra(BackFlowType.BACK_FLOW_TYPE, 0));
        return backFlowType.handleBackFlow(fragment, resultCode, data);
    }

    private static void printlog(int resultCode, Intent data) {
        String tip;
        if (data == null) {
            tip = "data is null...";
        } else if (data.getExtras() == null) {
            tip = "extras is null....";
        } else {
            tip = data.getExtras().toString();
        }
        Log.e(TAG, "result code:" + resultCode + ", data:" + tip);
    }

    private static boolean canHandle(int resultCode, Intent data) {
        if (resultCode != RESULT_CODE || data == null) {
            return false;
        }

        return data.hasExtra(BackFlowType.BACK_FLOW_TYPE);
    }

}


enum BackFlowType {
    /**
     * 结束整个App的所有的activity
     */
    finish_app(0) {
        @Override
        public void requestBackFlow(Activity activity, String activityClassName, String fragmentClassName) {
            Intent data = new Intent();
            data.putExtra(BACK_FLOW_TYPE, type);
            activity.setResult(BackFlow.RESULT_CODE, data);
            activity.finish();
            Log.e(BackFlow.TAG, "ytxu finish:" + activity.getClass().getName());
        }

        @Override
        public boolean handleBackFlow(Activity activity, int resultCode, Intent data) {
            return finishApp(activity);
        }

        private boolean finishApp(Activity activity) {
            requestBackFlow(activity, null, null);
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
    back_to_activity(1) {
        @Override
        public void requestBackFlow(Activity activity, String activityClassName, String fragmentClassName) {
            Intent data = new Intent();
            data.putExtra(BACK_FLOW_TYPE, type);
            data.putExtra(BACK_TO_ACTIVITY, activityClassName);
            activity.setResult(BackFlow.RESULT_CODE, data);
            activity.finish();
        }

        @Override
        public boolean handleBackFlow(Activity activity, int resultCode, Intent data) {
            String currActivityClassName = activity.getClass().getName();
            String targetActivityClassName = data.getStringExtra(BACK_TO_ACTIVITY);

            if (!currActivityClassName.equals(targetActivityClassName)) {// not arrived target activity
                requestBackFlow(activity, targetActivityClassName, null);
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
    back_to_fragment(2) {
        @Override
        public void requestBackFlow(Activity activity, String activityClassName, String fragmentClassName) {
            Intent data = new Intent();
            data.putExtra(BACK_FLOW_TYPE, type);
            data.putExtra(BACK_TO_FRAGMENT, fragmentClassName);
            activity.setResult(BackFlow.RESULT_CODE, data);
            activity.finish();
        }

        @Override
        public boolean handleBackFlow(Activity activity, int resultCode, Intent data) {
            return false;
        }

        @Override
        public boolean handleBackFlow(Fragment fragment, int resultCode, Intent data) {
            String currFragmentClassName = fragment.getClass().getName();
            String targetFragmentClassName = data.getStringExtra(BACK_TO_FRAGMENT);

            if (!currFragmentClassName.equals(targetFragmentClassName)) {// not arrived target fragment
                requestBackFlow(fragment.getActivity(), targetFragmentClassName, null);
            }
            return true;
        }
    },
    /**
     * 返回到activity和fragment都相同的位置
     */
    back_to_activity_fragment(3) {
        @Override
        public void requestBackFlow(Activity activity, String activityClassName, String fragmentClassName) {
            Intent data = new Intent();
            data.putExtra(BACK_FLOW_TYPE, type);
            data.putExtra(BACK_TO_ACTIVITY, activityClassName);
            data.putExtra(BACK_TO_FRAGMENT, fragmentClassName);
            activity.setResult(BackFlow.RESULT_CODE, data);
            activity.finish();
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
            if (!currFragmentClassName.equals(targetFragmentClassName) || !currActivityClassName.equals(targetActivityClassName)) {
                requestBackFlow(fragment.getActivity(), targetFragmentClassName, targetActivityClassName);
            }
            return true;
        }
    };

    public static final String BACK_FLOW_TYPE = "back_flow_type";
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

    public int getType() {
        return type;
    }

    public abstract void requestBackFlow(Activity activity, String activityClassName, String fragmentClassName);

    /**
     * @return handled 是否处理了；
     * true:不需要再次分发给BaseActivity去处理，否则继续分发给BaseActivity，
     */
    public abstract boolean handleBackFlow(Activity activity, int resultCode, Intent data);

    /**
     * @return handled 是否处理了；
     * true:不需要再次分发给BaseF去处理，否则继续分发给BaseActivity，
     */
    public abstract boolean handleBackFlow(Fragment fragment, int resultCode, Intent data);


    public static BackFlowType get(int type) {
        for (BackFlowType backFlowType : BackFlowType.values()) {
            if (backFlowType.type == type) {
                return backFlowType;
            }
        }
        // error status
        Log.w("BackFlowType", new Throwable("error back flow type(" + type + "), so return finisn_app back flow type"));
        return finish_app;
    }

}