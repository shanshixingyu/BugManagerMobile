package com.gf.BugManagerMobile;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import com.gf.BugManagerMobile.utils.MyConstant;

/**
 * 所有界面的基类
 * Created by GuLang on 2015-06-07.
 */
public class BaseActivity extends Activity {
    private static final String TAG = "BaseActivity";

    public static final String FINISH_ALL = "com.gf.BugManagerMobile.BaseActivity.FinishAll";
    public static final String FINISH_EXCEPT_LOGIN = "com.gf.BugManagerMobile.BaseActivity.FinishExceptLogin";

    private FinishBroadcastReceiver mFinishAllReceiver;
    private FinishBroadcastReceiver mFinishExceptLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        registerBroadcastReceiver();
    }

    private void registerBroadcastReceiver() {
        mFinishAllReceiver = new FinishBroadcastReceiver();
        IntentFilter finishAllIntentFilter = new IntentFilter(FINISH_ALL);
        registerReceiver(mFinishAllReceiver, finishAllIntentFilter);

        mFinishExceptLogin = new FinishBroadcastReceiver();
        IntentFilter finishExceptLogin = new IntentFilter(FINISH_EXCEPT_LOGIN);
        registerReceiver(mFinishExceptLogin, finishExceptLogin);
    }

    @Override
    protected void onDestroy() {
        if (mFinishAllReceiver != null)
            unregisterReceiver(mFinishAllReceiver);
        if (mFinishExceptLogin != null)
            unregisterReceiver(mFinishExceptLogin);
        super.onDestroy();
    }

    public class FinishBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null && action.equals(FINISH_EXCEPT_LOGIN)) {
                if (!(BaseActivity.this instanceof LoginActivity)) {
                    BaseActivity.this.finish();
                }
            } else {
                BaseActivity.this.finish();
            }
            // Log.i(TAG, "收到广播：" + BaseActivity.this.getClass().getName());
        }
    }

}
