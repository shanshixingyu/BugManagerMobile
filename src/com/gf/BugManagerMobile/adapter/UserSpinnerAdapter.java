package com.gf.BugManagerMobile.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.gf.BugManagerMobile.models.User;

/**
 * 用户的Spinner适配器
 * Created by Administrator on 5/15 0015.
 */
public class UserSpinnerAdapter extends ArrayAdapter<User> {
    private static final String TAG = "UserSpinnerAdapter";

    private LayoutInflater lytInflate;
    private List<User> userListData = null;

    public UserSpinnerAdapter(Context context, List<User> data) {
        super(context, android.R.layout.simple_spinner_item);
        this.lytInflate = LayoutInflater.from(context);
        this.userListData = data;
        if (userListData == null)
            this.userListData = new ArrayList<User>();
    }

    @Override
    public int getCount() {
        if (userListData == null)
            this.userListData = new ArrayList<User>();
        return userListData.size();
    }

    @Override
    public User getItem(int position) {
        if (userListData == null)
            this.userListData = new ArrayList<User>();
        if (position < 0 || position >= userListData.size())
            return null;
        return userListData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        final User itemUser = getItem(position);
        if (convertView == null) {
            convertView = lytInflate.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }
        try {
            TextView textView = (TextView) convertView;
            if (itemUser != null)
                textView.setText(itemUser.getName());
        } catch (Exception e) {
        }
        return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final User itemUser = getItem(position);
        if (convertView == null) {
            convertView = lytInflate.inflate(android.R.layout.simple_spinner_item, parent, false);
        }
        try {
            TextView textView = (TextView) convertView;
            if (itemUser != null)
                textView.setText(itemUser.getName());
        } catch (Exception e) {
        }
        return convertView;
    }
}
