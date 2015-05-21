package com.gf.BugManagerMobile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.alibaba.fastjson.JSON;
import com.gf.BugManagerMobile.adapter.AssignSpinnerAdapter;
import com.gf.BugManagerMobile.adapter.ModuleSpinnerAdapter;
import com.gf.BugManagerMobile.models.*;
import com.gf.BugManagerMobile.utils.HttpConnectResultUtils;
import com.gf.BugManagerMobile.utils.HttpVisitUtils;
import com.gf.BugManagerMobile.utils.LocalInfo;
import com.gf.BugManagerMobile.utils.MyConstant;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.List;

/**
 * 查询bug条件界面
 * Created by Administrator on 5/15 0015.
 */
public class SearchBugActivity extends BaseActivity {
    private static final String TAG = "SearchBugActivity";

    private int projectId;
    private String projectName;
    private TextView projectNameTv;
    private Spinner moduleSpinner, prioritySpinner, seriousSpinner, assignSpinner, statusSpinner;
    private EditText submitEt, keyWordEt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCenterContentLyt(R.layout.page_search_bug);

        Intent startIntent = getIntent();
        projectId = startIntent.getIntExtra(MyConstant.HOME_2_SEARCH_BUG_PROJECT_ID, 0);
        projectName = startIntent.getStringExtra(MyConstant.HOME_2_SEARCH_BUG_PROJECT_NAME);

        initComponent();

        HttpVisitUtils.getHttpVisit(this, LocalInfo.getBaseUrl(this) + "bug/get-module-member&projectId=" + projectId,
            true, "正在加载模块..", loadModuleFinishListener);

    }

    private void initComponent() {
        projectNameTv = (TextView) findViewById(R.id.search_bug_project_name_tv);
        projectNameTv.setText(projectName);

        moduleSpinner = (Spinner) findViewById(R.id.search_bug_module_sp);
        moduleSpinner.setOnItemSelectedListener(onModuleItemSelectedListener);

        prioritySpinner = (Spinner) findViewById(R.id.search_bug_priority_sp);
        seriousSpinner = (Spinner) findViewById(R.id.search_bug_serious_sp);
        assignSpinner = (Spinner) findViewById(R.id.search_bug_assign_sp);
        submitEt = (EditText) findViewById(R.id.search_bug_submit_et);
        statusSpinner = (Spinner) findViewById(R.id.search_bug_status_sp);
        keyWordEt = (EditText) findViewById(R.id.search_bug_key_word_et);
    }

    private HttpVisitUtils.OnHttpFinishListener loadModuleFinishListener = new HttpVisitUtils.OnHttpFinishListener() {
        @Override
        public void onVisitFinish(HttpResult result) {
            if (result == null)
                return;
            Log.i(TAG, result.toString());
            if (result.getHttpResponseCode() == HttpURLConnection.HTTP_OK) {
                if (result.getCode() == MyConstant.VISIT_CODE_SUCCESS) {
                    try {
                        JSONObject resultObj = new JSONObject(result.getResult());
                        // Log.i(TAG, resultObj.toString());
                        List<Module> modules = JSON.parseArray(resultObj.getString("modules"), Module.class);
                        moduleSpinner.setAdapter(new ModuleSpinnerAdapter(SearchBugActivity.this, modules));
                        List<User> users = JSON.parseArray(resultObj.getString("users"), User.class);
                        assignSpinner.setAdapter(new AssignSpinnerAdapter(SearchBugActivity.this, users));
                    } catch (Exception e) {
                        Toast.makeText(SearchBugActivity.this, "模块信息解析失败", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                HttpConnectResultUtils.optFailure(SearchBugActivity.this, result);
            }
        }
    };

    private AdapterView.OnItemSelectedListener onModuleItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            final Module module = (Module) moduleSpinner.getAdapter().getItem(position);
            if (module == null)
                return;
            // Log.i(TAG, "点击后得到的值:" + module.toString());
            String url =
                LocalInfo.getBaseUrl(SearchBugActivity.this) + "bug/get-assign&projectId=" + projectId + "&moduleId="
                    + module.getId();
            HttpVisitUtils.getHttpVisit(SearchBugActivity.this, url, true, "加载指派人..", onGetAssignFinishListener);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    private HttpVisitUtils.OnHttpFinishListener onGetAssignFinishListener = new HttpVisitUtils.OnHttpFinishListener() {
        @Override
        public void onVisitFinish(HttpResult result) {
            if (result == null)
                return;
            if (result.getHttpResponseCode() == HttpURLConnection.HTTP_OK) {
                if (result.getCode() == MyConstant.VISIT_CODE_SUCCESS) {
                    try {
                        List<User> users = JSON.parseArray(result.getResult(), User.class);
                        assignSpinner.setAdapter(new AssignSpinnerAdapter(SearchBugActivity.this, users));
                    } catch (Exception e) {
                        Toast.makeText(SearchBugActivity.this, "指派人信息解析失败", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                HttpConnectResultUtils.optFailure(SearchBugActivity.this, result);
            }
        }
    };

    public void onOptClick(View v) {
        switch (v.getId()) {
            case R.id.search_bug_back_imgv:
                this.finish();
                break;
            case R.id.search_bug_btn:
                SearchBugCondition searchBugCondition = new SearchBugCondition();
                searchBugCondition.setProjectId(projectId);
                SpinnerAdapter moduleSpinnerAdapter = moduleSpinner.getAdapter();
                if (moduleSpinnerAdapter != null) {
                    final Module module =
                        (Module) moduleSpinnerAdapter.getItem(moduleSpinner.getSelectedItemPosition());
                    if (module == null)
                        searchBugCondition.setModuleId(-1);
                    else
                        searchBugCondition.setModuleId(module.getId());
                }
                searchBugCondition.setPriority(prioritySpinner.getSelectedItemPosition());
                searchBugCondition.setSerious(seriousSpinner.getSelectedItemPosition());
                SpinnerAdapter assignAdapter = assignSpinner.getAdapter();
                if (assignAdapter != null) {
                    final User assign = (User) assignAdapter.getItem(assignSpinner.getSelectedItemPosition());
                    if (assign == null)
                        searchBugCondition.setAssign(-1);
                    else
                        searchBugCondition.setAssign(assign.getId());
                }
                searchBugCondition.setSubmit(submitEt.getText().toString());
                searchBugCondition.setStatus(statusSpinner.getSelectedItemPosition());
                searchBugCondition.setKeyWord(keyWordEt.getText().toString());

                // Log.i(TAG, "查询条件：" + searchBugCondition.getRequestCondition());

                Intent bugListIntent = new Intent(this, BugListActivity.class);
                bugListIntent.putExtra(MyConstant.SEARCH_BUG_2_BUG_LIST_CONDITION,
                    searchBugCondition.getRequestCondition());
                startActivity(bugListIntent);
                break;
            default:
        }
    }
}
