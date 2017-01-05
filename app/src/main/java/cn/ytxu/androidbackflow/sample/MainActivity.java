package cn.ytxu.androidbackflow.sample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import cn.ytxu.androidbackflow.BaseActivity;
import cn.ytxu.androidbackflow.R;
import cn.ytxu.androidbackflow.sample.multi_fragment.MultiFragmentActivity;
import cn.ytxu.androidbackflow.sample.multi_process.MultiProcessActivity;
import cn.ytxu.androidbackflow.sample.multi_task.MultiTaskActivity;
import cn.ytxu.androidbackflow.sample.multi_task_and_process.MultiTaskAndProcessActivity;
import cn.ytxu.androidbackflow.sample.request_activity_and_fragment.ContainerAF1Activity;
import cn.ytxu.androidbackflow.sample.request_activity_and_fragment.letter.LetterAFAFragment;
import cn.ytxu.androidbackflow.sample.request_fragment.ContainerActivity;
import cn.ytxu.androidbackflow.sample.request_fragment.letter.LetterAFragment;
import cn.ytxu.androidbackflow.sample.request_activity.letter.LetterAActivity;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Main View");
        $(R.id.main_btn_4_requedst_activity).setOnClickListener(this);
        $(R.id.main_btn_4_requedst_fragment).setOnClickListener(this);
        $(R.id.main_btn_4_requedst_activity_and_fragment).setOnClickListener(this);
        $(R.id.main_btn_4_multi_process).setOnClickListener(this);
        $(R.id.main_btn_4_multi_task).setOnClickListener(this);
        $(R.id.main_btn_4_multi_task_and_process).setOnClickListener(this);
        $(R.id.main_btn_4_multi_fragment).setOnClickListener(this);
        $(R.id.main_btn_4_multi_fragment_contains_sub_fragment).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_btn_4_requedst_activity:
                startActivity(new Intent(this, LetterAActivity.class));
                break;
            case R.id.main_btn_4_requedst_fragment: {
                Intent intent = new Intent(this, ContainerActivity.class);
                intent.putExtra(ContainerActivity.PARAM_CLASSNAME, LetterAFragment.class.getName());
                startActivity(intent);
            }
            break;
            case R.id.main_btn_4_requedst_activity_and_fragment: {
                Intent intent = new Intent(this, ContainerAF1Activity.class);
                intent.putExtra(ContainerAF1Activity.PARAM_CLASSNAME, LetterAFAFragment.class.getName());
                startActivity(intent);
            }
            break;
            case R.id.main_btn_4_multi_process: {
                Intent intent = new Intent(this, MultiProcessActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
            break;
            case R.id.main_btn_4_multi_task: {
                Intent intent = new Intent(this, MultiTaskActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
            break;
            case R.id.main_btn_4_multi_task_and_process: {
                Intent intent = new Intent(this, MultiTaskAndProcessActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
            break;
            case R.id.main_btn_4_multi_fragment: {
                Intent intent = new Intent(this, MultiFragmentActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.main_btn_4_multi_fragment_contains_sub_fragment: {
                // TODO
                Intent intent = new Intent(this, MultiTaskAndProcessActivity.class);
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
