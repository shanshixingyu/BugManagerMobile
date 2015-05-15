package com.gf.BugManagerMobile.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.alibaba.fastjson.JSON;
import com.gf.BugManagerMobile.models.HttpResult;
import com.gf.BugManagerMobile.view.DotProgressDialog;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 网络访问工具类
 * Created by Administrator on 5/13 0013.
 */
public class HttpVisitUtils {
    private static final String TAG = "HttpVisitUtils";

    private static final ExecutorService threadPool = Executors.newFixedThreadPool(5);
    private static final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MyConstant.MSG_HTTP_FINISH) {
                DataHolder dataHolder = (DataHolder) msg.obj;
                if (dataHolder != null) {
                    if (dataHolder.dotProgressDialog != null)
                        dataHolder.dotProgressDialog.dismiss();
                    if (dataHolder.onHttpFinishListener != null)
                        dataHolder.onHttpFinishListener.onVisitFinish(dataHolder.httpResult);
                }
            }
        }
    };

    /**
     * post网络请求
     * @param context
     * @param urlPath
     * @param postStr
     * @param isShowDialog
     * @param listener
     */
    public static void postHttpVisit(final Context context, final String urlPath, final String postStr,
        final boolean isShowDialog, final String dialogMessage, final OnHttpFinishListener listener) {
        DotProgressDialog dotProgressDialog = null;
        if (isShowDialog) {
            dotProgressDialog = new DotProgressDialog(context);
            dotProgressDialog.setMessage(dialogMessage);
            dotProgressDialog.show();
        }
        final DotProgressDialog dialog = dotProgressDialog;

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Message httpFinishMessage = Message.obtain();
                httpFinishMessage.what = MyConstant.MSG_HTTP_FINISH;

                try {
                    if (urlPath == null || "".equals(urlPath.trim()))
                        throw new Exception("URL地址不能为空");
                    String postData = postStr;// post主体中传送的数据
                    if (postData == null)
                        postData = "";
                    byte[] postDataBytes = postData.getBytes("UTF-8");
                    URL loginUrl = new URL(urlPath);
                    HttpURLConnection httpConnection = (HttpURLConnection) loginUrl.openConnection();
                    httpConnection.setDoOutput(true);
                    httpConnection.setDoInput(true);
                    httpConnection.setUseCaches(false);
                    httpConnection.setConnectTimeout(5000);
                    httpConnection.setRequestMethod("POST");
                    /* 使用Cookie */
                    useCookie(context, httpConnection);
                    httpConnection.setRequestProperty("Charset", "UTF-8");
                    httpConnection.setRequestProperty("Connection", "Keep-Alive");
                    httpConnection.setRequestProperty("Accept",
                        "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
                    httpConnection.setRequestProperty("Accept-Encoding", "gzip, deflate");
                    httpConnection.setRequestProperty("Content-Length", "" + postDataBytes.length);
                    OutputStream outputStream = httpConnection.getOutputStream();
                    outputStream.write(postDataBytes);
                    outputStream.flush();
                    outputStream.close();
                    int responseCode = httpConnection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        InputStream inputStream = httpConnection.getInputStream();
                        byte[] buffer = new byte[1024];
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        int len;
                        while ((len = inputStream.read(buffer)) != -1) {
                            byteArrayOutputStream.write(buffer, 0, len);
                        }
                        String resultData = new String(byteArrayOutputStream.toByteArray(), "UTF-8");
                        byteArrayOutputStream.close();
                        /* 成功，保存Cookie */
                        saveCookie(context, httpConnection);
                        Log.i(TAG, "post返回的结果:" + resultData);
                        HttpResult httpResult = JSON.parseObject(resultData, HttpResult.class);
                        httpResult.setHttpResponseCode(responseCode);
                        /* 访问正常，需回调返回结果 */
                        httpFinishMessage.obj = new DataHolder(httpResult, listener, dialog);
                    } else {
                        httpFinishMessage.obj =
                            new DataHolder(new HttpResult(MyConstant.VISIT_CODE_NO_OK, responseCode, "状态码不为200", ""),
                                listener, dialog);
                    }
                } catch (SocketTimeoutException e) {
                    httpFinishMessage.obj =
                        new DataHolder(new HttpResult(MyConstant.VISIT_CODE_CONNECT_TIME_OUT, -1, "网络连接超时", ""),
                            listener, dialog);
                } catch (Exception e) {
                    httpFinishMessage.obj =
                        new DataHolder(new HttpResult(MyConstant.VISIT_CODE_CONNECT_ERROR, -1, "网络连接问题", ""), listener,
                            dialog);
                } finally {
                    mHandler.sendMessage(httpFinishMessage);
                }
            }
        };
        threadPool.execute(runnable);
    }

    public static void getHttpVisit(final Context context, final String urlPath, final boolean isShowDialog,
        final String dialogMessage, final HttpVisitUtils.OnHttpFinishListener listener) {
        DotProgressDialog dotProgressDialog = null;
        if (isShowDialog) {
            dotProgressDialog = new DotProgressDialog(context);
            dotProgressDialog.setMessage(dialogMessage);
            dotProgressDialog.show();
        }
        final DotProgressDialog dialog = dotProgressDialog;

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Message httpFinishMessage = Message.obtain();
                httpFinishMessage.what = MyConstant.MSG_HTTP_FINISH;
                try {
                    if (urlPath == null || "".equals(urlPath.trim()))
                        throw new Exception("URL地址不能为空");
                    URL loginUrl = new URL(urlPath);
                    HttpURLConnection httpConnection = (HttpURLConnection) loginUrl.openConnection();
                    httpConnection.setDoInput(true);
                    httpConnection.setUseCaches(false);
                    httpConnection.setConnectTimeout(5000);
                    httpConnection.setRequestMethod("GET");
                    // 使用Cookie
                    useCookie(context, httpConnection);
                    httpConnection.setRequestProperty("Charset", "UTF-8");
                    httpConnection.setRequestProperty("Connection", "Keep-Alive");
                    httpConnection.setRequestProperty("Accept",
                        "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
                    httpConnection.setRequestProperty("Accept-Encoding", "gzip, deflate");
                    int httpResponseCode = httpConnection.getResponseCode();
                    if (httpResponseCode == HttpURLConnection.HTTP_OK) {
                        InputStream inputStream = httpConnection.getInputStream();
                        byte[] buffer = new byte[1024];
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        int len;
                        while ((len = inputStream.read(buffer)) != -1) {
                            byteArrayOutputStream.write(buffer, 0, len);
                        }
                        String resultData = new String(byteArrayOutputStream.toByteArray(), "UTF-8");
                        byteArrayOutputStream.close();
                        /* 成功，保存Cookie */
                        saveCookie(context, httpConnection);
                        HttpResult httpResult = JSON.parseObject(resultData, HttpResult.class);
                        httpResult.setHttpResponseCode(httpResponseCode);
                        /* 访问正常，需回调返回结果 */
                        httpFinishMessage.obj = new DataHolder(httpResult, listener, dialog);
                    } else {
                        httpFinishMessage.obj =
                            new DataHolder(
                                new HttpResult(MyConstant.VISIT_CODE_NO_OK, httpResponseCode, "状态码不是200", ""),
                                listener, dialog);
                    }
                } catch (SocketTimeoutException e) {
                    httpFinishMessage.obj =
                        new DataHolder(new HttpResult(MyConstant.VISIT_CODE_CONNECT_TIME_OUT, -1, "网络连接超时", ""),
                            listener, dialog);
                } catch (Exception e) {
                    httpFinishMessage.obj =
                        new DataHolder(new HttpResult(MyConstant.VISIT_CODE_CONNECT_ERROR, -1, "网络连接问题", ""), listener,
                            dialog);
                } finally {
                    mHandler.sendMessage(httpFinishMessage);
                }
            }
        };
        threadPool.execute(runnable);
    }

    /**
     * 尽可能保存可能存在的Cookie
     * @param context
     * @param httpURLConnection
     */
    private static void saveCookie(Context context, HttpURLConnection httpURLConnection) {
        if (httpURLConnection == null)
            return;
        String cookie = httpURLConnection.getHeaderField("Set-Cookie");
        if (cookie == null)// 不存在这项
            return;
        int startPosition = cookie.indexOf("PHPSESSID=");
        if (startPosition == -1)// 说明不存在PHPSESSID这项
            return;
        int endPosition = cookie.indexOf(";", startPosition);
        if (endPosition == -1)
            endPosition = cookie.length();
        cookie = cookie.substring(startPosition, endPosition);
        // 保存cookie
        SharedPreferenceUtils.save(context, MyConstant.COOKIE, cookie);
    }

    /**
     * 如果暂存的Cookie存在的话，使用配置文件中暂存的Cookie
     * @param context
     * @param httpURLConnection
     */
    private static void useCookie(Context context, HttpURLConnection httpURLConnection) {
        if (httpURLConnection == null)
            return;
        // 取得配置文件中的cookie
        String cookie = SharedPreferenceUtils.queryString(context, MyConstant.COOKIE);
        if (cookie != null && !"".equals(cookie.trim())) {
            httpURLConnection.setRequestProperty("Cookie", cookie);
        }
    }

    /**
     * 保存的对象
     */
    private static class DataHolder {
        public HttpResult httpResult;
        public OnHttpFinishListener onHttpFinishListener;
        public DotProgressDialog dotProgressDialog;

        public DataHolder() {
        }

        public DataHolder(HttpResult httpResult, OnHttpFinishListener listener, DotProgressDialog dialog) {
            this.httpResult = httpResult;
            this.onHttpFinishListener = listener;
            this.dotProgressDialog = dialog;
        }
    }

    /**
     * 回调接口
     */
    public interface OnHttpFinishListener {
        public void onVisitFinish(HttpResult result);
    }

}
