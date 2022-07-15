package com.gomaa.mustacheapp.network;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.gomaa.mustacheapp.data.VideoModel;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;

@Dao
public interface VideoDao {

    @Insert
    Completable InsertVideo(VideoModel videoModel);

    @Delete
    Completable DeleteVideo(VideoModel videoModel);

    @Query("Update VideoTable set Video_Tag=:VideoTag where Video_ID=:videoID")
    Completable UpdateTagVideo(int videoID, String VideoTag);

    @Query("Select * from VideoTable")
    LiveData<List<VideoModel>> getAllVideos();

    @Query("Select * from VideoTable where Video_ID =:VideoID")
    LiveData<VideoModel> getVideo(int VideoID);

    @Query("SELECT * FROM VideoTable ORDER BY Video_ID DESC LIMIT 1")
    LiveData<VideoModel> getLastVideo();

}
