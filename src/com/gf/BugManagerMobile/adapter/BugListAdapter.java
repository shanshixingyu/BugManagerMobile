package com.gf.BugManagerMobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.gf.BugManagerMobile.R;
import com.gf.BugManagerMobile.models.Bug;
import com.gf.BugManagerMobile.utils.BugUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * bug列表适配器
 * Created by Administrator on 5/16 0016.
 */
public class BugListAdapter extends BaseAdapter {
    private static final String TAG = "BugListAdapter";

    private LayoutInflater lytInflater;
    private List<Bug> bugDataList;

    public BugListAdapter(Context context, List<Bug> data) {
        lytInflater = LayoutInflater.from(context);
        if (data == null)
            bugDataList = new ArrayList<Bug>();
        else
            bugDataList = data;
    }

    @Override
    public int getCount() {
        if (bugDataList == null)
            bugDataList = new ArrayList<Bug>();
        return bugDataList.size();
    }

    @Override
    public Object getItem(int position) {
        if (bugDataList == null)
            bugDataList = new ArrayList<Bug>();
        if (position < 0 || position >= bugDataList.size())
            return null;
        return bugDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = lytInflater.inflate(R.layout.bug_list_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.nameTv = (TextView) convertView.findViewById(R.id.bug_list_item_name);
            viewHolder.priorityTv = (TextView) convertView.findViewById(R.id.bug_list_item_priority_tv);
            viewHolder.statusTv = (TextView) convertView.findViewById(R.id.bug_list_item_status_tv);
            viewHolder.seriousTv = (TextView) convertView.findViewById(R.id.bug_list_item_serious_tv);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final Bug bug = bugDataList.get(position);

        viewHolder.nameTv.setText(bug.getName());
        viewHolder.priorityTv.setText(BugUtils.getPriorityStr(bug.getPriority()));
        viewHolder.priorityTv.setTextColor(BugUtils.getPriorityColor(bug.getPriority()));
        viewHolder.statusTv.setText(BugUtils.getStatusStr(bug.getStatus()));
        viewHolder.statusTv.setTextColor(BugUtils.getStatusColor(bug.getStatus()));
        viewHolder.seriousTv.setText(BugUtils.getSeriousStr(bug.getSerious_id()));

        return convertView;
    }

    /**
     * 重置数据
     * @param data
     */
    public void resetData(List<Bug> data) {
        if (this.bugDataList == null)
            this.bugDataList = new ArrayList<Bug>();
        this.bugDataList.clear();
        if (data != null)
            this.bugDataList.addAll(data);
        notifyDataSetChanged();
    }

    /**
     * 添加数据
     * @param data
     */
    public void addData(List<Bug> data) {
        if (this.bugDataList == null)
            this.bugDataList = new ArrayList<Bug>();
        if (data != null && data.size() > 0)
            this.bugDataList.addAll(data);
        notifyDataSetChanged();
    }

    private class ViewHolder {
        public TextView nameTv;
        public TextView priorityTv;
        public TextView statusTv;
        public TextView seriousTv;
    }

}
