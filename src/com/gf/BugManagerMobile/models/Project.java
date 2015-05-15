package com.gf.BugManagerMobile.models;

/**
 * 项目信息封装
 * Created by Administrator on 5/15 0015.
 */
public class Project {
    private static final String TAG = "Project";

    private int id;
    private String name;

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

    @Override
    public String toString() {
        return "Project{" + "id=" + id + ", name='" + name + '\'' + '}';
    }
}
