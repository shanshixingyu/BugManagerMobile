package com.gf.BugManagerMobile.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.gf.BugManagerMobile.R;

/**
 * 确认对话框
 * Created by Administrator on 5/11 0011.
 */
public class ConfirmDialog extends Dialog implements View.OnClickListener {
    private static final String TAG = "ConfirmDialog";

    private OnConfirmDialogListener onConfirmDialogListener = null;

    private Button leftBtn, rightBtn;
    private TextView messageTv;

    private CharSequence leftBtnText, rightBtnText;
    private CharSequence messageTvText;

    public ConfirmDialog(Context context) {
        this(context, 0);
    }

    public ConfirmDialog(Context context, int theme) {
        super(context, R.style.CustomDialogTheme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_dialog);

        leftBtn = (Button) findViewById(R.id.confirm_dialog_left_btn);
        leftBtn.setOnClickListener(this);
        rightBtn = (Button) findViewById(R.id.confirm_dialog_right_btn);
        rightBtn.setOnClickListener(this);
        messageTv = (TextView) findViewById(R.id.confirm_dialog_msg);

        // 初始化
        if (messageTvText != null) {
            messageTv.setText(messageTvText);
        }
        if (leftBtnText != null) {
            leftBtn.setText(leftBtnText);
        }
        if (rightBtnText != null) {
            rightBtn.setText(rightBtnText);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirm_dialog_left_btn:
                if (onConfirmDialogListener != null)
                    onConfirmDialogListener.onLeftBtnClick(this);
                break;
            case R.id.confirm_dialog_right_btn:
                if (onConfirmDialogListener != null)
                    onConfirmDialogListener.onRightBtnClick(this);
                break;
            default:
        }
    }

    /**
     * 设置左边按钮的内容
     * @param text
     */
    public void setLeftBtnText(CharSequence text) {
        leftBtnText = text;
        if (leftBtn != null)
            leftBtn.setText(leftBtnText);
    }

    /**
     * 设置左边按钮的内容
     * @param strResId
     */
    public void setLeftBtnText(int strResId) {
        leftBtnText = getContext().getString(strResId);
        if (leftBtn != null)
            leftBtn.setText(leftBtnText);
    }

    /**
     * 设置右边按钮的内容
     * @param text
     */
    public void setRightBtnText(CharSequence text) {
        rightBtnText = text;
        if (rightBtn != null)
            rightBtn.setText(rightBtnText);
    }

    /**
     * 设置右边按钮的内容
     * @param strResId
     */
    public void setRightBtnText(int strResId) {
        rightBtnText = getContext().getString(strResId);
        if (rightBtn != null)
            rightBtn.setText(rightBtnText);
    }

    /**
     * 设置确认内容
     * @param text
     */
    public void setMessageTvText(CharSequence text) {
        messageTvText = text;
        if (messageTv != null)
            messageTv.setText(messageTvText);
    }

    /**
     * 设置确认内容
     * @param strResId
     */
    public void setMessageTvText(int strResId) {
        messageTvText = getContext().getString(strResId);
        if (messageTv != null)
            messageTv.setText(messageTvText);
    }

    /**
     * 设置事件监听
     * @param listener
     */
    public void setOnConfirmDialogListener(OnConfirmDialogListener listener) {
        this.onConfirmDialogListener = listener;
    }

    public interface OnConfirmDialogListener {
        public void onLeftBtnClick(ConfirmDialog dialog);

        public void onRightBtnClick(ConfirmDialog dialog);
    }

}
