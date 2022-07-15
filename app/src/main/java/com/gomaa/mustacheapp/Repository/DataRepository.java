package com.gomaa.mustacheapp.Repository;

import androidx.lifecycle.LiveData;

import com.gomaa.mustacheapp.data.VideoModel;
import com.gomaa.mustacheapp.network.VideoDao;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;

public class DataRepository {
    private final VideoDao videoDao;

    @Inject
    public DataRepository(VideoDao videoDao) {
        this.videoDao = videoDao;
    }

    /**
     * insert single Video To Video Table
     * @param videoModel
     * @return
     */
    public Completable InsertVideo(VideoModel videoModel) {
        return videoDao.InsertVideo(videoModel);
    }

    /**
     * Delete single Video From video Table
     * @param videoModel
     * @return
     */
    public Completable DeleteVideo(VideoModel videoModel) {
        return videoDao.DeleteVideo(videoModel);
    }

    /**
     * Get All Videos from video Table
     * @return
     */
    public LiveData<List<VideoModel>> getAllVideos(){
        return videoDao.getAllVideos();
    }

    /**
     * Get single video from video table by it's id
     * @param VideoId
     * @return
     */
    public LiveData<VideoModel> getVideo(int VideoId){
        return videoDao.getVideo(VideoId);
    }

    /**
     * Get Last Video from video table
     * @return
     */
    public LiveData<VideoModel> getLastVideo(){
        return videoDao.getLastVideo();
    }

    public Completable updateVideoTag(int VideoID, String VideoTag){
        return videoDao.UpdateTagVideo(VideoID,VideoTag);
    }

}
