package com.gomaa.mustacheapp.di;

import android.app.Application;

import androidx.room.Room;

import com.gomaa.mustacheapp.db.RoomDB;
import com.gomaa.mustacheapp.network.VideoDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class RoomDBModule {

    @Provides
    @Singleton
    public static RoomDB provideDB(Application application) {
        return Room.databaseBuilder(application, RoomDB.class, "video")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
    }

    @Provides
    @Singleton
    public static VideoDao provideDao(RoomDB video_DB) {
        return video_DB.videoDao();
    }

}
