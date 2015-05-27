package com.gf.BugManagerMobile;

import android.app.Activity;
import android.os.Bundle;

/**
 * 重置用户密码界面
 * Created by GuLang on 2015-05-24.
 */
public class ResetPasswordActivity extends Activity {
    private static final String TAG = "ResetPasswordActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
    }
}
