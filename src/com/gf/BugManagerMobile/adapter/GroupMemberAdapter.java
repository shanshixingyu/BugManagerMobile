package com.gf.BugManagerMobile.adapter;

import android.content.Context;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import com.gf.BugManagerMobile.R;
import com.gf.BugManagerMobile.models.User;
import java.util.ArrayList;
import java.util.List;

/**
 * 选择团队成员的对话框的适配器
 * Created by GuLang on 2015-06-01.
 */
public class GroupMemberAdapter extends BaseAdapter {
    private static final String TAG = "GroupMemberAdapter";

    private LayoutInflater lytInflater;
    private List<User> mGroupMember;
    private SparseBooleanArray mSparseArray = new SparseBooleanArray();// 记录指定的用户ID下是否选中

    public GroupMemberAdapter(Context context, List<User> groupMember) {
        lytInflater = LayoutInflater.from(context);
        if (groupMember == null)
            mGroupMember = new ArrayList<User>();
        else
            mGroupMember = groupMember;
    }

    @Override
    public int getCount() {
        if (mGroupMember == null)
            mGroupMember = new ArrayList<User>();
        return mGroupMember.size();
    }

    @Override
    public Object getItem(int position) {
        if (mGroupMember == null)
            mGroupMember = new ArrayList<User>();
        if (position < 0 || position >= mGroupMember.size())
            return null;
        return mGroupMember.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = lytInflater.inflate(R.layout.group_member_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.nameTv = (TextView) convertView.findViewById(R.id.group_member_item_tv);
            viewHolder.itemChb = (CheckBox) convertView.findViewById(R.id.group_member_item_chb);
            viewHolder.itemChb.setOnCheckedChangeListener(onCheckedChangeListener);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final User user = (User) getItem(position);
        if (user != null) {
            viewHolder.nameTv.setText(user.getName());
            //位置至关重要
            viewHolder.itemChb.setTag(user.getId());
            Boolean isChecked = mSparseArray.get(user.getId());
            if (isChecked)
                viewHolder.itemChb.setChecked(true);
            else
                viewHolder.itemChb.setChecked(false);
        }

        return convertView;
    }

    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener =
        new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                Object tagObj = compoundButton.getTag();
                if (tagObj != null) {
                    try {
                        int changedUserId = Integer.parseInt(tagObj.toString());
                        Log.i(TAG, "修改用户：" + changedUserId + ",状态为：" + isChecked);
                        mSparseArray.put(changedUserId, isChecked);
                    } catch (NumberFormatException e) {
                    }
                }
            }
        };

    private class ViewHolder {
        public TextView nameTv;
        public CheckBox itemChb;
    }

    /**
     * 改变选中状态
     * @param selectedUserIds
     */
    public void resetListState(List<Integer> selectedUserIds) {
        if (mSparseArray == null)
            mSparseArray = new SparseBooleanArray();
        else
            mSparseArray.clear();

        if (selectedUserIds != null) {
            int count = selectedUserIds.size();
            for (int i = 0; i < count; i++) {
                mSparseArray.put(selectedUserIds.get(i), true);
            }
        }
        notifyDataSetChanged();
    }

    /**
     * 获取选中的用户ID列表
     */
    public List<Integer> getSelectedUserId() {
        List<Integer> selectedUserId = new ArrayList<Integer>();

        int count = mSparseArray.size();
        for (int i = 0; i < count; i++) {
            int key = mSparseArray.keyAt(i);
            Boolean isChecked = mSparseArray.get(key);
            if (isChecked) {
                selectedUserId.add(key);
            }
        }
        Log.i(TAG, "选中用户ID=" + selectedUserId.toString());
        return selectedUserId;
    }

    /**
     * 重新设置用户列表信息
     * @param users
     */
    public void resetData(List<User> users) {
        if (mGroupMember == null)
            mGroupMember = new ArrayList<User>();
        mGroupMember.clear();
        mSparseArray.clear();
        if (users != null && users.size() > 0)
            mGroupMember.addAll(users);
        notifyDataSetChanged();
    }
}
