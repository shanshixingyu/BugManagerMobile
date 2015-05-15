package com.gf.BugManagerMobile;

import android.os.Bundle;
import android.view.View;
import com.gf.BugManagerMobile.adapter.BugListAdapter;
import com.gf.BugManagerMobile.models.HttpResult;
import com.gf.BugManagerMobile.utils.HttpVisitUtils;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * 项目缺陷列表
 * Created by Administrator on 5/15 0015.
 */
public class BugListActivity extends BaseActivity {
    private static final String TAG = "BugListActivity";

    private PullToRefreshListView bugPullToRefreshListLv;
    private BugListAdapter mBugListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCenterContentLyt(R.layout.page_bug_list);

        bugPullToRefreshListLv = (PullToRefreshListView) findViewById(R.id.bug_list_lv);
        // bugPullToRefreshListLv.setAdapter();
        mBugListAdapter = new BugListAdapter(this, null);
        bugPullToRefreshListLv.setAdapter(mBugListAdapter);
    }

    private HttpVisitUtils.OnHttpFinishListener loadFinishListener = new HttpVisitUtils.OnHttpFinishListener() {
        @Override
        public void onVisitFinish(HttpResult result) {

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

}
