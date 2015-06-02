package com.gf.BugManagerMobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.gf.BugManagerMobile.models.HttpResult;
import com.gf.BugManagerMobile.models.User;
import com.gf.BugManagerMobile.utils.HttpVisitUtils;
import com.gf.BugManagerMobile.utils.LocalInfo;
import com.gf.BugManagerMobile.utils.MyConstant;
import com.gf.BugManagerMobile.utils.RegexUtils;
import com.gf.BugManagerMobile.view.GroupMemberSelectDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * 添加团队界面
 * Created by GuLang on 2015-06-01.
 */
public class AddGroupActivity extends Activity {
    private static final String TAG = "AddGroupActivity";

    private EditText groupNameEt;
    private TextView groupMemberTv;
    private EditText groupIntroduceEt;
    private List<User> mAllUsers = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_add);

        groupNameEt = (EditText) findViewById(R.id.add_group_name_et);
        groupMemberTv = (TextView) findViewById(R.id.add_group_member_tv);
        groupIntroduceEt = (EditText) findViewById(R.id.add_group_introduce_et);

        HttpVisitUtils.getHttpVisit(this, LocalInfo.getBaseUrl(this) + "group/get-all-user", true, "获取系统用户中...",
            loadUserFinishListener);
    }

    private HttpVisitUtils.OnHttpFinishListener loadUserFinishListener = new HttpVisitUtils.OnHttpFinishListener() {
        @Override
        public void onVisitFinish(HttpResult result) {
            if (result == null)
                return;
            if (result.isVisitSuccess()) {
                try {
                    mAllUsers = JSON.parseArray(result.getResult(), User.class);
                    Log.i(TAG, mAllUsers.toString());
                } catch (Exception e) {
                    Toast.makeText(AddGroupActivity.this, "用户数据解析失败", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(AddGroupActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    };

    public void onOptClick(View v) {
        switch (v.getId()) {
            case R.id.add_group_back_imgv:
                this.finish();
                break;
            case R.id.add_group_save_btn:
                saveNewGroupInfo();
                break;
        }
    }

    private void saveNewGroupInfo() {
        /* 团队名称 */
        String groupName = groupNameEt.getText().toString().trim();
        if ("".equals(groupName)) {
            groupNameEt.setError("团队名称必填");
            return;
        }
        if (!RegexUtils.isMatch(MyConstant.NAME_PATTERN, groupName)) {
            groupNameEt.setError("只能输入中文、英文、数字、下划线");
            return;
        }

        /* 团队成员 */
        List<Integer> selectedUserIdData = null;
        if (mGroupMemberSelectDialog != null)
            selectedUserIdData = mGroupMemberSelectDialog.getSelectUserIdData();
        else
            selectedUserIdData = new ArrayList<Integer>();

        /* 团队简介 */
        String groupIntroduce = groupIntroduceEt.getText().toString().trim();

        // Log.i(TAG, "团队名称：" + groupName);
        // Log.i(TAG, "团队成员：" + JSON.toJSON(selectedUserIdData.toString()));
        // Log.i(TAG, "团队简介：" + groupIntroduce);

        String postData =
            "name=" + groupName + "&memberIds=" + JSON.toJSON(selectedUserIdData.toString()) + "&introduce="
                + groupIntroduce;

        HttpVisitUtils.postHttpVisit(this, LocalInfo.getBaseUrl(this) + "group/add", postData, true, "添加中...",
            onAddGroupFinishListener);
    }

    private HttpVisitUtils.OnHttpFinishListener onAddGroupFinishListener = new HttpVisitUtils.OnHttpFinishListener() {
        @Override
        public void onVisitFinish(HttpResult result) {
            if (result == null)
                return;
            if (result.isVisitSuccess()) {
                Toast.makeText(AddGroupActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                groupNameEt.setText("");
                groupIntroduceEt.setText("");
            } else {
                Toast.makeText(AddGroupActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                if (result.getCode() == MyConstant.VISIT_CODE_NO_LOGIN) {
                    Intent intent = new Intent(AddGroupActivity.this, LoginActivity.class);
                    startActivity(intent);
                    AddGroupActivity.this.finish();
                }
            }

        }
    };

    private GroupMemberSelectDialog mGroupMemberSelectDialog = null;

    public void onSelectMemberClick(View v) {
        if (mGroupMemberSelectDialog == null) {
            mGroupMemberSelectDialog = new GroupMemberSelectDialog(this);
            mGroupMemberSelectDialog.resetListViewData(mAllUsers);
        }
        if (!mGroupMemberSelectDialog.isShowing())
            mGroupMemberSelectDialog.show();
    }
}
