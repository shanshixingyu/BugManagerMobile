package com.gf.BugManagerMobile;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.gf.BugManagerMobile.utils.SharedPreferenceUtils;
import com.gf.BugManagerMobile.view.SettingInputDialog;

/**
 * 设置界面
 * Created by GuLang on 2015/05/09.
 */
public class SettingActivity extends Activity {

    private TextView serverIpTv;
    private TextView serverPortTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setting);
        init();
    }

    private void init() {
        serverIpTv = (TextView) findViewById(R.id.setting_server_ip_tv);
        serverPortTv = (TextView) findViewById(R.id.setting_server_port_tv);

        serverIpTv.setText(SharedPreferenceUtils.queryString(this, SharedPreferenceUtils.SERVER_IP));
        serverPortTv.setText(SharedPreferenceUtils.queryString(this, SharedPreferenceUtils.SERVER_PORT));
    }

    public void onClickBtn(View v) {
        switch (v.getId()) {
            case R.id.setting_back_imgv:
                this.finish();
                break;
            default:
                break;
        }
    }

    private SettingInputDialog settingInputDialog = null;

    /**
     * 设置界面的每一项的回调方法
     *
     * @param v
     * @author GuLang
     */
    public void onClickGroupItem(View v) {
        if (settingInputDialog == null) {
            settingInputDialog = new SettingInputDialog(this);
            settingInputDialog.setOnSettingInfoListener(onSettingInfoListener);
        }
        switch (v.getId()) {
            case R.id.setting_item_server_ip: // 服务器IP地址
                settingInputDialog.setDialogType(SettingInputDialog.DialogType.IP);
                settingInputDialog.show(serverIpTv.getText());
                break;
            case R.id.setting_item_server_port: // 服务器端口号
                settingInputDialog.setDialogType(SettingInputDialog.DialogType.PORT);
                settingInputDialog.show(serverPortTv.getText());
                break;
            default:
                break;
        }
    }

    private SettingInputDialog.OnSettingInfoListener onSettingInfoListener =
            new SettingInputDialog.OnSettingInfoListener() {
                @Override
                public void onSettingInfo(SettingInputDialog.DialogType type, String input) {
                    if (input == null)
                        input = "";
                    if (type == SettingInputDialog.DialogType.IP) {
                        SharedPreferenceUtils.save(SettingActivity.this, SharedPreferenceUtils.SERVER_IP, input);
                        serverIpTv.setText(input);
                    } else if (type == SettingInputDialog.DialogType.PORT) {
                        SharedPreferenceUtils.save(SettingActivity.this, SharedPreferenceUtils.SERVER_PORT, input);
                        serverPortTv.setText(input);
                    }
                }
            };

}
