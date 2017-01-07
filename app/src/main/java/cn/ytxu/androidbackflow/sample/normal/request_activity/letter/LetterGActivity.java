
package cn.ytxu.androidbackflow.sample.normal.request_activity.letter;

import android.os.Bundle;

import cn.ytxu.androidbackflow.sample.normal.request_activity.base.BaseLetterActivity;
import cn.ytxu.androidbackflow.sample.ContainerActivity;

public class LetterGActivity extends BaseLetterActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRollbackFlow(ContainerActivity.class, "回退栈中没有ContainerActivity，所以，会变为finish_task的效果");
    }

}
