package com.gf.BugManagerMobile;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.gf.BugManagerMobile.adapter.ModuleListAdapter;
import com.gf.BugManagerMobile.models.HttpResult;
import com.gf.BugManagerMobile.models.LoginSuccessInfo;
import com.gf.BugManagerMobile.models.Module;
import com.gf.BugManagerMobile.utils.HttpConnectResultUtils;
import com.gf.BugManagerMobile.utils.HttpVisitUtils;
import com.gf.BugManagerMobile.utils.LocalInfo;
import com.gf.BugManagerMobile.utils.MyConstant;
import com.gf.BugManagerMobile.view.ConfirmDialog;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * 项目的模块列表界面
 * Created by GuLang on 2015-06-03.
 */
public class ModuleListActivity extends BaseActivity {
    private static final String TAG = "ModuleListActivity";

    private static final int MSG_REFRESH_CLOSE = 0x123;

    private int projectId;
    private int projectCreator;
    private Button topAddBtn;
    private PullToRefreshListView mModuleInfoLv;
    private ModuleListAdapter mModuleListAdapter;
    private LoginSuccessInfo mLoginSuccessInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_list);

        Intent startIntent = getIntent();
        projectId = startIntent.getIntExtra(MyConstant.PROJECT_LIST_2_MODULE_LIST, -1);
        if (projectId < 0) {
            Toast.makeText(this, "界面却换传递项目ID失败", Toast.LENGTH_SHORT).show();
            return;
        }

        mLoginSuccessInfo = LocalInfo.getLoginSuccessInfo(this);
        topAddBtn = (Button) findViewById(R.id.module_list_opt_btn);

        mModuleInfoLv = (PullToRefreshListView) findViewById(R.id.module_list_lv);
        mModuleListAdapter = new ModuleListAdapter(this, null);
        mModuleInfoLv.setAdapter(mModuleListAdapter);
        mModuleListAdapter.setOnItemOptListener(onItemOptListener);

        mModuleInfoLv.setMode(PullToRefreshBase.Mode.BOTH);
        ILoadingLayout footLoadingLayout = mModuleInfoLv.getLoadingLayoutProxy(false, true);
        footLoadingLayout.setPullLabel("上拉加载下一页");
        footLoadingLayout.setReleaseLabel("放开以加载下一页...");
        footLoadingLayout.setRefreshingLabel("正在加载...");
        mModuleInfoLv.setOnRefreshListener(onRefreshListener2);

        HttpVisitUtils.getHttpVisit(this, LocalInfo.getBaseUrl(this) + "project/see-modules&projectId=" + projectId,
            true, "加载模块信息中...", onResetModulesFinishListener);
    }

    private final PullToRefreshBase.OnRefreshListener2 onRefreshListener2 = new PullToRefreshBase.OnRefreshListener2() {
        @Override
        public void onPullDownToRefresh(PullToRefreshBase refreshView) {
            HttpVisitUtils.getHttpVisit(ModuleListActivity.this, LocalInfo.getBaseUrl(ModuleListActivity.this)
                + "project/see-modules&projectId=" + projectId, true, "加载数据中...", onResetModulesFinishListener);
        }

        @Override
        public void onPullUpToRefresh(PullToRefreshBase refreshView) {
            int currentPage = mModuleListAdapter.getCurrentPage();
            if (currentPage >= mModuleListAdapter.getPageCount()) {
                Toast.makeText(ModuleListActivity.this, "已经是最后一页了", Toast.LENGTH_SHORT).show();
                mHandler.obtainMessage(MSG_REFRESH_CLOSE).sendToTarget();
                return;
            }
            HttpVisitUtils.getHttpVisit(ModuleListActivity.this, LocalInfo.getBaseUrl(ModuleListActivity.this)
                + "project/index&per-page=10&page=" + (currentPage + 1), true, "加载数据中...", onAddModulesFinishListener);
        }
    };

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_REFRESH_CLOSE)
                mModuleInfoLv.onRefreshComplete();
        }
    };

    private HttpVisitUtils.OnHttpFinishListener onResetModulesFinishListener =
        new HttpVisitUtils.OnHttpFinishListener() {
            @Override
            public void onVisitFinish(HttpResult result) {
                if (result == null)
                    return;
                if (result.isVisitSuccess()) {
                    // Log.i(TAG, result.getResult());
                    try {
                        JSONObject jsonObj = new JSONObject(result.getResult());
                        List<Module> modules = JSON.parseArray(jsonObj.getString("data"), Module.class);
                        mModuleListAdapter.resetData(modules);
                        mModuleListAdapter.setPageCount(jsonObj.getInt("pageCount"));
                        mModuleListAdapter.setCurrentPage(jsonObj.getInt("currentPage"));
                        projectCreator = jsonObj.getInt("projectCreator");
                        if (mLoginSuccessInfo.getRoleId() == 0 || mLoginSuccessInfo.getUserId() == projectCreator)
                            topAddBtn.setVisibility(View.VISIBLE);
                        else
                            topAddBtn.setVisibility(View.INVISIBLE);
                    } catch (JSONException e) {
                        Toast.makeText(ModuleListActivity.this, "模块信息解析失败", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    HttpConnectResultUtils.optFailure(ModuleListActivity.this, result);
                }

                mModuleInfoLv.onRefreshComplete();
            }
        };

    private HttpVisitUtils.OnHttpFinishListener onAddModulesFinishListener = new HttpVisitUtils.OnHttpFinishListener() {
        @Override
        public void onVisitFinish(HttpResult result) {
            if (result == null)
                return;
            if (result.isVisitSuccess()) {
                // Log.i(TAG, result.getResult());
                try {
                    JSONObject jsonObj = new JSONObject(result.getResult());
                    List<Module> modules = JSON.parseArray(jsonObj.getString("data"), Module.class);
                    mModuleListAdapter.addData(modules);
                    mModuleListAdapter.setPageCount(jsonObj.getInt("pageCount"));
                    mModuleListAdapter.setCurrentPage(jsonObj.getInt("currentPage"));
                    projectCreator = jsonObj.getInt("projectCreator");
                    if (mLoginSuccessInfo.getRoleId() == 0 || mLoginSuccessInfo.getUserId() == projectCreator)
                        topAddBtn.setVisibility(View.VISIBLE);
                    else
                        topAddBtn.setVisibility(View.INVISIBLE);
                } catch (JSONException e) {
                    Toast.makeText(ModuleListActivity.this, "模块信息解析失败", Toast.LENGTH_SHORT).show();
                }
            } else {
                HttpConnectResultUtils.optFailure(ModuleListActivity.this, result);
            }

            mModuleInfoLv.onRefreshComplete();
        }
    };

    public void onOptClick(View v) {
        switch (v.getId()) {
            case R.id.module_list_back_imgv:
                this.finish();
                break;
            case R.id.module_list_opt_btn:
                Intent intent = new Intent(this, AddModuleActivity.class);
                intent.putExtra(MyConstant.MODULE_LIST_2_MODULE_ADD, projectId);
                startActivity(intent);
                break;
        }
    }

    private ModuleListAdapter.OnItemOptListener onItemOptListener = new ModuleListAdapter.OnItemOptListener() {
        @Override
        public void onItemClick(int position, int moduleId) {
            Log.i(TAG, "onItemClick position=" + position + ",moduleId=" + moduleId);
            Intent intent = new Intent(ModuleListActivity.this, ModifyModuleActivity.class);
            intent.putExtra(MyConstant.MODULE_LIST_2_MODULE_MODIFY, moduleId);
            startActivity(intent);
        }

        @Override
        public void onDeleteClick(int position, int moduleId) {
            Log.i(TAG, "onDeleteClick position=" + position + ",moduleId=" + moduleId);
            optPosition = position;
            optModuleId = moduleId;
            initDeleteConfirmDialog();
            mDeleteConfirmDialog.show();
        }
    };

    private int optPosition;
    private int optModuleId;

    private ConfirmDialog mDeleteConfirmDialog = null;

    private void initDeleteConfirmDialog() {
        if (mDeleteConfirmDialog == null) {
            mDeleteConfirmDialog = new ConfirmDialog(this);
            mDeleteConfirmDialog.setMessageTvText("删除模块信息后将无法恢复，是否确认删除？");
            mDeleteConfirmDialog.setLeftBtnText("取消");
            mDeleteConfirmDialog.setRightBtnText("确认");
            mDeleteConfirmDialog.setOnConfirmDialogListener(new ConfirmDialog.OnConfirmDialogListener() {
                @Override
                public void onLeftBtnClick(ConfirmDialog dialog) {
                    dialog.dismiss();
                }

                @Override
                public void onRightBtnClick(ConfirmDialog dialog) {
                    dialog.dismiss();
                    HttpVisitUtils.getHttpVisit(ModuleListActivity.this, LocalInfo.getBaseUrl(ModuleListActivity.this)
                        + "project/delete-module&moduleId=" + optModuleId, true, "删除中...", onDeleteFinishListener);
                }
            });
        }
    }

    private HttpVisitUtils.OnHttpFinishListener onDeleteFinishListener = new HttpVisitUtils.OnHttpFinishListener() {
        @Override
        public void onVisitFinish(HttpResult result) {
            if (result == null)
                return;
            if (result.isVisitSuccess()) {
                // Log.i(TAG, result.getResult());
                Toast.makeText(ModuleListActivity.this, "删除模块信息成功", Toast.LENGTH_SHORT).show();
                mModuleListAdapter.deleteItem(optPosition);
            } else {
                HttpConnectResultUtils.optFailure(ModuleListActivity.this, result);
            }
        }
    };

}
