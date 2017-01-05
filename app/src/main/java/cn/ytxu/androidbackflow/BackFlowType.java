package cn.ytxu.androidbackflow;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

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
    error(Extra.ERROR_BACK_FLOW_TYPE) {
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
            Intent data = Extra.putActivity(initData(), atyClass);
            requestBackFlowInner(activity, data);
        }

        @Override
        public boolean handleBackFlow(Activity activity, List<Fragment> fragments, int requestCode, int resultCode, Intent data) {
            String currActivityClassName = activity.getClass().getName();
            String targetActivityClassName = Extra.getActivity(data);

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
            Intent data = Extra.putFragments(initData(), fragmentClazzs);
            requestBackFlowInner(activity, data);
        }

        @Override
        public boolean handleBackFlow(Activity activity, List<Fragment> fragments, int requestCode, int resultCode, Intent data) {
            List<String> fragmentClassNames = Extra.getFragments(data);
            try {
                Fragment fragment = FindTargetFragment.findTargetFragment(fragments, fragmentClassNames.listIterator());
                fragment.onActivityResult(requestCode, resultCode, data);
                return true;
            } catch (FindTargetFragment.NotFindTargetFragmentException e) {
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
            Intent data = Extra.putActivity(initData(), atyClass);
            Extra.putFragments(data, fragmentClazzs);
            requestBackFlowInner(activity, data);
        }

        @Override
        public boolean handleBackFlow(Activity activity, List<Fragment> fragments, int requestCode, int resultCode, Intent data) {
            String currActivityClassName = activity.getClass().getName();
            String targetActivityClassName = Extra.getActivity(data);
            if (!isMatch(currActivityClassName, targetActivityClassName)) {
                requestBackFlowInner(activity, data);// send request again
                return true;
            }

            List<String> fragmentClassNames = Extra.getFragments(data);
            try {
                Fragment fragment = FindTargetFragment.findTargetFragment(fragments, fragmentClassNames.listIterator());
                fragment.onActivityResult(requestCode, resultCode, data);
                return true;
            } catch (FindTargetFragment.NotFindTargetFragmentException e) {
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
        return Extra.init(type);
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
        int type = Extra.getType(data);
        for (BackFlowType backFlowType : BackFlowType.values()) {
            if (backFlowType.type == type) {
                return backFlowType;
            }
        }
        return error;
    }


    public static boolean isBackFlowType(Intent data) {
        return Extra.isBackFlowType(data);
    }
}


class FindTargetFragment {

    static Fragment findTargetFragment(List<Fragment> fragments, ListIterator<String> targetFragmentClassNameListIter) throws NotFindTargetFragmentException {
        if (fragments == null || fragments.isEmpty()) {
            throw new NotFindTargetFragmentException();
        }

        if (!targetFragmentClassNameListIter.hasNext()) {
            throw new NotFindTargetFragmentException();
        }

        final String targetFragmentClassName = targetFragmentClassNameListIter.next();
        for (Fragment fragment : fragments) {
            if (isTargetFragment(fragment, targetFragmentClassName)) {
                if (targetFragmentClassNameListIter.hasNext()) {
                    return findTargetFragment(fragment.getChildFragmentManager().getFragments(), targetFragmentClassNameListIter);
                }
                return fragment;
            }
        }
        throw new NotFindTargetFragmentException();
    }

    private static boolean isTargetFragment(Fragment fragment, String targetFragmentClassName) {
        return fragment.getClass().getName().equals(targetFragmentClassName);
    }

    static final class NotFindTargetFragmentException extends RuntimeException {
    }
}


class Extra {
    private static final String BACK_FLOW_TYPE = "back_flow_type";
    static final int ERROR_BACK_FLOW_TYPE = 0;

    /**
     * 返回到指定的activity
     * type is String
     */
    private static final String BACK_TO_ACTIVITY = "back_to_activity";
    /**
     * 返回到指定的fragment
     * type is String
     */
    private static final String BACK_TO_FRAGMENTS = "back_to_fragments";


    //**************************** back flow type ****************************
    static Intent init(int type) {
        return new Intent().putExtra(BACK_FLOW_TYPE, type);
    }

    static int getType(Intent data) {
        return data.getIntExtra(BACK_FLOW_TYPE, ERROR_BACK_FLOW_TYPE);
    }

    static boolean isBackFlowType(Intent data) {
        return data != null && data.hasExtra(BACK_FLOW_TYPE);
    }


    //**************************** activity class name ****************************
    static <A extends Activity> Intent putActivity(Intent data, @NonNull Class<A> atyClass) {
        return data.putExtra(BACK_TO_ACTIVITY, atyClass.getName());
    }

    static String getActivity(Intent data) {
        return data.getStringExtra(BACK_TO_ACTIVITY);
    }


    //**************************** fragment class name ****************************
    static <F extends Fragment> Intent putFragments(Intent data, @NonNull List<Class<F>> fragmentClazzs) {
        JSONArray jsonArray = new JSONArray();
        for (Class<F> fragmentClazz : fragmentClazzs) {
            jsonArray.put(fragmentClazz.getName());
        }
        return data.putExtra(BACK_TO_FRAGMENTS, jsonArray.toString());
    }

    static List<String> getFragments(Intent data) {
        String targetFragmentClassNames = data.getStringExtra(BACK_TO_FRAGMENTS);
        try {
            JSONArray jsonArray = new JSONArray(targetFragmentClassNames);
            List<String> fragmentClassNames = new ArrayList<>(jsonArray.length());
            for (int i = 0; i < jsonArray.length(); i++) {
                String fragmentClassName = jsonArray.getString(i);
                fragmentClassNames.add(fragmentClassName);
            }
            return fragmentClassNames;
        } catch (JSONException e) {
            e.printStackTrace();
            return Collections.EMPTY_LIST;
        }
    }
}