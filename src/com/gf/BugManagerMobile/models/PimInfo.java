package com.gf.BugManagerMobile.models;

import java.util.Arrays;
import java.util.List;

/**
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

    public class User {
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
            return "User{" + "id=" + id + ", role_id=" + role_id + ", email='" + email + '\'' + ", name='" + name
                + '\'' + ", create_time='" + create_time + '\'' + ", creator='" + creator + '\'' + '}';
        }
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
