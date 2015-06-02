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
import com.gf.BugManagerMobile.models.Group;
import com.gf.BugManagerMobile.models.LoginSuccessInfo;
import com.gf.BugManagerMobile.models.Project;
import com.gf.BugManagerMobile.utils.LocalInfo;
import com.gf.BugManagerMobile.utils.MyConstant;

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
    private LoginSuccessInfo loginSuccessInfo;
    private OnItemOptListener onItemOptListener;

    public ProjectListAdapter(Context context, List<Project> data) {
        this.lytInflater = LayoutInflater.from(context);
        if (data == null)
            this.mProjectDataList = new ArrayList<Project>();
        else
            this.mProjectDataList = data;
        loginSuccessInfo = LocalInfo.getLoginSuccessInfo(context);
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
            viewHolder.itemView = convertView.findViewById(R.id.slide_item_first);
            viewHolder.nameTv = (TextView) convertView.findViewById(R.id.project_list_item_name_tv);
            viewHolder.introudceTv = (TextView) convertView.findViewById(R.id.project_list_item_introduce_tv);
            viewHolder.moduleTv = (TextView) convertView.findViewById(R.id.project_list_item_module_tv);
            viewHolder.deleteTv = (TextView) convertView.findViewById(R.id.project_list_item_delete_tv);
            viewHolder.itemView.setOnClickListener(onItemClickListener);
            viewHolder.moduleTv.setOnClickListener(onModuleClickListener);
            viewHolder.deleteTv.setOnClickListener(onDeleteClickListener);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.itemView.setTag(position);
        viewHolder.moduleTv.setTag(position);
        viewHolder.deleteTv.setTag(position);

        final Project project = (Project) getItem(position);
        if (project != null) {
            viewHolder.nameTv.setText(project.getName());
            viewHolder.introudceTv.setText(project.getIntroduce());

            if (loginSuccessInfo != null
                && (loginSuccessInfo.getRoleId() == 0 || loginSuccessInfo.getUserId() == project.getCreator())) {
                viewHolder.deleteTv.setEnabled(true);
                viewHolder.deleteTv.setTextColor(Color.WHITE);
            } else {
                viewHolder.deleteTv.setEnabled(false);
                viewHolder.deleteTv.setTextColor(MyConstant.NOT_ENABLE_TEXT_COLOR);
            }
        }
        return convertView;
    }

    private View.OnClickListener onItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (onItemOptListener != null) {
                Object positionObj = view.getTag();
                try {
                    /* 包含了两种异常：空指针和转换成数字 */
                    int position = Integer.parseInt(positionObj.toString());
                    Project project = (Project) getItem(position);
                    if (project == null)
                        onItemOptListener.onItemClick(position, -1);
                    else
                        onItemOptListener.onItemClick(position, project.getId());
                } catch (Exception e) {
                }
            }
        }
    };

    private View.OnClickListener onModuleClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (onItemOptListener != null) {
                Object positionObj = view.getTag();
                try {
                    /* 包含了两种异常：空指针和转换成数字 */
                    int position = Integer.parseInt(positionObj.toString());
                    Log.i(TAG, "获得的position=" + position);
                    Project project = (Project) getItem(position);
                    if (project == null)
                        onItemOptListener.onModuleClick(position, -1);
                    else
                        onItemOptListener.onModuleClick(position, project.getId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };

    public View.OnClickListener onDeleteClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (onItemOptListener != null) {
                Object positionObj = view.getTag();
                try {
                    /* 包含了两种异常：空指针和转换成数字 */
                    int position = Integer.parseInt(positionObj.toString());
                    Project project = (Project) getItem(position);
                    if (project == null)
                        onItemOptListener.onDeleteClick(position, -1);
                    else
                        onItemOptListener.onDeleteClick(position, project.getId());
                } catch (Exception e) {
                }
            }
        }
    };

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
        public View itemView;
        public TextView nameTv;
        public TextView introudceTv;
        public TextView moduleTv;
        public TextView deleteTv;
    }

    /**
     * 设置监听
     * @param listener
     */
    public void setOnItemOptListener(OnItemOptListener listener) {
        this.onItemOptListener = listener;
    }

    public interface OnItemOptListener {
        public void onItemClick(int position, int projectId);

        public void onModuleClick(int position, int projectId);

        public void onDeleteClick(int position, int projectId);
    }

    /**
     * 删除指定项的内容
     * @param position
     */
    public void deleteItem(int position) {
        if (this.mProjectDataList == null)
            this.mProjectDataList = new ArrayList<Project>();
        if (0 <= position && position < this.mProjectDataList.size())
            this.mProjectDataList.remove(position);
        notifyDataSetChanged();
    }

}
