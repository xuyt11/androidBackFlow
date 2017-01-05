package cn.ytxu.androidbackflow;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by ytxu on 2017/1/5.
 */
class BackFlowExtra {
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
