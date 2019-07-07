package org.kicksound.Models;

import com.google.gson.annotations.SerializedName;

public class Mark {
    @SerializedName("id")
    private String id;

    @SerializedName("value")
    private float value;

    @SerializedName("musicId")
    private String musicId;

    public Mark(float value, String musicId) {
        this.value = value;
        this.musicId = musicId;
    }

    public Mark(String id, float value, String musicId) {
        this.id = id;
        this.value = value;
        this.musicId = musicId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public String getMusicId() {
        return musicId;
    }

    public void setMusicId(String musicId) {
        this.musicId = musicId;
    }
}
