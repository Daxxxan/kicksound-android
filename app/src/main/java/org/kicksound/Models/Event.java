package org.kicksound.Models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Event {
    @SerializedName("id")
    private String id;

    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String description;

    @SerializedName("ticketsNumber")
    private int ticketsNumber;

    @SerializedName("picture")
    private String picture;

    @SerializedName("disabled")
    private boolean disabled;

    @SerializedName("accountId")
    private int accountId;

    @SerializedName("date")
    private Date date;

    public Event(String title, String description, int ticketsNumber) {
        this.title = title;
        this.description = description;
        this.ticketsNumber = ticketsNumber;
    }

    public Event(String title, String description, int ticketsNumber, String picture, Date date) {
        this.title = title;
        this.description = description;
        this.ticketsNumber = ticketsNumber;
        this.picture = picture;
        this.date = date;
    }

    public Event(){}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTicketsNumber() {
        return ticketsNumber;
    }

    public void setTicketsNumber(int ticketsNumber) {
        this.ticketsNumber = ticketsNumber;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Date isDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
