package com.gf.BugManagerMobile.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import com.gf.BugManagerMobile.R;
import com.gf.BugManagerMobile.SubmitBugActivity;
import com.gf.BugManagerMobile.utils.SizeUtils;

/**
 * 主界面中的添加界面弹窗
 * Created by Administrator on 5/10 0010.
 */
public class BugSubmitPopWindow extends PopupWindow {
    private static final String TAG = "BugSubmitPopWindow";

    private OnItemClickListener onItemClickListener;

    public BugSubmitPopWindow(Context context) {
        this(context, null);
    }

    public BugSubmitPopWindow(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BugSubmitPopWindow(final Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setWidth(SizeUtils.dp2px(context, 135));
        setHeight(SizeUtils.dp2px(context, 100));
        setFocusable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new BitmapDrawable());
        final View contentView = LayoutInflater.from(context).inflate(R.layout.pop_home_opt, null, false);
        contentView.findViewById(R.id.home_submit_bug).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick();
                }
                dismiss();
            }
        });
        setContentView(contentView);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public interface OnItemClickListener {
        public void onItemClick();
    }

}
