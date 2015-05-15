package com.gf.BugManagerMobile.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import com.gf.BugManagerMobile.R;
import com.gf.BugManagerMobile.SubmitBugActivity;

/**
 * 专门为PopupWindow做工具类的
 * Created by Administrator on 5/10 0010.
 */
public class PopWindowUtils {
    private static final String TAG = "PopWindowUtils";

    public static PopupWindow getHomeOptPopWindow(final Context context) {
        final PopupWindow popupWindow = new PopupWindow();
        popupWindow.setWidth(275);
        popupWindow.setHeight(200);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        final View contentView = LayoutInflater.from(context).inflate(R.layout.pop_home_opt, null, false);
        contentView.findViewById(R.id.home_submit_bug).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent submitBugIntent = new Intent(context, SubmitBugActivity.class);
                context.startActivity(submitBugIntent);
                popupWindow.dismiss();
            }
        });
        popupWindow.setContentView(contentView);
        return popupWindow;
    }

}
