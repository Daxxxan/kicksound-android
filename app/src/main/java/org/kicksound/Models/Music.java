package org.kicksound.Models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class Music {
    @SerializedName("id")
    private String id;

    @SerializedName("title")
    private String title;

    @SerializedName("location")
    private String location;

    @SerializedName("releaseDate")
    private Date releaseDate;

    @SerializedName("disabled")
    private boolean disabled;

    @SerializedName("albumId")
    private String albumId;

    @SerializedName("accountId")
    private String accountId;

    @SerializedName("account")
    private Account account;

    @SerializedName("accountWhoLike")
    private List<Account> accountWhoLike;

    public Music() {
    }

    public Music(String title, String location, Date releaseDate) {
        this.title = title;
        this.location = location;
        this.releaseDate = releaseDate;
    }

    public Music(String id, String title, String location, Date releaseDate, boolean disabled, String albumId, String accountId) {
        this.id = id;
        this.title = title;
        this.location = location;
        this.releaseDate = releaseDate;
        this.disabled = disabled;
        this.albumId = albumId;
        this.accountId = accountId;
    }

    public Music(String id, String title, String location, Date releaseDate, boolean disabled, String albumId, String accountId, Account account) {
        this.id = id;
        this.title = title;
        this.location = location;
        this.releaseDate = releaseDate;
        this.disabled = disabled;
        this.albumId = albumId;
        this.accountId = accountId;
        this.account = account;
    }

    public Music(String id, String title, String location, Date releaseDate, boolean disabled, String albumId, String accountId, Account account, List<Account> accountWhoLike) {
        this.id = id;
        this.title = title;
        this.location = location;
        this.releaseDate = releaseDate;
        this.disabled = disabled;
        this.albumId = albumId;
        this.accountId = accountId;
        this.account = account;
        this.accountWhoLike = accountWhoLike;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public Account getAccounts() {
        return account;
    }

    public void setAccounts(Account account) {
        this.account = account;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public List<Account> getAccountWhoLike() {
        return accountWhoLike;
    }

    public void setAccountWhoLike(List<Account> accountWhoLike) {
        this.accountWhoLike = accountWhoLike;
    }

    @Override
    public String toString() {
        return "Music{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", location='" + location + '\'' +
                ", releaseDate=" + releaseDate +
                ", disabled=" + disabled +
                ", albumId='" + albumId + '\'' +
                ", accountId='" + accountId + '\'' +
                ", account=" + account +
                ", accountWhoLike=" + accountWhoLike +
                '}';
    }
}
