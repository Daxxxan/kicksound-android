package org.kicksound.Models;

import com.google.gson.annotations.SerializedName;

public class Account {
    @SerializedName("id")
    private String id;

    @SerializedName("firstname")
    private String firstname;

    @SerializedName("lastname")
    private String lastname;

    @SerializedName("username")
    private String username;

    @SerializedName("type")
    private Integer type;

    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

    @SerializedName("description")
    private String description;

    @SerializedName("picture")
    private String picture;

    private String accessToken;

    public Account(String id, String firstname, String lastname, String username, int type, String email, String password, String description, String accessToken, String picture) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.type = type;
        this.email = email;
        this.password = password;
        this.description = description;
        this.accessToken = accessToken;
        this.picture = picture;
    }

    public Account(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public Account(String picture) {
        this.picture = picture;
    }

    public Account() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id='" + id + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", username='" + username + '\'' +
                ", type=" + type +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", description='" + description + '\'' +
                ", picture='" + picture + '\'' +
                ", accessToken='" + accessToken + '\'' +
                '}';
    }
}
