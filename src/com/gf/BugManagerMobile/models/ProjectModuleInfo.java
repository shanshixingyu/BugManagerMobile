package com.gf.BugManagerMobile.models;

import java.util.List;

/**
 * 项目与模块信息的封装
 * Created by Administrator on 5/15 0015.
 */
public class ProjectModuleInfo {
    private static final String TAG = "ProjectModuleInfo";

    private Project project;
    private List<Module> modules;

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public List<Module> getModules() {
        return modules;
    }

    public void setModules(List<Module> modules) {
        this.modules = modules;
    }

    @Override
    public String toString() {
        return "ProjectModuleInfo{" + "project=" + project + ", modules=" + modules + '}';
    }
}
