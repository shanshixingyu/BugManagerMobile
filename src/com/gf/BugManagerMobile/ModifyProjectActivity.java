package com.gf.BugManagerMobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.alibaba.fastjson.JSON;
import com.gf.BugManagerMobile.adapter.GroupSpinnerAdapter;
import com.gf.BugManagerMobile.models.Group;
import com.gf.BugManagerMobile.models.HttpResult;
import com.gf.BugManagerMobile.models.Project;
import com.gf.BugManagerMobile.utils.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * 修改项目信息界面
 * Created by GuLang on 2015-06-03.
 */
public class ModifyProjectActivity extends Activity {
    private static final String TAG = "ModifyProjectActivity";

    private int projectId;
    private Button topSaveBtn;
    private EditText projectNameEt, projectIntroduceEt;
    private Spinner projectGroupSp;
    private GroupSpinnerAdapter mGroupSpinnerAdapter;
    private TextView projectCreatorTv, projectCreateTimeTv;
    private Project mProject;
    private String mCreatorName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_modify);

        Intent startIntent = getIntent();
        projectId = startIntent.getIntExtra(MyConstant.PROJECT_LIST_2_PROJECT_MODIFY, -1);
        if (projectId < 0) {
            Toast.makeText(this, "界面跳转数据传递出错", Toast.LENGTH_SHORT).show();
            return;
        }
        initComponent();
        HttpVisitUtils.getHttpVisit(this, LocalInfo.getBaseUrl(this) + "project/get-project&projectId=" + projectId,
            true, "加载项目信息中...", onLoadProjectFinishListener);
    }

    private HttpVisitUtils.OnHttpFinishListener onLoadProjectFinishListener =
        new HttpVisitUtils.OnHttpFinishListener() {
            @Override
            public void onVisitFinish(HttpResult result) {
                if (result == null)
                    return;
                if (result.isVisitSuccess()) {
                    Log.i(TAG, result.getResult());
                    try {
                        JSONObject jsonObj = new JSONObject(result.getResult());
                        mProject = JSON.parseObject(jsonObj.getString("project"), Project.class);
                        mCreatorName = jsonObj.getString("creatorName");
                        List<Group> groups = JSON.parseArray(jsonObj.getString("allGroup"), Group.class);

                        if (mProject != null) {
                            projectNameEt.setText(mProject.getName());
                            projectIntroduceEt.setText(mProject.getIntroduce());
                            mGroupSpinnerAdapter = new GroupSpinnerAdapter(ModifyProjectActivity.this, groups);
                            projectGroupSp.setAdapter(mGroupSpinnerAdapter);
                            // 找到负责团队，并且将其设置为选中状态
                            int selectedPosition = searchInitSelectedPosition(groups, mProject.getGroup_id());
                            projectGroupSp.setSelection(selectedPosition);
                            projectCreatorTv.setText(mCreatorName);
                            projectCreateTimeTv.setText(mProject.getCreate_time());
                        }
                        topSaveBtn.setVisibility(View.VISIBLE);

                    } catch (JSONException e) {
                        Toast.makeText(ModifyProjectActivity.this, "数据解析出错", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    HttpConnectResultUtils.optFailure(ModifyProjectActivity.this, result);
                }
            }
        };

    private void initComponent() {
        topSaveBtn = (Button) findViewById(R.id.project_modify_save_btn);
        projectNameEt = (EditText) findViewById(R.id.project_modify_name_et);
        projectIntroduceEt = (EditText) findViewById(R.id.project_modify_introduce_et);
        projectGroupSp = (Spinner) findViewById(R.id.project_modify_group_sp);
        projectGroupSp.setAdapter(mGroupSpinnerAdapter);
        projectCreatorTv = (TextView) findViewById(R.id.project_modify_creator_tv);
        projectCreateTimeTv = (TextView) findViewById(R.id.project_modify_create_time_tv);
    }

    /**
     * 在列表数据中找到指定ID的Group的下标
     * @param groups
     * @param targetGroupId
     * @return
     */
    private int searchInitSelectedPosition(List<Group> groups, int targetGroupId) {
        int count = groups.size();
        for (int i = 0; i < count; i++) {
            if (targetGroupId == groups.get(i).getId())
                return i;
        }
        return Spinner.INVALID_POSITION;
    }

    public void onOptClick(View v) {
        switch (v.getId()) {
            case R.id.project_modify_back_imgv:
                this.finish();
                break;
            case R.id.project_modify_save_btn:
                modifyProjectInfo();
                break;
        }
    }

    private void modifyProjectInfo() {
        /* 项目名称 */
        String projectName = projectNameEt.getText().toString().trim();
        if ("".equals(projectName)) {
            projectNameEt.setError("项目名称必填");
            return;
        }
        if (!RegexUtils.isMatch(MyConstant.NAME_PATTERN, projectName)) {
            projectNameEt.setError("只能输入中文、英文、数字和下划线");
            return;
        }

        /* 负责团队 */
        int selectedPosition = projectGroupSp.getSelectedItemPosition();
        if (selectedPosition == Spinner.INVALID_POSITION) {
            Toast.makeText(this, "请先添加团队信息", Toast.LENGTH_SHORT).show();
            return;
        }
        int groupId;
        if (mGroupSpinnerAdapter != null) {
            Group group = mGroupSpinnerAdapter.getItem(selectedPosition);
            if (group == null) {
                Toast.makeText(this, "获取的索引有错", Toast.LENGTH_SHORT).show();
                return;
            } else {
                groupId = group.getId();
            }
        } else {
            Toast.makeText(this, "没有团队信息", Toast.LENGTH_SHORT).show();
            return;
        }

        /* 项目简介 */
        String projectIntroduce = projectIntroduceEt.getText().toString().trim();

        Log.i(TAG, "项目名称：" + projectName);
        Log.i(TAG, "负责团队：" + groupId);
        Log.i(TAG, "项目简介：" + projectIntroduce);

        if (!isChanged(projectName, groupId, projectIntroduce)) {
            Toast.makeText(this, "项目信息未发生任何修改", Toast.LENGTH_SHORT).show();
            return;
        }

        String postData = "name=" + projectName + "&groupId=" + groupId + "&introduce=" + projectIntroduce;
        HttpVisitUtils.postHttpVisit(this,
            LocalInfo.getBaseUrl(this) + "project/modify-project&projectId=" + projectId, postData, true, "修改中...",
            onModifyProjectFinishListener);
    }

    private HttpVisitUtils.OnHttpFinishListener onModifyProjectFinishListener =
        new HttpVisitUtils.OnHttpFinishListener() {
            @Override
            public void onVisitFinish(HttpResult result) {
                if (result == null)
                    return;
                if (result.isVisitSuccess()) {
                    Toast.makeText(ModifyProjectActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                } else {
                    HttpConnectResultUtils.optFailure(ModifyProjectActivity.this, result);
                }
            }
        };

    /**
     * 判断用户输入是否发生改变
     * @param name
     * @param groupId
     * @param introduce
     * @return
     */
    private boolean isChanged(String name, int groupId, String introduce) {
        if (mProject == null)
            return false;
        if (name != null && !name.equals(mProject.getName()))
            return true;
        if (groupId != mProject.getGroup_id())
            return true;
        if (introduce != null && !introduce.equals(mProject.getIntroduce()))
            return true;
        return false;
    }

}
