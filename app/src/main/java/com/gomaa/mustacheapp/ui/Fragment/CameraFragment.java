package com.gomaa.mustacheapp.ui.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.gomaa.mustacheapp.R;
import com.gomaa.mustacheapp.databinding.FragmentCameraBinding;
import com.google.ar.core.Config;
import com.google.ar.core.Session;
import com.google.ar.sceneform.ux.ArFragment;

import java.util.EnumSet;
import java.util.Set;

import dagger.hilt.android.AndroidEntryPoint;

public class CameraFragment extends ArFragment {

    private FragmentCameraBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCameraBinding.inflate(getLayoutInflater());
        FrameLayout frameLayout = (FrameLayout) super.onCreateView(inflater,container,savedInstanceState);
        getPlaneDiscoveryController().hide();
        getPlaneDiscoveryController().setInstructionView(null);

        return frameLayout;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    protected Config getSessionConfiguration(Session session) {
       Config config = new Config(session);
       config.setAugmentedFaceMode(Config.AugmentedFaceMode.MESH3D);

       this.getArSceneView().setupSession(session);
       return config;
    }

    @Override
    protected Set<Session.Feature> getSessionFeatures() {
        return EnumSet.of(Session.Feature.FRONT_CAMERA);
    }
}