package com.gf.BugManagerMobile;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.gf.BugManagerMobile.models.HttpResult;
import com.gf.BugManagerMobile.models.LoginSuccessInfo;
import com.gf.BugManagerMobile.models.Project;
import com.gf.BugManagerMobile.models.User;
import com.gf.BugManagerMobile.utils.*;
import com.gf.BugManagerMobile.view.GroupMemberSelectDialog;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 添加模块界面
 * Created by GuLang on 2015-06-03.
 */
public class AddModuleActivity extends BaseActivity {
    private static final String TAG = "AddModuleActivity";

    private int projectId;

    private Button topSaveBtn;
    private EditText moduleNameEt, moduleIntroduceEt;
    private TextView mModuleFzrTv, projectNameTv;
    private LoginSuccessInfo mLoginSuccessInfo;
    private List<User> mGroupMembers;
    private Project mProject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_add);

        if (savedInstanceState != null)
            projectId = savedInstanceState.getInt("MODULE_ID", -1);
        else
            projectId = getIntent().getIntExtra(MyConstant.MODULE_LIST_2_MODULE_ADD, -1);
        if (projectId < 0) {
            Toast.makeText(this, "跳转传递项目ID出错", Toast.LENGTH_SHORT).show();
            return;
        }

        mLoginSuccessInfo = LocalInfo.getLoginSuccessInfo(this);
        topSaveBtn = (Button) findViewById(R.id.module_add_save_btn);
        moduleNameEt = (EditText) findViewById(R.id.module_add_name_et);
        moduleIntroduceEt = (EditText) findViewById(R.id.module_add_introduce_et);
        mModuleFzrTv = (TextView) findViewById(R.id.module_add_fzr_tv);
        projectNameTv = (TextView) findViewById(R.id.module_add_project_name_tv);

        HttpVisitUtils.getHttpVisit(this, LocalInfo.getBaseUrl(this) + "project/get-add-module&projectId=" + projectId,
            true, "加载数据中...", onLoadFinishListener);
    }

    private HttpVisitUtils.OnHttpFinishListener onLoadFinishListener = new HttpVisitUtils.OnHttpFinishListener() {
        @Override
        public void onVisitFinish(HttpResult result) {
            if (result == null)
                return;
            if (result.isVisitSuccess()) {
                Log.i(TAG, result.getResult());
                try {
                    JSONObject jsonObject = new JSONObject(result.getResult());
                    mGroupMembers = JSON.parseArray(jsonObject.getString("memberUsers"), User.class);
                    mProject = JSON.parseObject(jsonObject.getString("project"), Project.class);

                    if (mProject != null) {
                        if (mLoginSuccessInfo.getRoleId() == 0
                            || mProject.getCreator() == mLoginSuccessInfo.getUserId()) {
                            topSaveBtn.setVisibility(View.VISIBLE);
                            mModuleFzrTv.setVisibility(View.VISIBLE);
                        } else {
                            topSaveBtn.setVisibility(View.INVISIBLE);
                            mModuleFzrTv.setVisibility(View.INVISIBLE);
                        }
                        projectNameTv.setText(mProject.getName());
                    }
                } catch (JSONException e) {
                    Toast.makeText(AddModuleActivity.this, "数据解析失败", Toast.LENGTH_SHORT).show();
                }
            } else {
                HttpConnectResultUtils.optFailure(AddModuleActivity.this, result);
            }
        }
    };

    private GroupMemberSelectDialog mGroupMemberSelectDialog = null;

    public void onSelectMemberClick(View v) {
        if (mGroupMemberSelectDialog == null) {
            mGroupMemberSelectDialog = new GroupMemberSelectDialog(this);
            mGroupMemberSelectDialog.resetListViewData(mGroupMembers);
        }
        if (!mGroupMemberSelectDialog.isShowing())
            mGroupMemberSelectDialog.show();
    }

    public void onOptClick(View v) {
        switch (v.getId()) {
            case R.id.module_add_back_imgv:
                this.finish();
                break;
            case R.id.module_add_save_btn:
                addNewModule();
                break;
        }
    }

    private void addNewModule() {
        /* 模块名称 */
        String moduleName = moduleNameEt.getText().toString().trim();
        if ("".equals(moduleName)) {
            moduleNameEt.setError("模块名称必填");
            return;
        }
        if (!RegexUtils.isMatch(MyConstant.NAME_PATTERN, moduleName)) {
            moduleNameEt.setError("只能输入中文、英文、数字和下划线");
            return;
        }

        /* 负责人 */
        if (mGroupMemberSelectDialog == null || mGroupMemberSelectDialog.getSelectUserIdData().size() <= 0) {
            Toast.makeText(this, "负责人必选", Toast.LENGTH_SHORT).show();
            return;
        }
        List<Integer> selectedUserIdData = mGroupMemberSelectDialog.getSelectUserIdData();

        /* 模块简介 */
        String moduleIntroduce = moduleIntroduceEt.getText().toString().trim();

        String postData =
            "name=" + moduleName + "&fzr=" + JSON.toJSON(selectedUserIdData.toString()) + "&introduce="
                + moduleIntroduce;

        HttpVisitUtils.postHttpVisit(this, LocalInfo.getBaseUrl(this) + "project/add-module&projectId=" + projectId,
            postData, true, "保存中...", onSaveFinishListener);

    }

    private HttpVisitUtils.OnHttpFinishListener onSaveFinishListener = new HttpVisitUtils.OnHttpFinishListener() {
        @Override
        public void onVisitFinish(HttpResult result) {
            if (result == null)
                return;
            if (result.isVisitSuccess()) {
                Toast.makeText(AddModuleActivity.this, "模块添加成功", Toast.LENGTH_SHORT).show();
                moduleNameEt.setText("");
                if (mGroupMemberSelectDialog != null)
                    mGroupMemberSelectDialog.resetListState(null);
                moduleIntroduceEt.setText("");
            } else {
                HttpConnectResultUtils.optFailure(AddModuleActivity.this, result);
            }
        }
    };

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (outState != null) {
            outState.putInt("PROJECT_ID", projectId);
        }
        super.onSaveInstanceState(outState);
    }
}
