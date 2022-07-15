package com.gomaa.mustacheapp.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "VideoTable")
public class VideoModel implements Serializable {
    @PrimaryKey (autoGenerate = true)
    @ColumnInfo(name = "Video_ID")
    private int id;
    @ColumnInfo(name = "Video_uri")
    private String uri;
    @ColumnInfo(name = "Video_Timer")
    private long duration;
    @ColumnInfo(name = "Video_Tag")
    private String Tag;

    public VideoModel() {
    }

    public VideoModel(String uri, long duration, String tag) {
        this.uri = uri;
        this.duration = duration;
        Tag = tag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getTag() {
        return Tag;
    }

    public void setTag(String tag) {
        Tag = tag;
    }
}
