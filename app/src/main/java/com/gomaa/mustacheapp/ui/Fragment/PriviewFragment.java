package com.gomaa.mustacheapp.ui.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gomaa.mustacheapp.R;
import com.gomaa.mustacheapp.data.VideoModel;
import com.gomaa.mustacheapp.databinding.FragmentPriviewBinding;
import com.gomaa.mustacheapp.network.OnclickHandler;
import com.gomaa.mustacheapp.ui.Adapters.VideoAdapter;
import com.gomaa.mustacheapp.ui.ViewModel.MainViewModel;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class PriviewFragment extends Fragment implements OnclickHandler<VideoModel> {

    private MainViewModel viewModel;
    private FragmentPriviewBinding binding;
    @Inject
    VideoAdapter adapter;
    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPriviewBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);
        SetupView();
    }

    private void SetupView() {
        //SetupNav
        NavHostFragment navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        navController = navHostFragment.getNavController();
        //SetUp recyclerView
        binding.VideoRecyclerView.setHasFixedSize(true);
        binding.VideoRecyclerView.setAdapter(adapter);
        //setup data
        viewModel.getAllVideos().observe(getViewLifecycleOwner(), Videos -> {
            if (!Videos.isEmpty()) {
                adapter.setVideoModelList(Videos);
            }
        });

        adapter.setVideoModelOnclickHandler(this);
    }

    @Override
    public void onItemClicked(VideoModel data) {
        //Send Data of that video to Parent Activity
        // To notify if there is any update will happen at this video
        viewModel.setVideoCommunicationData(data);
        // Here we Handle onclick listener wo let me show the video
        // And let user change the tag
        Bundle bundle = new Bundle();
        bundle.putSerializable("path", data);

        navController.navigate(R.id.action_priviewFragment_to_videoFragment, bundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.SetCommunicationData("Records");
        getActivity().invalidateOptionsMenu();
    }
}