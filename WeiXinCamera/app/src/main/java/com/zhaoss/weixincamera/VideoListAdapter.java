package com.zhaoss.weixincamera;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by Zhaoss on 2016/3/16.
 */
public class VideoListAdapter extends RecyclerView.Adapter {

    private List<Bitmap> fileList;
    private MainActivity activity;

    public VideoListAdapter(MainActivity activity, List<Bitmap> fileList) {
        this.fileList = fileList;
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(View.inflate(activity, R.layout.item_video, null));
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        MyViewHolder vh = (MyViewHolder) holder;
        vh.iv_video.setBackground(new BitmapDrawable(fileList.get(position)));
    }

    @Override
    public int getItemCount() {
        return fileList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView iv_video;

        public MyViewHolder(View itemView) {
            super(itemView);
            iv_video = (ImageView) itemView.findViewById(R.id.iv_video);
        }
    }
}
