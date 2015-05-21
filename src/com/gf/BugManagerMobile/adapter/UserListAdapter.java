package com.gf.BugManagerMobile.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.gf.BugManagerMobile.R;
import com.gf.BugManagerMobile.models.LoginSuccessInfo;
import com.gf.BugManagerMobile.models.User;
import com.gf.BugManagerMobile.utils.LocalInfo;
import com.gf.BugManagerMobile.utils.UserUtils;
import com.gf.BugManagerMobile.view.SlideItemView;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户列表适配器
 * Created by GuLang on 2015-05-19.
 */
public class UserListAdapter extends BaseAdapter {
    private static final String TAG = "UserListAdapter";

    private LayoutInflater lytInflater;
    private List<User> userData;
    private int mOpenDeleteItemPosition = -1;
    private int mPageCount = 0;
    private int mCurrentPage = 0;
    private LoginSuccessInfo loginSuccessInfo;
    private int notEnableColor;

    public UserListAdapter(Context context, List<User> data) {
        this.lytInflater = LayoutInflater.from(context);
        if (data == null)
            this.userData = new ArrayList<User>();
        else
            this.userData = data;
        mOpenDeleteItemPosition = -1;
        loginSuccessInfo = LocalInfo.getLoginSuccessInfo(context);
        notEnableColor = Color.parseColor("#ffbcbcbe");
    }

    @Override
    public int getCount() {
        if (this.userData == null)
            this.userData = new ArrayList<User>();
        return this.userData.size();
    }

    @Override
    public Object getItem(int position) {
        if (this.userData == null)
            this.userData = new ArrayList<User>();
        if (position < 0 || position >= this.userData.size())
            return null;
        return this.userData.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = lytInflater.inflate(R.layout.user_list_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.nameTv = (TextView) convertView.findViewById(R.id.user_list_item_name_tv);
            viewHolder.roleTv = (TextView) convertView.findViewById(R.id.user_list_item_role_tv);
            viewHolder.emailTv = (TextView) convertView.findViewById(R.id.user_list_item_email_tv);
            viewHolder.itemView = convertView.findViewById(R.id.slide_item_first);
            viewHolder.itemView.setOnClickListener(onItemClickListener);
            viewHolder.deleteTv = (TextView) convertView.findViewById(R.id.slide_item_second);
            viewHolder.deleteTv.setOnClickListener(onDeleteClickListener);
            viewHolder.itemView.setTag(viewHolder);
            viewHolder.deleteTv.setTag(viewHolder);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.position = position;
        final User user = (User) getItem(position);
        if (user != null) {
            viewHolder.nameTv.setText(user.getName());
            viewHolder.roleTv.setText(UserUtils.getUserRoleName(user.getRole_id()));
            viewHolder.emailTv.setText(user.getEmail());
            if (user.getCreator() == loginSuccessInfo.getUserId()) {
                viewHolder.deleteTv.setTextColor(Color.WHITE);
                viewHolder.deleteTv.setEnabled(true);
            } else {
                viewHolder.deleteTv.setTextColor(notEnableColor);
                viewHolder.deleteTv.setEnabled(false);
            }
        }

        SlideItemView slideItemView = (SlideItemView) convertView;
        if (position == mOpenDeleteItemPosition) {
            slideItemView.openItem();
        } else {
            slideItemView.closeItem();
        }

        return convertView;
    }

    private class ViewHolder {
        public int position;
        public View itemView;
        public TextView nameTv;
        public TextView roleTv;
        public TextView emailTv;
        public TextView deleteTv;
    }

    private View.OnClickListener onItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ViewHolder viewHolder = (ViewHolder) view.getTag();
            if (viewHolder != null && onSlideItemClickListener != null) {
                int userId;
                final User user = (User) getItem(viewHolder.position);
                if (user == null)
                    userId = -1;
                else
                    userId = user.getId();
                onSlideItemClickListener.onItemClick(viewHolder.position, userId);
            }
        }
    };

    private View.OnClickListener onDeleteClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ViewHolder viewHolder = (ViewHolder) view.getTag();
            if (viewHolder != null && onSlideItemClickListener != null) {
                int userId;
                final User user = (User) getItem(viewHolder.position);
                if (user == null)
                    userId = -1;
                else
                    userId = user.getId();
                onSlideItemClickListener.onDeleteClick(viewHolder.position, userId);
            }
        }
    };

    private OnSlideItemClickListener onSlideItemClickListener;

    public void setOnSlideItemClickListener(OnSlideItemClickListener listener) {
        this.onSlideItemClickListener = listener;
    }

    public interface OnSlideItemClickListener {
        public void onItemClick(int position, int userId);

        public void onDeleteClick(int position, int userId);
    }

    /**
     * 设置左滑删除的位置
     * @param position
     */
    public void setOpenDeleteItemPosition(int position) {
        if (mOpenDeleteItemPosition != position) {
            mOpenDeleteItemPosition = position;
            notifyDataSetChanged();
        }
    }

    /**
     * 重新设置数据
     * @param data
     */
    public void resetData(List<User> data) {
        if (this.userData == null)
            this.userData = new ArrayList<User>();
        else
            this.userData.clear();
        if (data != null) {
            this.userData.addAll(data);
        }
        notifyDataSetChanged();
    }

    /**
     * 添加数据
     * @param data
     */
    public void addData(List<User> data) {
        if (this.userData == null)
            this.userData = new ArrayList<User>();
        if (data != null) {
            this.userData.addAll(data);
        }
        notifyDataSetChanged();
    }

    /* 删除指定项 */
    public void deleteItem(int position) {
        if (this.userData == null)
            this.userData = new ArrayList<User>();
        if (0 <= position && position < this.userData.size())
            this.userData.remove(position);
        notifyDataSetChanged();
    }

    public void setPageCount(int count) {
        this.mPageCount = count;
    }

    public int getPageCount() {
        return this.mPageCount;
    }

    public void setCurrentPage(int page) {
        this.mCurrentPage = page;
    }

    public int getCurrentPage() {
        return this.mCurrentPage;
    }

}
