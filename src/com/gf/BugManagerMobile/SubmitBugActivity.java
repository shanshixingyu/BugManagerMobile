package com.gf.BugManagerMobile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.alibaba.fastjson.JSON;
import com.gf.BugManagerMobile.adapter.ModuleSpinnerAdapter;
import com.gf.BugManagerMobile.adapter.ProjectSpinnerAdapter;
import com.gf.BugManagerMobile.adapter.UserSpinnerAdapter;
import com.gf.BugManagerMobile.models.HttpResult;
import com.gf.BugManagerMobile.models.Module;
import com.gf.BugManagerMobile.models.Project;
import com.gf.BugManagerMobile.models.User;
import com.gf.BugManagerMobile.utils.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * 提交新bug界面
 * Created by Administrator on 5/11 0011.
 */
public class SubmitBugActivity extends BaseActivity {
    private static final String TAG = "SubmitBugActivity";

    private TextView mSubmitTitleTv;
    private Button mSubmitSaveBtn;
    private EditText mBugNameEt;
    private Spinner mProjectSp;
    private Spinner mModuleSp;
    private Spinner mAssignSp;
    private Spinner mPrioritySp;
    private Spinner mSeriousSp;
    private EditText mIntroduceEt;
    private EditText mReappearEt;

    private ProjectSpinnerAdapter mProjectSpAdapter;
    private ModuleSpinnerAdapter mModuleSpAdapter;
    private UserSpinnerAdapter mMemberSpAdapter;

    private List<Project> mProjectData;
    private List<Module> mModuleData;
    private Module mAllInfoModule;
    private List<User> mMemberData;

    // 以下这两个变量的目的是为了避免重新设置spinner内容的时候导致其选中的回调
    private int mProjectResetCount = -1;
    private int mModuleResetCount = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bug_edit);

        initComponent();
        initData();

        mProjectResetCount = -1;
        mModuleResetCount = -1;
        HttpVisitUtils.getHttpVisit(this, LocalInfo.getBaseUrl(this) + "bug/get-bug-add", true, "数据加载中...",
            onLoadFinishListener);
    }

    private void initComponent() {
        mSubmitTitleTv = (TextView) findViewById(R.id.bug_edit_title_tv);
        mSubmitTitleTv.setText(R.string.bug_submit_title);
        mSubmitSaveBtn = (Button) findViewById(R.id.bug_edit_save_btn);
        mSubmitSaveBtn.setText(R.string.bug_submit_add);

        mBugNameEt = (EditText) findViewById(R.id.bug_edit_name_et);
        mProjectSp = (Spinner) findViewById(R.id.bug_edit_project_sp);
        mModuleSp = (Spinner) findViewById(R.id.bug_edit_module_sp);
        mAssignSp = (Spinner) findViewById(R.id.bug_edit_assign_sp);
        mPrioritySp = (Spinner) findViewById(R.id.bug_edit_priority_sp);
        mSeriousSp = (Spinner) findViewById(R.id.bug_edit_serious_sp);
        mIntroduceEt = (EditText) findViewById(R.id.bug_edit_introduce_et);
        mReappearEt = (EditText) findViewById(R.id.bug_edit_reappear_et);

        mProjectSp.setOnItemSelectedListener(onProjectItemSelectedListener);
        mModuleSp.setOnItemSelectedListener(onModuleItemSelectedListener);

    }

    private void initData() {
        mAllInfoModule = new Module();
        mAllInfoModule.setId(-1);
        mAllInfoModule.setName("全部");
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
                    mProjectData = JSON.parseArray(jsonObject.getString("projects"), Project.class);
                    mModuleData = JSON.parseArray(jsonObject.getString("modules"), Module.class);
                    mMemberData = JSON.parseArray(jsonObject.getString("members"), User.class);
                    if (mProjectData != null) {
                        mProjectSpAdapter = new ProjectSpinnerAdapter(SubmitBugActivity.this, mProjectData);
                        mProjectSp.setAdapter(mProjectSpAdapter);

                        mModuleSpAdapter = new ModuleSpinnerAdapter(SubmitBugActivity.this, mModuleData);
                        mModuleSp.setAdapter(mModuleSpAdapter);

                        mMemberSpAdapter = new UserSpinnerAdapter(SubmitBugActivity.this, mMemberData);
                        mAssignSp.setAdapter(mMemberSpAdapter);

                        mSubmitSaveBtn.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    Toast.makeText(SubmitBugActivity.this, "数据解析失败", Toast.LENGTH_SHORT).show();
                }
            } else {
                HttpConnectResultUtils.optFailure(SubmitBugActivity.this, result);
            }
        }
    };

    private AdapterView.OnItemSelectedListener onProjectItemSelectedListener =
        new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, "Project onItemSelected position=" + position);
                mProjectResetCount++;
                if (mProjectResetCount <= 0)
                    return;
                if (mProjectSpAdapter == null)
                    return;
                Project selectedProject = mProjectSpAdapter.getItem(position);
                if (selectedProject != null) {
                    mModuleResetCount = -1;
                    HttpVisitUtils.getHttpVisit(SubmitBugActivity.this, LocalInfo.getBaseUrl(SubmitBugActivity.this)
                        + "bug/get-project-edit&projectId=" + selectedProject.getId(), true, "数据加载中...",
                        onProjectFinishListener);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        };

    private HttpVisitUtils.OnHttpFinishListener onProjectFinishListener = new HttpVisitUtils.OnHttpFinishListener() {
        @Override
        public void onVisitFinish(HttpResult result) {
            if (result == null)
                return;
            if (result.isVisitSuccess()) {
                Log.i(TAG, result.getResult());
                try {
                    JSONObject jsonObject = new JSONObject(result.getResult());
                    mModuleData = JSON.parseArray(jsonObject.getString("modules"), Module.class);
                    mMemberData = JSON.parseArray(jsonObject.getString("members"), User.class);

                    mModuleSpAdapter = new ModuleSpinnerAdapter(SubmitBugActivity.this, mModuleData);
                    mModuleSp.setAdapter(mModuleSpAdapter);

                    mMemberSpAdapter = new UserSpinnerAdapter(SubmitBugActivity.this, mMemberData);
                    mAssignSp.setAdapter(mMemberSpAdapter);
                } catch (JSONException e) {
                    Toast.makeText(SubmitBugActivity.this, "数据解析失败", Toast.LENGTH_SHORT).show();
                }
            } else {
                HttpConnectResultUtils.optFailure(SubmitBugActivity.this, result);
            }
        }
    };

    private AdapterView.OnItemSelectedListener onModuleItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Log.i(TAG, "Module onItemSelected position=" + position);
            // 如此做的目的是为了避免重新设置spinner内容的时候导致其选中的回调
            mModuleResetCount++;
            if (mModuleResetCount <= 0)
                return;
            if (mModuleSpAdapter == null)
                return;
            Module selectedModule = mModuleSpAdapter.getItem(position);
            if (selectedModule != null) {
                HttpVisitUtils.getHttpVisit(SubmitBugActivity.this, LocalInfo.getBaseUrl(SubmitBugActivity.this)
                    + "bug/get-module-edit&moduleId=" + selectedModule.getId(), true, "数据加载中...",
                    onModuleFinishListener);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    private HttpVisitUtils.OnHttpFinishListener onModuleFinishListener = new HttpVisitUtils.OnHttpFinishListener() {
        @Override
        public void onVisitFinish(HttpResult result) {
            if (result == null)
                return;
            if (result.isVisitSuccess()) {
                Log.i(TAG, result.getResult());
                try {
                    mMemberData = JSON.parseArray(result.getResult(), User.class);
                    mMemberSpAdapter = new UserSpinnerAdapter(SubmitBugActivity.this, mMemberData);
                    mAssignSp.setAdapter(mMemberSpAdapter);
                } catch (Exception e) {
                    Toast.makeText(SubmitBugActivity.this, "数据解析失败", Toast.LENGTH_SHORT).show();
                }
            } else {
                HttpConnectResultUtils.optFailure(SubmitBugActivity.this, result);
            }
        }
    };

    public void onOptClick(View v) {
        switch (v.getId()) {
            case R.id.bug_edit_back_imgv:
                this.finish();
                break;
            case R.id.bug_edit_save_btn:
                submitNewBug();
                break;
            default:

        }
    }

    private void submitNewBug() {
        /* 缺陷名称 */
        String bugName = mBugNameEt.getText().toString().trim();
        if ("".equals(bugName)) {
            mBugNameEt.setError("缺陷名称必填");
            return;
        }
        if (!RegexUtils.isMatch(MyConstant.NAME_PATTERN, bugName)) {
            mBugNameEt.setError("只能输入中文、英文、数字和下划线");
            return;
        }

        /* 项目名称 */
        Project selectedProject;
        if (mProjectSpAdapter == null) {
            Toast.makeText(this, "项目未选", Toast.LENGTH_SHORT).show();
            return;
        } else {
            int selectedPosition = mProjectSp.getSelectedItemPosition();
            if (selectedPosition == Spinner.INVALID_POSITION) {
                Toast.makeText(this, "项目未选", Toast.LENGTH_SHORT).show();
                return;
            }
            selectedProject = mProjectSpAdapter.getItem(selectedPosition);
            if (selectedProject == null) {
                Toast.makeText(this, "所选项目信息为空", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        /* 模块名称 */
        Module selectedModule;
        if (mModuleSpAdapter == null) {
            Toast.makeText(this, "模块未选", Toast.LENGTH_SHORT).show();
            return;
        } else {
            int selectedPosition = mModuleSp.getSelectedItemPosition();
            if (selectedPosition == Spinner.INVALID_POSITION) {
                Toast.makeText(this, "模块未选", Toast.LENGTH_SHORT).show();
                return;
            }
            selectedModule = mModuleSpAdapter.getItem(selectedPosition);
            if (selectedModule == null) {
                Toast.makeText(this, "所选模块信息为空", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        /* 指派给 */
        User selectedAssign;
        if (mMemberSpAdapter == null) {
            Toast.makeText(this, "被指派人未选", Toast.LENGTH_SHORT).show();
            return;
        } else {
            int selectedPosition = mAssignSp.getSelectedItemPosition();
            if (selectedPosition == Spinner.INVALID_POSITION) {
                Toast.makeText(this, "被指派人未选", Toast.LENGTH_SHORT).show();
                return;
            }
            selectedAssign = mMemberSpAdapter.getItem(selectedPosition);
            if (selectedAssign == null) {
                Toast.makeText(this, "所选被指派人信息为空", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        /* 优先级 */
        int selectedPriorityPosition = mPrioritySp.getSelectedItemPosition();
        if (selectedPriorityPosition == Spinner.INVALID_POSITION) {
            Toast.makeText(this, "优先级未选", Toast.LENGTH_SHORT).show();
            return;
        }

        /* 影响程度 */
        int selectedSeriousPosition = mSeriousSp.getSelectedItemPosition();
        if (selectedSeriousPosition == Spinner.INVALID_POSITION) {
            Toast.makeText(this, "影响程度未选", Toast.LENGTH_SHORT).show();
            return;
        }

        /* 缺陷注释 */
        String bugIntroduce = mIntroduceEt.getText().toString().trim();

        /* 缺陷注释 */
        String bugReappear = mReappearEt.getText().toString().trim();

        String postData =
            "bugName=" + bugName + "&projectId=" + selectedProject.getId() + "&moduleId=" + selectedModule.getId()
                + "&assignId=" + selectedAssign.getId() + "&priority=" + selectedPriorityPosition + "&serious="
                + selectedSeriousPosition + "&introduce=" + bugIntroduce + "&reappear=" + bugReappear;

        Log.i(TAG, postData);

        HttpVisitUtils.postHttpVisit(this, LocalInfo.getBaseUrl(this) + "bug/add", postData, true, "提交中...",
            onSubmitFinishListener);
    }

    private HttpVisitUtils.OnHttpFinishListener onSubmitFinishListener = new HttpVisitUtils.OnHttpFinishListener() {
        @Override
        public void onVisitFinish(HttpResult result) {
            if (result == null)
                return;
            if (result.isVisitSuccess()) {
                mBugNameEt.setText("");
                mIntroduceEt.setText("");
                mPrioritySp.setSelection(0);
                mSeriousSp.setSelection(0);
                mReappearEt.setText("");
                Toast.makeText(SubmitBugActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
            } else {
                HttpConnectResultUtils.optFailure(SubmitBugActivity.this, result);
            }
        }
    };

    public void onSelectMemberClick(View v) {
        switch (v.getId()) {
            case R.id.bug_edit_image_tv:

                break;
            case R.id.bug_edit_attachment_tv:
                Intent intent = new Intent(this, AttachmentSelectActivity.class);
                startActivity(intent);
                break;
        }
    }

}
