package com.gf.BugManagerMobile.utils;

import android.content.Context;
import android.widget.Toast;
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
        // if (result.getHttpResponseCode() == HttpURLConnection.HTTP_OK) {
        Toast.makeText(context, result.getMessage(), Toast.LENGTH_SHORT).show();
        // }
        // switch (result.getCode()) {
        // case MyConstant.VISIT_CODE_NO_OK:
        // /* 加载失败失败 */
        // Toast.makeText(context, "状态码不为200", Toast.LENGTH_SHORT).show();
        // break;
        // case MyConstant.VISIT_CODE_CONNECT_TIME_OUT:
        // /* 连接超时 */
        // Toast.makeText(context, R.string.connect_time_out, Toast.LENGTH_SHORT).show();
        // break;
        // default:
        // /* 连接错错误或者其它 */
        // Toast.makeText(context, R.string.connect_error, Toast.LENGTH_SHORT).show();
        // }
    }
}
