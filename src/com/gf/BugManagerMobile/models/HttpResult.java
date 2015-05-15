package com.gf.BugManagerMobile.models;

import com.gf.BugManagerMobile.utils.MyConstant;

import java.net.HttpURLConnection;

/**
 * Http网络访问获取的结果
 * Created by Administrator on 5/13 0013.
 */
public class HttpResult {
    private static final String TAG = "Result";

    private int code;
    private int httpResponseCode;
    private String message;
    private String result;

    public HttpResult() {
    }

    public HttpResult(int code, int httpResponseCode, String message, String result) {
        this.code = code;
        this.httpResponseCode = httpResponseCode;
        this.message = message;
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getHttpResponseCode() {
        return httpResponseCode;
    }

    public void setHttpResponseCode(int httpResponseCode) {
        this.httpResponseCode = httpResponseCode;
    }

    /**
     * 是否访问成功,并且获得正确的字符串
     * @return
     */
    public boolean isVisitSuccess() {
        if (this.code == MyConstant.VISIT_CODE_SUCCESS && this.httpResponseCode == HttpURLConnection.HTTP_OK)
            return true;
        else
            return false;
    }

    @Override
    public String toString() {
        return "HttpResult{" + "code=" + code + ", httpResponseCode=" + httpResponseCode + ", message='" + message
            + '\'' + ", result='" + result + '\'' + '}';
    }
}
