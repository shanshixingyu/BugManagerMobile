package com.gf.BugManagerMobile.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.gf.BugManagerMobile.R;
import com.gf.BugManagerMobile.utils.LocalInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * 展示截图的适配器
 * Created by Administrator on 5/17 0017.
 */
public class ImageRlvAdapter extends RecyclerView.Adapter<ImageRlvAdapter.RlvViewHolder> {
    private static final String TAG = "ImageRlvAdapter";

    private Context context;
    private LayoutInflater lytInflater;
    private List<String> pathDataList;
    private DisplayImageOptions displayImageOptions;

    public ImageRlvAdapter(Context context, List<String> data) {
        this.context = context;
        this.lytInflater = LayoutInflater.from(context);
        if (data == null)
            this.pathDataList = new ArrayList<String>();
        else
            this.pathDataList = data;
        if (!ImageLoader.getInstance().isInited())
            ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(context));
        displayImageOptions = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.ic_load_failure)
                .showImageOnFail(R.drawable.ic_load_failure)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
    }

    @Override
    public ImageRlvAdapter.RlvViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        return new RlvViewHolder(lytInflater.inflate(R.layout.image_rlv_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ImageRlvAdapter.RlvViewHolder viewHolder, int position) {
        if (pathDataList == null)
            pathDataList = new ArrayList<String>();
        String fileName;
        if (position < 0 || position >= pathDataList.size())
            fileName = null;
        else
            fileName = pathDataList.get(position);
        if (fileName != null) {
            ImageLoader.getInstance().displayImage(LocalInfo.getBufferImageFileUrl(context) + fileName, viewHolder.imageView, displayImageOptions);
        }
    }

    @Override
    public int getItemCount() {
        if (pathDataList == null)
            pathDataList = new ArrayList<String>();
        return pathDataList.size();
    }

    public void resetData(List<String> fileNames) {
        if (pathDataList == null)
            pathDataList = new ArrayList<String>();
        else
            pathDataList.clear();
        if (fileNames != null)
            pathDataList.addAll(fileNames);
        notifyDataSetChanged();
    }

    public class RlvViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;

        public RlvViewHolder(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.bug_detail_image_imgv);
        }
    }

}
