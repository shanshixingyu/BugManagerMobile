package com.gf.BugManagerMobile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.gf.BugManagerMobile.adapter.HomeBugOverLvAdapter;
import com.gf.BugManagerMobile.models.ProjectBugOverview;
import com.gf.BugManagerMobile.models.HttpResult;
import com.gf.BugManagerMobile.utils.LocalInfo;
import com.gf.BugManagerMobile.utils.HttpConnectResultUtils;
import com.gf.BugManagerMobile.utils.HttpVisitUtils;
import com.gf.BugManagerMobile.utils.MyConstant;
import com.gf.BugManagerMobile.view.BugSubmitPopWindow;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * 主页（Bug概况）
 * Created by Administrator on 5/10 0010.
 */
public class HomeActivity extends BaseHomeActivity {
    private static final String TAG = "HomeActivity";

    private PullToRefreshListView mBugOverviewLv;
    private HomeBugOverLvAdapter mHomeBugOverLvAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCenterContentLyt(R.layout.page_home);

        initComponent();
        HttpVisitUtils.getHttpVisit(this, LocalInfo.getBaseUrl(this) + "bug/index", true, "正在加载...",
            onResetFinishListener);
    }

    private void initComponent() {
        mBugOverviewLv = (PullToRefreshListView) findViewById(R.id.home_bug_overview_lv);
        mHomeBugOverLvAdapter = new HomeBugOverLvAdapter(this);
        mBugOverviewLv.setAdapter(mHomeBugOverLvAdapter);
        mBugOverviewLv.setOnItemClickListener(onItemClickListener);
        mBugOverviewLv.setMode(PullToRefreshBase.Mode.BOTH);
    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // 注意：此地的position是从1开始的，所以要减1
            Log.i(TAG, "position=" + position);
            final ProjectBugOverview.ProjectBugInfo projectBugInfo =
                (ProjectBugOverview.ProjectBugInfo) mHomeBugOverLvAdapter.getItem(position - 1);
            if (projectBugInfo == null) {
                Toast.makeText(HomeActivity.this, "获取数据错误", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(HomeActivity.this, SearchBugActivity.class);
            intent.putExtra(MyConstant.HOME_2_SEARCH_BUG_PROJECT_ID, projectBugInfo.getProjectId());
            intent.putExtra(MyConstant.HOME_2_SEARCH_BUG_PROJECT_NAME, projectBugInfo.getProjectName());
            startActivity(intent);
        }
    };

    private HttpVisitUtils.OnHttpFinishListener onResetFinishListener = new HttpVisitUtils.OnHttpFinishListener() {
        @Override
        public void onVisitFinish(HttpResult result) {
            Log.i(TAG, "解析前：" + result.toString());
            if (result.isVisitSuccess()) {
                ProjectBugOverview bugOverviewResult = null;
                try {
                    bugOverviewResult = JSON.parseObject(result.getResult(), ProjectBugOverview.class);
                    if (mBugOverviewLv != null) {
                        Log.i(TAG, "解析后：" + bugOverviewResult.toString());
                        mHomeBugOverLvAdapter.addProjectBugInfo(bugOverviewResult.getData());
                    }
                } catch (Exception e) {
                    // 解析失败
                    mBugOverviewLv = null;
                    Toast.makeText(HomeActivity.this, "数据解析失败", Toast.LENGTH_SHORT).show();
                }

            } else {
                HttpConnectResultUtils.optFailure(HomeActivity.this, result);
            }
        }
    };

    private BugSubmitPopWindow mBugSubmitPopWindow = null;

    /**
     * 处理顶部栏点击事件
     * @param v
     */
    public void onOptClick(View v) {
        if (mBugSubmitPopWindow == null) {
            mBugSubmitPopWindow = new BugSubmitPopWindow(this);
            mBugSubmitPopWindow.setOnItemClickListener(new BugSubmitPopWindow.OnItemClickListener() {
                @Override
                public void onItemClick() {
                    Intent submitBugIntent = new Intent(HomeActivity.this, SubmitBugActivity.class);
                    HomeActivity.this.startActivity(submitBugIntent);
                }
            });
        }
        mBugSubmitPopWindow.showAsDropDown(v);
    }

    /**
     * 上次按的时间
     */
    private long preBackPressTime = 0;

    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - preBackPressTime <= MyConstant.NEXT_EXIT_TIME_LENGTH) {
            super.onBackPressed();
            System.exit(0);
        } else {
            preBackPressTime = currentTime;
            Toast.makeText(this, "再按一次退出应用", Toast.LENGTH_SHORT).show();
        }
    }
}
