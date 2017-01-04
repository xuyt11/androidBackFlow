package cn.ytxu.androidbackflow.sample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import cn.ytxu.androidbackflow.BaseActivity;
import cn.ytxu.androidbackflow.R;
import cn.ytxu.androidbackflow.sample.request_fragment.ContainerActivity;
import cn.ytxu.androidbackflow.sample.request_fragment.letter.LetterAFragment;
import cn.ytxu.androidbackflow.sample.request_activity.letter.LetterAActivity;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Main View");
        $(R.id.main_btn_4_requedst_activity).setOnClickListener(this);
        $(R.id.main_btn_4_requedst_fragment).setOnClickListener(this);
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
            default:
                break;
        }
    }
}
