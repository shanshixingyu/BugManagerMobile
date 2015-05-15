package com.gf.BugManagerMobile;

import android.os.Bundle;
import android.view.View;

/**
 * 查询bug条件界面
 * Created by Administrator on 5/15 0015.
 */
public class SearchBugActivity extends BaseActivity {
    private static final String TAG = "SearchBugActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCenterContentLyt(R.layout.page_search_bug);

    }

    public void onOptClick(View v) {
        switch (v.getId()) {
            case R.id.search_bug_back_imgv:
                this.finish();
                break;
            case R.id.search_bug_btn:

                break;
            default:
        }
    }

}
