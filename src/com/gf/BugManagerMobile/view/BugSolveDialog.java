package com.gf.BugManagerMobile.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.gf.BugManagerMobile.R;
import com.gf.BugManagerMobile.models.HttpResult;
import com.gf.BugManagerMobile.utils.HttpConnectResultUtils;
import com.gf.BugManagerMobile.utils.HttpVisitUtils;
import com.gf.BugManagerMobile.utils.LocalInfo;

/**
 * 解决Bug的对话框
 * Created by GuLang on 2015-06-04.
 */
public class BugSolveDialog extends Dialog {
    private static final String TAG = "BugSolveDialog";

    private int bugId;
    private OnBugOptListener onBugOptListener;

    private InputMethodManager inputMethodManager = null;
    private RectF mDialogBoundRectF = null;
    private View mDialogRootView;
    private RadioGroup mTypeRadioGroup;
    private EditText mIntroduceEt;

    public BugSolveDialog(Context context) {
        this(context, 0);
    }

    public BugSolveDialog(Context context, int theme) {
        super(context, R.style.CustomDialogTheme2);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_solve_bug);

        mDialogRootView = findViewById(R.id.bug_solve_dialog_root_lyt);
        mTypeRadioGroup = (RadioGroup) findViewById(R.id.bug_solve_type_rg);
        mIntroduceEt = (EditText) findViewById(R.id.bug_solve_introduce_et);
        findViewById(R.id.bug_solve_cancel_btn).setOnClickListener(onClickListener);
        findViewById(R.id.bug_solve_sure_btn).setOnClickListener(onClickListener);
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
                case R.id.bug_solve_cancel_btn:
                    BugSolveDialog.this.dismiss();
                    break;
                case R.id.bug_solve_sure_btn:
                    int solveType = 0;
                    if (mTypeRadioGroup.getCheckedRadioButtonId() == R.id.bug_solve_solve_rbtn)
                        solveType = 0;
                    else
                        solveType = 1;

                    String solveIntroduce = mIntroduceEt.getText().toString().trim();

                    if ("".equals(solveIntroduce)) {
                        Toast.makeText(BugSolveDialog.this.getContext(), "解决注释必填", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String postData = "type=" + solveType + "&introduce=" + solveIntroduce;

                    HttpVisitUtils.postHttpVisit(BugSolveDialog.this.getContext(),
                        LocalInfo.getBaseUrl(BugSolveDialog.this.getContext()) + "bug/solve&bugId=" + bugId, postData,
                        true, "保存中...", onSolveFinishListener);
                    break;
            }
        }
    };

    private HttpVisitUtils.OnHttpFinishListener onSolveFinishListener = new HttpVisitUtils.OnHttpFinishListener() {
        @Override
        public void onVisitFinish(HttpResult result) {
            if (result == null)
                return;
            if (result.isVisitSuccess()) {
                Toast.makeText(BugSolveDialog.this.getContext(), "保存成功", Toast.LENGTH_SHORT).show();
                if (onBugOptListener != null) {
                    onBugOptListener.onSaveSuccessFinish();
                }
                mTypeRadioGroup.check(R.id.bug_solve_solve_rbtn);
                mIntroduceEt.setText("");
                BugSolveDialog.this.dismiss();
            } else {
                HttpConnectResultUtils.optFailure(BugSolveDialog.this.getContext(), result);
            }
        }
    };

    public void setBugId(int bugId) {
        this.bugId = bugId;
    }

    public void setOnBugOptInterface(OnBugOptListener listener) {
        this.onBugOptListener = listener;
    }

}
