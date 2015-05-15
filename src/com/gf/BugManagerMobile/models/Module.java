package com.gf.BugManagerMobile.models;

/**
 * Created by Administrator on 5/15 0015.
 */
public class Module {
    private static final String TAG = "Module";

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
        return "Module{" + "id=" + id + ", name='" + name + '\'' + '}';
    }
}
