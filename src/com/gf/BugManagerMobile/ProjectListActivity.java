package com.gf.BugManagerMobile;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.gf.BugManagerMobile.adapter.ProjectListAdapter;
import com.gf.BugManagerMobile.models.Group;
import com.gf.BugManagerMobile.models.HttpResult;
import com.gf.BugManagerMobile.models.Project;
import com.gf.BugManagerMobile.utils.HttpVisitUtils;
import com.gf.BugManagerMobile.utils.LocalInfo;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import org.json.JSONObject;

import java.util.List;

/**
 * 项目管理界面
 * Created by GuLang on 2015-05-20.
 */
public class ProjectListActivity extends Activity {
    private static final String TAG = "ProjectListActivity";

    private static final int MSG_REFRESH_CLOSE = 0x123;

    private PullToRefreshListView mPullToRefreshListView;
    private ProjectListAdapter mProjectListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_list);

        mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.project_list_lv);
        mPullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        mProjectListAdapter = new ProjectListAdapter(this, null);
        mPullToRefreshListView.setAdapter(mProjectListAdapter);
        mPullToRefreshListView.setOnRefreshListener(onRefreshListener2);
        ILoadingLayout footLoadingLayout = mPullToRefreshListView.getLoadingLayoutProxy(false, true);
        footLoadingLayout.setPullLabel("上拉加载下一页");
        footLoadingLayout.setReleaseLabel("放开以加载下一页...");
        footLoadingLayout.setRefreshingLabel("正在加载...");
        HttpVisitUtils.getHttpVisit(this, LocalInfo.getBaseUrl(this) + "project/index", true, "加载数据中...",
            resetOnHttpFinishListener);
    }

    private final PullToRefreshBase.OnRefreshListener2 onRefreshListener2 = new PullToRefreshBase.OnRefreshListener2() {
        @Override
        public void onPullDownToRefresh(PullToRefreshBase refreshView) {
            HttpVisitUtils.getHttpVisit(ProjectListActivity.this, LocalInfo.getBaseUrl(ProjectListActivity.this)
                + "project/index", true, "加载数据中...", resetOnHttpFinishListener);
        }

        @Override
        public void onPullUpToRefresh(PullToRefreshBase refreshView) {
            int currentPage = mProjectListAdapter.getCurrentPage();
            if (currentPage >= mProjectListAdapter.getPageCount()) {
                Toast.makeText(ProjectListActivity.this, "已经是最后一页了", Toast.LENGTH_SHORT).show();
                mHandler.obtainMessage(MSG_REFRESH_CLOSE).sendToTarget();
                return;
            }
            HttpVisitUtils.getHttpVisit(ProjectListActivity.this, LocalInfo.getBaseUrl(ProjectListActivity.this)
                + "project/index&per-page=10&page=" + (currentPage + 1), true, "加载数据中...", addOnHttpFinishListener);
        }
    };

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_REFRESH_CLOSE)
                mPullToRefreshListView.onRefreshComplete();
        }
    };

    private HttpVisitUtils.OnHttpFinishListener resetOnHttpFinishListener = new HttpVisitUtils.OnHttpFinishListener() {
        @Override
        public void onVisitFinish(HttpResult result) {
            if (result == null)
                return;
            Log.i(TAG, result.toString());
            if (result.isVisitSuccess()) {
                try {
                    JSONObject jsonObject = new JSONObject(result.getResult());
                    List<Project> projects = JSON.parseArray(jsonObject.getString("data"), Project.class);
                    mProjectListAdapter.resetData(projects);
                    mProjectListAdapter.setPageCount(jsonObject.getInt("pageCount"));
                    mProjectListAdapter.setCurrentPage(jsonObject.getInt("currentPage"));
                } catch (Exception e) {
                    Toast.makeText(ProjectListActivity.this, "数据解析失败", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(ProjectListActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
            }
            mPullToRefreshListView.onRefreshComplete();
        }
    };
    private HttpVisitUtils.OnHttpFinishListener addOnHttpFinishListener = new HttpVisitUtils.OnHttpFinishListener() {
        @Override
        public void onVisitFinish(HttpResult result) {
            if (result == null)
                return;
            Log.i(TAG, result.toString());
            if (result.isVisitSuccess()) {
                try {
                    JSONObject jsonObject = new JSONObject(result.getResult());
                    List<Project> projects = JSON.parseArray(jsonObject.getString("data"), Project.class);
                    mProjectListAdapter.addData(projects);
                    mProjectListAdapter.setPageCount(jsonObject.getInt("pageCount"));
                    mProjectListAdapter.setCurrentPage(jsonObject.getInt("currentPage"));
                } catch (Exception e) {
                    Toast.makeText(ProjectListActivity.this, "数据解析失败", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(ProjectListActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
            }
            mPullToRefreshListView.onRefreshComplete();
        }
    };

    public void onOptClick(View v) {
        switch (v.getId()) {
            case R.id.project_list_back_imgv:
                this.finish();
                break;
            case R.id.project_list_opt_btn:
                break;
        }
    }
}
