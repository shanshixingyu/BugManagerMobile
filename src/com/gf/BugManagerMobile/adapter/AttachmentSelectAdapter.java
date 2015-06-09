package com.gf.BugManagerMobile.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.gf.BugManagerMobile.R;
import com.gf.BugManagerMobile.view.DotProgressDialog;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 选择附件界面的适配器
 * Created by GuLang on 2015-06-08.
 */
public class AttachmentSelectAdapter extends BaseAdapter {
    private static final String TAG = "AttachmentSelectAdapter";

    private Context context;
    private LayoutInflater lytInflater;
    private List<File> mFileData;
    private boolean mHasParent = false;
    private ListView mAttachmentLv;
    private LoadFileAsyncTask mLoadFileAsyncTask;
    private DotProgressDialog mDotProgressDialog;
    private FileFilter mFileFilter;
    private FileFilter mDirectoryFilter;
    private File mSelectedAttachmentFile = null;
    private String mSelectedAttachmentPath = null;

    public AttachmentSelectAdapter(Context context, ListView listView) {
        this.context = context;
        this.lytInflater = LayoutInflater.from(context);
        this.mFileData = new ArrayList<File>();
        this.mAttachmentLv = listView;
        if (mAttachmentLv != null)
            mAttachmentLv.setOnItemClickListener(onLvItemClickListener);
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(context));

        mFileFilter = new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                if (pathname.isFile())
                    return true;
                return false;
            }
        };
        mDirectoryFilter = new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                if (pathname.isFile())
                    return false;
                return true;
            }
        };

        String state = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(state) && !Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            Toast.makeText(context, "外部存储有问题", Toast.LENGTH_SHORT).show();
            return;
        }
        tryUpdateListView(Environment.getExternalStorageDirectory());
    }

    /**
     * 尽力更新列表
     * @param newCurrentFile
     */

    private void tryUpdateListView(File newCurrentFile) {
        if (newCurrentFile == null)
            return;
        if (!newCurrentFile.canRead()) {
            Toast.makeText(context, "文件不可读", Toast.LENGTH_SHORT).show();
            return;
        }
        if (newCurrentFile.isFile()) {
            String tempSelectFilePath = newCurrentFile.getAbsolutePath();
            if (mSelectedAttachmentPath != null && mSelectedAttachmentPath.equals(tempSelectFilePath)) {
                // 说明之前选中,取消选中
                mSelectedAttachmentFile = null;
                mSelectedAttachmentPath = null;
            } else {
                mSelectedAttachmentFile = newCurrentFile;
                mSelectedAttachmentPath = tempSelectFilePath;
            }
            notifyDataSetChanged();
            return;
        }
        if (!newCurrentFile.isDirectory())
            return;

        mHasParent = (newCurrentFile.getParentFile() != null);
        mFileData.clear();
        if (mHasParent)
            mFileData.add(newCurrentFile.getParentFile());

        if (mLoadFileAsyncTask != null && mLoadFileAsyncTask.getStatus() != AsyncTask.Status.FINISHED
            && !mLoadFileAsyncTask.isCancelled()) {
            mLoadFileAsyncTask.cancel(true);
        }
        mLoadFileAsyncTask = new LoadFileAsyncTask();
        mLoadFileAsyncTask.execute(newCurrentFile);
    }

    private AdapterView.OnItemClickListener onLvItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.i(TAG, "onItemClick position=" + position);
            File selectedFile = (File) getItem(position);
            if (selectedFile == null)
                return;
            tryUpdateListView(selectedFile);
        }
    };

    @Override
    public int getCount() {
        if (mFileData == null)
            mFileData = new ArrayList<File>();
        return mFileData.size();
    }

    @Override
    public Object getItem(int position) {
        if (mFileData == null)
            mFileData = new ArrayList<File>();
        if (position < 0 || position >= mFileData.size())
            return null;
        return mFileData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = lytInflater.inflate(R.layout.attachment_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.iconImgv = (ImageView) convertView.findViewById(R.id.attachment_item_icon_imgv);
            viewHolder.nameTv = (TextView) convertView.findViewById(R.id.attachment_item_name_tv);
            viewHolder.selectedImgv = (ImageView) convertView.findViewById(R.id.attachment_item_selected_imgv);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final File file = (File) getItem(position);
        if (file != null) {
            if (mHasParent && position == 0) {
                ImageLoader.getInstance().displayImage(
                    ImageDownloader.Scheme.DRAWABLE.wrap("" + R.drawable.ic_parent_directory), viewHolder.iconImgv);
                viewHolder.nameTv.setText(R.string.attachment_go_parent);
            } else {
                if (file.isFile()) {
                    ImageLoader.getInstance().displayImage(
                        ImageDownloader.Scheme.DRAWABLE.wrap("" + R.drawable.ic_file), viewHolder.iconImgv);
                } else {
                    ImageLoader.getInstance().displayImage(
                        ImageDownloader.Scheme.DRAWABLE.wrap("" + R.drawable.ic_child_directory), viewHolder.iconImgv);
                }
                viewHolder.nameTv.setText(file.getName());
            }

            if (mSelectedAttachmentPath != null && mSelectedAttachmentPath.equals(file.getAbsolutePath()))
                viewHolder.selectedImgv.setVisibility(View.VISIBLE);
            else
                viewHolder.selectedImgv.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }

    private class ViewHolder {
        public ImageView iconImgv;
        public TextView nameTv;
        public ImageView selectedImgv;
    }

    private class LoadFileAsyncTask extends AsyncTask<File, List<File>, Void> {
        @Override
        protected void onPreExecute() {
            if (mDotProgressDialog == null) {
                mDotProgressDialog = new DotProgressDialog(context);
                mDotProgressDialog.setMessage("加载中...");
            }
            mDotProgressDialog.show();
        }

        @Override
        protected Void doInBackground(File... params) {
            if (params != null && params[0] != null && params[0].isDirectory()) {
                File[] directories = params[0].listFiles(mDirectoryFilter);
                if (directories != null) {
                    publishProgress(Arrays.asList(directories));
                }
                File[] files = params[0].listFiles(mFileFilter);
                if (files != null) {
                    publishProgress(Arrays.asList(files));
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(List<File>... values) {
            if (values != null && values[0] != null) {
                if (mFileData == null)
                    mFileData = new ArrayList<File>();
                mFileData.addAll(values[0]);
                notifyDataSetChanged();
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (mDotProgressDialog != null)
                mDotProgressDialog.dismiss();
        }

        @Override
        protected void onCancelled() {
            if (mDotProgressDialog != null)
                mDotProgressDialog.dismiss();
        }
    }

    public File getSelectedAttachmentFile() {
        return mSelectedAttachmentFile;
    }

    public String getSelectedAttachmentPath() {
        return mSelectedAttachmentPath;
    }

    /**
     * 设置选中文件
     * @param attachmentPath
     */
    public void setmSelectedAttachment(String attachmentPath) {
        if (attachmentPath != null) {
            File attachmentFile = new File(attachmentPath);
            if (attachmentFile.exists() && attachmentFile.isFile()) {
                mSelectedAttachmentPath = attachmentPath;
                mSelectedAttachmentFile = attachmentFile;
                notifyDataSetChanged();
                return;
            }
        }
        // 判断，主要是为了优化
        if (mSelectedAttachmentFile != null) {
            mSelectedAttachmentFile = null;
            mSelectedAttachmentPath = null;
            notifyDataSetChanged();
        }
    }

}
