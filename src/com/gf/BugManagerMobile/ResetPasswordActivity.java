package com.gf.BugManagerMobile;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.gf.BugManagerMobile.models.HttpResult;
import com.gf.BugManagerMobile.utils.HttpVisitUtils;
import com.gf.BugManagerMobile.utils.LocalInfo;
import com.gf.BugManagerMobile.utils.MyConstant;
import com.gf.BugManagerMobile.utils.RegexUtils;

import java.net.URLEncoder;

/**
 * 重置用户密码界面
 * Created by GuLang on 2015-05-24.
 */
public class ResetPasswordActivity extends BaseActivity {
    private static final String TAG = "ResetPasswordActivity";

    private EditText userNameEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        userNameEt = (EditText) findViewById(R.id.reset_user_name_et);
    }

    public void onOptClick(View v) {
        switch (v.getId()) {
            case R.id.reset_back_imgv:
                this.finish();
                break;
            case R.id.top_reset_btn:
                String userName = userNameEt.getText().toString().trim();
                /* 判断用户是否输入 */
                if ("".equals(userName)) {
                    userNameEt.setError("用户名称必填");
                    return;
                }

                // 对输入的内容进行正则匹配
                if (!RegexUtils.isMatch(MyConstant.NAME_PATTERN, userName)) {
                    userNameEt.setError("只能输入中文、英文、数字、下划线");
                    return;
                }

                // 发送请求
                HttpVisitUtils.getHttpVisit(this, LocalInfo.getBaseUrl(this) + "site/reset-password&userName="
                    + URLEncoder.encode(userName), true, "重置密码中...", resetHttpFinishListener);

                break;
        }
    }

    private HttpVisitUtils.OnHttpFinishListener resetHttpFinishListener = new HttpVisitUtils.OnHttpFinishListener() {
        @Override
        public void onVisitFinish(HttpResult result) {
            if (result == null)
                return;
            if (result.isVisitSuccess()) {
                userNameEt.setText("");
            }
            Toast.makeText(ResetPasswordActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };
}
