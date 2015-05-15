package com.gf.BugManagerMobile.models;

/**
 * 用户信息
 * Created by Administrator on 5/15 0015.
 */
public class User {
    private static final String TAG = "User";

    private int id;
    private int role_id;
    private String email;
    private String name;
    private String create_time;
    private String creator;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRole_id() {
        return role_id;
    }

    public void setRole_id(int role_id) {
        this.role_id = role_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", role_id=" + role_id + ", email='" + email + '\'' + ", name='" + name + '\''
            + ", create_time='" + create_time + '\'' + ", creator='" + creator + '\'' + '}';
    }
}
