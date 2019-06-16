package org.kicksound.Models;

import com.google.gson.annotations.SerializedName;

public class EventTicketsCount {
    @SerializedName("count")
    private Integer count;

    public EventTicketsCount(){};

    public EventTicketsCount(Integer count) {
        this.count = count;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "EventTicketsCount{" +
                "count=" + count +
                '}';
    }
}
