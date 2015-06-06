package com.gf.BugManagerMobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.gf.BugManagerMobile.adapter.BugListAdapter;
import com.gf.BugManagerMobile.models.Bug;
import com.gf.BugManagerMobile.models.HttpResult;
import com.gf.BugManagerMobile.utils.LocalInfo;
import com.gf.BugManagerMobile.utils.HttpVisitUtils;
import com.gf.BugManagerMobile.utils.MyConstant;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import org.json.JSONObject;
import java.util.List;

/**
 * 项目缺陷列表
 * Created by Administrator on 5/15 0015.
 */
public class BugListActivity extends Activity {
    private static final String TAG = "BugListActivity";

    private static final int MSG_REFRESH_CLOSE = 0x011;

    private String mSearchBugCondition;
    private PullToRefreshListView bugPullToRefreshListLv;
    private BugListAdapter mBugListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bug_list);

        bugPullToRefreshListLv = (PullToRefreshListView) findViewById(R.id.bug_list_lv);
        bugPullToRefreshListLv.setMode(PullToRefreshBase.Mode.BOTH);
        mBugListAdapter = new BugListAdapter(this, null);
        bugPullToRefreshListLv.setAdapter(mBugListAdapter);
        bugPullToRefreshListLv.setOnRefreshListener(onRefreshListener2);
        bugPullToRefreshListLv.setOnItemClickListener(onItemClickListener);

        mSearchBugCondition = getIntent().getStringExtra(MyConstant.SEARCH_BUG_2_BUG_LIST_CONDITION);
        if (mSearchBugCondition == null) {
            Toast.makeText(this, "传递查询条件的时候出现异常", Toast.LENGTH_SHORT).show();
        } else {
            Log.i(TAG, TAG + "查询条件：" + mSearchBugCondition);
            HttpVisitUtils.postHttpVisit(this, LocalInfo.getBaseUrl(this) + "bug/search-bug", mSearchBugCondition,
                true, "加载数据中...", onResetFinishListener);
        }
    }

    private PullToRefreshBase.OnRefreshListener2 onRefreshListener2 = new PullToRefreshBase.OnRefreshListener2() {
        @Override
        public void onPullDownToRefresh(PullToRefreshBase refreshView) {
            if (mSearchBugCondition == null) {
                Toast.makeText(BugListActivity.this, "查询条件为空", Toast.LENGTH_SHORT).show();
                mHander.obtainMessage(MSG_REFRESH_CLOSE).sendToTarget();
                return;
            }
            // Log.i(TAG, TAG + "查询条件：" + mSearchBugCondition);
            // Log.i(TAG, "从顶部拉动");
            HttpVisitUtils.postHttpVisit(BugListActivity.this, LocalInfo.getBaseUrl(BugListActivity.this)
                + "bug/search-bug", mSearchBugCondition, true, "加载数据中...", onResetFinishListener);
        }

        @Override
        public void onPullUpToRefresh(PullToRefreshBase refreshView) {
            if (mSearchBugCondition == null) {
                Toast.makeText(BugListActivity.this, "查询条件为空", Toast.LENGTH_SHORT).show();
                mHander.obtainMessage(MSG_REFRESH_CLOSE).sendToTarget();
                return;
            }
            // Log.i(TAG, TAG + "查询条件：" + mSearchBugCondition);
            // Log.i(TAG, "从底部拉动");
            int currentPage = mBugListAdapter.getCurrentPage();
            if (currentPage >= mBugListAdapter.getPageCount()) {
                Toast.makeText(BugListActivity.this, "已经是最后一页了", Toast.LENGTH_SHORT).show();
                mHander.obtainMessage(MSG_REFRESH_CLOSE).sendToTarget();
                return;
            }
            HttpVisitUtils.postHttpVisit(BugListActivity.this, LocalInfo.getBaseUrl(BugListActivity.this)
                + "bug/search-bug&page=" + (currentPage + 1) + "&per-page=10", mSearchBugCondition, true, "加载数据中...",
                onAddFinishListener);
        }
    };

    private Handler mHander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_REFRESH_CLOSE)
                bugPullToRefreshListLv.onRefreshComplete();
        }
    };

    private HttpVisitUtils.OnHttpFinishListener onResetFinishListener = new HttpVisitUtils.OnHttpFinishListener() {
        @Override
        public void onVisitFinish(HttpResult result) {
            if (result == null) {
            } else {
                if (result.isVisitSuccess()) {
                    try {
                        JSONObject jsonObject = new JSONObject(result.getResult());
                        List<Bug> bugs = JSON.parseArray(jsonObject.getString("data"), Bug.class);
                        mBugListAdapter.resetData(bugs);
                        mBugListAdapter.setPageCount(jsonObject.getInt("pageCount"));
                        mBugListAdapter.setCurrentPage(jsonObject.getInt("currentPage"));
                    } catch (Exception e) {
                        Toast.makeText(BugListActivity.this, "数据解析失败", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(BugListActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            bugPullToRefreshListLv.onRefreshComplete();
        }
    };

    private HttpVisitUtils.OnHttpFinishListener onAddFinishListener = new HttpVisitUtils.OnHttpFinishListener() {
        @Override
        public void onVisitFinish(HttpResult result) {
            if (result == null) {
            } else {
                Log.i(TAG, "返回结果是：" + result.toString());
                if (result.isVisitSuccess()) {
                    try {
                        JSONObject jsonObject = new JSONObject(result.getResult());
                        List<Bug> bugs = JSON.parseArray(jsonObject.getString("data"), Bug.class);
                        mBugListAdapter.addData(bugs);
                        mBugListAdapter.setPageCount(jsonObject.getInt("pageCount"));
                        mBugListAdapter.setCurrentPage(jsonObject.getInt("currentPage"));
                    } catch (Exception e) {
                        Toast.makeText(BugListActivity.this, "数据解析失败", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(BugListActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            bugPullToRefreshListLv.onRefreshComplete();
        }
    };

    public void onOptClick(View v) {
        switch (v.getId()) {
            case R.id.bug_list_back_imgv:
                this.finish();
                break;
            default:
        }
    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /* 注意：position是从1开始的 */
            Log.i(TAG, "position=" + position);
            final Bug bug = (Bug) mBugListAdapter.getItem(position - 1);
            if (bug == null) {
                Toast.makeText(BugListActivity.this, "获取选中项数据失败", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(BugListActivity.this, BugDetailActivity.class);
            intent.putExtra(MyConstant.BUG_LIST_2_BUG_DETAIL_BUG_ID, bug.getId());
            startActivity(intent);

        }
    };

}
