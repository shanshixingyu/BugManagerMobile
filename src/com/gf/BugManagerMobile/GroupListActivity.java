package com.gf.BugManagerMobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.gf.BugManagerMobile.adapter.GroupListAdapter;
import com.gf.BugManagerMobile.models.Group;
import com.gf.BugManagerMobile.models.HttpResult;
import com.gf.BugManagerMobile.utils.HttpConnectResultUtils;
import com.gf.BugManagerMobile.utils.HttpVisitUtils;
import com.gf.BugManagerMobile.utils.LocalInfo;
import com.gf.BugManagerMobile.utils.MyConstant;
import com.gf.BugManagerMobile.view.ConfirmDialog;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import org.json.JSONObject;

import java.util.List;

/**
 * 团队管理界面
 * Created by GuLang on 2015-05-20.
 */
public class GroupListActivity extends Activity {
    private static final String TAG = "GroupListActivity";

    private static final int MSG_REFRESH_CLOSE = 0x123;

    private PullToRefreshListView mPullToRefreshListView;
    private GroupListAdapter mGroupListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);

        mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.group_list_lv);
        mPullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        mGroupListAdapter = new GroupListAdapter(this, null);
        mGroupListAdapter.setOnSlideItemClickListener(onSlideItemClickListener);
        mPullToRefreshListView.setAdapter(mGroupListAdapter);
        mPullToRefreshListView.setOnRefreshListener(onRefreshListener2);
        ILoadingLayout footLoadingLayout = mPullToRefreshListView.getLoadingLayoutProxy(false, true);
        footLoadingLayout.setPullLabel("上拉加载下一页");
        footLoadingLayout.setReleaseLabel("放开以加载下一页...");
        footLoadingLayout.setRefreshingLabel("正在加载...");
        HttpVisitUtils.getHttpVisit(this, LocalInfo.getBaseUrl(this) + "group/index", true, "数据加载中...",
            resetOnHttpFinishListener);
    }

    private PullToRefreshBase.OnRefreshListener2 onRefreshListener2 = new PullToRefreshBase.OnRefreshListener2() {
        @Override
        public void onPullDownToRefresh(PullToRefreshBase refreshView) {
            HttpVisitUtils.getHttpVisit(GroupListActivity.this, LocalInfo.getBaseUrl(GroupListActivity.this)
                + "group/index", true, "数据加载中...", resetOnHttpFinishListener);
        }

        @Override
        public void onPullUpToRefresh(PullToRefreshBase refreshView) {
            int currentPage = mGroupListAdapter.getCurrentPage();
            if (currentPage >= mGroupListAdapter.getPageCount()) {
                Toast.makeText(GroupListActivity.this, "已经是最后一页了", Toast.LENGTH_SHORT).show();
                mHandler.obtainMessage(MSG_REFRESH_CLOSE).sendToTarget();
                return;
            }
            HttpVisitUtils.getHttpVisit(GroupListActivity.this, LocalInfo.getBaseUrl(GroupListActivity.this)
                + "group/index&per-page=10&page=" + (currentPage + 1), true, "数据加载中...", addOnHttpFinishListener);
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
                    List<Group> users = JSON.parseArray(jsonObject.getString("data"), Group.class);
                    mGroupListAdapter.resetData(users);
                    mGroupListAdapter.setPageCount(jsonObject.getInt("pageCount"));
                    mGroupListAdapter.setCurrentPage(jsonObject.getInt("currentPage"));
                } catch (Exception e) {
                    Toast.makeText(GroupListActivity.this, "数据解析失败", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(GroupListActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
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
                    List<Group> users = JSON.parseArray(jsonObject.getString("data"), Group.class);
                    mGroupListAdapter.addData(users);
                    mGroupListAdapter.setPageCount(jsonObject.getInt("pageCount"));
                    mGroupListAdapter.setCurrentPage(jsonObject.getInt("currentPage"));
                } catch (Exception e) {
                    Toast.makeText(GroupListActivity.this, "数据解析失败", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(GroupListActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
            }
            mPullToRefreshListView.onRefreshComplete();
        }
    };

    public void onOptClick(View v) {
        switch (v.getId()) {
            case R.id.group_list_back_imgv:
                this.finish();
                break;
            case R.id.group_list_opt_btn:
                Intent intent = new Intent(this, AddGroupActivity.class);
                startActivity(intent);
                break;
        }
    }

    private GroupListAdapter.OnSlideItemClickListener onSlideItemClickListener =
        new GroupListAdapter.OnSlideItemClickListener() {
            @Override
            public void onItemClick(int position, int groupId) {
                // Log.i(TAG, "position=" + position + ",groupId=" + groupId);
                Intent intent = new Intent(GroupListActivity.this, ModifyGroupActivity.class);
                intent.putExtra(MyConstant.GROUP_LIST_2_GROUP_MODIFY, groupId);
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(int position, int groupId) {
                // Log.i(TAG, "position=" + position + ",groupId=" + groupId);
                optGroupId = groupId;
                optPosition = position;
                initDeleteConfirmDialog();
                mDeleteConfirmDialog.show();
            }
        };

    private int optPosition;
    private int optGroupId;

    private ConfirmDialog mDeleteConfirmDialog = null;

    private void initDeleteConfirmDialog() {
        if (mDeleteConfirmDialog == null) {
            mDeleteConfirmDialog = new ConfirmDialog(this);
            mDeleteConfirmDialog.setMessageTvText("删除团队信息后将无法恢复，是否确认删除？");
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
                    HttpVisitUtils.getHttpVisit(GroupListActivity.this, LocalInfo.getBaseUrl(GroupListActivity.this)
                        + "group/delete&groupId=" + optGroupId, true, "删除中...", onDeleteFinishListener);
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
                mGroupListAdapter.deleteItem(optPosition);
                Toast.makeText(GroupListActivity.this, "用户删除成功", Toast.LENGTH_SHORT).show();
            } else {
                HttpConnectResultUtils.optFailure(GroupListActivity.this, result);
            }
        }
    };
}
