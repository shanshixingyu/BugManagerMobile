package com.gf.BugManagerMobile;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.gf.BugManagerMobile.adapter.GroupSpinnerAdapter;
import com.gf.BugManagerMobile.models.Group;
import com.gf.BugManagerMobile.models.HttpResult;
import com.gf.BugManagerMobile.utils.*;

import java.util.List;

/**
 * 添加项目界面
 * Created by GuLang on 2015-06-02.
 */
public class AddProjectActivity extends Activity {
    private static final String TAG = "AddProjectActivity";

    private Button topSaveBtn;
    private EditText projectNameEt, projectIntroduceEt;
    private Spinner projectGroupSp;
    private GroupSpinnerAdapter mGroupSpinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_add);

        HttpVisitUtils.getHttpVisit(this, LocalInfo.getBaseUrl(this) + "project/get-all-group", true, "加载团队信息中...",
            onLoadGroupsFinishListener);

        initComponent();
    }

    private HttpVisitUtils.OnHttpFinishListener onLoadGroupsFinishListener = new HttpVisitUtils.OnHttpFinishListener() {
        @Override
        public void onVisitFinish(HttpResult result) {
            if (result == null)
                return;
            if (result.isVisitSuccess()) {
                try {
                    List<Group> groupDataList = JSON.parseArray(result.getResult(), Group.class);
                    mGroupSpinnerAdapter = new GroupSpinnerAdapter(AddProjectActivity.this, groupDataList);
                    projectGroupSp.setAdapter(mGroupSpinnerAdapter);
                    topSaveBtn.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    Toast.makeText(AddProjectActivity.this, "团队信息解析失败", Toast.LENGTH_SHORT).show();
                }
            } else {
                HttpConnectResultUtils.optFailure(AddProjectActivity.this, result);
            }
        }
    };

    private void initComponent() {
        projectNameEt = (EditText) findViewById(R.id.project_add_name_et);
        projectIntroduceEt = (EditText) findViewById(R.id.project_add_introduce_et);
        projectGroupSp = (Spinner) findViewById(R.id.project_add_group_sp);
        projectGroupSp.setAdapter(mGroupSpinnerAdapter);
        topSaveBtn = (Button) findViewById(R.id.project_add_save_btn);
    }

    public void onOptClick(View v) {
        switch (v.getId()) {
            case R.id.project_add_back_imgv:
                this.finish();
                break;
            case R.id.project_add_save_btn:
                addNewProject();
                break;
        }
    }

    private void addNewProject() {
        /* 项目名称 */
        String projectName = projectNameEt.getText().toString().trim();
        if (projectName.equals("")) {
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
            Toast.makeText(AddProjectActivity.this, "请先添加团队信息", Toast.LENGTH_SHORT).show();
            return;
        }
        int groupId;
        if (mGroupSpinnerAdapter != null) {
            Group group = mGroupSpinnerAdapter.getItem(selectedPosition);
            if (group == null) {
                Toast.makeText(AddProjectActivity.this, "获取的索引有错", Toast.LENGTH_SHORT).show();
                return;
            } else {
                groupId = group.getId();
            }
        } else {
            Toast.makeText(AddProjectActivity.this, "没有团队信息", Toast.LENGTH_SHORT).show();
            return;
        }

        /* 项目简介 */
        String projectIntroduc = projectIntroduceEt.getText().toString().trim();

        String postData = "name=" + projectName + "&groupId=" + groupId + "&introduce=" + projectIntroduc;

        HttpVisitUtils.postHttpVisit(AddProjectActivity.this, LocalInfo.getBaseUrl(AddProjectActivity.this)
            + "project/add-project", postData, true, "添加中...", onAddProjectFinishListener);
    }

    private HttpVisitUtils.OnHttpFinishListener onAddProjectFinishListener = new HttpVisitUtils.OnHttpFinishListener() {
        @Override
        public void onVisitFinish(HttpResult result) {
            if (result == null)
                return;
            if (result.isVisitSuccess()) {
                // Log.i(TAG, result.getResult());
                Toast.makeText(AddProjectActivity.this, "添加项目信息成功", Toast.LENGTH_SHORT).show();
                projectNameEt.setText("");
                projectIntroduceEt.setText("");
            } else {
                HttpConnectResultUtils.optFailure(AddProjectActivity.this, result);
            }
        }
    };

}
