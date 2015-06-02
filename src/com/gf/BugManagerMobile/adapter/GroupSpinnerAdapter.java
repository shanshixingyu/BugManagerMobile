package com.gf.BugManagerMobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.gf.BugManagerMobile.R;
import com.gf.BugManagerMobile.models.Group;
import com.gf.BugManagerMobile.models.Module;

import java.util.ArrayList;
import java.util.List;

/**
 * 团队下拉菜单的适配器
 * Created by GuLang on 2015-06-02.
 */
public class GroupSpinnerAdapter extends ArrayAdapter<Group> {
    private static final String TAG = "GroupSpinnerAdapter";

    private LayoutInflater lytInflate;
    private List<Group> mGroupDataList;

    public GroupSpinnerAdapter(Context context, List<Group> data) {
        super(context, android.R.layout.simple_spinner_item);
        this.lytInflate = LayoutInflater.from(context);
        this.mGroupDataList = data;
        if (mGroupDataList == null)
            this.mGroupDataList = new ArrayList<Group>();
    }

    @Override
    public int getCount() {
        if (mGroupDataList == null)
            mGroupDataList = new ArrayList<Group>();
        return mGroupDataList.size();
    }

    @Override
    public Group getItem(int position) {
        if (mGroupDataList == null)
            mGroupDataList = new ArrayList<Group>();
        if (position < 0 || position >= mGroupDataList.size())
            return null;
        return mGroupDataList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = lytInflate.inflate(android.R.layout.simple_spinner_item, parent, false);
        }
        final Group group = getItem(position);
        try {
            TextView textView = (TextView) convertView;
            if (group != null)
                textView.setText(group.getName());
        } catch (Exception e) {
        }
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = lytInflate.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }
        final Group group = getItem(position);
        try {
            TextView textView = (TextView) convertView;
            if (group != null)
                textView.setText(group.getName());
        } catch (Exception e) {
        }
        return convertView;
    }

}
