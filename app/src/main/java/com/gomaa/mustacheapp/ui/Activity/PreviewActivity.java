package com.gomaa.mustacheapp.ui.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.gomaa.mustacheapp.R;
import com.gomaa.mustacheapp.data.VideoModel;
import com.gomaa.mustacheapp.network.Communicator;
import com.gomaa.mustacheapp.ui.BottomSheets.TagBottomSheet;
import com.gomaa.mustacheapp.ui.ViewModel.MainViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class PreviewActivity extends AppCompatActivity implements Communicator {
    private MainViewModel viewModel;
    private MenuItem editItem;
    private VideoModel currentVideoModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        getSupportActionBar().setTitle("Records");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        viewModel.getCommunicator().observe(this, Data -> {
            getSupportActionBar().setTitle(Data);
        });
        viewModel.getVideoCommunication().observe(this, videoModel -> currentVideoModel = videoModel);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        editItem = menu.findItem(R.id.EditTag);
        editItem.setVisible(!getSupportActionBar().getTitle().toString().equals("Records"));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.EditTag) {
            DisplayTagBottomSheet();
        } else {
            if (getSupportActionBar().getTitle().equals("Records")) {
                finish();
            } else {
                NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
                NavController navController = navHostFragment.getNavController();
                navController.popBackStack();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void DisplayTagBottomSheet() {
        TagBottomSheet tagBottomSheet = new TagBottomSheet();
        tagBottomSheet.show(getSupportFragmentManager(), "tag");
        tagBottomSheet.setCommunicator(this);
        tagBottomSheet.setTag(currentVideoModel.getTag());
    }

    @Override
    public void communicate(String Data) {
        //Check if Data is not null
        if (Data != null) {
            //Then Check if Data is not changed
            if (!Data.equals(currentVideoModel.getTag())) {
                //Then update this Item
                viewModel.updateVideoTag(currentVideoModel.getId(), Data);
                //Then change the current action bar title
                getSupportActionBar().setTitle(Data);
            }
        }
    }
}