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
 * 激活Bug的对话框
 * Created by GuLang on 2015-06-04.
 */
public class BugActiveDialog extends Dialog {
    private static final String TAG = "BugActiveDialog";

    private int bugId;
    private OnBugOptListener onBugOptListener;

    private InputMethodManager inputMethodManager = null;
    private RectF mDialogBoundRectF = null;
    private View mDialogRootView;
    private EditText mReasonEt;

    public BugActiveDialog(Context context) {
        this(context, 0);
    }

    public BugActiveDialog(Context context, int theme) {
        super(context, R.style.CustomDialogTheme2);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_active_bug);

        mDialogRootView = findViewById(R.id.bug_active_dialog_root_lyt);
        mReasonEt = (EditText) findViewById(R.id.bug_active_reason_et);
        findViewById(R.id.bug_active_cancel_btn).setOnClickListener(onClickListener);
        findViewById(R.id.bug_active_sure_btn).setOnClickListener(onClickListener);
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
                case R.id.bug_active_cancel_btn:
                    BugActiveDialog.this.dismiss();
                    break;
                case R.id.bug_active_sure_btn:
                    String activeReason = mReasonEt.getText().toString().trim();
                    if ("".equals(activeReason)) {
                        Toast.makeText(BugActiveDialog.this.getContext(), "激活原因必填", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String postData = "reason=" + activeReason;

                    HttpVisitUtils.postHttpVisit(BugActiveDialog.this.getContext(),
                        LocalInfo.getBaseUrl(BugActiveDialog.this.getContext()) + "bug/active&bugId=" + bugId,
                        postData, true, "保存中...", onActiveFinishListener);

                    break;
            }
        }
    };

    private HttpVisitUtils.OnHttpFinishListener onActiveFinishListener = new HttpVisitUtils.OnHttpFinishListener() {
        @Override
        public void onVisitFinish(HttpResult result) {
            if (result == null)
                return;
            if (result.isVisitSuccess()) {
                Toast.makeText(BugActiveDialog.this.getContext(), "激活成功", Toast.LENGTH_SHORT).show();
                if (onBugOptListener != null) {
                    onBugOptListener.onSaveSuccessFinish();
                }
                mReasonEt.setText("");
                BugActiveDialog.this.dismiss();
            } else {
                HttpConnectResultUtils.optFailure(BugActiveDialog.this.getContext(), result);
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
