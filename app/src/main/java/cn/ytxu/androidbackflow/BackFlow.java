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


    //********************* quickly request back flow *********************

    //********************* finish task *********************
    public static void finishTask(Activity activity) {
        builder(BackFlowType.finish_task, activity).create().request();
    }

    public static void finishTask(Fragment fragment) {
        builder(BackFlowType.finish_task, fragment).create().request();
    }

    //********************* request activity *********************
    public static void request(Activity activity, @NonNull Class<? extends Activity> atyClass) {
        builder(BackFlowType.back_to_activity, activity).setActivity(atyClass).create().request();
    }

    public static void request(Fragment fragment, @NonNull Class<? extends Activity> atyClass) {
        builder(BackFlowType.back_to_activity, fragment).setActivity(atyClass).create().request();
    }

    //********************* request fragments *********************
    public static void request(Activity activity, @NonNull Class<? extends Fragment>... fragmentClazzs) {
        builder(BackFlowType.back_to_fragments, activity).setFragments(fragmentClazzs).create().request();
    }

    public static void request(Fragment fragment, @NonNull Class<? extends Fragment>... fragmentClazzs) {
        builder(BackFlowType.back_to_fragments, fragment).setFragments(fragmentClazzs).create().request();
    }

    //********************* request activity and fragments *********************
    public static void request(Activity activity, @NonNull Class<? extends Activity> atyClass, @NonNull Class<? extends Fragment>... fragmentClazzs) {
        builder(BackFlowType.back_to_activity_fragments, activity).setActivity(atyClass).setFragments(fragmentClazzs).create().request();
    }

    public static void request(Fragment fragment, @NonNull Class<? extends Activity> atyClass, @NonNull Class<? extends Fragment>... fragmentClazzs) {
        builder(BackFlowType.back_to_activity_fragments, fragment).setActivity(atyClass).setFragments(fragmentClazzs).create().request();
    }

    //********************* request activity count *********************
    public static void request(Activity activity, int backActivityCount) {
        builder(BackFlowType.back_activity_count, activity).setBackActivityCount(backActivityCount).create().request();
    }

    public static void request(Fragment fragment, int backActivityCount) {
        builder(BackFlowType.back_activity_count, fragment).setBackActivityCount(backActivityCount).create().request();
    }

    static void request(@NonNull Activity activity, @NonNull Intent backFlowData) {
        activity.setResult(RESULT_CODE, backFlowData);
        activity.finish();
    }


    //********************* builder request param and get extra *********************
    public static BackFlowParam.Builder builder(@NonNull BackFlowType type, @NonNull Activity activity) {
        return new BackFlowParam.Builder(type, activity);
    }

    public static BackFlowParam.Builder builder(@NonNull BackFlowType type, @NonNull Fragment fragment) {
        return new BackFlowParam.Builder(type, fragment);
    }

    public static boolean hasExtra(Intent data) {
        return BackFlowIntent.hasExtra(data);
    }

    public static Bundle getExtra(Intent data) {
        return BackFlowIntent.getExtra(data);
    }


    //********************* handle back flow *********************

    /**
     * @return true：已经处理了，不需要再次分发给该activity的super.onActivityResult去处理；
     * false：不能处理，需要继续分发；
     */
    public static boolean handle(Activity activity, List<Fragment> fragments, int requestCode, int resultCode, Intent data) {
        Logger.log(BackFlow._TAG, "called onActivityResult:" + activity.getClass().getSimpleName());
        if (!canHandle(resultCode, data)) {
            return false;
        }

        Logger.logIntent(activity, data);
        return BackFlowType.get(data).handle(activity, fragments, requestCode, resultCode, data);
    }

    private static boolean canHandle(int resultCode, Intent data) {
        return resultCode == RESULT_CODE && BackFlowType.isBackFlowType(data);
    }


    //********************* back flow log *********************
    public static final class Logger {
        public static void logIntent(Activity handleObject, Intent data) {
            logIntent(handleObject.getClass().getSimpleName(), data);
        }

        public static void logIntent(Fragment handleObject, Intent data) {
            logIntent(handleObject.getClass().getSimpleName(), data);
        }

        public static void logIntent(String handleObjectSimpleName, Intent data) {
            if (data == null || data.getExtras() == null) {
                return;
            }

            log(BackFlow._TAG, "╔═══════════════════════════════════════════════════════════════════════════════════════");
            log(BackFlow._TAG, "║curr handle object:" + handleObjectSimpleName);

            Bundle bundle = data.getExtras();
            for (String key : bundle.keySet()) {
                String value = String.valueOf(bundle.get(key));
                log(BackFlow._TAG, "║" + key + ":" + value);
            }

            log(BackFlow._TAG, "╚═══════════════════════════════════════════════════════════════════════════════════════");
        }

        /**
         * u can setup switch to manage the log print
         */
        public static void log(String tag, String msg) {
            Log.i(tag, BackFlow.TAG + msg);
        }
    }

}

