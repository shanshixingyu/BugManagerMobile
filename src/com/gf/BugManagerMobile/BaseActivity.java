package com.gf.BugManagerMobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.gf.BugManagerMobile.view.SlideView;

/**
 * 所有缺陷管理界面的父类(除登录、设置、关于界面)
 * Created by Administrator on 5/9 0009.
 */
public class BaseActivity extends Activity {
    private static final String TAG = "BaseActivity";

    private SlideView slideView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        slideView = (SlideView) findViewById(R.id.slide_view);

    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            slideView.toggleMenu();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

}