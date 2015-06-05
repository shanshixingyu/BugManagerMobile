package com.gf.BugManagerMobile.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import android.widget.Toast;
import com.gf.BugManagerMobile.R;
import com.gf.BugManagerMobile.models.HttpResult;
import com.gf.BugManagerMobile.utils.HttpConnectResultUtils;
import com.gf.BugManagerMobile.utils.HttpVisitUtils;
import com.gf.BugManagerMobile.utils.LocalInfo;

/**
 * 关闭Bug的对话框
 * Created by GuLang on 2015-06-04.
 */
public class BugCloseDialog extends Dialog {
    private static final String TAG = "BugCloseDialog";

    private int bugId;
    private OnBugOptListener onBugOptListener;

    private InputMethodManager inputMethodManager = null;
    private RectF mDialogBoundRectF = null;
    private View mDialogRootView;
    private EditText mReasonEt;

    public BugCloseDialog(Context context) {
        this(context, 0);
    }

    public BugCloseDialog(Context context, int theme) {
        super(context, R.style.CustomDialogTheme2);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_close_bug);

        mDialogRootView = findViewById(R.id.bug_close_dialog_root_lyt);
        mReasonEt = (EditText) findViewById(R.id.bug_close_reason_et);
        findViewById(R.id.bug_close_cancel_btn).setOnClickListener(onClickListener);
        findViewById(R.id.bug_close_sure_btn).setOnClickListener(onClickListener);
        inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mDialogBoundRectF == null) {
            mDialogBoundRectF = new RectF();
            mDialogBoundRectF.set(mDialogRootView.getLeft(), mDialogRootView.getTop(), mDialogRootView.getRight(),
                mDialogRootView.getBottom());
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (mDialogBoundRectF.contains(event.getX(), event.getY())) {
                // 在对话框内部
            } else {
                // 在对话框外面
                inputMethodManager.hideSoftInputFromWindow(mDialogRootView.getWindowToken(), 0);
            }
        }
        return true;
    }

    public View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.bug_close_cancel_btn:
                    BugCloseDialog.this.dismiss();
                    break;
                case R.id.bug_close_sure_btn:
                    String closeReason = mReasonEt.getText().toString().trim();
                    if ("".equals(closeReason)) {
                        Toast.makeText(BugCloseDialog.this.getContext(), "关闭原因必填", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String postData = "reason=" + closeReason;

                    HttpVisitUtils.postHttpVisit(BugCloseDialog.this.getContext(),
                        LocalInfo.getBaseUrl(BugCloseDialog.this.getContext()) + "bug/close&bugId=" + bugId, postData,
                        true, "保存中...", onCloseFinishListener);

                    break;
            }
        }
    };

    private HttpVisitUtils.OnHttpFinishListener onCloseFinishListener = new HttpVisitUtils.OnHttpFinishListener() {
        @Override
        public void onVisitFinish(HttpResult result) {
            if (result == null)
                return;
            if (result.isVisitSuccess()) {
                Toast.makeText(BugCloseDialog.this.getContext(), "关闭成功", Toast.LENGTH_SHORT).show();
                if (onBugOptListener != null) {
                    onBugOptListener.onSaveSuccessFinish();
                }
                mReasonEt.setText("");
                BugCloseDialog.this.dismiss();
            } else {
                HttpConnectResultUtils.optFailure(BugCloseDialog.this.getContext(), result);
            }
        }
    };

    public void setBugId(int bugId) {
        this.bugId = bugId;
    }

    public void setOnBugOptListener(OnBugOptListener listener) {
        this.onBugOptListener = listener;
    }

}
