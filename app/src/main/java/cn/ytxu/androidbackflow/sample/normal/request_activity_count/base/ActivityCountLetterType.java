package cn.ytxu.androidbackflow.sample.normal.request_activity_count.base;

import android.app.Activity;
import android.support.annotation.NonNull;

/**
 * Created by ytxu on 2016/12/31.
 */
public enum ActivityCountLetterType {
    a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z;

    private final String atyName;

    ActivityCountLetterType() {
        this.atyName = String.format("cn.ytxu.androidbackflow.sample.normal.request_activity_count.letter.ACLetter%sActivity", name().toUpperCase());
    }

    public static Class getNextActivity(Activity context) throws ClassNotFoundException {
        int nextOrdinal = getCurrPosition(context) + 1;
        ActivityCountLetterType nextType = ActivityCountLetterType.values()[nextOrdinal];
        return context.getClassLoader().loadClass(nextType.atyName);
    }

    public static int getCurrPosition(Activity context) {
        String currName = context.getClass().getName();
        ActivityCountLetterType letterType = ActivityCountLetterType.get(currName);
        return letterType.ordinal();
    }

    public static int getCurrPosition(@NonNull Class<? extends Activity> clazz) {
        return get(clazz).ordinal();
    }

    public static ActivityCountLetterType get(@NonNull Class<? extends Activity> clazz) {
        return get(clazz.getName());
    }

    private static ActivityCountLetterType get(String currName) {
        for (ActivityCountLetterType letterType : ActivityCountLetterType.values()) {
            if (letterType.atyName.equals(currName)) {
                return letterType;
            }
        }
        throw new IllegalArgumentException("error letter activity name is " + currName);
    }
}
