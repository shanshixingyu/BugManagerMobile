package com.gf.BugManagerMobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.gf.BugManagerMobile.models.Module;

import java.util.ArrayList;
import java.util.List;

/**
 * 模块的Spinner适配器
 * Created by Administrator on 5/15 0015.
 */
public class ModuleSpinnerAdapter extends ArrayAdapter<Module> {
    private static final String TAG = "ModuleSpinnerAdapter";

    private LayoutInflater lytInflate;
    private List<Module> moduleListData = null;
    private Module module = null;

    public ModuleSpinnerAdapter(Context context, List<Module> data) {
        super(context, android.R.layout.simple_spinner_item);
        this.lytInflate = LayoutInflater.from(context);
        this.moduleListData = data;
        if (moduleListData == null)
            this.moduleListData = new ArrayList<Module>();
        module = new Module();
        module.setId(-1);
        module.setName("全部");
        moduleListData.add(0, module);
    }

    @Override
    public int getCount() {
        if (moduleListData == null) {
            this.moduleListData = new ArrayList<Module>();
            moduleListData.add(0, module);
        }
        return moduleListData.size();
    }

    @Override
    public Module getItem(int position) {
        if (moduleListData == null) {
            this.moduleListData = new ArrayList<Module>();
            moduleListData.add(0, module);
        }
        if (position < 0 || position >= moduleListData.size())
            return null;
        return moduleListData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        final Module itemModule = getItem(position);
        if (convertView == null) {
            convertView = lytInflate.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }
        try {
            TextView textView = (TextView) convertView;
            if (itemModule != null)
                textView.setText(itemModule.getName());
        } catch (Exception e) {
        }
        return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Module itemModule = getItem(position);
        if (convertView == null) {
            convertView = lytInflate.inflate(android.R.layout.simple_spinner_item, parent, false);
        }
        try {
            TextView textView = (TextView) convertView;
            if (itemModule != null)
                textView.setText(itemModule.getName());
        } catch (Exception e) {
        }
        return convertView;
    }
}
