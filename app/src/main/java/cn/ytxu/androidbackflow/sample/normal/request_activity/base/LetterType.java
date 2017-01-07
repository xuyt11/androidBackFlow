package cn.ytxu.androidbackflow.sample.normal.request_activity.base;

import android.app.Activity;

/**
 * Created by ytxu on 2016/12/31.
 */
public enum LetterType {
    a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z;

    private final String atyName;

    LetterType() {
        this.atyName = String.format("cn.ytxu.androidbackflow.sample.normal.request_activity.letter.Letter%sActivity", name().toUpperCase());
    }

    public static Class getNextActivity(Activity context) throws ClassNotFoundException {
        String currName = context.getClass().getName();
        LetterType letterType = LetterType.get(currName);

        int nextOrdinal = letterType.ordinal() + 1;
        LetterType nextType = LetterType.values()[nextOrdinal];
        return context.getClassLoader().loadClass(nextType.atyName);
    }

    private static LetterType get(String currName) {
        for (LetterType letterType : LetterType.values()) {
            if (letterType.atyName.equals(currName)) {
                return letterType;
            }
        }
        throw new IllegalArgumentException("error letter activity name is " + currName);
    }
}
