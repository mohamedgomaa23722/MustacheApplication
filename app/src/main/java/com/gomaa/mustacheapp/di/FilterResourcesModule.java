package com.gomaa.mustacheapp.di;

import com.gomaa.mustacheapp.R;
import com.gomaa.mustacheapp.data.FilterResources;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class FilterResourcesModule {

    @Provides
    @Singleton
    public List<FilterResources> FilterRes() {
        List<FilterResources> resources = new ArrayList<>();
        resources.add(new FilterResources(R.drawable.must_1_prev, R.drawable.must_1));
        resources.add(new FilterResources(R.drawable.must_2_prev, R.drawable.must_2));
        resources.add(new FilterResources(R.drawable.must_3_prev, R.drawable.must_3));
        resources.add(new FilterResources(R.drawable.must_4_prev, R.drawable.must_4));
        resources.add(new FilterResources(R.drawable.must_5_prev, R.drawable.must_5));
        return resources;
    }
}
