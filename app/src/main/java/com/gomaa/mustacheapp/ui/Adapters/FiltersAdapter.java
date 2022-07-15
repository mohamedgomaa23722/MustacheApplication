package com.gomaa.mustacheapp.ui.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gomaa.mustacheapp.R;
import com.gomaa.mustacheapp.data.FilterResources;
import com.gomaa.mustacheapp.network.OnclickHandler;
import com.gomaa.mustacheapp.ui.ViewHolders.FilterViewHolder;

import java.util.List;

import javax.inject.Inject;

public class FiltersAdapter extends RecyclerView.Adapter<FilterViewHolder> {
    private final List<FilterResources> filterResourcesList;
    private OnclickHandler<Integer> onclickHandler;

    @Inject
    public FiltersAdapter(List<FilterResources> filterResourcesList) {
        this.filterResourcesList = filterResourcesList;
    }


    public void setOnclickHandler(OnclickHandler<Integer> onclickHandler) {
        this.onclickHandler = onclickHandler;
    }

    @NonNull
    @Override
    public FilterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FilterViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.filter_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FilterViewHolder holder, int position) {
        FilterResources filterResource = filterResourcesList.get(position);
        holder.filterImage.setImageResource(filterResource.getResourceImage());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclickHandler.onItemClicked(filterResource.getResourceTexture());
            }
        });
    }

    @Override
    public int getItemCount() {
        return filterResourcesList.size();
    }
}
