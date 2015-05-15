package com.gf.BugManagerMobile.models;

import android.content.Context;
import com.gf.BugManagerMobile.R;

import java.util.List;

/**
 * 项目bug情况表
 * Created by Administrator on 5/13 0013.
 */
public class ProjectBugOverview {
    private static final String TAG = "BugOverview";

    private int pageCount;
    private int currentPage;
    private List<ProjectBugInfo> data;

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public List<ProjectBugInfo> getData() {
        return data;
    }

    public void setData(List<ProjectBugInfo> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "BugOverviewResult{" + "pageCount=" + pageCount + ", currentPage=" + currentPage + ", data=" + data
            + '}';
    }

    /**
     * 项目bug概况
     */
    public class ProjectBugInfo {
        private static final String DIVIDER_SPACE = "   |   ";
        /**
         * 项目Id
         */
        private int projectId;
        /**
         * 项目名称
         */
        private String projectName;
        /**
         * 项目bug数量
         */
        private int projectBugCount;
        /**
         * 项目关闭bug数量
         */
        private int bugStatusClosed;
        /**
         * 项目未解决bug数量
         */
        private int bugStatusUnsolved;
        /**
         * 项目已解决bug数量
         */
        private int bugStatusSolved;
        /**
         * 项目重新激活bug数量
         */
        private int bugStatusActive;
        /**
         * 项目bug的临时信息变量
         */
        private String tempBugInfo;

        public int getProjectId() {
            return projectId;
        }

        public void setProjectId(int projectId) {
            this.projectId = projectId;
        }

        public String getProjectName() {
            return projectName;
        }

        public void setProjectName(String projectName) {
            this.projectName = projectName;
        }

        public int getProjectBugCount() {
            return projectBugCount;
        }

        public void setProjectBugCount(int projectBugCount) {
            this.projectBugCount = projectBugCount;
        }

        public int getBugStatusClosed() {
            return bugStatusClosed;
        }

        public void setBugStatusClosed(int bugStatusClosed) {
            this.bugStatusClosed = bugStatusClosed;
        }

        public int getBugStatusUnsolved() {
            return bugStatusUnsolved;
        }

        public void setBugStatusUnsolved(int bugStatusUnsolved) {
            this.bugStatusUnsolved = bugStatusUnsolved;
        }

        public int getBugStatusSolved() {
            return bugStatusSolved;
        }

        public void setBugStatusSolved(int bugStatusSolved) {
            this.bugStatusSolved = bugStatusSolved;
        }

        public int getBugStatusActive() {
            return bugStatusActive;
        }

        public void setBugStatusActive(int bugStatusActive) {
            this.bugStatusActive = bugStatusActive;
        }

        /**
         * 获得临时变量
         */
        public String getTempBugInfo(Context context) {
            if (tempBugInfo == null) {
                StringBuilder strBuilder = new StringBuilder();
                // strBuilder.append(context.getString(R.string.home_bug_item_total));
                // strBuilder.append(projectBugCount);
                // strBuilder.append(DIVIDER_SPACE);
                strBuilder.append(context.getString(R.string.home_bug_item_unsolved));
                strBuilder.append(bugStatusUnsolved);
                strBuilder.append(DIVIDER_SPACE);
                strBuilder.append(context.getString(R.string.home_bug_item_active));
                strBuilder.append(bugStatusActive);
                strBuilder.append(DIVIDER_SPACE);
                strBuilder.append(context.getString(R.string.home_bug_item_solved));
                strBuilder.append(bugStatusSolved);
                strBuilder.append(DIVIDER_SPACE);
                strBuilder.append(context.getString(R.string.home_bug_item_closed));
                strBuilder.append(bugStatusClosed);
                tempBugInfo = strBuilder.toString();
            }
            return tempBugInfo;
        }

        @Override
        public String toString() {
            return "ProjectBugInfo{" + "projectId=" + projectId + ", projectName='" + projectName + '\''
                + ", projectBugCount=" + projectBugCount + ", bugStatusClosed=" + bugStatusClosed
                + ", bugStatusUnsolved=" + bugStatusUnsolved + ", bugStatusSolved=" + bugStatusSolved
                + ", bugStatusActive=" + bugStatusActive + ", tempBugInfo='" + tempBugInfo + '\'' + '}';
        }
    }

}
