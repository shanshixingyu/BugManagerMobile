package com.gf.BugManagerMobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.gf.BugManagerMobile.R;
import com.gf.BugManagerMobile.models.Project;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目管理适配器
 * Created by GuLang on 2015-05-20.
 */
public class ProjectListAdapter extends BaseAdapter {
    private static final String TAG = "ProjectListAdapter";

    private LayoutInflater lytInflater;
    private List<Project> mProjectDataList;
    private int mPageCount;
    private int mCurrentPage;

    public ProjectListAdapter(Context context, List<Project> data) {
        this.lytInflater = LayoutInflater.from(context);
        if (data == null)
            this.mProjectDataList = new ArrayList<Project>();
        else
            this.mProjectDataList = data;
    }

    @Override
    public int getCount() {
        if (mProjectDataList == null)
            mProjectDataList = new ArrayList<Project>();
        return mProjectDataList.size();
    }

    @Override
    public Object getItem(int position) {
        if (mProjectDataList == null)
            mProjectDataList = new ArrayList<Project>();
        if (position < 0 || position >= mProjectDataList.size())
            return null;
        return mProjectDataList.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = lytInflater.inflate(R.layout.project_list_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.nameTv = (TextView) convertView.findViewById(R.id.project_list_item_name_tv);
            viewHolder.introudceTv = (TextView) convertView.findViewById(R.id.project_list_item_introduce_tv);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Project project = (Project) getItem(position);
        if (project != null) {
            viewHolder.nameTv.setText(project.getName());
            viewHolder.introudceTv.setText(project.getIntroduce());
        }
        return convertView;
    }

    public int getPageCount() {
        return mPageCount;
    }

    public void setPageCount(int mPageCount) {
        this.mPageCount = mPageCount;
    }

    public int getCurrentPage() {
        return mCurrentPage;
    }

    public void setCurrentPage(int mCurrentPage) {
        this.mCurrentPage = mCurrentPage;
    }

    /**
     *重置数据
     * @param data
     */
    public void resetData(List<Project> data) {
        if (mProjectDataList == null)
            mProjectDataList = new ArrayList<Project>();
        else
            mProjectDataList.clear();
        if (data != null)
            mProjectDataList.addAll(data);
        notifyDataSetChanged();
    }

    /**
     * 给适配器设置增加数据
     * @param data
     */
    public void addData(List<Project> data) {
        if (mProjectDataList == null)
            mProjectDataList = new ArrayList<Project>();
        if (data != null)
            mProjectDataList.addAll(data);
        notifyDataSetChanged();
    }

    private class ViewHolder {
        public TextView nameTv;
        public TextView introudceTv;
    }

}
