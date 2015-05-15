package com.gf.BugManagerMobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import com.gf.BugManagerMobile.R;
import com.gf.BugManagerMobile.models.Module;
import com.gf.BugManagerMobile.models.Project;
import com.gf.BugManagerMobile.models.ProjectModuleInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目与模块的界面下拉菜单的适配器
 * Created by Administrator on 5/14 0014.
 */
public class ProjectModuleAdapter extends BaseExpandableListAdapter {
    private static final String TAG = "ProjectModuleAdapter";

    private LayoutInflater lytInflater;
    private List<ProjectModuleInfo> projectModuleData;

    public ProjectModuleAdapter(Context context) {
        this.lytInflater = LayoutInflater.from(context);
        this.projectModuleData = new ArrayList<ProjectModuleInfo>();
    }

    @Override
    public int getGroupCount() {
        if (projectModuleData == null)
            projectModuleData = new ArrayList<ProjectModuleInfo>();
        return projectModuleData.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (projectModuleData == null)
            projectModuleData = new ArrayList<ProjectModuleInfo>();
        int groupCount = projectModuleData.size();
        if (groupPosition < 0 || groupPosition >= groupCount)
            return 0;
        List<Module> projectModules = projectModuleData.get(groupPosition).getModules();
        if (projectModules == null)
            return 0;
        return projectModules.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        if (projectModuleData == null)
            projectModuleData = new ArrayList<ProjectModuleInfo>();
        int groupCount = projectModuleData.size();
        if (groupPosition < 0 || groupPosition >= groupCount)
            return null;
        return projectModuleData.get(groupPosition).getProject();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        if (projectModuleData == null)
            projectModuleData = new ArrayList<ProjectModuleInfo>();
        int groupCount = projectModuleData.size();
        if (groupPosition < 0 || groupPosition >= groupCount)
            return null;
        List<Module> projectModules = projectModuleData.get(groupPosition).getModules();
        if (projectModules == null)
            return null;
        int childCount = projectModules.size();
        if (childPosition < 0 || childPosition >= childCount)
            return null;
        return projectModules.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder groupViewHolder = null;
        if (convertView == null) {
            convertView = this.lytInflater.inflate(R.layout.project_module_group_item, parent, false);
            groupViewHolder = new GroupViewHolder();
            groupViewHolder.textView = (TextView) convertView.findViewById(R.id.project_module_group_tv);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        final Project project = (Project) getGroup(groupPosition);
        if (project != null)
            groupViewHolder.textView.setText(project.getName());
        else
            groupViewHolder.textView.setText("");
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
        ViewGroup parent) {
        ChildViewHolder childViewHolder = null;
        if (convertView == null) {
            convertView = this.lytInflater.inflate(R.layout.project_module_child_item, parent, false);
            childViewHolder = new ChildViewHolder();
            childViewHolder.textView = (TextView) convertView.findViewById(R.id.project_module_child_tv);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        final Module module = (Module) getChild(groupPosition, childPosition);
        if (module != null)
            childViewHolder.textView.setText(module.getName());
        else
            childViewHolder.textView.setText("");
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    public void setData(List<ProjectModuleInfo> data) {
        if (projectModuleData == null)
            projectModuleData = new ArrayList<ProjectModuleInfo>();
        projectModuleData.clear();
        if (data != null && data.size() > 0)
            projectModuleData.addAll(data);
        notifyDataSetChanged();
    }

    private class GroupViewHolder {
        public TextView textView;
    }

    private class ChildViewHolder {
        public TextView textView;
    }

}
