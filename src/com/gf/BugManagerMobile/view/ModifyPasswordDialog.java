package com.gf.BugManagerMobile.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;
import com.gf.BugManagerMobile.R;

/**
 * 设置密码对话框
 * Created by Administrator on 5/14 0014.
 */
public class ModifyPasswordDialog extends Dialog {
    private static final String TAG = "ModifyPasswordDialog";

    private EditText modifyOldPwdEt, modifyNewPwdEt, modifyRepeatPwdEt;
    private OnModifyPasswordListener onModifyPasswordListener;
    private RectF modifyRect = null;
    private View modifyPasswordLyt;
    private InputMethodManager inputMethodManager;

    public ModifyPasswordDialog(Context context) {
        this(context, 0);
    }

    public ModifyPasswordDialog(Context context, int theme) {
        super(context, R.style.CustomDialogTheme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_modify_password);

        modifyPasswordLyt = findViewById(R.id.modify_pwd_lyt);
        modifyOldPwdEt = (EditText) findViewById(R.id.pim_modify_pwd_old_et);
        modifyNewPwdEt = (EditText) findViewById(R.id.pim_modify_pwd_new_et);
        modifyRepeatPwdEt = (EditText) findViewById(R.id.pim_modify_pwd_repeat_et);

        findViewById(R.id.pim_modify_pwd_left_btn).setOnClickListener(onClickListener);
        findViewById(R.id.pim_modify_pwd_right_btn).setOnClickListener(onClickListener);

        inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (modifyRect == null) {
            modifyRect = new RectF();
            modifyRect.set(modifyPasswordLyt.getLeft(), modifyPasswordLyt.getTop(), modifyPasswordLyt.getRight(),
                modifyPasswordLyt.getBottom());
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (modifyRect.contains(event.getX(), event.getY())) {
                // 在对话框内部
            } else {
                // 在对话框外面
                inputMethodManager.hideSoftInputFromWindow(modifyOldPwdEt.getWindowToken(), 0);
            }
        }
        return true;
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.pim_modify_pwd_left_btn:
                    ModifyPasswordDialog.this.dismiss();
                    break;
                case R.id.pim_modify_pwd_right_btn:
                    String inputOldPwd = modifyOldPwdEt.getText().toString().trim();
                    String inputNewPwd = modifyNewPwdEt.getText().toString().trim();
                    String inputRepeatPwd = modifyRepeatPwdEt.getText().toString().trim();
                    if (inputOldPwd.equals("")) {
                        Toast.makeText(getContext(), R.string.pim_modify_pwd_dialog_not_old, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (inputNewPwd.equals("")) {
                        Toast.makeText(getContext(), R.string.pim_modify_pwd_dialog_not_new, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!inputNewPwd.equals(inputRepeatPwd)) {
                        Toast.makeText(getContext(), R.string.pim_modify_pwd_dialog_not_match, Toast.LENGTH_SHORT)
                            .show();
                        return;
                    }

                    if (onModifyPasswordListener != null) {
                        onModifyPasswordListener.onModify(inputOldPwd, inputNewPwd);
                    }
                    break;
            }
        }
    };

    public void setOnModifyPasswordListener(OnModifyPasswordListener listener) {
        this.onModifyPasswordListener = listener;
    }

    public interface OnModifyPasswordListener {
        public void onModify(String oldPassword, String newPassword);
    }

}
