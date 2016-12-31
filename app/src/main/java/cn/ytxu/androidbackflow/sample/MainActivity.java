package cn.ytxu.androidbackflow.sample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import cn.ytxu.androidbackflow.BaseActivity;
import cn.ytxu.androidbackflow.R;
import cn.ytxu.androidbackflow.letter.LetterAActivity;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.main_jump_to_letter_btn).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_jump_to_letter_btn:
                startActivity(new Intent(this, LetterAActivity.class));
                break;
            default:
                break;
        }
    }
}
