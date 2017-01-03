package cn.ytxu.androidbackflow.sample.container;

import android.support.v4.app.Fragment;

/**
 * Created by ytxu on 2016/12/31.
 */
public enum LetterFragmentType {
    a, b, c, d, e, f, g;//, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z;

    private final String fragmentName;

    LetterFragmentType() {
        this.fragmentName = String.format("cn.ytxu.androidbackflow.sample.container.letter.Letter%sFragment", name().toUpperCase());
    }


    public String getFragmentName() {
        return fragmentName;
    }

    public static String getNextFragmentName(Fragment fragment) {
        LetterFragmentType type = get(fragment);
        return LetterFragmentType.values()[type.ordinal() + 1].getFragmentName();
    }

    public static LetterFragmentType get(Fragment fragment) {
        String currName = fragment.getClass().getName();
        for (LetterFragmentType letterType : LetterFragmentType.values()) {
            if (letterType.fragmentName.equals(currName)) {
                return letterType;
            }
        }
        throw new IllegalArgumentException("error letter activity name is " + currName);
    }
}
