
package com.gomaa.mustacheapp.ui.Activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.CamcorderProfile;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.Settings;

import android.util.Log;
import android.view.View;

import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gomaa.mustacheapp.BuildConfig;
import com.gomaa.mustacheapp.R;
import com.gomaa.mustacheapp.data.VideoModel;
import com.gomaa.mustacheapp.databinding.ActivityMainBinding;
import com.gomaa.mustacheapp.network.Communicator;
import com.gomaa.mustacheapp.network.OnclickHandler;
import com.gomaa.mustacheapp.ui.Adapters.FiltersAdapter;
import com.gomaa.mustacheapp.ui.BottomSheets.TagBottomSheet;
import com.gomaa.mustacheapp.ui.Fragment.CameraFragment;
import com.gomaa.mustacheapp.ui.ViewModel.MainViewModel;
import com.gomaa.mustacheapp.utils.VideoRecorder;
import com.google.ar.core.AugmentedFace;

import com.google.ar.core.TrackingState;

import com.google.ar.sceneform.ArSceneView;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.rendering.Texture;
import com.google.ar.sceneform.ux.AugmentedFaceNode;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity implements View.OnClickListener, Scene.OnUpdateListener, OnclickHandler<Integer>, Communicator {
    private Texture texture;
    private VideoRecorder videoRecorder;
    private ActivityMainBinding binding;
    private MainViewModel viewModel;
    private ArSceneView sceneView;
    private Scene scene;
    private CameraFragment cameraFragment;
    private final HashMap<AugmentedFace, AugmentedFaceNode> faceNodeMap = new HashMap<>();
    private AugmentedFaceNode faceNode;
    private VideoModel videoModel;
    @Inject
    FiltersAdapter adapter;
    private ActivityResultLauncher<String[]> permissionLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        AskForPermission();
        SetupView();
        videoModel = new VideoModel();

    }

    /**
     * At this method we setup views
     */
    @SuppressLint("CheckResult")
    private void SetupView() {
        //Get last added element
        viewModel.getLastVideo().observe(this, videoModel -> {
            if (videoModel != null)
                Glide.with(this)
                        .load(videoModel.getUri())
                        .into(binding.Storage);
        });
        // SetupAr View
        SetUpArView();
        // Setup Filter adapter
        SetUpFilterAdapter();
        //Handle Onclick Listener
        binding.StartRecording.setOnClickListener(this);
        binding.filters.setOnClickListener(this);
        binding.RemoveFilterView.setOnClickListener(this);
        binding.Storage.setOnClickListener(this);
    }

    /**
     * This Method we only Initialize the view to apply the texture
     */
    private void SetUpArView() {
        cameraFragment = (CameraFragment) getSupportFragmentManager().findFragmentById(R.id.arFragment);
        SetupTexture(R.drawable.must_1);
        sceneView = cameraFragment.getArSceneView();
        sceneView.setCameraStreamRenderPriority(Renderable.RENDER_PRIORITY_FIRST);
        scene = sceneView.getScene();
        scene.addOnUpdateListener(this);
    }

    /**
     * Method which apply  different Mustache Texture
     *
     * @param resource
     */
    private void SetupTexture(int resource) {
        Texture.builder()
                .setSource(this, resource)
                .build()
                .thenAccept(texture -> this.texture = texture);
    }

    /**
     * Method help me to start and resume the video and save it as MP4
     * from class @VideoRecorder
     */
    private void Record() {
        if (videoRecorder == null) {
            videoRecorder = new VideoRecorder();
            videoRecorder.setSceneView(cameraFragment.getArSceneView());

            int orientation = getResources().getConfiguration().orientation;

            videoRecorder.setVideoQuality(CamcorderProfile.QUALITY_HIGH, orientation);

        }
        boolean isRecording = videoRecorder.onToggleRecord();

        if (isRecording) {
            Toast.makeText(MainActivity.this, "Started Recording", Toast.LENGTH_SHORT).show();
            binding.Timer.setVisibility(View.VISIBLE);
            binding.Timer.setBase(SystemClock.elapsedRealtime());
            binding.Timer.start();
        } else {
            Toast.makeText(MainActivity.this, "Recording Stopped", Toast.LENGTH_SHORT).show();
            binding.Timer.setVisibility(View.INVISIBLE);
            binding.Timer.stop();
            long time = SystemClock.elapsedRealtime() - binding.Timer.getBase() - 1000;
            videoModel.setDuration(time);
            DisplayTagBottomSheet();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.StartRecording:
                //Start or stop recording
                Record();
                break;
            case R.id.filters:
                //Open filter table
                SetLayoutVisibleStatue(View.VISIBLE, View.GONE);
                break;
            case R.id.Storage:
                //open Records Storage
                Intent intent = new Intent(getApplicationContext(), PreviewActivity.class);
                startActivity(intent);
                break;
            case R.id.Remove_Filter_View:
                // remove filter layout and display operators layout
                SetLayoutVisibleStatue(View.GONE, View.VISIBLE);
                break;
        }
    }

    @Override
    public void onUpdate(FrameTime frameTime) {
        if (texture == null) {
            return;
        }

        Collection<AugmentedFace> faceList =
                sceneView.getSession().getAllTrackables(AugmentedFace.class);

        // Make new AugmentedFaceNodes for any new faces.
        for (AugmentedFace face : faceList) {
            if (!faceNodeMap.containsKey(face)) {
                faceNode = new AugmentedFaceNode(face);
                faceNode.setParent(scene);
                faceNode.setFaceMeshTexture(texture);
                faceNodeMap.put(face, faceNode);
                Log.d("TAG", "onUpdate: detect");
            }
        }

        // Remove any AugmentedFaceNodes associated with an AugmentedFace that stopped tracking.
        Iterator<Map.Entry<AugmentedFace, AugmentedFaceNode>> iter =
                faceNodeMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<AugmentedFace, AugmentedFaceNode> entry = iter.next();
            AugmentedFace face = entry.getKey();
            if (face.getTrackingState() == TrackingState.STOPPED) {
                AugmentedFaceNode faceNode = entry.getValue();
                faceNode.setParent(null);
                faceNode.setFaceMeshTexture(null);
                iter.remove();
            }
        }
    }

    private void SetLayoutVisibleStatue(int FilterLayoutVisibility, int RecordingLayoutVisibility) {
        binding.FilterLayout.setVisibility(FilterLayoutVisibility);
        binding.RecordingLayout.setVisibility(RecordingLayoutVisibility);
    }

    private void SetUpFilterAdapter() {
        adapter.setOnclickHandler(this);
        binding.FilterRecycler.setHasFixedSize(true);
        binding.FilterRecycler.setAdapter(adapter);
    }

    @Override
    public void onItemClicked(Integer data) {
        faceNodeMap.clear();
        texture = null;
        faceNode.setParent(null);
        Log.d("TAG", "onItemClicked: " + faceNode.getParent());
        SetupTexture(data);
    }

    private void DisplayTagBottomSheet() {
        TagBottomSheet tagBottomSheet = new TagBottomSheet();
        tagBottomSheet.show(getSupportFragmentManager(), "tag");
        tagBottomSheet.setCommunicator(this);
    }


    @Override
    public void communicate(String Data) {
        if (Data == null) {
            Toast.makeText(this, "Video Cancel", Toast.LENGTH_SHORT).show();
        } else {
            // build File Storage
            //Insert Data to our storage
            videoModel.setTag(Data);
            videoModel.setUri(videoRecorder.getVideoPath().getPath());
            viewModel.InsertVideo(videoModel);
            Toast.makeText(this, "Video Added successfully", Toast.LENGTH_SHORT).show();
        }
    }

    boolean isReadPermissionGranted = false, isWritePermissionGranted = false, isAudioPermissionGranted = false, isCameraPermissionGranted=false;

    private void initializePermissions() {
        boolean isReadPermission = ContextCompat.checkSelfPermission(
                getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;

        boolean isWritePermission = ContextCompat.checkSelfPermission(
                getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;

        boolean isAudioPermission = ContextCompat.checkSelfPermission(
                getApplicationContext(),
                Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED;

        boolean isCameraPermission = ContextCompat.checkSelfPermission(
                getApplicationContext(),
                Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;


        boolean minSdk = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q;

        isReadPermissionGranted = isReadPermission;
        isWritePermissionGranted = isWritePermission || minSdk;
        isAudioPermissionGranted = isAudioPermission;
        isCameraPermissionGranted = isCameraPermission;
        ArrayList<String> RequestedPermissions = new ArrayList<>();

        if (!isReadPermissionGranted) {
            RequestedPermissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (!isWritePermissionGranted) {
            RequestedPermissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!isAudioPermissionGranted) {
            RequestedPermissions.add(Manifest.permission.RECORD_AUDIO);
        }
        if (!isCameraPermissionGranted) {
            RequestedPermissions.add(Manifest.permission.CAMERA);
        }

        if (!RequestedPermissions.isEmpty()) {
            String[] requested = new String[RequestedPermissions.size()];
            permissionLauncher.launch(RequestedPermissions.toArray(requested));
        }
    }

    private ActivityResultContracts.RequestMultiplePermissions multiplePermissions;

    public void AskForPermission() {
        multiplePermissions = new ActivityResultContracts.RequestMultiplePermissions();
        permissionLauncher = registerForActivityResult(multiplePermissions, permissions -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                isReadPermissionGranted = permissions.get(Manifest.permission.READ_EXTERNAL_STORAGE);
                isAudioPermissionGranted = permissions.get(Manifest.permission.RECORD_AUDIO);
                isCameraPermissionGranted = Boolean.TRUE.equals(permissions.get(Manifest.permission.CAMERA));
                if (isReadPermissionGranted && isAudioPermissionGranted && isCameraPermissionGranted) {
                    permissionLauncher.unregister();
                }
            } else {
                isReadPermissionGranted = permissions.get(Manifest.permission.READ_EXTERNAL_STORAGE);
                isWritePermissionGranted = permissions.get(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                isAudioPermissionGranted = permissions.get(Manifest.permission.RECORD_AUDIO);
                isCameraPermissionGranted = Boolean.TRUE.equals(permissions.get(Manifest.permission.CAMERA));
                if (isReadPermissionGranted && isWritePermissionGranted && isAudioPermissionGranted && isCameraPermissionGranted) {
                    permissionLauncher.unregister();
                }
            }
        });
        initializePermissions();
    }

}