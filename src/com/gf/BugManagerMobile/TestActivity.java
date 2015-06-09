package com.gf.BugManagerMobile;

import android.os.Bundle;
import android.widget.Spinner;

/**
 * 测试界面
 * Created by Administrator on 5/10 0010.
 */
public class TestActivity extends BaseActivity {
    private static final String TAG = "TestActivity";

    private Spinner spinner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        // setCenterContentLyt(R.layout.activity_test);

        spinner = (Spinner) findViewById(R.id.test_spinner);

    }

}
