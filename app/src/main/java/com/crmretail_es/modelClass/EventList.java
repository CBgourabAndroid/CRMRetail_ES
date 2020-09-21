
package com.crmretail_es.modelClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EventList {

    @SerializedName("event_id")
    @Expose
    private Integer eventId;
    @SerializedName("no_of_attn")
    @Expose
    private Integer noOfAttn;
    @SerializedName("budget")
    @Expose
    private String budget;
    @SerializedName("event_date")
    @Expose
    private String eventDate;
    @SerializedName("start_time")
    @Expose
    private String startTime;
    @SerializedName("time_format")
    @Expose
    private Object timeFormat;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("event_type")
    @Expose
    private String eventType;

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    public Integer getNoOfAttn() {
        return noOfAttn;
    }

    public void setNoOfAttn(Integer noOfAttn) {
        this.noOfAttn = noOfAttn;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public Object getTimeFormat() {
        return timeFormat;
    }

    public void setTimeFormat(Object timeFormat) {
        this.timeFormat = timeFormat;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

}