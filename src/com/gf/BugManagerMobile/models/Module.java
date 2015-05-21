package com.gf.BugManagerMobile.models;

/**
 * 项目模块封装类
 * Created by Administrator on 5/15 0015.
 */
public class Module {
    private static final String TAG = "Module";

    private int id;
    private String name;
    private int project_id;
    private String fuzeren;
    private String introduce;
    private int creator;
    private String create_time;

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

    public int getProject_id() {
        return project_id;
    }

    public void setProject_id(int project_id) {
        this.project_id = project_id;
    }

    public String getFuzeren() {
        return fuzeren;
    }

    public void setFuzeren(String fuzeren) {
        this.fuzeren = fuzeren;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
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

    @Override
    public String toString() {
        return "Module{" + "id=" + id + ", name='" + name + '\'' + ", project_id=" + project_id + ", fuzeren='"
            + fuzeren + '\'' + ", introduce='" + introduce + '\'' + ", creator=" + creator + ", create_time='"
            + create_time + '\'' + '}';
    }
}
