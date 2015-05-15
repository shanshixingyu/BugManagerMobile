package com.gf.BugManagerMobile.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import com.gf.BugManagerMobile.R;

/**
 * 设置界面中的设置对话框
 * Created by Administrator on 5/9 0009.
 */
public class SettingInputDialog extends Dialog {
    private static final String TAG = "SettingInputDialog";

    public enum DialogType {
        IP, PORT
    }

    private TextView settingDialogMsgTv;
    private EditText settingDialogInputEt;

    private OnSettingInfoListener onSettingInfoListener = null;

    private DialogType dialogType = DialogType.IP;// 默认的

    private int msgResId = R.string.setting_dialog_msg_ip;// 默认
    private int inputResId = R.string.setting_dialog_input_hint_server_ip;// 默认

    private RectF settingDialogBoundRectF = null;
    private View settingDialogLyt = null;
    private InputMethodManager inputMethodManager = null;

    public SettingInputDialog(Context context) {
        this(context, 0);
    }

    public SettingInputDialog(Context context, int theme) {
        super(context, R.style.CustomDialogTheme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_setting_input);

        settingDialogLyt = findViewById(R.id.setting_dialog_lyt);

        settingDialogMsgTv = (TextView) findViewById(R.id.setting_dialog_msg_tv);
        settingDialogInputEt = (EditText) findViewById(R.id.setting_dialog_input_et);
        findViewById(R.id.setting_dialog_sure_btn).setOnClickListener(this.onClickListener);
        findViewById(R.id.setting_dialog_cancel_btn).setOnClickListener(this.onClickListener);

        inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        settingDialogMsgTv.setHint(msgResId);
        settingDialogInputEt.setHint(inputResId);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.setting_dialog_sure_btn:
                    if (onSettingInfoListener != null)
                        onSettingInfoListener.onSettingInfo(dialogType, settingDialogInputEt.getText().toString());
                    SettingInputDialog.this.dismiss();
                    break;
                case R.id.setting_dialog_cancel_btn:
                    SettingInputDialog.this.dismiss();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (settingDialogBoundRectF == null) {
            settingDialogBoundRectF = new RectF();
            settingDialogBoundRectF.set(settingDialogLyt.getLeft(), settingDialogLyt.getTop(),
                settingDialogLyt.getRight(), settingDialogLyt.getBottom());
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (settingDialogBoundRectF.contains(event.getX(), event.getY())) {
                // 在对话框内部
            } else {
                // 在对话框外面
                inputMethodManager.hideSoftInputFromWindow(settingDialogInputEt.getWindowToken(), 0);
            }
        }
        return true;
    }

    /**
     * 设置监听
     *
     * @param listener
     */
    public void setOnSettingInfoListener(OnSettingInfoListener listener) {
        this.onSettingInfoListener = listener;
    }

    /**
     * 设置对户口类型
     *
     * @param dialogType
     */
    public void setDialogType(DialogType dialogType) {
        this.dialogType = dialogType;
        if (this.dialogType == DialogType.IP) {
            this.msgResId = R.string.setting_dialog_msg_ip;
            this.inputResId = R.string.setting_dialog_input_hint_server_ip;
        } else if (this.dialogType == DialogType.PORT) {
            this.msgResId = R.string.setting_dialog_msg_port;
            this.inputResId = R.string.setting_dialog_input_hint_server_port;
        }
        if (this.settingDialogMsgTv != null) {
            this.settingDialogMsgTv.setHint(this.msgResId);
        }
        if (this.settingDialogInputEt != null) {
            this.settingDialogInputEt.setHint(this.inputResId);
        }
    }

    /**
     * 显示对话框
     *
     * @param text
     */
    public void show(CharSequence text) {
        this.show();
        this.settingDialogInputEt.setText(text);
    }

    /**
     * 获得对话框当前的类型
     *
     * @return
     */
    public DialogType getDialogType() {
        return this.dialogType;
    }

    public interface OnSettingInfoListener {
        public void onSettingInfo(DialogType type, String input);
    }
}
