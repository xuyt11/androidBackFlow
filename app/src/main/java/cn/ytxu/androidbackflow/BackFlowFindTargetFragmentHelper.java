package cn.ytxu.androidbackflow;

import android.support.v4.app.Fragment;

import java.util.List;
import java.util.ListIterator;

/**
 * Created by ytxu on 2017/1/5.
 */
class BackFlowFindTargetFragmentHelper {

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
