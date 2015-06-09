package com.gf.BugManagerMobile;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 关于界面
 * Created by GuLang on 2014/12/28.
 */
public class AboutActivity extends BaseActivity {

    private TextView versionNameTv, versionCodeTv;
    private ImageView aboutImgv = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        aboutImgv = (ImageView) findViewById(R.id.about_imgv);

        versionNameTv = (TextView) findViewById(R.id.about_version_name);
        versionCodeTv = (TextView) findViewById(R.id.about_version_code);
        PackageInfo packageInfo = getPackageInfo();
        if (packageInfo != null) {
            versionNameTv.setText(getResources().getString(R.string.about_version_name_pre) + packageInfo.versionName);
            versionCodeTv.setText(getResources().getString(R.string.about_version_code_pre) + packageInfo.versionCode);
        }

    }

    public void onOptClick(View v) {
        switch (v.getId()) {
            case R.id.about_back_imgv:
                this.finish();
                break;
            default:
                break;
        }
    }

    private PackageInfo getPackageInfo() {
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageInfo;
    }
}
