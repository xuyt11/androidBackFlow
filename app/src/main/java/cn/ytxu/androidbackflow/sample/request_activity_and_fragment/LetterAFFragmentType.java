package cn.ytxu.androidbackflow.sample.request_activity_and_fragment;

import android.support.v4.app.Fragment;

/**
 * Created by ytxu on 2016/12/31.
 */
public enum LetterAFFragmentType {
    a, b, c, d, e, f, g;//, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z;

    private final String fragmentName;

    LetterAFFragmentType() {
        this.fragmentName = String.format("cn.ytxu.androidbackflow.sample.request_activity_and_fragment.letter.LetterAF%sFragment", name().toUpperCase());
    }


    public String getFragmentName() {
        return fragmentName;
    }

    public static String getNextFragmentName(Fragment fragment) {
        LetterAFFragmentType type = get(fragment);
        return LetterAFFragmentType.values()[type.ordinal() + 1].getFragmentName();
    }

    public static LetterAFFragmentType get(Fragment fragment) {
        String currName = fragment.getClass().getName();
        for (LetterAFFragmentType letterType : LetterAFFragmentType.values()) {
            if (letterType.fragmentName.equals(currName)) {
                return letterType;
            }
        }
        throw new IllegalArgumentException("error letter fragment name is " + currName);
    }
}
