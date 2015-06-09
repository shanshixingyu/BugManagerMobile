package com.gf.BugManagerMobile;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.gf.BugManagerMobile.models.HttpResult;
import com.gf.BugManagerMobile.utils.*;
import com.gf.BugManagerMobile.models.LoginSuccessInfo;

public class LoginActivity extends BaseActivity {
    private static final String TAG = "LoginActivity";

    private TextView titleTv;
    private EditText userNameEt, passwordEt;

    private CheckBox rememberChk;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initComponent();
        /**
         * 设置为true或者没有设置（默认为true）时表示需要自动登录,从其它界面跳转过来且为false时表示不需要
         */
        if(getIntent().getBooleanExtra(MyConstant.OTHER_ACTIVITY_2_LOGIN_SHOULD_AUTO, true)){
            HttpVisitUtils.getHttpVisit(this, LocalInfo.getBaseUrl(this) + "site/auto-login", true, "自动登录中...",
                    onAutoLoginFinishListener);
        }

        Intent finishOtherIntent = new Intent(FINISH_EXCEPT_LOGIN);
        sendBroadcast(finishOtherIntent);
    }

    private HttpVisitUtils.OnHttpFinishListener onAutoLoginFinishListener = new HttpVisitUtils.OnHttpFinishListener() {
        @Override
        public void onVisitFinish(HttpResult result) {
            if (result == null)
                return;
            if (result.isVisitSuccess()) {
                try {
                    LoginSuccessInfo loginSuccessInfo = JSON.parseObject(result.getResult(), LoginSuccessInfo.class);
                    // 保存到配置文件中
                    SharedPreferenceUtils.save(LoginActivity.this, MyConstant.SP_LOGIN_USE_ID,
                        loginSuccessInfo.getUserId());
                    SharedPreferenceUtils.save(LoginActivity.this, MyConstant.SP_LOGIN_USE_NAME,
                        loginSuccessInfo.getUserName());
                    SharedPreferenceUtils.save(LoginActivity.this, MyConstant.SP_LOGIN_PASSWORD,
                        loginSuccessInfo.getPassword());
                    SharedPreferenceUtils.save(LoginActivity.this, MyConstant.SP_LOGIN_ROLE_ID,
                        loginSuccessInfo.getRoleId());
                    SharedPreferenceUtils.save(LoginActivity.this, MyConstant.SP_LOGIN_ROLE_NAME,
                        loginSuccessInfo.getRoleName());

                    LocalInfo.setLoginSuccessInfo(loginSuccessInfo);

                    Log.i(TAG, "自动登录成功：" + loginSuccessInfo.toString());

                    // 跳转到主页
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                    LoginActivity.this.finish();
                } catch (Exception e) {
                    // Toast.makeText(LoginActivity.this, R.string.login_parse_failure, Toast.LENGTH_SHORT).show();
                }
                // } else {
                // HttpConnectResultUtils.optFailure(LoginActivity.this, result);
            }
        }
    };

    /**
     * 初始化控件
     */
    private void initComponent() {
        titleTv = (TextView) findViewById(R.id.login_title_tv);
        Typeface titleTypeface = Typeface.createFromAsset(getAssets(), "fonts/ygyxsziti2.ttf");
        titleTv.setTypeface(titleTypeface);

        userNameEt = (EditText) findViewById(R.id.login_username_et);
        passwordEt = (EditText) findViewById(R.id.login_password_et);
        rememberChk = (CheckBox) findViewById(R.id.login_remember);

        userNameEt.setText(SharedPreferenceUtils.queryString(this, MyConstant.SP_USER_NAME));
        passwordEt.setText(SharedPreferenceUtils.queryString(this, MyConstant.SP_PASSWORD));
        rememberChk.setChecked(SharedPreferenceUtils.queryBoolean(this, MyConstant.SP_REMEMBER_ME));

    }

    /**
     * 处理点击事件
     *
     * @param view
     */
    public void onBtnClick(View view) {
        switch (view.getId()) {
            case R.id.login_login_btn:
                String userName = userNameEt.getText().toString().trim();
                String password = passwordEt.getText().toString().trim();
                Log.i(TAG, "userName=" + userName + ",length=" + userName.length());
                // 对输入内容进行验证
                if (!RegexUtils.isMatch(MyConstant.NAME_PATTERN, userName)) {
                    Toast.makeText(this, R.string.login_user_name_pattern, Toast.LENGTH_SHORT).show();
                    userNameEt.setText("");
                    passwordEt.setText("");
                    return;
                }
                // 看是否保存密码
                if (rememberChk.isChecked()) {
                    SharedPreferenceUtils.save(this, MyConstant.SP_USER_NAME, userName);
                    SharedPreferenceUtils.save(this, MyConstant.SP_PASSWORD, password);
                }
                final String targetIp = SharedPreferenceUtils.queryString(this, MyConstant.SERVER_IP);
                final String dataStr = "name=" + userName + "&password=" + password;
                HttpVisitUtils.postHttpVisit(this, LocalInfo.getBaseUrl(LoginActivity.this) + "site/login", dataStr,
                    true, getResources().getString(R.string.login_loading), onHttpFinishListener);

                break;
            default:
        }
    }

    private HttpVisitUtils.OnHttpFinishListener onHttpFinishListener = new HttpVisitUtils.OnHttpFinishListener() {
        @Override
        public void onVisitFinish(HttpResult result) {
            if (result == null)
                throw new NullPointerException("网络访问时回调方法返回为空指针");
            if (result.isVisitSuccess()) {
                try {
                    LoginSuccessInfo loginSuccessInfo = JSON.parseObject(result.getResult(), LoginSuccessInfo.class);
                    // 保存到配置文件中
                    SharedPreferenceUtils.save(LoginActivity.this, MyConstant.SP_LOGIN_USE_ID,
                        loginSuccessInfo.getUserId());
                    SharedPreferenceUtils.save(LoginActivity.this, MyConstant.SP_LOGIN_USE_NAME,
                        loginSuccessInfo.getUserName());
                    SharedPreferenceUtils.save(LoginActivity.this, MyConstant.SP_LOGIN_PASSWORD,
                        loginSuccessInfo.getPassword());
                    SharedPreferenceUtils.save(LoginActivity.this, MyConstant.SP_LOGIN_ROLE_ID,
                        loginSuccessInfo.getRoleId());
                    SharedPreferenceUtils.save(LoginActivity.this, MyConstant.SP_LOGIN_ROLE_NAME,
                        loginSuccessInfo.getRoleName());

                    LocalInfo.setLoginSuccessInfo(loginSuccessInfo);

                    Log.i(TAG, "登录成功：" + loginSuccessInfo.toString());

                    // 跳转到主页
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                    LoginActivity.this.finish();
                } catch (Exception e) {
                    Toast.makeText(LoginActivity.this, R.string.login_parse_failure, Toast.LENGTH_SHORT).show();
                }
            } else if (result.getCode() == MyConstant.VISIT_CODE_NO_OK) {
                /* 登录失败 */
                Toast.makeText(LoginActivity.this, R.string.login_failure, Toast.LENGTH_SHORT).show();
            } else if (result.getCode() == MyConstant.VISIT_CODE_CONNECT_TIME_OUT) {
                /* 连接超时 */
                Toast.makeText(LoginActivity.this, R.string.login_time_out, Toast.LENGTH_SHORT).show();
            } else {
                /* 连接错错误或者其它 */
                Toast.makeText(LoginActivity.this, R.string.login_error, Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_setting:
                Intent settingIntent = new Intent(this, SettingActivity.class);
                startActivity(settingIntent);
                break;
            case R.id.menu_about:
                // Intent testIntent = new Intent(this, TestActivity.class);
                // startActivity(testIntent);

                Intent aboutIntent = new Intent(this, AboutActivity.class);
                startActivity(aboutIntent);
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    public void onTvClick(View v) {
        if (v.getId() == R.id.login_reset_password) {
            Intent intent = new Intent(this, ResetPasswordActivity.class);
            startActivity(intent);
        }
    }

    /**
     * 上次按的时间
     */
    private long preBackPressTime = 0;

    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - preBackPressTime <= MyConstant.NEXT_EXIT_TIME_LENGTH) {
            super.onBackPressed();
            System.exit(0);
        } else {
            preBackPressTime = currentTime;
            Toast.makeText(this, "再按一次退出应用", Toast.LENGTH_SHORT).show();
        }
    }

}
