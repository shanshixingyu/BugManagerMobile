package com.gf.BugManagerMobile;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.alibaba.fastjson.JSON;
import com.gf.BugManagerMobile.models.*;
import com.gf.BugManagerMobile.utils.*;
import com.gf.BugManagerMobile.view.GroupMemberSelectDialog;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * 修改模块信息界面
 * Created by GuLang on 2015-06-04.
 */
public class ModifyModuleActivity extends BaseActivity {
    private static final String TAG = "ModifyModuleActivity";

    private int moduleId;
    private Button topSaveBtn;
    private EditText moduleNameEt, moduleIntroduceEt;
    private TextView projectNameTv, moduleFzrTv, moduleCreatorTv, moduleCreateTimeTv;
    private LoginSuccessInfo mLoginSuccessInfo;
    private Project mProject;
    private Module mModule;
    private List<User> mGroupMembers;
    private String mCreatorName;
    private List<Integer> mFzrDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_modify);

        moduleId = getIntent().getIntExtra(MyConstant.MODULE_LIST_2_MODULE_MODIFY, -1);
        if (moduleId < 0) {
            Toast.makeText(this, "界面跳转传递数据出错", Toast.LENGTH_SHORT).show();
            return;
        }
        intComponent();

        mLoginSuccessInfo = LocalInfo.getLoginSuccessInfo(this);

        HttpVisitUtils.getHttpVisit(this,
            LocalInfo.getBaseUrl(this) + "project/get-modify-module&moduleId=" + moduleId, true, "数据加载中...",
            onLoadFinishListener);

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
                    mProject = JSON.parseObject(jsonObject.getString("project"), Project.class);
                    mModule = JSON.parseObject(jsonObject.getString("module"), Module.class);
                    mGroupMembers = JSON.parseArray(jsonObject.getString("memberUsers"), User.class);
                    mCreatorName = jsonObject.getString("creatorName");

                    if (mProject != null && mModule != null) {
                        if (mLoginSuccessInfo.getRoleId() == 0 || mLoginSuccessInfo.getUserId() == mModule.getCreator()) {
                            topSaveBtn.setVisibility(View.VISIBLE);
                        }
                        moduleNameEt.setText(mModule.getName());
                        projectNameTv.setText(mProject.getName());
                        moduleIntroduceEt.setText(mModule.getIntroduce());
                        moduleCreatorTv.setText("" + mCreatorName);
                        moduleCreateTimeTv.setText(mModule.getCreate_time());
                        mFzrDataList = JSON.parseArray(mModule.getFuzeren(), Integer.class);
                    }
                } catch (JSONException e) {
                    Toast.makeText(ModifyModuleActivity.this, "数据解析失败", Toast.LENGTH_SHORT).show();
                }
            } else {
                HttpConnectResultUtils.optFailure(ModifyModuleActivity.this, result);
            }
        }
    };

    private void intComponent() {
        topSaveBtn = (Button) findViewById(R.id.module_modify_save_btn);
        moduleNameEt = (EditText) findViewById(R.id.module_modify_name_et);
        projectNameTv = (TextView) findViewById(R.id.module_modify_project_name_tv);
        moduleFzrTv = (TextView) findViewById(R.id.module_modify_fzr_tv);
        moduleIntroduceEt = (EditText) findViewById(R.id.module_modify_introduce_et);
        moduleCreatorTv = (TextView) findViewById(R.id.module_modify_creator_tv);
        moduleCreateTimeTv = (TextView) findViewById(R.id.module_modify_create_time_tv);
    }

    public void onOptClick(View v) {
        switch (v.getId()) {
            case R.id.module_modify_back_imgv:
                this.finish();
                break;
            case R.id.module_modify_save_btn:
                modifyModuleInfo();
                break;
        }
    }

    private GroupMemberSelectDialog mGroupMemberSelectDialog = null;

    public void onSelectMemberClick(View v) {
        if (mGroupMemberSelectDialog == null) {
            mGroupMemberSelectDialog = new GroupMemberSelectDialog(this);
            mGroupMemberSelectDialog.resetListViewData(mGroupMembers);
            mGroupMemberSelectDialog.resetListState(mFzrDataList);
        }
        if (!mGroupMemberSelectDialog.isShowing())
            mGroupMemberSelectDialog.show();
    }

    private void modifyModuleInfo() {
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

        /* 负责人信息 */
        List<Integer> selectedFzrIdData = null;
        if (mGroupMemberSelectDialog != null)
            selectedFzrIdData = mGroupMemberSelectDialog.getSelectUserIdData();
        else
            selectedFzrIdData = mFzrDataList;

        /* 模块简介 */
        String moduleIntroduce = moduleIntroduceEt.getText().toString().trim();

        if (!isChanged(moduleName, selectedFzrIdData, moduleIntroduce)) {
            Toast.makeText(this, "数据未发生任何改变", Toast.LENGTH_SHORT).show();
            return;
        }

        String postData =
            "name=" + moduleName + "&fzr=" + JSON.toJSON(selectedFzrIdData) + "&introduce=" + moduleIntroduce;

        HttpVisitUtils.postHttpVisit(this, LocalInfo.getBaseUrl(this) + "project/modify-module&moduleId=" + moduleId,
            postData, true, "修改中...", onModifyFinishListener);
    }

    private HttpVisitUtils.OnHttpFinishListener onModifyFinishListener = new HttpVisitUtils.OnHttpFinishListener() {
        @Override
        public void onVisitFinish(HttpResult result) {
            if (result == null)
                return;
            if (result.isVisitSuccess()) {
                Toast.makeText(ModifyModuleActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                /* 需要更新选中的负责人信息，不然下次保存判断是否修改的时候就会出错 */
                if (mGroupMemberSelectDialog != null)
                    mFzrDataList = mGroupMemberSelectDialog.getSelectUserIdData();
            } else {
                HttpConnectResultUtils.optFailure(ModifyModuleActivity.this, result);
            }
        }
    };

    /**
     * 判断数据是否发生修改
     * @param moduleName
     * @param selectedFzr
     * @param moduleIntroduce
     * @return
     */
    private boolean isChanged(String moduleName, List<Integer> selectedFzr, String moduleIntroduce) {
        if (mModule == null)
            return false;
        if (moduleName != null && !moduleName.equals(mModule.getName()))
            return true;
        if (moduleIntroduce != null && !moduleIntroduce.equals(mModule.getIntroduce()))
            return true;

        if (selectedFzr != null && mFzrDataList != null) {
            int count = mGroupMembers.size();
            if (count == mFzrDataList.size()) {
                for (int i = 0; i < count; i++)
                    if (!mGroupMembers.contains(selectedFzr.get(i)))
                        return true;
            } else {
                return true;
            }
        }

        return false;
    }
}
