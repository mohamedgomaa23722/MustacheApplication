package com.gomaa.mustacheapp.ui.BottomSheets;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.gomaa.mustacheapp.R;
import com.gomaa.mustacheapp.databinding.TagBottomSheetBinding;
import com.gomaa.mustacheapp.network.Communicator;
import com.gomaa.mustacheapp.network.OnclickHandler;
import com.gomaa.mustacheapp.ui.ViewModel.MainViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class TagBottomSheet extends BottomSheetDialogFragment implements View.OnClickListener {
    private TagBottomSheetBinding binding;
    private Communicator communicator;
    private String Tag;

    public void setTag(String tag) {
        Tag = tag;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = TagBottomSheetBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SetUpView();
    }

    private void SetUpView() {
        binding.submitButton.setOnClickListener(this);
        binding.cancelButton.setOnClickListener(this);

        if (Tag != null) {
            binding.TagEditText.setText(Tag);
        }
    }

    private boolean ValidateTagInput(String Tag) {
        //check if Tag is Empty
        if (Tag.isEmpty() || Tag == null) {
            //Show Error
            binding.textInputLayout.setError("Please Enter The Tag");
            communicator.communicate(null);
            return false;
        } else {
            communicator.communicate(Tag);
            this.dismiss();
            return true;
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.submit_button) {
            ValidateTagInput(binding.TagEditText.getText().toString());
        } else {
            communicator.communicate(null);
            this.dismiss();
        }
    }

    public void setCommunicator(Communicator communicator) {
        this.communicator = communicator;
    }
}
