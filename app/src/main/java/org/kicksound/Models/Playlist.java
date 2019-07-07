package org.kicksound.Models;

import com.google.gson.annotations.SerializedName;

public class Playlist {
    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("disabled")
    private boolean disabled;

    @SerializedName("accountId")
    private String accountId;

    public Playlist(){};

    public Playlist(String name) {
        this.name = name;
    }

    public Playlist(String id, String name, boolean disabled, String accountId) {
        this.id = id;
        this.name = name;
        this.disabled = disabled;
        this.accountId = accountId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    @Override
    public String toString() {
        return "Playlists{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", disabled=" + disabled +
                ", accountId='" + accountId + '\'' +
                '}';
    }
}
