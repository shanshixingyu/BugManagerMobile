package com.gf.BugManagerMobile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.gf.BugManagerMobile.models.HttpResult;
import com.gf.BugManagerMobile.models.LocalInfo;
import com.gf.BugManagerMobile.models.PimInfo;
import com.gf.BugManagerMobile.models.User;
import com.gf.BugManagerMobile.utils.HttpConnectResultUtils;
import com.gf.BugManagerMobile.utils.HttpVisitUtils;
import com.gf.BugManagerMobile.utils.MyConstant;
import com.gf.BugManagerMobile.view.ModifyEmailDialog;
import com.gf.BugManagerMobile.view.ModifyPasswordDialog;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * 个人信息界面
 * Created by Administrator on 5/14 0014.
 */
public class PimActivity extends BaseActivity {
    private static final String TAG = "PimActivity";

    private TextView userIdTv, userNameTv, roleNameTv, emailTv, creatorTv, createTimeTv;
    private TextView joinGroupTv, joinProjectModuleTv;
    private PimInfo mPimInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCenterContentLyt(R.layout.page_pim);

        initComponent();
        /**
         * 获取用户个人信息
         */
        HttpVisitUtils.getHttpVisit(this, LocalInfo.getBaseUrl(this) + "site/pim", true, "正在加载...",
            onLoadDataHttpFinishListener);

    }

    private void initComponent() {
        userIdTv = (TextView) findViewById(R.id.pim_user_id_tv);
        userNameTv = (TextView) findViewById(R.id.pim_user_name_tv);
        roleNameTv = (TextView) findViewById(R.id.pim_role_name_tv);
        emailTv = (TextView) findViewById(R.id.pim_email_tv);
        creatorTv = (TextView) findViewById(R.id.pim_creator_tv);
        createTimeTv = (TextView) findViewById(R.id.pim_create_time_tv);

        joinGroupTv = (TextView) findViewById(R.id.pim_join_group_tv);
        joinProjectModuleTv = (TextView) findViewById(R.id.pim_join_project_module_tv);

    }

    private HttpVisitUtils.OnHttpFinishListener onLoadDataHttpFinishListener =
        new HttpVisitUtils.OnHttpFinishListener() {
            @Override
            public void onVisitFinish(HttpResult result) {
                if (result.isVisitSuccess()) {

                    mPimInfo = JSON.parseObject(result.getResult(), PimInfo.class);
                    Log.i(TAG, mPimInfo.toString());

                    User user = mPimInfo.getUser();
                    userIdTv.setText("" + user.getId());
                    userNameTv.setText(user.getName());
                    roleNameTv.setText(mPimInfo.getRoleName());
                    emailTv.setText(user.getEmail());
                    creatorTv.setText(mPimInfo.getCreatorName());
                    createTimeTv.setText(user.getCreate_time());

                    joinGroupTv.setText(mPimInfo.getGroupNames().length + "个");
                    joinProjectModuleTv.setText(mPimInfo.getProjectModuleData().size() + "个");
                } else if (result.getCode() == MyConstant.VISIT_CODE_NO_OK) {
                    /* 获取失败失败 */
                    Toast.makeText(PimActivity.this, R.string.pim_connect_fetch_failure, Toast.LENGTH_SHORT).show();
                } else if (result.getCode() == MyConstant.VISIT_CODE_CONNECT_TIME_OUT) {
                    /* 连接超时 */
                    Toast.makeText(PimActivity.this, R.string.pim_connect_time_out, Toast.LENGTH_SHORT).show();
                } else {
                    /* 连接错错误或者其它 */
                    Toast.makeText(PimActivity.this, R.string.pim_connect_error, Toast.LENGTH_SHORT).show();
                }
            }
        };

    /**
     * 顶部栏上的点击事件
     * @param v
     */
    public void onOptClick(View v) {
        switch (v.getId()) {
            case R.id.pim_top_back_imgv:
                this.finish();
                break;
            default:

        }
    }

    private ModifyPasswordDialog modifyPasswordDialog = null;
    private ModifyEmailDialog modifyEmailDialog = null;

    /**
     * 个人信息点击某一行
     * @param v
     */
    public void onRowItemClick(View v) {
        switch (v.getId()) {
            case R.id.pim_row_pwd_lyt:
                // 点击修改密码
                if (modifyPasswordDialog == null) {
                    modifyPasswordDialog = new ModifyPasswordDialog(this);
                    modifyPasswordDialog.setOnModifyPasswordListener(onModifyPasswordListener);
                }
                if (!modifyPasswordDialog.isShowing())
                    modifyPasswordDialog.show();
                break;
            case R.id.pim_row_email_lyt:
                // 点击修改邮箱
                if (modifyEmailDialog == null) {
                    modifyEmailDialog = new ModifyEmailDialog(this);
                    modifyEmailDialog.setOnModifyEmailListener(onModifyEmailListener);
                }
                if (!modifyEmailDialog.isShowing())
                    modifyEmailDialog.show();
                break;
            case R.id.pim_row_join_group_lyt:
                if (mPimInfo == null || mPimInfo.getGroupNames() == null || mPimInfo.getGroupNames().length == 0) {
                    Toast.makeText(PimActivity.this, "暂时没有参加任何团队", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent showInfoIntent = new Intent(PimActivity.this, ShowInfoActivity.class);
                showInfoIntent.putExtra(MyConstant.SHOW_INFO_DATA, mPimInfo.getGroupNames());
                startActivity(showInfoIntent);

                break;
            case R.id.pim_row_project_module_lyt:
                if (mPimInfo == null || mPimInfo.getProjectModuleData() == null
                    || mPimInfo.getProjectModuleData().size() == 0) {
                    Toast.makeText(PimActivity.this, "暂时没有参与任何项目与模块", Toast.LENGTH_SHORT).show();
                    return;
                }
                List<PimInfo.ProjectModuleInfo> projectModuleInfos = mPimInfo.getProjectModuleData();
                List<String> showInfos = new ArrayList<String>();
                for (PimInfo.ProjectModuleInfo info : projectModuleInfos) {
                    showInfos.add("【" + info.getProjectName() + "】" + info.getModuleName());
                }
                Intent projectModuleIntent = new Intent(PimActivity.this, ShowInfoActivity.class);
                projectModuleIntent.putExtra(MyConstant.SHOW_INFO_DATA, showInfos.toArray(new String[1]));
                startActivity(projectModuleIntent);
                break;

            default:

        }
    }

    private ModifyEmailDialog.OnModifyEmailListener onModifyEmailListener =
        new ModifyEmailDialog.OnModifyEmailListener() {
            @Override
            public void onModify(final String email) {
                // emailTv.setText(input);
                HttpVisitUtils.postHttpVisit(PimActivity.this, LocalInfo.getBaseUrl(PimActivity.this)
                    + "site/modify-email", "email=" + email, true, "正在保存...",
                    new HttpVisitUtils.OnHttpFinishListener() {
                        @Override
                        public void onVisitFinish(HttpResult result) {
                            if (result.getHttpResponseCode() == HttpURLConnection.HTTP_OK) {
                                if (result.getCode() == MyConstant.VISIT_CODE_SUCCESS) {
                                    emailTv.setText(email);
                                    if (modifyEmailDialog != null)
                                        modifyEmailDialog.dismiss();
                                } else {
                                    Toast.makeText(PimActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
                                }
                                Log.i(TAG, result.toString());
                            } else {
                                HttpConnectResultUtils.optFailure(PimActivity.this, result);
                            }
                        }
                    });
            }
        };

    private ModifyPasswordDialog.OnModifyPasswordListener onModifyPasswordListener =
        new ModifyPasswordDialog.OnModifyPasswordListener() {
            @Override
            public void onModify(String oldPassword, String newPassword) {
                String postData = "oldPassword=" + oldPassword + "&newPassword=" + newPassword;
                HttpVisitUtils.postHttpVisit(PimActivity.this, LocalInfo.getBaseUrl(PimActivity.this)
                    + "site/modify-password", postData, true, "正在修改...", new HttpVisitUtils.OnHttpFinishListener() {
                    @Override
                    public void onVisitFinish(HttpResult result) {
                        if (result.getHttpResponseCode() == HttpURLConnection.HTTP_OK) {
                            if (result.getCode() == MyConstant.VISIT_CODE_SUCCESS) {
                                Toast.makeText(PimActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                                if (modifyPasswordDialog != null)
                                    modifyPasswordDialog.dismiss();
                            } else if (result.getCode() == MyConstant.VISIT_CODE_WRONG_PASSWORD) {
                                Toast.makeText(PimActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(PimActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                            }
                            Log.i(TAG, result.toString());
                        } else {
                            HttpConnectResultUtils.optFailure(PimActivity.this, result);
                        }
                    }
                });
            }
        };

}
