package cn.ytxu.androidbackflow;

import android.app.Activity;
import android.support.v4.app.Fragment;

import java.util.List;
import java.util.ListIterator;

/**
 * Created by ytxu on 2017/1/5.
 */
class BackFlowViewHelper {

    //**************************** activity ****************************
    static boolean isTargetActivity(Activity activity, String targetActivityClassName) {
        return getActivityClassName(activity.getClass()).equals(targetActivityClassName);
    }

    static String getActivityClassName(Class<? extends Activity> atyClass) {
        return atyClass.getName();
    }


    //**************************** fragment ****************************
    static Fragment findTargetFragment(List<Fragment> fragments, ListIterator<String> targetFragmentClassNameListIter) throws NotFindTargetFragmentException {
        if (fragments == null || fragments.isEmpty()) {
            throw new NotFindTargetFragmentException();
        }

        if (!targetFragmentClassNameListIter.hasNext()) {
            throw new NotFindTargetFragmentException();
        }

        final String currFragmentClassName = targetFragmentClassNameListIter.next();
        for (Fragment fragment : fragments) {
            if (isSameFragment(fragment, currFragmentClassName)) {
                if (isTargetFragment(targetFragmentClassNameListIter)) {
                    return fragment;
                }
                return findTargetFragment(fragment.getChildFragmentManager().getFragments(), targetFragmentClassNameListIter);
            }
        }
        throw new NotFindTargetFragmentException();
    }

    private static boolean isSameFragment(Fragment fragment, String currFragmentClassName) {
        return getFragmentClassName(fragment.getClass()).equals(currFragmentClassName);
    }

    static String getFragmentClassName(Class<? extends Fragment> fragmentClass) {
        return fragmentClass.getName();
    }

    private static boolean isTargetFragment(ListIterator<String> targetFragmentClassNameListIter) {
        return !targetFragmentClassNameListIter.hasNext();
    }

    static final class NotFindTargetFragmentException extends RuntimeException {
    }


}
