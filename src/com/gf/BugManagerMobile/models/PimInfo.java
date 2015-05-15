package com.gf.BugManagerMobile.models;

import java.util.Arrays;
import java.util.List;

/**
 * 返回的个人信息
 * Created by Administrator on 5/14 0014.
 */
public class PimInfo {
    private static final String TAG = "PimInfo";

    private User user;
    private List<ProjectModuleInfo> projectModuleData;
    private String roleName;
    private String[] groupNames;
    private String creatorName;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<ProjectModuleInfo> getProjectModuleData() {
        return projectModuleData;
    }

    public void setProjectModuleData(List<ProjectModuleInfo> projectModuleData) {
        this.projectModuleData = projectModuleData;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String[] getGroupNames() {
        return groupNames;
    }

    public void setGroupNames(String[] groupNames) {
        this.groupNames = groupNames;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    @Override
    public String toString() {
        return "PimInfo{" + "user=" + user + ", projectModuleData=" + projectModuleData + ", roleName='" + roleName
            + '\'' + ", groupNames=" + Arrays.toString(groupNames) + ", creatorName='" + creatorName + '\'' + '}';
    }

    public class ProjectModuleInfo {
        private String moduleName;
        private String projectName;

        public String getModuleName() {
            return moduleName;
        }

        public void setModuleName(String moduleName) {
            this.moduleName = moduleName;
        }

        public String getProjectName() {
            return projectName;
        }

        public void setProjectName(String projectName) {
            this.projectName = projectName;
        }

        @Override
        public String toString() {
            return "ProjectModuleInfo{" + "moduleName='" + moduleName + '\'' + ", projectName='" + projectName + '\''
                + '}';
        }
    }

}
