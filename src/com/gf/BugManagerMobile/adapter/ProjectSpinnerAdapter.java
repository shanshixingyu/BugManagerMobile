package com.gf.BugManagerMobile.adapter;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.gf.BugManagerMobile.models.Project;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目的下拉列表的适配器
 * Created by GuLang on 2015-06-05.
 */
public class ProjectSpinnerAdapter extends ArrayAdapter<Project> {
    private static final String TAG = "ProjectSpinnerAdapter";

    private LayoutInflater lytInflater;
    private List<Project> mProjectData;

    public ProjectSpinnerAdapter(Context context, List<Project> projectData) {
        super(context, android.R.layout.simple_spinner_item);
        this.lytInflater = LayoutInflater.from(context);
        if (projectData == null)
            mProjectData = new ArrayList<Project>();
        else
            mProjectData = projectData;
    }

    @Override
    public int getCount() {
        if (mProjectData == null)
            mProjectData = new ArrayList<Project>();
        return mProjectData.size();
    }

    @Override
    public Project getItem(int position) {
        if (mProjectData == null)
            mProjectData = new ArrayList<Project>();
        if (position < 0 || position >= mProjectData.size())
            return null;
        return mProjectData.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = lytInflater.inflate(android.R.layout.simple_spinner_item, parent, false);
        }
        final Project project = getItem(position);
        try {
            TextView textView = (TextView) convertView;
            if (project != null)
                textView.setText(project.getName());
        } catch (Exception e) {
        }
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = lytInflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }
        final Project project = getItem(position);
        try {
            TextView textView = (TextView) convertView;
            if (project != null)
                textView.setText(project.getName());
        } catch (Exception e) {
        }
        return convertView;
    }
}
