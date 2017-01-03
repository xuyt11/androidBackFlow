
package cn.ytxu.androidbackflow.sample.letter;

import android.os.Bundle;

import cn.ytxu.androidbackflow.sample.letter.base.BaseLetterActivity;
import cn.ytxu.androidbackflow.sample.container.ContainerActivity;

public class LetterGActivity extends BaseLetterActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRollbackFlow(ContainerActivity.class, "回退栈中没有ContainerActivity，所以，会变为finish_app的效果");
    }

}
