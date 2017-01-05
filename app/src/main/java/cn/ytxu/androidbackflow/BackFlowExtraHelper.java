package cn.ytxu.androidbackflow;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by ytxu on 2017/1/5.
 */
class BackFlowExtraHelper {
    /**
     * 回退功能的类型: 类型的枚举是BackFlowType中的type
     * type is int
     */
    private static final String BACK_FLOW_TYPE = "back_flow_type";
    static final int ERROR_BACK_FLOW_TYPE = 0;

    /**
     * 回退功能中指定的activity
     * type is String
     */
    private static final String BACK_FLOW_ACTIVITY = "back_flow_activity";

    /**
     * 回退功能中指定的fragment顺序列
     * type is String
     */
    private static final String BACK_FLOW_FRAGMENTS = "back_flow_fragments";

    /**
     * 回退功能中用户带入的额外数据
     * type is Bundle
     */
    private static final String BACK_FLOW_EXTRA = "back_flow_extra";


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
    static Intent putActivity(Intent data, @NonNull Class<? extends Activity> atyClass) {
        return data.putExtra(BACK_FLOW_ACTIVITY, atyClass.getName());
    }

    static String getActivity(Intent data) {
        return data.getStringExtra(BACK_FLOW_ACTIVITY);
    }


    //**************************** fragment class name ****************************
    static Intent putFragments(Intent data, @NonNull List<Class<? extends Fragment>> fragmentClazzs) {
        JSONArray fragmentClassNameJsonArray = new JSONArray();
        for (Class<? extends Fragment> fragmentClazz : fragmentClazzs) {
            fragmentClassNameJsonArray.put(fragmentClazz.getName());
        }
        return data.putExtra(BACK_FLOW_FRAGMENTS, fragmentClassNameJsonArray.toString());
    }

    static List<String> getFragments(Intent data) {
        String targetFragmentClassNames = data.getStringExtra(BACK_FLOW_FRAGMENTS);
        try {
            JSONArray fragmentClassNameJsonArray = new JSONArray(targetFragmentClassNames);
            List<String> fragmentClassNames = new ArrayList<>(fragmentClassNameJsonArray.length());
            for (int i = 0; i < fragmentClassNameJsonArray.length(); i++) {
                String fragmentClassName = fragmentClassNameJsonArray.getString(i);
                fragmentClassNames.add(fragmentClassName);
            }
            return fragmentClassNames;
        } catch (JSONException e) {
            e.printStackTrace();
            return Collections.EMPTY_LIST;
        }
    }


    //**************************** extra ****************************
    static Intent putExtra(Intent data, @Nullable Bundle extra) {
        if (extra != null) {
            data.putExtra(BACK_FLOW_EXTRA, extra);
        }
        return data;
    }

    public static Bundle getExtra(Intent data) {
        return data.getBundleExtra(BACK_FLOW_EXTRA);
    }

    public static boolean hasExtra(Intent data) {
        return data.hasExtra(BACK_FLOW_EXTRA);
    }

}
