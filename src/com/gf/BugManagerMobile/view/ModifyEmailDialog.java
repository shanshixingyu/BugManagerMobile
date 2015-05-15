package com.gf.BugManagerMobile.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.RectF;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import android.widget.Toast;
import com.gf.BugManagerMobile.R;
import com.gf.BugManagerMobile.utils.MyConstant;
import com.gf.BugManagerMobile.utils.RegexUtils;
import org.w3c.dom.TypeInfo;

/**
 * 个人信息界面中修改用户邮箱的对话框
 * Created by Administrator on 5/9 0009.
 */
public class ModifyEmailDialog extends Dialog {
    private static final String TAG = "ModifyEmailDialog";

    private TextView modifyEmailDialogTitleTv, modifyEmailDialogMsgTv;
    private EditText modifyEmailDialogEt;

    private OnModifyEmailListener onModifyEmailListener = null;

    private RectF settingDialogBoundRectF = null;
    private View settingDialogLyt = null;
    private InputMethodManager inputMethodManager = null;

    public ModifyEmailDialog(Context context) {
        this(context, 0);
    }

    public ModifyEmailDialog(Context context, int theme) {
        super(context, R.style.CustomDialogTheme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_setting_input);

        setCanceledOnTouchOutside(true);
        setCancelable(true);

        settingDialogLyt = findViewById(R.id.setting_dialog_lyt);

        modifyEmailDialogTitleTv = (TextView) findViewById(R.id.setting_dialog_title_tv);
        modifyEmailDialogMsgTv = (TextView) findViewById(R.id.setting_dialog_msg_tv);
        modifyEmailDialogEt = (EditText) findViewById(R.id.setting_dialog_input_et);
        findViewById(R.id.setting_dialog_sure_btn).setOnClickListener(this.onClickListener);
        findViewById(R.id.setting_dialog_cancel_btn).setOnClickListener(this.onClickListener);

        inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        modifyEmailDialogTitleTv.setText(R.string.pim_modify_email_dialog_title);
        modifyEmailDialogMsgTv.setText(R.string.pim_modify_email_dialog_msg);
        modifyEmailDialogEt.setHint(R.string.pim_modify_email_dialog_hint);
        modifyEmailDialogEt.setRawInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.setting_dialog_sure_btn:
                    String inputStr = modifyEmailDialogEt.getText().toString();
                    if (!RegexUtils.isMatch(MyConstant.EMAIL_PATTERN, inputStr)) {
                        Toast.makeText(getContext(), R.string.pim_modify_email_dialog_not_email, Toast.LENGTH_SHORT)
                            .show();
                        modifyEmailDialogEt.setSelection(0, inputStr.length());
                        return;
                    }
                    if (onModifyEmailListener != null)
                        onModifyEmailListener.onModify(inputStr);
                    ModifyEmailDialog.this.dismiss();
                    break;
                case R.id.setting_dialog_cancel_btn:
                    ModifyEmailDialog.this.dismiss();
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
                inputMethodManager.hideSoftInputFromWindow(modifyEmailDialogEt.getWindowToken(), 0);
            }
        }
        return true;
    }

    /**
     * 设置监听
     *
     * @param listener
     */
    public void setOnModifyEmailListener(OnModifyEmailListener listener) {
        this.onModifyEmailListener = listener;
    }

    public interface OnModifyEmailListener {
        public void onModify(String input);
    }
}
