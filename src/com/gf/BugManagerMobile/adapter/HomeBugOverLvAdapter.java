package com.gf.BugManagerMobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.gf.BugManagerMobile.R;
import com.gf.BugManagerMobile.models.ProjectBugOverview;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页中的bug概况中的列表适配器
 * Created by Administrator on 5/13 0013.
 */
public class HomeBugOverLvAdapter extends BaseAdapter {
    private static final String TAG = "HomeBugOverLvAdapter";

    private Context context;
    private LayoutInflater lytInflater;
    private List<ProjectBugOverview.ProjectBugInfo> dataList = null;

    public HomeBugOverLvAdapter(Context context) {
        this.context = context;

        dataList = new ArrayList<ProjectBugOverview.ProjectBugInfo>();

        lytInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (dataList == null) {
            dataList = new ArrayList<ProjectBugOverview.ProjectBugInfo>();
        }
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        if (dataList == null) {
            dataList = new ArrayList<ProjectBugOverview.ProjectBugInfo>();
        }
        if (position < 0 || position >= dataList.size())
            return null;
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ProjectBugOverview.ProjectBugInfo itemInfo = dataList.get(position);
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = lytInflater.inflate(R.layout.project_bug_overview_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.nameTv = (TextView) convertView.findViewById(R.id.home_bug_item_name_tv);
            viewHolder.totalTv = (TextView) convertView.findViewById(R.id.home_bug_item_total_tv);
            viewHolder.otherTv = (TextView) convertView.findViewById(R.id.home_bug_item_other_tv);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.nameTv.setText(itemInfo.getProjectName());
        viewHolder.totalTv.setText(context.getString(R.string.home_bug_item_total) + itemInfo.getProjectBugCount());
        viewHolder.otherTv.setText(itemInfo.getTempBugInfo(context));

        return convertView;
    }

    public void addProjectBugInfo(List<ProjectBugOverview.ProjectBugInfo> data) {
        if (data == null || data.size() <= 0)
            return;
        this.dataList.addAll(data);
        notifyDataSetChanged();
    }

    private class ViewHolder {
        public TextView nameTv;
        public TextView totalTv;
        public TextView otherTv;

    }

}
