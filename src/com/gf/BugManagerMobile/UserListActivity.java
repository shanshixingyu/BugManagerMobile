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
import com.gf.BugManagerMobile.adapter.UserListAdapter;
import com.gf.BugManagerMobile.models.HttpResult;
import com.gf.BugManagerMobile.models.User;
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
 * 用户管理界面
 * Created by GuLang on 2015-05-18.
 */
public class UserListActivity extends Activity {
    private static final String TAG = "UserListActivity";

    private static final int MSG_REFRESH_CLOSE = 0x132;

    private PullToRefreshListView mPullToRefreshListView;
    private UserListAdapter mUserListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.user_list_lv);
        mUserListAdapter = new UserListAdapter(this, null);
        mUserListAdapter.setOnSlideItemClickListener(onSlideItemClickListener);
        mPullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        mPullToRefreshListView.setAdapter(mUserListAdapter);
        mPullToRefreshListView.setOnRefreshListener(onRefreshListener2);
        ILoadingLayout footLoadingLayout = mPullToRefreshListView.getLoadingLayoutProxy(false, true);
        footLoadingLayout.setPullLabel("上拉加载下一页");
        footLoadingLayout.setReleaseLabel("放开以加载下一页...");
        footLoadingLayout.setRefreshingLabel("正在加载...");
        HttpVisitUtils.getHttpVisit(this, LocalInfo.getBaseUrl(this) + "user/index", true, "加载数据中...",
            onResetFinishListener);
    }

    private PullToRefreshBase.OnRefreshListener2 onRefreshListener2 = new PullToRefreshBase.OnRefreshListener2() {
        @Override
        public void onPullDownToRefresh(PullToRefreshBase refreshView) {
            HttpVisitUtils.getHttpVisit(UserListActivity.this, LocalInfo.getBaseUrl(UserListActivity.this)
                + "user/index", true, "加载数据中...", onResetFinishListener);
        }

        @Override
        public void onPullUpToRefresh(PullToRefreshBase refreshView) {
            int currentPage = mUserListAdapter.getCurrentPage();
            if (currentPage >= mUserListAdapter.getPageCount()) {
                Toast.makeText(UserListActivity.this, "已经是最后一页了", Toast.LENGTH_SHORT).show();
                mHandler.obtainMessage(MSG_REFRESH_CLOSE).sendToTarget();
                return;
            }
            HttpVisitUtils.getHttpVisit(UserListActivity.this, LocalInfo.getBaseUrl(UserListActivity.this)
                + "user/index&page=" + (currentPage + 1) + "&per-page=10", true, "加载数据中...", onAddFinishListener);
        }
    };

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_REFRESH_CLOSE) {
                mPullToRefreshListView.onRefreshComplete();
            }
        }
    };

    private HttpVisitUtils.OnHttpFinishListener onResetFinishListener = new HttpVisitUtils.OnHttpFinishListener() {
        @Override
        public void onVisitFinish(HttpResult result) {
            if (result == null)
                return;
            if (result.isVisitSuccess()) {
                Log.i(TAG, result.toString());
                try {
                    JSONObject jsonObject = new JSONObject(result.getResult());
                    List<User> users = JSON.parseArray(jsonObject.getString("data"), User.class);
                    mUserListAdapter.resetData(users);
                    mUserListAdapter.setPageCount(jsonObject.getInt("pageCount"));
                    mUserListAdapter.setCurrentPage(jsonObject.getInt("currentPage"));
                } catch (Exception e) {
                    Toast.makeText(UserListActivity.this, "数据解析失败", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(UserListActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
            }
            mPullToRefreshListView.onRefreshComplete();
        }
    };
    private HttpVisitUtils.OnHttpFinishListener onAddFinishListener = new HttpVisitUtils.OnHttpFinishListener() {
        @Override
        public void onVisitFinish(HttpResult result) {
            if (result == null)
                return;
            if (result.isVisitSuccess()) {
                Log.i(TAG, result.toString());
                try {
                    JSONObject jsonObject = new JSONObject(result.getResult());
                    List<User> users = JSON.parseArray(jsonObject.getString("data"), User.class);
                    mUserListAdapter.addData(users);
                    mUserListAdapter.setPageCount(jsonObject.getInt("pageCount"));
                    mUserListAdapter.setCurrentPage(jsonObject.getInt("currentPage"));
                } catch (Exception e) {
                    Toast.makeText(UserListActivity.this, "数据解析失败", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(UserListActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
            }
            mPullToRefreshListView.onRefreshComplete();
        }
    };

    public void onOptClick(View v) {
        switch (v.getId()) {
            case R.id.user_list_back_imgv:
                this.finish();
                break;
            case R.id.user_list_opt_btn:
                Intent addUserIntent = new Intent(this, AddUserActivity.class);
                startActivity(addUserIntent);
                break;
        }
    }

    private UserListAdapter.OnSlideItemClickListener onSlideItemClickListener =
        new UserListAdapter.OnSlideItemClickListener() {
            @Override
            public void onItemClick(int position, int userId) {
                Log.i(TAG, "onItemClick position=" + position + ",userId=" + userId);
                // 不允许修改超级用户信息
                final User user = (User) mUserListAdapter.getItem(position);
                if (user != null && user.getRole_id() != 0) {
                    Intent intent = new Intent(UserListActivity.this, ModifyUserActivity.class);
                    intent.putExtra(MyConstant.USER_LIST_2_USER_MODIFY, userId);
                    startActivity(intent);
                } else {
                    Toast.makeText(UserListActivity.this, "超级管理员信息不能修改", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onDeleteClick(int position, int userId) {
                Log.i(TAG, "onDeleteClick position=" + position + ",userId=" + userId);
                optPosition = position;
                optUserId = userId;
                initDeleteConfirmDialog();
                mDeleteConfirmDialog.show();
            }
        };

    private int optPosition;
    private int optUserId;

    private ConfirmDialog mDeleteConfirmDialog = null;

    private void initDeleteConfirmDialog() {
        if (mDeleteConfirmDialog == null) {
            mDeleteConfirmDialog = new ConfirmDialog(this);
            mDeleteConfirmDialog.setMessageTvText("删除用户的同时将删除其在的用户组的信息，并且无法恢复，是否确认删除用户？");
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
                    HttpVisitUtils.getHttpVisit(UserListActivity.this, LocalInfo.getBaseUrl(UserListActivity.this)
                        + "user/delete&userId=" + optUserId, true, "删除中...", onDeleteFinishListener);
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
                mUserListAdapter.deleteItem(optPosition);
                Toast.makeText(UserListActivity.this, "用户删除成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(UserListActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    };
}
