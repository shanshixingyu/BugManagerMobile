package com.gf.BugManagerMobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.gf.BugManagerMobile.R;
import com.gf.BugManagerMobile.models.BugIntroduceItem;
import com.gf.BugManagerMobile.utils.LocalInfo;
import com.gf.BugManagerMobile.models.LoginSuccessInfo;
import java.util.ArrayList;
import java.util.List;

/**
 * bug注释界面适配器
 * Created by GuLang on 2015-05-17.
 */
public class BugIntroduceLvAdapter extends BaseAdapter {
    private static final String TAG = "BugIntroduceLvAdapter";

    private LayoutInflater lytInflater;
    private List<BugIntroduceItem> mBugIntroduceItemList;
    private LoginSuccessInfo loginSuccessInfo;

    public BugIntroduceLvAdapter(Context context, List<BugIntroduceItem> data) {
        this.lytInflater = LayoutInflater.from(context);
        if (data == null)
            this.mBugIntroduceItemList = new ArrayList<BugIntroduceItem>();
        else
            this.mBugIntroduceItemList = data;
        this.loginSuccessInfo = LocalInfo.getLoginSuccessInfo(context);
    }

    @Override
    public int getCount() {
        if (mBugIntroduceItemList == null)
            mBugIntroduceItemList = new ArrayList<BugIntroduceItem>();
        return mBugIntroduceItemList.size();
    }

    @Override
    public Object getItem(int position) {
        if (mBugIntroduceItemList == null)
            mBugIntroduceItemList = new ArrayList<BugIntroduceItem>();
        if (position < 0 || position >= mBugIntroduceItemList.size())
            return null;
        return mBugIntroduceItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        final BugIntroduceItem item = (BugIntroduceItem) getItem(position);
        if (item != null && item.getName().equals(loginSuccessInfo.getUserName()))
            return 1;
        else
            return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            if (getItemViewType(position) == 0)
                convertView = lytInflater.inflate(R.layout.bug_introduce_item_left, parent, false);
            else
                convertView = lytInflater.inflate(R.layout.bug_introduce_item_right, parent, false);
            viewHolder.titleTv = (TextView) convertView.findViewById(R.id.bug_introduce_item_title);
            viewHolder.contentTv = (TextView) convertView.findViewById(R.id.bug_introduce_item_content);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final BugIntroduceItem item = (BugIntroduceItem) getItem(position);
        if (item != null) {
            viewHolder.titleTv.setText(item.getTime() + " " + item.getType() + " by " + item.getName());
            viewHolder.contentTv.setText(item.getContent());
        }
        return convertView;
    }

    private class ViewHolder {
        public TextView titleTv;
        public TextView contentTv;
    }

    public void resetData(List<BugIntroduceItem> data) {
        if (mBugIntroduceItemList == null)
            this.mBugIntroduceItemList = new ArrayList<BugIntroduceItem>();
        else
            this.mBugIntroduceItemList.clear();
        if (data != null)
            this.mBugIntroduceItemList.addAll(data);
        notifyDataSetChanged();
    }
}
