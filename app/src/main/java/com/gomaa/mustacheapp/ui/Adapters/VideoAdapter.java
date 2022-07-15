package com.gomaa.mustacheapp.ui.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gomaa.mustacheapp.R;
import com.gomaa.mustacheapp.data.VideoModel;
import com.gomaa.mustacheapp.network.OnclickHandler;
import com.gomaa.mustacheapp.ui.ViewHolders.VideoViewHolder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;

public class VideoAdapter extends RecyclerView.Adapter<VideoViewHolder> {
    private Context context;
    private List<VideoModel> videoModelList=new ArrayList<>();
    private OnclickHandler<VideoModel> videoModelOnclickHandler;
    @Inject
    public VideoAdapter(@ApplicationContext Context context) {
        this.context = context;
    }

    public void setVideoModelList(List<VideoModel> videoModelList) {
        this.videoModelList = videoModelList;
        notifyDataSetChanged();
    }

    public void setVideoModelOnclickHandler(OnclickHandler<VideoModel> videoModelOnclickHandler) {
        this.videoModelOnclickHandler = videoModelOnclickHandler;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VideoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.video_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
       VideoModel videoModel = videoModelList.get(position);
       // set image
        Glide.with(context)
                .load(videoModel.getUri())
                .into(holder.VideoImage);
        //set duration of the video
        holder.VideoDuration.setText(TimeFormat(videoModel.getDuration()));
        //handle Onclick
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoModelOnclickHandler.onItemClicked(videoModel);
            }
        });
    }

    @Override
    public int getItemCount() {
        return videoModelList.size();
    }

    private String TimeFormat(long Time){
        @SuppressLint("SimpleDateFormat") DateFormat formatter = new SimpleDateFormat("mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        String result = formatter.format(Time);
        return result;
    }
}
