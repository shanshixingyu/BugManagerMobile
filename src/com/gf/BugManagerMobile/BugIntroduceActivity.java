package com.gf.BugManagerMobile;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import com.gf.BugManagerMobile.adapter.BugIntroduceLvAdapter;
import com.gf.BugManagerMobile.models.BugIntroduceItem;
import com.gf.BugManagerMobile.utils.MyConstant;
import java.util.Collections;
import java.util.List;

/**
 * bug注释
 * Created by GuLang on 2015-05-17.
 */
public class BugIntroduceActivity extends BaseActivity {
    private static final String TAG = "BugIntroduceActivity";

    private ListView introduceLv;
    private BugIntroduceLvAdapter bugIntroduceLvAdapter;
    private List<BugIntroduceItem> mBugIntroduceItems = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bug_introduce);

        this.mBugIntroduceItems = getIntent().getParcelableArrayListExtra(MyConstant.BUG_DETAIL_2_BUG_INTRODUCE);
        if (mBugIntroduceItems == null)
            Toast.makeText(this, "缺陷注释传递出现异常", Toast.LENGTH_SHORT).show();
        else
            Collections.reverse(this.mBugIntroduceItems);

        introduceLv = (ListView) findViewById(R.id.bug_introduce_lv);
        bugIntroduceLvAdapter = new BugIntroduceLvAdapter(this, this.mBugIntroduceItems);
        introduceLv.setAdapter(bugIntroduceLvAdapter);
    }

    public void onOptClick(View v) {
        switch (v.getId()) {
            case R.id.bug_introduce_back_imgv:
                this.finish();
                break;
        }
    }

}
