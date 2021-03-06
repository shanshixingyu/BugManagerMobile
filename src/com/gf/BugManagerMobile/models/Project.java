package com.gf.BugManagerMobile.models;

/**
 * 项目信息封装
 * Created by Administrator on 5/15 0015.
 */
public class Project {
    private static final String TAG = "Project";

    private int id;
    private String name;
    private int group_id;
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

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
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
        return "Project{" + "id=" + id + ", name='" + name + '\'' + ", group_id=" + group_id + ", creator=" + creator
            + ", create_time='" + create_time + '\'' + ", introduce='" + introduce + '\'' + '}';
    }
}
