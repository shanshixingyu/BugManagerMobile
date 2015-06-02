package com.gf.BugManagerMobile.models;

import java.util.List;

/**
 * 用户团队信息
 * Created by GuLang on 2015-05-20.
 */
public class Group {
    private static final String TAG = "Group";

    private int id;
    private String name;
    private String member;
    private int creator;
    private String create_time;
    private String introduce;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public int getCreator() {
        return creator;
    }

    public void setCreator(int creator) {
        this.creator = creator;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    @Override
    public String toString() {
        return "Group{" + "id=" + id + ", name='" + name + '\'' + ", member=" + member + ", creator=" + creator
            + ", create_time='" + create_time + '\'' + ", introduce='" + introduce + '\'' + '}';
    }
}
