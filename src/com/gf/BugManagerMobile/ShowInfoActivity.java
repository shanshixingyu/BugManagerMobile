package com.gf.BugManagerMobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.gf.BugManagerMobile.utils.MyConstant;

/**
 * 显示界面
 * Created by Administrator on 5/14 0014.
 */
public class ShowInfoActivity extends Activity {
    private static final String TAG = "ShowInfoActivity";

    private ListView showInfoLv;
    private ArrayAdapter<String> showInfoAdapter;
    private TextView titleTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_show_info);

        Intent startIntent = getIntent();
        String[] showInfoData = startIntent.getStringArrayExtra(MyConstant.SHOW_INFO_DATA);
        if (showInfoData == null)
            showInfoData = new String[] {};

        showInfoLv = (ListView) findViewById(R.id.show_info_lv);
        showInfoAdapter = new ArrayAdapter<String>(this, R.layout.show_info_item, showInfoData);
        showInfoLv.setAdapter(showInfoAdapter);
    }
}
