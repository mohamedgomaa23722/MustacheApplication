package com.gomaa.mustacheapp.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.gomaa.mustacheapp.data.VideoModel;
import com.gomaa.mustacheapp.network.VideoDao;

@Database(entities = {VideoModel.class}, version = 1)
public abstract class RoomDB extends RoomDatabase {
    public abstract VideoDao videoDao();
}
