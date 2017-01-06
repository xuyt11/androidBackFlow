package cn.ytxu.androidbackflow;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
    error(BackFlowExtraHelper.ERROR_BACK_FLOW_TYPE) {
        @Override
        Intent createRequestData(BackFlowRequestParam param) {
            throw new IllegalArgumentException("error back flow type");
        }

        @Override
        boolean handleBackFlow(Activity activity, List<Fragment> fragments, int requestCode, int resultCode, Intent data) {
            Log.w(BackFlowType.class.getSimpleName(), new Throwable("error back flow type"));
            return false;
        }
    },

    /**
     * 结束App功能：结束App中所有的activity（准确的说是：finish该task中所有的activity）<br>
     * 若在整个回退流程流程中，没有匹配到目标，也相当于finish_app的功能。
     */
    finish_app(1) {
        @Override
        Intent createRequestData(BackFlowRequestParam param) {
            if (param.atyClass != null || !param.fragmentClazzs.isEmpty()) {
                throw new IllegalArgumentException("error back flow param");
            }
            return initData(param.extra);
        }

        @Override
        boolean handleBackFlow(Activity activity, List<Fragment> fragments, int requestCode, int resultCode, Intent data) {
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
        Intent createRequestData(BackFlowRequestParam param) {
            if (param.atyClass == null) {
                throw new IllegalArgumentException("atyClass param must be non null");
            }
            if (!param.fragmentClazzs.isEmpty()) {
                throw new IllegalArgumentException("error back flow param");
            }
            return BackFlowExtraHelper.putActivity(initData(param.extra), param.atyClass);
        }

        @Override
        boolean handleBackFlow(Activity activity, List<Fragment> fragments, int requestCode, int resultCode, Intent data) {
            String targetActivityClassName = BackFlowExtraHelper.getActivity(data);
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
        Intent createRequestData(BackFlowRequestParam param) {
            if (param.atyClass != null) {
                throw new IllegalArgumentException("atyClass param must be null");
            }
            if (param.fragmentClazzs.isEmpty()) {
                throw new IllegalArgumentException("fragmentClazzs param must not empty");
            }

            return BackFlowExtraHelper.putFragments(initData(param.extra), param.fragmentClazzs);
        }

        @Override
        boolean handleBackFlow(Activity activity, List<Fragment> fragments, int requestCode, int resultCode, Intent data) {
            List<String> fragmentClassNames = BackFlowExtraHelper.getFragments(data);
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
        Intent createRequestData(BackFlowRequestParam param) {
            if (param.atyClass == null) {
                throw new IllegalArgumentException("atyClass param must be non null");
            }
            if (param.fragmentClazzs.isEmpty()) {
                throw new IllegalArgumentException("fragmentClazzs param must not empty");
            }
            Intent data = BackFlowExtraHelper.putActivity(initData(param.extra), param.atyClass);
            return BackFlowExtraHelper.putFragments(data, param.fragmentClazzs);
        }

        @Override
        boolean handleBackFlow(Activity activity, List<Fragment> fragments, int requestCode, int resultCode, Intent data) {
            String targetActivityClassName = BackFlowExtraHelper.getActivity(data);
            if (!BackFlowViewHelper.isTargetActivity(activity, targetActivityClassName)) {
                BackFlow.request(activity, data);// send request again
                return true;
            }

            List<String> fragmentClassNames = BackFlowExtraHelper.getFragments(data);
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

    abstract Intent createRequestData(BackFlowRequestParam param);

    protected Intent initData(@Nullable Bundle extra) {
        return BackFlowExtraHelper.putExtra(BackFlowExtraHelper.init(type), extra);
    }

    /**
     * @return handled 是否处理了；
     * true:不需要再次分发给BaseActivity去处理，否则继续分发给BaseActivity，
     * 若找到目标组，则停止分发；
     * 且若目标为fragment，会调用onActivityResult(resultCode, resultCode, data)方法
     */
    abstract boolean handleBackFlow(Activity activity, List<Fragment> fragments, int requestCode, int resultCode, Intent data);


    static BackFlowType get(Intent data) {
        int type = BackFlowExtraHelper.getType(data);
        for (BackFlowType backFlowType : BackFlowType.values()) {
            if (backFlowType.type == type) {
                return backFlowType;
            }
        }
        return error;
    }


    static boolean isBackFlowType(Intent data) {
        return BackFlowExtraHelper.isBackFlowType(data);
    }

}
