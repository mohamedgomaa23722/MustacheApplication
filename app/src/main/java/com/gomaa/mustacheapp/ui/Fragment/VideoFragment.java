package com.gomaa.mustacheapp.ui.Fragment;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gomaa.mustacheapp.R;
import com.gomaa.mustacheapp.data.VideoModel;
import com.gomaa.mustacheapp.databinding.FragmentVideoBinding;
import com.gomaa.mustacheapp.ui.ViewModel.MainViewModel;



public class VideoFragment extends Fragment implements View.OnClickListener {
    private FragmentVideoBinding binding;
    private VideoModel videoModel;
    private MainViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentVideoBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);
        videoModel = (VideoModel) getArguments().getSerializable("path");
        viewModel.SetCommunicationData(videoModel.getTag());
        SetUpView();
    }

    /**
     * Setup View
     */
    private void SetUpView() {
        //Initialize Video player view
        InitializeVideoPlayer();

        //Handle Onclick
        binding.exoRew.setOnClickListener(this);
        binding.exoForward.setOnClickListener(this);
        binding.exoPlay.setOnClickListener(this);

        getActivity().invalidateOptionsMenu();
    }

    /**
     * First Step we initialize the video Player
     */
    private void InitializeVideoPlayer() {
        binding.videoPlayer.setVideoURI(Uri.parse(videoModel.getUri()));
        PlayVideo();
    }

    /**
     * Play Video
     */
    private void PlayVideo() {
        binding.videoPlayer.start();
    }

    /**
     * Pause Video
     */
    private void PauseVideo() {
        binding.videoPlayer.pause();
    }

    /**
     * Resume Video
     */
    private void ResumeVideo() {
        binding.videoPlayer.resume();
    }

    /**
     * Check Video statue if it is played or paused to change some
     * Ui element and to change the statue of the video
     */
    private void VideoStatue() {
        boolean statue = binding.videoPlayer.isPlaying();
        if (statue) {
            //is currently Play
            PauseVideo();
            binding.exoPlay.setImageResource(R.drawable.ic_baseline_play_circle_outline_24);
        } else {
            //is Currently paused
            binding.videoPlayer.start();
            binding.exoPlay.setImageResource(R.drawable.ic_outline_pause_circle_outline_24);

        }
    }

    /**
     * Regular operation of any video is seek forward or rew so we
     * need to handle this problem by this method
     *
     * @param isSeekForward
     */
    private void SeekTo(boolean isSeekForward) {
        int CurrentPosition = binding.videoPlayer.getCurrentPosition();
        if (isSeekForward) {
            //seek Forward
            if ((CurrentPosition + 2000) <= videoModel.getDuration())
                CurrentPosition += 2000;
        } else {
            // rew
            if (CurrentPosition >= 1000)
                CurrentPosition -= 1000;
        }
        binding.videoPlayer.seekTo(CurrentPosition);
    }

    private void TrackVideo(){
        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).start();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.exo_rew:
                SeekTo(false);
                break;
            case R.id.exo_forward:
                SeekTo(true);
                break;
            case R.id.exo_play:
                VideoStatue();
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        PauseVideo();
    }

}