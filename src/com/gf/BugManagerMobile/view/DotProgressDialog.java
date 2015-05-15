package com.gf.BugManagerMobile.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;
import com.gf.BugManagerMobile.R;

/**
 * 菊花进度条对话框
 * Created by Administrator on 5/13 0013.
 */
public class DotProgressDialog extends Dialog {
    private static final String TAG = "JHProgressDialog";

    private TextView messageTv;
    private CharSequence message;

    public DotProgressDialog(Context context) {
        this(context, 0);
    }

    public DotProgressDialog(Context context, int theme) {
        super(context, R.style.CustomDialogTheme);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_jh_progress);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        messageTv = (TextView) findViewById(R.id.dot_dialog_message);
        if (message != null) {
            messageTv.setText(message);
        }
    }

    public void setMessage(CharSequence message) {
        this.message = message;
        if (this.messageTv != null)
            this.messageTv.setText(this.message);
    }

}
