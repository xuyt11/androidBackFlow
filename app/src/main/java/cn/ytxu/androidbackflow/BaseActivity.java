package cn.ytxu.androidbackflow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    @Override
    public void startActivity(Intent intent) {
        startActivityForResult(intent, BackFlow.REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (BackFlow.handle(this, resultCode, data)) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
