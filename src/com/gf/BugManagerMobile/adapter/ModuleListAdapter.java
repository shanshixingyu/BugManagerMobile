package com.gf.BugManagerMobile.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gf.BugManagerMobile.R;
import com.gf.BugManagerMobile.models.LoginSuccessInfo;
import com.gf.BugManagerMobile.models.Module;
import com.gf.BugManagerMobile.utils.LocalInfo;
import com.gf.BugManagerMobile.utils.MyConstant;

/**
 * 项目的模块管理适配器
 * Created by GuLang on 2015-06-03.
 */
public class ModuleListAdapter extends BaseAdapter {
    private static final String TAG = "ProjectListAdapter";

    private LayoutInflater lytInflater;
    private List<Module> mModuleDataList;
    private int mPageCount;
    private int mCurrentPage;
    private LoginSuccessInfo loginSuccessInfo;
    private OnItemOptListener onItemOptListener;

    public ModuleListAdapter(Context context, List<Module> data) {
        this.lytInflater = LayoutInflater.from(context);
        if (data == null)
            this.mModuleDataList = new ArrayList<Module>();
        else
            this.mModuleDataList = data;
        loginSuccessInfo = LocalInfo.getLoginSuccessInfo(context);
    }

    @Override
    public int getCount() {
        if (mModuleDataList == null)
            mModuleDataList = new ArrayList<Module>();
        return mModuleDataList.size();
    }

    @Override
    public Object getItem(int position) {
        if (mModuleDataList == null)
            mModuleDataList = new ArrayList<Module>();
        if (position < 0 || position >= mModuleDataList.size())
            return null;
        return mModuleDataList.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = lytInflater.inflate(R.layout.module_list_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.itemView = convertView.findViewById(R.id.slide_item_first);
            viewHolder.nameTv = (TextView) convertView.findViewById(R.id.module_list_item_name_tv);
            viewHolder.introduceTv = (TextView) convertView.findViewById(R.id.module_list_item_introduce_tv);
            viewHolder.deleteTv = (TextView) convertView.findViewById(R.id.slide_item_second);
            viewHolder.itemView.setOnClickListener(onItemClickListener);
            viewHolder.deleteTv.setOnClickListener(onDeleteClickListener);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.itemView.setTag(position);
        viewHolder.deleteTv.setTag(position);

        final Module module = (Module) getItem(position);
        if (module != null) {
            viewHolder.nameTv.setText(module.getName());
            viewHolder.introduceTv.setText(module.getIntroduce());

            if (loginSuccessInfo != null
                && (loginSuccessInfo.getRoleId() == 0 || loginSuccessInfo.getUserId() == module.getCreator())) {
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
                    Module module = (Module) getItem(position);
                    if (module == null)
                        onItemOptListener.onItemClick(position, -1);
                    else
                        onItemOptListener.onItemClick(position, module.getId());
                } catch (Exception e) {
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
                    Module module = (Module) getItem(position);
                    if (module == null)
                        onItemOptListener.onDeleteClick(position, -1);
                    else
                        onItemOptListener.onDeleteClick(position, module.getId());
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
    public void resetData(List<Module> data) {
        if (mModuleDataList == null)
            mModuleDataList = new ArrayList<Module>();
        else
            mModuleDataList.clear();
        if (data != null)
            mModuleDataList.addAll(data);
        notifyDataSetChanged();
    }

    /**
     * 给适配器设置增加数据
     * @param data
     */
    public void addData(List<Module> data) {
        if (mModuleDataList == null)
            mModuleDataList = new ArrayList<Module>();
        if (data != null)
            mModuleDataList.addAll(data);
        notifyDataSetChanged();
    }

    private class ViewHolder {
        public View itemView;
        public TextView nameTv;
        public TextView introduceTv;
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
        public void onItemClick(int position, int moduleId);

        public void onDeleteClick(int position, int moduleId);
    }

    /**
     * 删除指定项的内容
     * @param position
     */
    public void deleteItem(int position) {
        if (this.mModuleDataList == null)
            this.mModuleDataList = new ArrayList<Module>();
        if (0 <= position && position < this.mModuleDataList.size())
            this.mModuleDataList.remove(position);
        notifyDataSetChanged();
    }

}
