package com.gf.BugManagerMobile.utils;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import com.gf.BugManagerMobile.LoginActivity;
import com.gf.BugManagerMobile.R;
import com.gf.BugManagerMobile.models.HttpResult;

import java.net.HttpURLConnection;

/**
 * http网络连接结果处理
 * Created by Administrator on 5/14 0014.
 */
public class HttpConnectResultUtils {
    private static final String TAG = "HttpConnectUtils";

    public static void optFailure(Context context, HttpResult result) {
        if (result == null)
            return;

        Toast.makeText(context, result.getMessage(), Toast.LENGTH_SHORT).show();
        if (result.getCode() == MyConstant.VISIT_CODE_NO_LOGIN) {
            Intent intent = new Intent(context, LoginActivity.class);
            intent.putExtra(MyConstant.OTHER_ACTIVITY_2_LOGIN_SHOULD_AUTO, false);
            context.startActivity(intent);
        }
    }
}
