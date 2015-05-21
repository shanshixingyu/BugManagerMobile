package com.gf.BugManagerMobile.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 用户登录成功后的用户信息
 * Created by Administrator on 5/13 0013.
 */
public class LoginSuccessInfo implements Parcelable {
    private static final String TAG = "LoginSucessInfo";

    private int userId;
    private String userName;
    private String roleName;
    private String password;
    private int roleId;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(userId);
        dest.writeString(userName);
        dest.writeString(roleName);
        dest.writeString(password);
        dest.writeInt(roleId);
    }

    public Parcelable.Creator<LoginSuccessInfo> CREATOR = new Parcelable.Creator<LoginSuccessInfo>() {
        @Override
        public LoginSuccessInfo[] newArray(int size) {
            return new LoginSuccessInfo[size];
        }

        @Override
        public LoginSuccessInfo createFromParcel(Parcel source) {
            LoginSuccessInfo loginSuccessInfo = new LoginSuccessInfo();
            loginSuccessInfo.setUserId(source.readInt());
            loginSuccessInfo.setUserName(source.readString());
            loginSuccessInfo.setRoleName(source.readString());
            loginSuccessInfo.setPassword(source.readString());
            loginSuccessInfo.setRoleId(source.readInt());
            return loginSuccessInfo;
        }
    };

    @Override
    public String toString() {
        return "LoginSuccessInfo{" + "userId=" + userId + ", userName='" + userName + '\'' + ", roleName='" + roleName
            + '\'' + ", password='" + password + '\'' + ", roleId=" + roleId + ", CREATOR=" + CREATOR + '}';
    }
}
