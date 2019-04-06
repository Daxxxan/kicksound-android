package org.kicksound.Models;

import com.google.gson.annotations.SerializedName;

public class Login {
    @SerializedName("id")
    private String id;
    private String email;
    private String password;
    private static Login instance = null;

    public Login(String id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public Login(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Login(String id) {
        this.id = id;
    }

    public Login() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static Login getLogin() {
        if(instance == null) {
            instance = new Login();
        }
        return instance;
    }
}
