package cn.ytxu.androidbackflow;

import android.app.Activity;
import android.content.Intent;
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
public enum BackFlowType {
    /**
     * 所有错误的type，都将返回该类型，且都不会处理
     */
    error(BackFlowIntent.ERROR_BACK_FLOW_TYPE) {
        @Override
        Intent createRequestData(BackFlowParam param) {
            throw new IllegalArgumentException("error back flow type");
        }

        @Override
        boolean handle(Activity activity, List<Fragment> fragments, int requestCode, int resultCode, Intent data) {
            Log.w(BackFlowType.class.getSimpleName(), new Throwable(BackFlow.TAG + "error back flow type"));
            return false;
        }
    },

    /**
     * 结束task：若该App是单task，则有结束App中所有的activity效果（finish该task中所有的activity）<br>
     * 1、若在整个回退流程流程中，没有匹配到目标，也相当于finish_task的功能。
     * 2、若中间有onActivityResult方法被消耗，则会停留在最后一个被消耗的activity（因为setResult已无效）。
     */
    finish_task(1) {
        @Override
        Intent createRequestData(BackFlowParam param) {
            if (param.atyClass != null || !param.fragmentClazzs.isEmpty()) {
                throw new IllegalArgumentException("error back flow param");
            }
            return new BackFlowIntent.Builder(type).putExtra(param.extra).create();
        }

        @Override
        boolean handle(Activity activity, List<Fragment> fragments, int requestCode, int resultCode, Intent data) {
            BackFlow.request(activity, data);// send request again
            return true;
        }
    },

    /**
     * 返回到指定的activity（回退到指定的activity），若有多个activity实例，则只会回退到第一个匹配；<br>
     * atyClass!=null && fragmentClazzs.isEmpty()
     */
    back_to_activity(2) {
        @Override
        Intent createRequestData(BackFlowParam param) {
            if (param.atyClass == null) {
                throw new IllegalArgumentException("atyClass param must be non null");
            }
            if (!param.fragmentClazzs.isEmpty()) {
                throw new IllegalArgumentException("error back flow param");
            }
            return new BackFlowIntent.Builder(type).putExtra(param.extra)
                    .putActivity(param.atyClass).create();
        }

        @Override
        boolean handle(Activity activity, List<Fragment> fragments, int requestCode, int resultCode, Intent data) {
            String targetActivityClassName = BackFlowIntent.getActivity(data);
            if (!BackFlowViewHelper.isTargetActivity(activity, targetActivityClassName)) {
                BackFlow.request(activity, data);// send request again
            }
            return true;
        }
    },

    /**
     * 返回到指定的fragment列（回退到第一个匹配该fragment顺序列的activity）<br>
     * atyClass==null && !fragmentClazzs.isEmpty()
     */
    back_to_fragments(3) {
        @Override
        Intent createRequestData(BackFlowParam param) {
            if (param.atyClass != null) {
                throw new IllegalArgumentException("atyClass param must be null");
            }
            if (param.fragmentClazzs.isEmpty()) {
                throw new IllegalArgumentException("fragmentClazzs param must not empty");
            }
            return new BackFlowIntent.Builder(type).putExtra(param.extra)
                    .putFragments(param.fragmentClazzs).create();
        }

        @Override
        boolean handle(Activity activity, List<Fragment> fragments, int requestCode, int resultCode, Intent data) {
            List<String> fragmentClassNames = BackFlowIntent.getFragments(data);
            try {
                Fragment fragment = BackFlowViewHelper.findTargetFragment(fragments, fragmentClassNames.listIterator());
                fragment.onActivityResult(requestCode, resultCode, data);
                return true;
            } catch (BackFlowViewHelper.NotFindTargetFragmentException e) {
                BackFlow.request(activity, data);// send request again
                return true;
            }
        }
    },

    /**
     * 返回到activity和fragment列都一致的activity（回退到包含了该fragment顺序列的activity）<br>
     * atyClass!=null && !fragmentClazzs.isEmpty()
     */
    back_to_activity_fragments(4) {
        @Override
        Intent createRequestData(BackFlowParam param) {
            if (param.atyClass == null) {
                throw new IllegalArgumentException("atyClass param must be non null");
            }
            if (param.fragmentClazzs.isEmpty()) {
                throw new IllegalArgumentException("fragmentClazzs param must not empty");
            }
            return new BackFlowIntent.Builder(type).putExtra(param.extra)
                    .putActivity(param.atyClass).putFragments(param.fragmentClazzs)
                    .create();
        }

        @Override
        boolean handle(Activity activity, List<Fragment> fragments, int requestCode, int resultCode, Intent data) {
            String targetActivityClassName = BackFlowIntent.getActivity(data);
            if (!BackFlowViewHelper.isTargetActivity(activity, targetActivityClassName)) {
                BackFlow.request(activity, data);// send request again
                return true;
            }

            List<String> fragmentClassNames = BackFlowIntent.getFragments(data);
            try {
                Fragment fragment = BackFlowViewHelper.findTargetFragment(fragments, fragmentClassNames.listIterator());
                fragment.onActivityResult(requestCode, resultCode, data);
                return true;
            } catch (BackFlowViewHelper.NotFindTargetFragmentException e) {
                BackFlow.request(activity, data);// send request again
                return true;
            }
        }
    };

    protected final int type;

    BackFlowType(int type) {
        this.type = type;
    }

    abstract Intent createRequestData(BackFlowParam param);


    /**
     * @return handled 是否处理了；
     * true:不需要再次分发给BaseActivity去处理，否则继续分发给BaseActivity，
     * 若找到目标组，则停止分发；
     * 且若目标为fragment，会调用onActivityResult(resultCode, resultCode, data)方法
     */
    abstract boolean handle(Activity activity, List<Fragment> fragments, int requestCode, int resultCode, Intent requestData);


    static BackFlowType get(Intent data) {
        int type = BackFlowIntent.getType(data);
        for (BackFlowType backFlowType : BackFlowType.values()) {
            if (backFlowType.type == type) {
                return backFlowType;
            }
        }
        return error;
    }


    static boolean isBackFlowType(Intent data) {
        return BackFlowIntent.isBackFlowType(data);
    }

}
