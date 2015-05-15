package com.gf.BugManagerMobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.gf.BugManagerMobile.models.HttpResult;
import com.gf.BugManagerMobile.models.LocalInfo;
import com.gf.BugManagerMobile.models.LoginSuccessInfo;
import com.gf.BugManagerMobile.utils.HttpConnectResultUtils;
import com.gf.BugManagerMobile.utils.HttpVisitUtils;
import com.gf.BugManagerMobile.utils.MyConstant;
import com.gf.BugManagerMobile.utils.SharedPreferenceUtils;
import com.gf.BugManagerMobile.view.SlideView;

import java.net.HttpURLConnection;

/**
 * 所有缺陷管理界面的父类(除登录、设置、关于界面)
 * Created by Administrator on 5/9 0009.
 */
public class BaseActivity extends Activity {
    private static final String TAG = "BaseActivity";

    protected SlideView slideView;
    private ViewGroup mCenterContentLyt = null;
    private TextView slideLeftMenuUserNameTv, slideLeftMenuRoleNameTv;
    private View mSatelliteMenuLyt;

    /**
     * 登录的用户信息
     */
    private LoginSuccessInfo loginSuccessInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_base);

        // 获得登录的用户信息
        loginSuccessInfo = LocalInfo.getLoginSuccessInfo(this);

        initComponent();

    }

    /**
     * 初始化控件
     */
    private void initComponent() {
        slideView = (SlideView) findViewById(R.id.slide_view);
        mCenterContentLyt = (ViewGroup) findViewById(R.id.center_content_lyt);
        slideLeftMenuUserNameTv = (TextView) findViewById(R.id.slide_left_user_name_tv);
        slideLeftMenuRoleNameTv = (TextView) findViewById(R.id.slide_left_role_name_tv);
        slideLeftMenuUserNameTv.setText(loginSuccessInfo.getUserName());
        slideLeftMenuRoleNameTv.setText(loginSuccessInfo.getRoleName());
        mSatelliteMenuLyt = findViewById(R.id.satellite_menu_lyt);
        if (loginSuccessInfo.getRoleId() == 0 || loginSuccessInfo.getRoleId() == 1) {
            mSatelliteMenuLyt.setVisibility(View.VISIBLE);
        } else {
            mSatelliteMenuLyt.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (slideView.getSlideState() == SlideView.SlideState.OPEN) {
            slideView.toggleMenu();
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        throw new IllegalStateException("继承自BaseActivity类后不能调用setContentView方法，请调用setCenterContentLyt");
    }

    @Override
    public void setContentView(View view) {
        throw new IllegalStateException("继承自BaseActivity类后不能调用setContentView方法，请调用setCenterContentLyt");
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        throw new IllegalStateException("继承自BaseActivity类后不能调用setContentView方法，请调用setCenterContentLyt");
    }

    /**
     * 给中间内容布局设置视图内容
     *
     * @param lytResId
     */
    public void setCenterContentLyt(int lytResId) {
        View centerContentView = LayoutInflater.from(this).inflate(lytResId, this.mCenterContentLyt, false);
        this.mCenterContentLyt.addView(centerContentView, 0);
    }

    /**
     * 给中间内容布局设置视图内容
     *
     * @param lytView
     */
    public void setCenterContentLyt(View lytView) {
        this.mCenterContentLyt.addView(lytView);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            slideView.toggleMenu();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    /**
     * 侧滑菜单项点击
     *
     * @param v
     */
    public void onLeftMenuClick(View v) {
        switch (v.getId()) {
            case R.id.slide_left_menu_pim:
                // 当前所在界面就是个人信息界面
                if (this instanceof PimActivity) {
                    slideView.toggleMenu();
                    return;
                }
                Intent pimIntent = new Intent(this, PimActivity.class);
                startActivity(pimIntent);
                break;
            case R.id.slide_left_menu_home:
                if (this instanceof HomeActivity) {
                    slideView.toggleMenu();
                    return;
                }
                break;
            case R.id.slide_left_menu_project_module:
                if (this instanceof ProjectModuleActivity) {
                    slideView.toggleMenu();
                    return;
                }
                Intent projectModuleIntent = new Intent(this, ProjectModuleActivity.class);
                startActivity(projectModuleIntent);
                break;
            case R.id.slide_left_menu_submit:
                break;
            case R.id.slide_left_menu_assign:
                break;
            case R.id.slide_left_menu_opt:
                break;
            case R.id.slide_left_menu_setting:
                Intent settingIntent = new Intent(this, SettingActivity.class);
                startActivity(settingIntent);
                break;
            case R.id.slide_left_menu_about:
                Intent aboutIntent = new Intent(this, AboutActivity.class);
                startActivity(aboutIntent);
                break;
        }
    }

    /**
     * 点击退出登录按钮
     *
     * @param v
     */
    public void onExitClick(View v) {
        // 直接删除本地的Cookie
        SharedPreferenceUtils.delete(BaseActivity.this, MyConstant.COOKIE);
        Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
        startActivity(intent);

        // HttpVisitUtils.getHttpVisit(this, LocalInfo.getBaseUrl(this) + "site/logout", true, "正在退出...",
        // new HttpVisitUtils.OnHttpFinishListener() {
        // @Override
        // public void onVisitFinish(HttpResult result) {
        // if (result == null)
        // throw new NullPointerException("退出返回结果是空指针");
        // if (result.getHttpResponseCode() == HttpURLConnection.HTTP_OK) {
        // if (result.getCode() == MyConstant.VISIT_CODE_SUCCESS) {
        // Toast.makeText(BaseActivity.this, "退出成功!", Toast.LENGTH_SHORT).show();
        // Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
        // startActivity(intent);
        // } else {
        // Toast.makeText(BaseActivity.this, "退出失败!", Toast.LENGTH_SHORT).show();
        // }
        // } else {
        // HttpConnectResultUtils.optFailure(BaseActivity.this, result);
        // }
        // }
        // });
    }

}
