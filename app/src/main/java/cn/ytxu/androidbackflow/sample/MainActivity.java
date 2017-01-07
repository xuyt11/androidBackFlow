package cn.ytxu.androidbackflow.sample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import cn.ytxu.androidbackflow.BaseActivity;
import cn.ytxu.androidbackflow.R;
import cn.ytxu.androidbackflow.sample.fragments.fragment_contains_sub_fragment.first_level.FcsfAFragment;
import cn.ytxu.androidbackflow.sample.fragments.multi_fragment.MultiFragmentActivity;
import cn.ytxu.androidbackflow.sample.multi_task_or_process.multi_process.MultiProcessActivity;
import cn.ytxu.androidbackflow.sample.multi_task_or_process.multi_task.MultiTaskActivity;
import cn.ytxu.androidbackflow.sample.multi_task_or_process.multi_task_and_process.MultiTaskAndProcessActivity;
import cn.ytxu.androidbackflow.sample.normal.request_activity_and_fragment.ContainerAF1Activity;
import cn.ytxu.androidbackflow.sample.normal.request_activity_and_fragment.letter.LetterAFAFragment;
import cn.ytxu.androidbackflow.sample.normal.request_fragment.letter.LetterAFragment;
import cn.ytxu.androidbackflow.sample.normal.request_activity.letter.LetterAActivity;

public class MainActivity extends BaseActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Main View");

        setNormalListener();
        setMultiListener();
        setFragmentsListener();
        setLaunchModeListener();
    }

    private void setNormalListener() {
        final View.OnClickListener l = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.main_btn_4_requedst_activity:
                        startActivity(new Intent(MainActivity.this, LetterAActivity.class));
                        break;
                    case R.id.main_btn_4_requedst_fragment: {
                        Intent intent = new Intent(MainActivity.this, ContainerActivity.class);
                        intent.putExtra(ContainerActivity.PARAM_CLASSNAME, LetterAFragment.class.getName());
                        startActivity(intent);
                    }
                    break;
                    case R.id.main_btn_4_requedst_activity_and_fragment: {
                        Intent intent = new Intent(MainActivity.this, ContainerAF1Activity.class);
                        intent.putExtra(ContainerAF1Activity.PARAM_CLASSNAME, LetterAFAFragment.class.getName());
                        startActivity(intent);
                    }
                    break;
                    default:
                        break;
                }
            }
        };
        $(R.id.main_btn_4_requedst_activity).setOnClickListener(l);
        $(R.id.main_btn_4_requedst_fragment).setOnClickListener(l);
        $(R.id.main_btn_4_requedst_activity_and_fragment).setOnClickListener(l);
    }

    private void setMultiListener() {
        final View.OnClickListener l = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.main_btn_4_multi_process: {
                        Intent intent = new Intent(MainActivity.this, MultiProcessActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                    break;
                    case R.id.main_btn_4_multi_task: {
                        Intent intent = new Intent(MainActivity.this, MultiTaskActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                    break;
                    case R.id.main_btn_4_multi_task_and_process: {
                        Intent intent = new Intent(MainActivity.this, MultiTaskAndProcessActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                    break;
                    default:
                        break;
                }
            }
        };
        $(R.id.main_btn_4_multi_process).setOnClickListener(l);
        $(R.id.main_btn_4_multi_task).setOnClickListener(l);
        $(R.id.main_btn_4_multi_task_and_process).setOnClickListener(l);
    }

    private void setFragmentsListener() {
        final View.OnClickListener l = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.main_btn_4_multi_fragment: {
                        Intent intent = new Intent(MainActivity.this, MultiFragmentActivity.class);
                        startActivity(intent);
                    }
                    break;
                    case R.id.main_btn_4_fragment_contains_sub_fragment: {
                        Intent intent = new Intent(MainActivity.this, ContainerActivity.class);
                        intent.putExtra(ContainerActivity.PARAM_CLASSNAME, FcsfAFragment.class.getName());
                        startActivity(intent);
                    }
                    break;
                    default:
                        break;
                }
            }
        };
        $(R.id.main_btn_4_multi_fragment).setOnClickListener(l);
        $(R.id.main_btn_4_fragment_contains_sub_fragment).setOnClickListener(l);
    }

    private void setLaunchModeListener() {
        final View.OnClickListener l = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.main_btn_4_single_task: {
                        // TODO
                    }
                    break;
                    case R.id.main_btn_4_single_top: {
                        // TODO
                    }
                    break;
                    case R.id.main_btn_4_single_instance: {
                        // TODO
                    }
                    break;
                    default:
                        break;
                }
            }
        };
        $(R.id.main_btn_4_single_task).setOnClickListener(l);
        $(R.id.main_btn_4_single_top).setOnClickListener(l);
        $(R.id.main_btn_4_single_instance).setOnClickListener(l);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
