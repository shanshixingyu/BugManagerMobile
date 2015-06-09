package com.gf.BugManagerMobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.TextView;
import com.gf.BugManagerMobile.utils.HttpVisitUtils;
import com.gf.BugManagerMobile.utils.LocalInfo;
import com.gf.BugManagerMobile.models.LoginSuccessInfo;
import com.gf.BugManagerMobile.utils.MyConstant;
import com.gf.BugManagerMobile.utils.SharedPreferenceUtils;
import com.gf.BugManagerMobile.view.SatelliteMenuLyt;
import com.gf.BugManagerMobile.view.SlideView;

/**
 * 所有缺陷管理界面的父类(除登录、设置、关于界面)
 * Created by Administrator on 5/9 0009.
 */
public class BaseHomeActivity extends BaseActivity {
    private static final String TAG = "BaseActivity";

    protected SlideView slideView;
    private ViewGroup mCenterContentLyt = null;
    private TextView slideLeftMenuUserNameTv, slideLeftMenuRoleNameTv;
    private SatelliteMenuLyt mSatelliteMenuLyt;

    /**
     * 登录的用户信息
     */
    private LoginSuccessInfo loginSuccessInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_home_base);

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
        mSatelliteMenuLyt = (SatelliteMenuLyt) findViewById(R.id.satellite_menu_lyt);
        mSatelliteMenuLyt.setOnSatelliteMenuItemClickListener(onSatelliteMenuItemClickListener);
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
                Intent pimIntent = new Intent(this, PimActivity.class);
                startActivity(pimIntent);
                break;
            case R.id.slide_left_menu_home:
                if (this instanceof HomeActivity) {
                    slideView.toggleMenu();
                    return;
                }
                Intent homeIntent = new Intent(this, HomeActivity.class);
                startActivity(homeIntent);
                break;
            case R.id.slide_left_menu_project_module:
                Intent projectModuleIntent = new Intent(this, ProjectModuleActivity.class);
                startActivity(projectModuleIntent);
                break;
            case R.id.slide_left_menu_submit:
                Intent submitIntent = new Intent(this, MyBugActivity.class);
                submitIntent.putExtra(MyBugActivity.ACTIVITY_TYPE, MyBugActivity.TYPE_SUBMIT);
                startActivity(submitIntent);
                break;
            case R.id.slide_left_menu_assign:
                Intent assignIntent = new Intent(this, MyBugActivity.class);
                assignIntent.putExtra(MyBugActivity.ACTIVITY_TYPE, MyBugActivity.TYPE_ASSIGIN);
                startActivity(assignIntent);
                break;
            case R.id.slide_left_menu_opt:
                Intent optIntent = new Intent(this, MyBugActivity.class);
                optIntent.putExtra(MyBugActivity.ACTIVITY_TYPE, MyBugActivity.TYPE_OPT);
                startActivity(optIntent);
                break;
            case R.id.slide_left_menu_charts:
                Intent chartsSearchIntent = new Intent(this, ChartsSearchActivity.class);
                startActivity(chartsSearchIntent);
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

    private SatelliteMenuLyt.OnSatelliteMenuItemClickListener onSatelliteMenuItemClickListener =
        new SatelliteMenuLyt.OnSatelliteMenuItemClickListener() {
            @Override
            public void onMenuItemClick(int index, String tag, View childView) {
                if ("user".equals(tag)) {
                    Intent userIntent = new Intent(BaseHomeActivity.this, UserListActivity.class);
                    startActivity(userIntent);
                } else if ("group".equals(tag)) {
                    Intent groupIntent = new Intent(BaseHomeActivity.this, GroupListActivity.class);
                    startActivity(groupIntent);
                } else if ("project".equals(tag)) {
                    Intent projectIntent = new Intent(BaseHomeActivity.this, ProjectListActivity.class);
                    startActivity(projectIntent);
                } else if ("setting".equals(tag)) {

                }
            }
        };

    /**
     * 点击退出登录按钮
     *
     * @param v
     */
    public void onExitClick(View v) {
        // 返回结果如何都无关紧要了，但是必须在删除本地cookie前
        HttpVisitUtils.getHttpVisit(this, LocalInfo.getBaseUrl(this) + "site/logout", false, "", null);

        // 直接删除本地的Cookie
        SharedPreferenceUtils.delete(BaseHomeActivity.this, MyConstant.COOKIE);
        SharedPreferenceUtils.delete(BaseHomeActivity.this, MyConstant.SP_LOGIN_USE_NAME);
        SharedPreferenceUtils.delete(BaseHomeActivity.this, MyConstant.SP_LOGIN_USE_ID);
        SharedPreferenceUtils.delete(BaseHomeActivity.this, MyConstant.SP_LOGIN_ROLE_ID);
        SharedPreferenceUtils.delete(BaseHomeActivity.this, MyConstant.SP_LOGIN_ROLE_NAME);
        SharedPreferenceUtils.delete(BaseHomeActivity.this, MyConstant.SP_LOGIN_PASSWORD);

        Intent intent = new Intent(BaseHomeActivity.this, LoginActivity.class);
        intent.putExtra(MyConstant.OTHER_ACTIVITY_2_LOGIN_SHOULD_AUTO, false);
        startActivity(intent);
        this.finish();
    }

}
