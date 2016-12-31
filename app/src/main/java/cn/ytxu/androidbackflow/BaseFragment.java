package cn.ytxu.androidbackflow;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.view.View;

public class BaseFragment extends Fragment {

    protected View root;

    public <T extends View> T $(@IdRes int id) {
        return (T) root.findViewById(id);
    }

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
