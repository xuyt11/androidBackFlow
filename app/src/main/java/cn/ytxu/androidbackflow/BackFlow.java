package cn.ytxu.androidbackflow;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;

/**
 * Created by ytxu on 16/12/30.
 * 回退流程，回退到制定的activity或fragment上，在这中间的activity都将finish
 * tip: 需要有BaseActivity与BaseFragment两个基础类，用于处理
 */
public class BackFlow {
    public static final String TAG = BackFlow.class.getSimpleName();
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
     * @return true：已经处理了，不需要再次分发给该activity去处理；
     * false：不能处理，需要继续分发；
     */
    public static boolean handle(Activity activity, int resultCode, Intent data) {
        if (!canHandle(resultCode, data)) {
            return false;
        }

        logData("handle activity", activity.getClass().getName(), data);
        return BackFlowType.get(data).handleBackFlow(activity, resultCode, data);
    }

    /**
     * tip: 在activity已经处理了finishApp与back_to_activity两个分类，
     * 不需要处理finishApp与back_to_activity两个分类
     *
     * @return true：已经处理了，不需要再次分发给该fragment去处理；
     * false：不能处理，需要继续分发；
     */
    public static boolean handle(Fragment fragment, int resultCode, Intent data) {
        if (!canHandle(resultCode, data)) {
            return false;
        }

        logData("handle fragment", fragment.getClass().getName(), data);
        return BackFlowType.get(data).handleBackFlow(fragment, resultCode, data);
    }

    private static boolean canHandle(int resultCode, Intent data) {
        if (resultCode != RESULT_CODE || data == null) {
            return false;
        }

        return data.hasExtra(BackFlowType.BACK_FLOW_TYPE);
    }

    private static void logData(String handleTag, String handleObject, Intent data) {
        Log.i(TAG, "ytxu-->╔═══════════════════════════════════════════════════════════════════════════════════════");
        Log.i(TAG, "ytxu-->║" + handleTag + ":" + handleObject);

        Bundle bundle = data.getExtras();
        for (String key : bundle.keySet()) {
            String value = String.valueOf(bundle.get(key));
            Log.i(TAG, "ytxu-->║" + key + ":" + value);
        }

        Log.i(TAG, "ytxu-->╚═══════════════════════════════════════════════════════════════════════════════════════");
    }

}

