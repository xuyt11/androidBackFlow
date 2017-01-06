package cn.ytxu.androidbackflow;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.util.List;

/**
 * Created by ytxu on 16/12/30.
 * 回退流程，回退到制定的activity或fragment上，在这中间的activity都将finish
 * tip: 需要有BaseActivity与BaseFragment两个基础类，用于处理
 */
public class BackFlow {
    private static final String _TAG = BackFlow.class.getSimpleName();
    public static final String TAG = _TAG + "-->";

    /**
     * 这是回退功能的核心结构，其他的操作的resultCode不能与其一样，否则会有错误
     */
    public static final int RESULT_CODE = Integer.MAX_VALUE;

    /**
     * startActivity方法调用的，防止不能触发onActivityResult方法
     * 其他的requestCode，不能与其一样，否则App内部业务逻辑可能有异常情况
     * tip: Can only use lower 16 bits for requestCode
     */
    public static final int REQUEST_CODE = 0x0000ffff;


    //********************* execute back flow *********************
    public static void finishApp(Activity activity) {
        new BackFlowRequestParam.Builder(BackFlowType.finish_app, activity).create().request();
    }

    public static void finishApp(Fragment fragment) {
        new BackFlowRequestParam.Builder(BackFlowType.finish_app, fragment).create().request();
    }

    public static void request(Activity activity, @NonNull Class<? extends Activity> atyClass) {
        new BackFlowRequestParam.Builder(BackFlowType.back_to_activity, activity).setActivity(atyClass).create().request();
    }

    public static void request(Activity activity, @NonNull Bundle extra, @NonNull Class<? extends Activity> atyClass) {
        new BackFlowRequestParam.Builder(BackFlowType.back_to_activity, activity).setActivity(atyClass).setExtra(extra).create().request();
    }

    public static void request(Fragment fragment, @NonNull Class<? extends Activity> atyClass) {
        new BackFlowRequestParam.Builder(BackFlowType.back_to_activity, fragment).setActivity(atyClass).create().request();
    }

    public static void request(Fragment fragment, @NonNull Bundle extra, @NonNull Class<? extends Activity> atyClass) {
        new BackFlowRequestParam.Builder(BackFlowType.back_to_activity, fragment).setActivity(atyClass).setExtra(extra).create().request();
    }

    public static void request(Activity activity, @NonNull Class<? extends Fragment>... fragmentClazzs) {
        new BackFlowRequestParam.Builder(BackFlowType.back_to_fragments, activity).setFragments(fragmentClazzs).create().request();
    }

    public static void request(Activity activity, @NonNull Bundle extra, @NonNull Class<? extends Fragment>... fragmentClazzs) {
        new BackFlowRequestParam.Builder(BackFlowType.back_to_fragments, activity).setFragments(fragmentClazzs).setExtra(extra).create().request();
    }

    public static void request(Fragment fragment, @NonNull Class<? extends Fragment>... fragmentClazzs) {
        new BackFlowRequestParam.Builder(BackFlowType.back_to_fragments, fragment).setFragments(fragmentClazzs).create().request();
    }

    public static void request(Fragment fragment, @NonNull Bundle extra, @NonNull Class<? extends Fragment>... fragmentClazzs) {
        new BackFlowRequestParam.Builder(BackFlowType.back_to_fragments, fragment).setFragments(fragmentClazzs).setExtra(extra).create().request();
    }

    public static void request(Activity activity, @NonNull Class<? extends Activity> atyClass, @NonNull Class<? extends Fragment>... fragmentClazzs) {
        new BackFlowRequestParam.Builder(BackFlowType.back_to_activity_fragments, activity).setActivity(atyClass).setFragments(fragmentClazzs).create().request();
    }

    public static void request(Activity activity, @NonNull Bundle extra, @NonNull Class<? extends Activity> atyClass, @NonNull Class<? extends Fragment>... fragmentClazzs) {
        new BackFlowRequestParam.Builder(BackFlowType.back_to_activity_fragments, activity).setActivity(atyClass).setFragments(fragmentClazzs).setExtra(extra).create().request();
    }

    public static void request(Fragment fragment, @NonNull Class<? extends Activity> atyClass, @NonNull Class<? extends Fragment>... fragmentClazzs) {
        new BackFlowRequestParam.Builder(BackFlowType.back_to_activity_fragments, fragment).setActivity(atyClass).setFragments(fragmentClazzs).create().request();
    }

    public static void request(Fragment fragment, @NonNull Bundle extra, @NonNull Class<? extends Activity> atyClass, @NonNull Class<? extends Fragment>... fragmentClazzs) {
        new BackFlowRequestParam.Builder(BackFlowType.back_to_activity_fragments, fragment).setActivity(atyClass).setFragments(fragmentClazzs).setExtra(extra).create().request();
    }

    static void request(@NonNull Activity activity, @NonNull Intent requestData) {
        activity.setResult(RESULT_CODE, requestData);
        activity.finish();
    }


    //********************* builder request param *********************
    public static BackFlowRequestParam.Builder builder(@NonNull BackFlowType type, @NonNull Activity activity) {
        return new BackFlowRequestParam.Builder(type, activity);
    }

    public static BackFlowRequestParam.Builder builder(@NonNull BackFlowType type, @NonNull Fragment fragment) {
        return new BackFlowRequestParam.Builder(type, fragment);
    }


    //********************* handle back flow *********************

    /**
     * @return true：已经处理了，不需要再次分发给该activity的super.onActivityResult去处理；
     * false：不能处理，需要继续分发；
     */
    public static boolean handle(Activity activity, List<Fragment> fragments, int requestCode, int resultCode, Intent data) {
        Log.i(BackFlow._TAG, BackFlow.TAG + "called onActivityResult:" + activity.getClass().getSimpleName());
        if (!canHandle(resultCode, data)) {
            return false;
        }

        logData(activity.getClass().getSimpleName(), data);
        return BackFlowType.get(data).handleBackFlow(activity, fragments, requestCode, resultCode, data);
    }

    private static boolean canHandle(int resultCode, Intent data) {
        return resultCode == RESULT_CODE && BackFlowType.isBackFlowType(data);
    }

    public static void logData(String handleObject, Intent data) {
        Log.i(BackFlow._TAG, BackFlow.TAG + "╔═══════════════════════════════════════════════════════════════════════════════════════");
        Log.i(BackFlow._TAG, BackFlow.TAG + "║curr handle object:" + handleObject);

        Bundle bundle = data.getExtras();
        for (String key : bundle.keySet()) {
            String value = String.valueOf(bundle.get(key));
            Log.i(BackFlow._TAG, BackFlow.TAG + "║" + key + ":" + value);
        }

        Log.i(BackFlow._TAG, BackFlow.TAG + "╚═══════════════════════════════════════════════════════════════════════════════════════");
    }

}

