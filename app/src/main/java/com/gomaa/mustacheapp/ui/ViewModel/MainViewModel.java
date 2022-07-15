package com.gomaa.mustacheapp.ui.ViewModel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gomaa.mustacheapp.Repository.DataRepository;
import com.gomaa.mustacheapp.data.FilterResources;
import com.gomaa.mustacheapp.data.VideoModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class MainViewModel extends ViewModel {
    private static final String TAG = "MainViewModel";
    private final DataRepository repository;
    private final MutableLiveData<String> Communicator = new MutableLiveData<>();
    private final MutableLiveData<VideoModel> videoCommunication = new MutableLiveData<>();

    @Inject
    public MainViewModel(DataRepository repository) {
        this.repository = repository;
    }

    public void InsertVideo(VideoModel videoModel) {
        repository.InsertVideo(videoModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> Log.d(TAG, "InsertVideo: successfully Inserted"),
                        error -> Log.e(TAG, "InsertVideo: ", error));
    }

    public void DeleteVideo(VideoModel videoModel) {
        repository.DeleteVideo(videoModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> Log.d(TAG, "DeleteVideo: successfully Deleted"),
                        error -> Log.e(TAG, "DeleteVideo: ", error));
    }

    public void updateVideoTag(int VideoID, String VideoTag) {
        repository.updateVideoTag(VideoID, VideoTag)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> Log.d(TAG, "updateVideoTag: successfully Updated"),
                        error -> Log.e(TAG, "updateVideoTag: ", error));
    }

    public LiveData<List<VideoModel>> getAllVideos() {
        return repository.getAllVideos();
    }

    public LiveData<VideoModel> getVideo(int videoId) {
        return repository.getVideo(videoId);
    }

    public LiveData<VideoModel> getLastVideo() {
        return repository.getLastVideo();
    }

    public LiveData<String> getCommunicator() {
        return Communicator;
    }

    public void SetCommunicationData(String TAG) {
        Communicator.setValue(TAG);
    }

    public void setVideoCommunicationData(VideoModel videoModel) {
        videoCommunication.setValue(videoModel);
    }

    public LiveData<VideoModel> getVideoCommunication() {
        return videoCommunication;
    }
}
