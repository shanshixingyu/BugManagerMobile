package com.gf.BugManagerMobile;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.gf.BugManagerMobile.models.HttpResult;
import com.gf.BugManagerMobile.utils.HttpVisitUtils;
import com.gf.BugManagerMobile.utils.LocalInfo;
import com.gf.BugManagerMobile.utils.MyConstant;
import com.gf.BugManagerMobile.utils.RegexUtils;

/**
 * 添加用户界面
 * Created by GuLang on 2015-05-20.
 */
public class AddUserActivity extends BaseActivity {
    private static final String TAG = "AddUser";

    private EditText userNameEt;
    private Spinner userRoleSpinner;
    private EditText userEmailEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_add);

        userNameEt = (EditText) findViewById(R.id.add_user_name_et);
        userRoleSpinner = (Spinner) findViewById(R.id.add_user_role_sp);
        userEmailEt = (EditText) findViewById(R.id.add_user_email_et);
    }

    public void onOptClick(View v) {
        switch (v.getId()) {
            case R.id.add_user_back_imgv:
                this.finish();
                break;
            case R.id.add_user_save_btn:
                /* 用户名称 */
                String nameStr = userNameEt.getText().toString();
                if ("".equals(nameStr.trim())) {
                    userNameEt.requestFocus();
                    userNameEt.setError("用户名称必填", null);
                    return;
                }
                if (!RegexUtils.isMatch(MyConstant.NAME_PATTERN, nameStr)) {
                    userNameEt.requestFocus();
                    userNameEt.setError("只能中文、英文、数字字符、下划线");
                    return;
                }
                /* 用户角色(+1的原因是：0表示超级管理员) */
                int roleId = userRoleSpinner.getSelectedItemPosition() + 1;

                String emailStr = userEmailEt.getText().toString();
                if ("".equals(emailStr.trim())) {
                    userEmailEt.requestFocus();
                    userEmailEt.setError("邮箱必填");
                    return;
                }
                if (!RegexUtils.isMatch(MyConstant.EMAIL_PATTERN, emailStr)) {
                    userEmailEt.requestFocus();
                    userEmailEt.setError("格式不合法");
                    return;
                }

                String postData = "name=" + nameStr + "&roleId=" + roleId + "&email=" + emailStr;
                Log.i(TAG, "传递的数据：" + postData);
                HttpVisitUtils.postHttpVisit(AddUserActivity.this, LocalInfo.getBaseUrl(AddUserActivity.this)
                    + "user/add", postData, true, "添加用户中...", addOnHttpFinishListener);
                break;
        }
    }

    private HttpVisitUtils.OnHttpFinishListener addOnHttpFinishListener = new HttpVisitUtils.OnHttpFinishListener() {
        @Override
        public void onVisitFinish(HttpResult result) {
            if (result == null)
                return;
            if (result.isVisitSuccess()) {
                Toast.makeText(AddUserActivity.this, "添加用户成功", Toast.LENGTH_SHORT).show();
                userNameEt.setText("");
                userRoleSpinner.setSelection(0);
                userEmailEt.setText("");
            } else {
                Toast.makeText(AddUserActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                if (result.getCode() == 500) {// 用户名字已经存在
                    userNameEt.setError(result.getMessage());
                }
            }
        }
    };
}
