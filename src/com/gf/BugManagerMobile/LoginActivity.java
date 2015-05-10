package com.gf.BugManagerMobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

public class LoginActivity extends Activity {
    private static final String TAG = "LoginActivity";

    private CheckBox rememberChk;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initComponent();

    }

    /**
     * 初始化控件
     */
    private void initComponent() {
        rememberChk = (CheckBox) findViewById(R.id.login_remember);
        rememberChk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Toast.makeText(LoginActivity.this, "当前状态" + b + ",ischecked=" + rememberChk.isChecked(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 处理点击事件
     *
     * @param view
     */
    public void onBtnClick(View view) {
        switch (view.getId()) {
            case R.id.login_login_btn:
                Intent intent = new Intent(this, BaseActivity.class);
                startActivity(intent);
                break;
            default:

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_setting:
                Intent intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
//                Toast.makeText(this, "BaseActivity 点击menu 设置按钮", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_about:
                Toast.makeText(this, "BaseActivity 点击menu 关于按钮", Toast.LENGTH_SHORT).show();
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }


}
