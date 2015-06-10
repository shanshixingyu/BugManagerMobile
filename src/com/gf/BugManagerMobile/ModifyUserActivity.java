package com.gf.BugManagerMobile;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.alibaba.fastjson.JSON;
import com.gf.BugManagerMobile.models.HttpResult;
import com.gf.BugManagerMobile.models.LoginSuccessInfo;
import com.gf.BugManagerMobile.models.User;
import com.gf.BugManagerMobile.utils.HttpVisitUtils;
import com.gf.BugManagerMobile.utils.LocalInfo;
import com.gf.BugManagerMobile.utils.MyConstant;
import com.gf.BugManagerMobile.utils.RegexUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 修改用户信息
 * Created by GuLang on 2015-05-21.
 */
public class ModifyUserActivity extends BaseActivity {
    private static final String TAG = "ModifyUserActivity";

    private EditText userNameEt;
    private Spinner userRoleSpinner;
    private EditText userEmailEt;
    private TextView userCreatorTv;
    private TextView userCreateTimeTv;
    private Button modifyUserBtn;
    private int mUserId;
    private User mUser;
    private User mCreateUser;
    private LoginSuccessInfo loginSuccessInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_modify);

        userNameEt = (EditText) findViewById(R.id.modify_user_name_et);
        userRoleSpinner = (Spinner) findViewById(R.id.modify_user_role_sp);
        userEmailEt = (EditText) findViewById(R.id.modify_user_email_et);
        userCreatorTv = (TextView) findViewById(R.id.modify_user_creator_tv);
        userCreateTimeTv = (TextView) findViewById(R.id.modify_user_create_time_tv);
        modifyUserBtn = (Button) findViewById(R.id.modify_user_modify_btn);
        loginSuccessInfo = LocalInfo.getLoginSuccessInfo(this);

        // 设置修改按钮状态
        setModifyUserBtnState();

        if (savedInstanceState != null)
            mUserId = savedInstanceState.getInt("BUG_ID", -1);
        else
            mUserId = getIntent().getIntExtra(MyConstant.USER_LIST_2_USER_MODIFY, -100);
        if (mUserId < 0)
            Toast.makeText(this, "获取用户ID失败", Toast.LENGTH_SHORT).show();
        else
            HttpVisitUtils.getHttpVisit(this, LocalInfo.getBaseUrl(this) + "user/user&userId=" + mUserId, true,
                "获取数据中...", loadOnHttpFinishListener);
    }

    private void setModifyUserBtnState() {
        if (loginSuccessInfo != null && mUser != null
            && (loginSuccessInfo.getRoleId() == 0 || loginSuccessInfo.getUserId() == mUser.getCreator())) {
            modifyUserBtn.setVisibility(View.VISIBLE);
            userNameEt.setEnabled(true);
            userRoleSpinner.setEnabled(true);
            userEmailEt.setEnabled(true);
        } else {
            modifyUserBtn.setVisibility(View.GONE);
            userNameEt.setEnabled(false);
            userRoleSpinner.setEnabled(false);
            userEmailEt.setEnabled(false);
        }
    }

    private HttpVisitUtils.OnHttpFinishListener loadOnHttpFinishListener = new HttpVisitUtils.OnHttpFinishListener() {
        @Override
        public void onVisitFinish(HttpResult result) {
            if (result == null)
                return;
            Log.i(TAG, result.toString());
            if (result.isVisitSuccess()) {
                try {
                    JSONObject jsonObject = new JSONObject(result.getResult());
                    mUser = JSON.parseObject(jsonObject.getString("user"), User.class);
                    mCreateUser = JSON.parseObject(jsonObject.getString("creator"), User.class);
                    if (mUser != null) {
                        userNameEt.setText(mUser.getName());
                        userRoleSpinner.setSelection(mUser.getRole_id() - 1);
                        userEmailEt.setText(mUser.getEmail());
                        userCreateTimeTv.setText(mUser.getCreate_time());
                    }
                    if (mCreateUser != null)
                        userCreatorTv.setText(mCreateUser.getName());
                    // 设置修改按钮状态
                    setModifyUserBtnState();
                } catch (JSONException e) {
                    Toast.makeText(ModifyUserActivity.this, "解析失败", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(ModifyUserActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }
    };

    public void onOptClick(View v) {
        switch (v.getId()) {
            case R.id.modify_user_back_imgv:
                this.finish();
                break;
            case R.id.modify_user_modify_btn:
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

                if (!isChanged(nameStr, roleId, emailStr)) {
                    Toast.makeText(ModifyUserActivity.this, "信息没有发生任何修改", Toast.LENGTH_SHORT).show();
                    return;
                }

                String postData = "name=" + nameStr + "&roleId=" + roleId + "&email=" + emailStr;
                Log.i(TAG, "传递的数据：" + postData);
                HttpVisitUtils.postHttpVisit(ModifyUserActivity.this, LocalInfo.getBaseUrl(ModifyUserActivity.this)
                    + "user/modify&userId=" + mUserId, postData, true, "修改中...", modifyOnHttpFinishListener);
                break;
        }
    }

    /**
     * 判断是否已经发生改变
     * @param newName
     * @param newRoleId
     * @param newEmail
     * @return
     */
    private boolean isChanged(String newName, int newRoleId, String newEmail) {
        if (mUser == null)
            return false;
        if (mUser.getName() != null && !newName.equals(mUser.getName()))
            return true;
        if (mUser.getRole_id() != newRoleId)
            return true;
        if (mUser.getEmail() != null && !mUser.getEmail().equals(newEmail))
            return true;
        return false;
    }

    private HttpVisitUtils.OnHttpFinishListener modifyOnHttpFinishListener = new HttpVisitUtils.OnHttpFinishListener() {
        @Override
        public void onVisitFinish(HttpResult result) {
            if (result == null)
                return;
            Log.i(TAG, result.toString());
            if (result.isVisitSuccess()) {
                try {
                    mUser = JSON.parseObject(result.getResult(), User.class);
                    if (mUser != null) {
                        userNameEt.setText(mUser.getName());
                        userRoleSpinner.setSelection(mUser.getRole_id() - 1);
                        userEmailEt.setText(mUser.getEmail());
                        userCreateTimeTv.setText(mUser.getCreate_time());
                    }
                } catch (Exception e) {
                    // Toast.makeText(ModifyUserActivity.this, "解析用户新失败", Toast.LENGTH_SHORT).show();
                }
                // 设置修改按钮状态
                setModifyUserBtnState();
                Toast.makeText(ModifyUserActivity.this, "信息修改成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ModifyUserActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (outState != null) {
            outState.putInt("USER_ID", mUserId);
        }
        super.onSaveInstanceState(outState);
    }
}
