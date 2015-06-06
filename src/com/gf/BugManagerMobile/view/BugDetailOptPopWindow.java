package com.gf.BugManagerMobile.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.gf.BugManagerMobile.R;
import com.gf.BugManagerMobile.utils.SizeUtils;

/**
 * Bug详细页的的操作弹窗
 * Created by GuLang on 2015-05-18.
 */
public class BugDetailOptPopWindow extends PopupWindow {
    private static final String TAG = "BugDetailOptPopWindow";

    private OnOptItemClickListener onOptItemClickListener;
    private TextView solveTv, modifyTv, activeTv, closeTv, deleteTv;

    public BugDetailOptPopWindow(Context context) {
        this(context, null);
    }

    public BugDetailOptPopWindow(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BugDetailOptPopWindow(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setWidth(SizeUtils.dp2px(context, 90));
        setHeight(SizeUtils.dp2px(context, 230));
        View convertView = LayoutInflater.from(context).inflate(R.layout.bug_detail_opt_pop, null, false);
        setContentView(convertView);
        setFocusable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new BitmapDrawable());

        // solveTv, modifyTv, activeTv, closeTv, deleteTv;

        solveTv = (TextView) convertView.findViewById(R.id.bug_detail_opt_solve_tv);
        modifyTv = (TextView) convertView.findViewById(R.id.bug_detail_opt_modify_tv);
        activeTv = (TextView) convertView.findViewById(R.id.bug_detail_opt_active_tv);
        closeTv = (TextView) convertView.findViewById(R.id.bug_detail_opt_close_tv);
        deleteTv = (TextView) convertView.findViewById(R.id.bug_detail_opt_delete_tv);

        solveTv.setOnClickListener(onClickListener);
        modifyTv.setOnClickListener(onClickListener);
        activeTv.setOnClickListener(onClickListener);
        closeTv.setOnClickListener(onClickListener);
        deleteTv.setOnClickListener(onClickListener);

    }

    /**
     * 设置某一项是否可操作
     * @param itemType
     * @param isEnable
     */
    public void setPopItemStatus(ItemType itemType, boolean isEnable) {
        TextView textView = null;
        if (itemType == ItemType.Solve) {
            textView = solveTv;
        } else if (itemType == ItemType.Modify) {
            textView = modifyTv;
        } else if (itemType == ItemType.Active) {
            textView = activeTv;
        } else if (itemType == ItemType.Close) {
            textView = closeTv;
        } else if (itemType == ItemType.Delete) {
            textView = deleteTv;
        }

        if (textView != null) {
            textView.setEnabled(isEnable);
            if (isEnable)
                textView.setTextColor(Color.WHITE);
            else
                textView.setTextColor(Color.parseColor("#ffbcbcbc"));
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.bug_detail_opt_solve_tv:
                    if (onOptItemClickListener != null)
                        onOptItemClickListener.onClick(ItemType.Solve);
                    break;
                case R.id.bug_detail_opt_modify_tv:
                    if (onOptItemClickListener != null)
                        onOptItemClickListener.onClick(ItemType.Modify);
                    break;
                case R.id.bug_detail_opt_active_tv:
                    if (onOptItemClickListener != null)
                        onOptItemClickListener.onClick(ItemType.Active);
                    break;
                case R.id.bug_detail_opt_close_tv:
                    if (onOptItemClickListener != null)
                        onOptItemClickListener.onClick(ItemType.Close);
                    break;
                case R.id.bug_detail_opt_delete_tv:
                    if (onOptItemClickListener != null)
                        onOptItemClickListener.onClick(ItemType.Delete);
                    break;
            }
            BugDetailOptPopWindow.this.dismiss();
        }
    };

    public enum ItemType {
        Solve, Modify, Active, Close, Delete
    }

    public void setOnOptItemClickListener(OnOptItemClickListener listener) {
        this.onOptItemClickListener = listener;
    }

    public interface OnOptItemClickListener {
        public void onClick(ItemType itemType);
    }
}
