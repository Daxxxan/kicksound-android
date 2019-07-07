package org.kicksound.Models;

import com.google.gson.annotations.SerializedName;

public class Ticket {
    @SerializedName("id")
    private String id;

    @SerializedName("price")
    private Double price;

    @SerializedName("disabled")
    private boolean disabled;

    @SerializedName("accountId")
    private String accountId;

    @SerializedName("eventId")
    private String eventId;

    public Ticket(){};

    public Ticket(Double price) {
        this.price = price;
    }

    public Ticket(Double price, String accountId) {
        this.price = price;
        this.accountId = accountId;
    }

    public Ticket(String id, Double price, boolean disabled, String accountId, String eventId) {
        this.id = id;
        this.price = price;
        this.disabled = disabled;
        this.accountId = accountId;
        this.eventId = eventId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
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

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id='" + id + '\'' +
                ", price=" + price +
                ", disabled=" + disabled +
                ", accountId='" + accountId + '\'' +
                ", eventId='" + eventId + '\'' +
                '}';
    }
}
