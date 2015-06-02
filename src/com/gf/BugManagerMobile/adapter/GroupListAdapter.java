package com.gf.BugManagerMobile.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.gf.BugManagerMobile.R;
import com.gf.BugManagerMobile.models.Group;
import com.gf.BugManagerMobile.models.LoginSuccessInfo;
import com.gf.BugManagerMobile.utils.LocalInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 团队列表适配器
 * Created by GuLang on 2015-05-20.
 */
public class GroupListAdapter extends BaseAdapter {
    private static final String TAG = "GroupListAdapter";

    private LayoutInflater lytInflater;
    private List<Group> mGroupDataList;
    private int mPageCount;
    private int mCurrentPage;
    private LoginSuccessInfo loginSuccessInfo;
    private int notEnableColor;
    private OnSlideItemClickListener onSlideItemClickListener;

    public GroupListAdapter(Context context, List<Group> data) {
        this.lytInflater = LayoutInflater.from(context);
        if (data == null)
            mGroupDataList = new ArrayList<Group>();
        else
            mGroupDataList = data;
        loginSuccessInfo = LocalInfo.getLoginSuccessInfo(context);
        notEnableColor = Color.parseColor("#ffbcbcbe");
    }

    @Override
    public int getCount() {
        if (this.mGroupDataList == null)
            this.mGroupDataList = new ArrayList<Group>();
        return this.mGroupDataList.size();
    }

    @Override
    public Object getItem(int position) {
        if (this.mGroupDataList == null)
            this.mGroupDataList = new ArrayList<Group>();
        if (position < 0 || position >= this.mGroupDataList.size())
            return null;
        return this.mGroupDataList.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = lytInflater.inflate(R.layout.group_list_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.nameTv = (TextView) convertView.findViewById(R.id.group_list_item_name_tv);
            viewHolder.introduceTv = (TextView) convertView.findViewById(R.id.group_list_item_introduce_tv);
            viewHolder.deleteTv = (TextView) convertView.findViewById(R.id.slide_item_second);
            viewHolder.itemView = convertView.findViewById(R.id.slide_item_first);
            viewHolder.itemView.setOnClickListener(onItemClickListener);
            viewHolder.deleteTv = (TextView) convertView.findViewById(R.id.slide_item_second);
            viewHolder.deleteTv.setOnClickListener(onDeleteClickListener);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.itemView.setTag(position);
        viewHolder.deleteTv.setTag(position);

        final Group group = (Group) getItem(position);
        if (group != null) {
            viewHolder.nameTv.setText(group.getName());
            viewHolder.introduceTv.setText(group.getIntroduce());

            if (group.getCreator() == loginSuccessInfo.getUserId()) {
                viewHolder.deleteTv.setTextColor(Color.WHITE);
                viewHolder.deleteTv.setEnabled(true);
            } else {
                viewHolder.deleteTv.setTextColor(notEnableColor);
                viewHolder.deleteTv.setEnabled(false);
            }

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
     * 重新设置适配器数据
     * @param data
     */
    public void resetData(List<Group> data) {
        if (mGroupDataList == null)
            mGroupDataList = new ArrayList<Group>();
        else
            mGroupDataList.clear();
        if (data != null)
            mGroupDataList.addAll(data);
        notifyDataSetChanged();
    }

    /**
     * 添加数据
     * @param data
     */
    public void addData(List<Group> data) {
        if (mGroupDataList == null)
            mGroupDataList = new ArrayList<Group>();
        if (data != null) {
            mGroupDataList.addAll(data);
        }
        notifyDataSetChanged();
    }

    private class ViewHolder {
        public View itemView;
        public TextView nameTv;
        public TextView introduceTv;
        public TextView deleteTv;
    }

    public interface OnSlideItemClickListener {
        public void onItemClick(int position, int groupId);

        public void onDeleteClick(int position, int groupId);
    }

    public void setOnSlideItemClickListener(OnSlideItemClickListener listener) {
        this.onSlideItemClickListener = listener;
    }

    private View.OnClickListener onItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Object positionObj = view.getTag();
            try {
                int position = Integer.parseInt(positionObj.toString());
                if (onSlideItemClickListener != null) {
                    Group group = (Group) getItem(position);
                    if (group == null) {
                        onSlideItemClickListener.onItemClick(position, -1);
                    } else {
                        onSlideItemClickListener.onItemClick(position, group.getId());
                    }
                }
            } catch (NumberFormatException e) {
            }
        }
    };

    private View.OnClickListener onDeleteClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Object positionObj = view.getTag();
            try {
                int position = Integer.parseInt(positionObj.toString());
                if (onSlideItemClickListener != null) {
                    Group group = (Group) getItem(position);
                    if (group == null) {
                        onSlideItemClickListener.onDeleteClick(position, -1);
                    } else {
                        onSlideItemClickListener.onDeleteClick(position, group.getId());
                    }
                }
            } catch (NumberFormatException e) {
            }
        }
    };

    /* 删除指定项 */
    public void deleteItem(int position) {
        if (this.mGroupDataList == null)
            this.mGroupDataList = new ArrayList<Group>();
        if (0 <= position && position < this.mGroupDataList.size())
            this.mGroupDataList.remove(position);
        notifyDataSetChanged();
    }
}
