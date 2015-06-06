package com.gf.BugManagerMobile;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

/**
 * 提交新bug界面
 * Created by Administrator on 5/11 0011.
 */
public class SubmitBugActivity extends BaseActivity {
    private static final String TAG = "SubmitBugActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCenterContentLyt(R.layout.activity_bug_submit);

    }

    public void onOptClick(View v) {
        switch (v.getId()) {
            case R.id.submit_bug_back_imgv:
                this.finish();
                break;
            case R.id.submit_bug_add_btn:
                Toast.makeText(this, "添加", Toast.LENGTH_SHORT).show();
                break;
            default:

        }
    }

    public void onClickBtn(View v) {
        switch (v.getId()) {

        }
    }

}
