package com.gf.BugManagerMobile.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;
import com.gf.BugManagerMobile.models.LoginSuccessInfo;

import java.io.File;

/**
 * 本次运行系统需要的静态变量
 * Created by Administrator on 5/13 0013.
 */
public class LocalInfo {
    private static final String TAG = "LocalInfo";
    /**
     * 获得登录用户信息的锁
     */
    private static final Object mLoginSuccessInfoLock = new Object();

    /**
     * 登录用户信息封装实例
     */
    private static LoginSuccessInfo mLoginSuccessInfo = null;

    /**
     * 设置登录用户信息
     *
     * @param mLoginSuccessInfo
     */
    public static void setLoginSuccessInfo(LoginSuccessInfo mLoginSuccessInfo) {
        synchronized (mLoginSuccessInfoLock) {
            LocalInfo.mLoginSuccessInfo = mLoginSuccessInfo;
        }
    }

    /**
     * 获得登录用户信息
     *
     * @param context
     * @return
     */
    public static LoginSuccessInfo getLoginSuccessInfo(Context context) {
        if (mLoginSuccessInfo == null) {
            synchronized (mLoginSuccessInfoLock) {
                if (mLoginSuccessInfo == null) {
                    // 从配置文件中获取
                    mLoginSuccessInfo = new LoginSuccessInfo();
                    mLoginSuccessInfo.setUserId(SharedPreferenceUtils.queryInt(context, MyConstant.SP_LOGIN_USE_ID));
                    mLoginSuccessInfo.setUserName(SharedPreferenceUtils.queryString(context,
                        MyConstant.SP_LOGIN_USE_NAME));
                    mLoginSuccessInfo.setRoleName(SharedPreferenceUtils.queryString(context,
                        MyConstant.SP_LOGIN_ROLE_NAME));
                    mLoginSuccessInfo.setPassword(SharedPreferenceUtils.queryString(context,
                        MyConstant.SP_LOGIN_PASSWORD));
                    mLoginSuccessInfo.setRoleId(SharedPreferenceUtils.queryInt(context, MyConstant.SP_LOGIN_ROLE_ID));
                }
            }
        }
        return mLoginSuccessInfo;
    }

    /**
     * 基本URL地址
     */
    private static String mBaseUrl = null;
    private static final Object mBaseURLLock = new Object();

    /**
     * 获取基本的URL地址
     *
     * @param context
     * @return
     */
    public static String getBaseUrl(Context context) {
        if (mBaseUrl == null) {
            synchronized (mBaseURLLock) {
                if (mBaseUrl == null) {
                    // 从配置文件中获得URL地址
                    String serverIp = SharedPreferenceUtils.queryString(context, MyConstant.SERVER_IP);
                    String serverPort = SharedPreferenceUtils.queryString(context, MyConstant.SERVER_PORT);
                    if (serverPort == null || "".equals(serverPort.trim())) {
                        mBaseUrl = "http://" + serverIp + "/BugManagerWeb/web/index.php?r=api/";
                    } else {
                        mBaseUrl = "http://" + serverIp + ":" + serverPort + "/BugManagerWeb/web/index.php?r=api/";
                    }
                }
            }
        }
        return mBaseUrl;
    }

    /**
     * 基本URL地址
     */
    private static String mWebUrl = null;
    private static final Object mWebURLLock = new Object();

    /**
     * 获得web目录的路径
     *
     * @param context
     * @return
     */
    public static String getWebUrl(Context context) {
        if (mWebUrl == null) {
            synchronized (mWebURLLock) {
                if (mWebUrl == null) {
                    // 从配置文件中获得URL地址
                    String serverIp = SharedPreferenceUtils.queryString(context, MyConstant.SERVER_IP);
                    String serverPort = SharedPreferenceUtils.queryString(context, MyConstant.SERVER_PORT);
                    if (serverPort == null || "".equals(serverPort.trim())) {
                        mWebUrl = "http://" + serverIp + "/BugManagerWeb/web/";
                    } else {
                        mWebUrl = "http://" + serverIp + ":" + serverPort + "/BugManagerWeb/web/";
                    }
                }
            }
        }
        return mWebUrl;
    }

    /**
     * 获得截图路径
     *
     * @param context
     * @return
     */
    public static String getBufferImageFileUrl(Context context) {
        String baseWebPath = getWebUrl(context);
        return baseWebPath + "BufferFile/images/";
    }

    /**
     * 获得附件路径
     * @param context
     * @return
     */
    public static String getAttachmentFileUrl(Context context) {
        String baseWebPath = getWebUrl(context);
        return baseWebPath + "BufferFile/attachments/";
    }

    /**
     * 获得附件路径
     *
     * @param context
     * @return
     */
    public static String getBufferAttachmentFileUrl(Context context) {
        String baseWebPath = getWebUrl(context);
        return baseWebPath + "BufferFile/attachments/";
    }

    /**
     * 获得下载附件在本地的保存路径
     * @return
     * @throws IllegalStateException 
     */
    public static String getAttachmentSavePath()
        throws IllegalStateException {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            throw new IllegalStateException("存储出现问题");
        String dirPath = Environment.getExternalStorageDirectory() + "/BugManagerMobile/download/attachment/";
        File directionFile = new File(dirPath);
        if (!directionFile.exists()) {
            boolean success = directionFile.mkdirs();
            Log.i(TAG, "目录不存在，并且创建目录结果为：" + success);
        }
        return dirPath;
    }
}
