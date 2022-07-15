package com.gomaa.mustacheapp.ui.ViewHolders;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gomaa.mustacheapp.R;

public class FilterViewHolder extends RecyclerView.ViewHolder {
    public ImageView filterImage;
    public FilterViewHolder(@NonNull View itemView) {
        super(itemView);
        filterImage = itemView.findViewById(R.id.FilterImage);
    }
}
