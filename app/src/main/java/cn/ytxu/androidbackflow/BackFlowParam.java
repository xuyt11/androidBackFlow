package cn.ytxu.androidbackflow;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by ytxu on 17/1/6.<br>
 * 回退功能的参数<br>
 * 1、若atyClass!=null && !fragmentClazzs.isEmpty()，则会回退到包含了该fragment顺序列的activity；<br>
 * 2、若atyClass!=null && fragmentClazzs.isEmpty()，则会回退到该activity；<br>
 * 若有多个activity实例，则只会回退到第一个匹配；<br>
 * 3、若atyClass==null && !fragmentClazzs.isEmpty()，则会回退到第一个匹配该fragment顺序列的activity；<br>
 * 4、若在整个回退流程流程中，没有匹配到目标，则相当于finish_task的功能。<br>
 */
public final class BackFlowParam {
    private final BackFlowType type;
    private final Activity activity;

    Class<? extends Activity> atyClass;// 回退到该activity
    List<Class<? extends Fragment>> fragmentClazzs = Collections.EMPTY_LIST;// 回退到该fragment的顺序列表

    /**
     * 回退Activity界面的数量,每一次回退都会--backActivityNumber,当backNumber为0的时候，不再回退
     * 若为0，则只finish当前的activity
     */
    int backActivityNumber = 0;

    Bundle extra;// 额外的附加数据

    private Intent backFlowData;

    private BackFlowParam(@NonNull BackFlowType type, @NonNull Activity activity) {
        this.type = type;
        this.activity = activity;
    }

    public void request() {
        BackFlow.request(activity, backFlowData);
    }


    //********************* builder request param *********************
    public static final class Builder {
        private final BackFlowParam P;

        Builder(@NonNull BackFlowType type, @NonNull Activity activity) {
            P = new BackFlowParam(type, activity);
        }

        Builder(@NonNull BackFlowType type, @NonNull Fragment fragment) {
            P = new BackFlowParam(type, fragment.getActivity());
        }

        public Builder setActivity(@NonNull Class<? extends Activity> atyClass) {
            P.atyClass = atyClass;
            return this;
        }

        public Builder setFragments(@NonNull Class<? extends Fragment>... fragmentClazzs) {
            P.fragmentClazzs = Arrays.asList(fragmentClazzs);
            return this;
        }

        public Builder setActivityNumber(int backActivityNumber) {
            if (backActivityNumber < 0) {
                throw new IndexOutOfBoundsException("backActivityNumber can not be less than 0");
            }
            P.backActivityNumber = backActivityNumber;
            return this;
        }

        public Builder setExtra(@NonNull Bundle extra) {
            P.extra = extra;
            return this;
        }

        public BackFlowParam create() {
            P.backFlowData = P.type.createRequestData(P);
            return P;
        }
    }

}
