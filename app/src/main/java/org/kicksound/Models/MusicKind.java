package org.kicksound.Models;

import com.google.gson.annotations.SerializedName;

public class MusicKind {
    @SerializedName("id")
    String id;

    @SerializedName("name")
    String name;

    public MusicKind(String id, String name) {
        this.id = id;
        this.name = name;
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
}
