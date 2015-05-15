package com.gf.BugManagerMobile.models;

/**
 * Created by Administrator on 5/15 0015.
 */
public class SearchBugCondition {
    private static final String TAG = "BugSearchCondition";

    private int projectId;
    private int moduleId;
    private int priority;
    private int serious;
    private int assign;
    private String submit;
    private int status;
    private String keyWord;

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setSerious(int serious) {
        this.serious = serious;
    }

    public void setAssign(int assign) {
        this.assign = assign;
    }

    public void setSubmit(String submit) {
        this.submit = submit;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    /**
     * 获得查询条件
     * @return
     */
    public String getRequestCondition() {
        String condition = "projectId=" + projectId;
        if (moduleId > 0)
            condition += "&moduleId=" + moduleId;
        --priority;
        if (priority >= 0)
            condition += "&priority=" + priority;
        --serious;
        if (serious >= 0)
            condition += "&serious=" + serious;
        if (assign > 0)
            condition += "&assign=" + assign;
        if (submit != null && !"".equals(submit.trim()))
            condition += "&submit=" + submit;
        --status;
        if (status >= 0)
            condition += "&status=" + status;
        return condition;
    }

    @Override
    public String toString() {
        return "BugSearchCondition{" + "projectId=" + projectId + ", moduleId=" + moduleId + ", priority=" + priority
            + ", serious=" + serious + ", assign=" + assign + ", submit='" + submit + '\'' + ", status=" + status
            + ", keyWord='" + keyWord + '\'' + '}';
    }
}
