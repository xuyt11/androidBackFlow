package cn.ytxu.androidbackflow.sample.multi_task_or_process.multi_task;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import cn.ytxu.androidbackflow.BaseActivity;
import cn.ytxu.androidbackflow.R;
import cn.ytxu.androidbackflow.sample.normal.request_activity.letter.LetterAActivity;
import cn.ytxu.androidbackflow.sample.normal.request_activity_and_fragment.ContainerAF1Activity;
import cn.ytxu.androidbackflow.sample.normal.request_activity_and_fragment.letter.LetterAFAFragment;
import cn.ytxu.androidbackflow.sample.ContainerActivity;
import cn.ytxu.androidbackflow.sample.normal.request_fragment.letter.LetterAFragment;

/**
 * Created by ytxu on 17/1/4.
 */
public class MultiTaskActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = MultiTaskActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multi_activity);

        setTitle("Multi Task View");
        $(R.id.main_btn_4_requedst_activity).setOnClickListener(this);
        $(R.id.main_btn_4_requedst_fragment).setOnClickListener(this);
        $(R.id.main_btn_4_requedst_activity_and_fragment).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_btn_4_requedst_activity: {
                Intent intent = new Intent(this, LetterAActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
            break;
            case R.id.main_btn_4_requedst_fragment: {
                Intent intent = new Intent(this, ContainerActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(ContainerActivity.PARAM_CLASSNAME, LetterAFragment.class.getName());
                startActivity(intent);
            }
            break;
            case R.id.main_btn_4_requedst_activity_and_fragment: {
                Intent intent = new Intent(this, ContainerAF1Activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(ContainerAF1Activity.PARAM_CLASSNAME, LetterAFAFragment.class.getName());
                startActivity(intent);
            }
            break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
