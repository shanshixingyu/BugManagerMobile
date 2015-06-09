package com.gf.BugManagerMobile;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.gf.BugManagerMobile.adapter.AttachmentSelectAdapter;

import java.io.File;

/**
 * 附件选择界面
 * Created by GuLang on 2015-06-08.
 */
public class AttachmentSelectActivity extends BaseActivity {
    private static final String TAG = "AttachmentSelectActivity";

    private ListView mAttachmentLv;
    private AttachmentSelectAdapter mAttachmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attachment_select);

        mAttachmentLv = (ListView) findViewById(R.id.attachment_select_lv);
        mAttachmentAdapter = new AttachmentSelectAdapter(this, mAttachmentLv);
        mAttachmentLv.setAdapter(mAttachmentAdapter);
    }

    public void onOptClick(View view) {
        switch (view.getId()) {
            case R.id.attachment_back_imgv:
                this.finish();
                break;
        }
    }
}
