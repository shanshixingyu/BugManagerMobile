package com.gf.BugManagerMobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.gf.BugManagerMobile.adapter.BugListAdapter;
import com.gf.BugManagerMobile.models.Bug;
import com.gf.BugManagerMobile.models.HttpResult;
import com.gf.BugManagerMobile.utils.HttpVisitUtils;
import com.gf.BugManagerMobile.utils.LocalInfo;
import com.gf.BugManagerMobile.utils.MyConstant;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import org.json.JSONObject;

import java.util.List;

/**
 * 我提交的Bug信息列表
 * Created by GuLang on 2015-05-18.
 */
public class MyBugActivity extends Activity {
    private static final String TAG = "MySubmitActivity";

    public static final String ACTIVITY_TYPE = "ActivityTyp";
    public static final String TYPE_SUBMIT = "Submit";
    public static final String TYPE_ASSIGIN = "Assign";
    public static final String TYPE_OPT = "Opt";

    private String activityType;

    private static final int MSG_REFRESH_CLOSE = 0x011;

    private PullToRefreshListView bugPullToRefreshListLv;
    private BugListAdapter mBugListAdapter;
    private TextView titleTv;
    private String mVisitUrl = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_person_bug_list);

        titleTv = (TextView) findViewById(R.id.bug_list_title_tv);

        activityType = getIntent().getStringExtra(ACTIVITY_TYPE);
        if (activityType == null || TYPE_SUBMIT.equals(activityType)) {
            activityType = TYPE_SUBMIT;
            titleTv.setText(R.string.my_submit_title);
            mVisitUrl = LocalInfo.getBaseUrl(this) + "person/submit";
        } else if (TYPE_ASSIGIN.equals(activityType)) {
            titleTv.setText(R.string.my_assign_title);
            mVisitUrl = LocalInfo.getBaseUrl(this) + "person/assign";
        } else {
            titleTv.setText(R.string.my_opt_title);
            mVisitUrl = LocalInfo.getBaseUrl(this) + "person/opt";
        }

        bugPullToRefreshListLv = (PullToRefreshListView) findViewById(R.id.my_submit_lv);
        bugPullToRefreshListLv.setMode(PullToRefreshBase.Mode.BOTH);
        mBugListAdapter = new BugListAdapter(this, null);
        bugPullToRefreshListLv.setAdapter(mBugListAdapter);
        bugPullToRefreshListLv.setMode(PullToRefreshBase.Mode.BOTH);
        bugPullToRefreshListLv.setOnRefreshListener(onRefreshListener2);
        bugPullToRefreshListLv.setOnItemClickListener(onItemClickListener);

        HttpVisitUtils.getHttpVisit(this, mVisitUrl, true, "加载数据中...", onResetFinishListener);
    }

    private PullToRefreshBase.OnRefreshListener2 onRefreshListener2 = new PullToRefreshBase.OnRefreshListener2() {
        @Override
        public void onPullDownToRefresh(PullToRefreshBase refreshView) {
            HttpVisitUtils.getHttpVisit(MyBugActivity.this, mVisitUrl, true, "加载数据中...", onResetFinishListener);
        }

        @Override
        public void onPullUpToRefresh(PullToRefreshBase refreshView) {
            int currentPage = mBugListAdapter.getCurrentPage();
            if (currentPage >= mBugListAdapter.getPageCount()) {
                Toast.makeText(MyBugActivity.this, "已经是最后一页了", Toast.LENGTH_SHORT).show();
                mHander.obtainMessage(MSG_REFRESH_CLOSE).sendToTarget();
                return;
            }
            HttpVisitUtils.getHttpVisit(MyBugActivity.this, mVisitUrl + "&page=" + (currentPage + 1) + "&per-page=10",
                true, "加载数据中...", onAddFinishListener);
        }
    };

    private Handler mHander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_REFRESH_CLOSE)
                bugPullToRefreshListLv.onRefreshComplete();
        }
    };

    private AbsListView.OnScrollListener onScrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView absListView, int i) {
            Log.i(TAG, "onScrollStateChanged" + i);
        }

        @Override
        public void onScroll(AbsListView absListView, int i, int i1, int i2) {
            Log.i(TAG, "onScroll" + i);
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
                        Toast.makeText(MyBugActivity.this, "数据解析失败", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MyBugActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(MyBugActivity.this, "数据解析失败", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MyBugActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(MyBugActivity.this, "获取选中项数据失败", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(MyBugActivity.this, BugDetailActivity.class);
            intent.putExtra(MyConstant.BUG_LIST_2_BUG_DETAIL_BUG_ID, bug.getId());
            startActivity(intent);
        }
    };

}
