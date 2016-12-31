package cn.ytxu.androidbackflow;

import android.content.Intent;
import android.support.v4.app.Fragment;

public class BaseFragment extends Fragment {

    @Override
    public void startActivity(Intent intent) {
        startActivityForResult(intent, BackFlow.REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (BackFlow.handle(this, resultCode, data)) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
