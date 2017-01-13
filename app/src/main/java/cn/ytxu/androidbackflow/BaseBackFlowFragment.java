package cn.ytxu.androidbackflow;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

public class BaseBackFlowFragment extends Fragment {

    protected View root;

    public void setRoot(View root) {
        this.root = root;
    }

    public <T extends View> T $(@IdRes int id) {
        return (T) root.findViewById(id);
    }


    //******************** start activity replace method ********************
    public void startActivity4NonBackFlow(Intent intent) {
        startActivityForResult(intent, BackFlow.REQUEST_CODE_4_NON_BACK_FLOW);
    }

    public void startActivity4NonBackFlow(Intent intent, @Nullable Bundle options) {
        startActivityForResult(intent, BackFlow.REQUEST_CODE_4_NON_BACK_FLOW, options);
    }


    //******************** back flow ********************
    @Override
    public void startActivity(Intent intent) {
        startActivityForResult(intent, BackFlow.REQUEST_CODE);
    }

    @Override
    public void startActivity(Intent intent, @Nullable Bundle options) {
        startActivityForResult(intent, BackFlow.REQUEST_CODE, options);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        BackFlow.Logger.logIntent(this, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

}
