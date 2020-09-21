
package com.crmretail_es.modelClass;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EventListResponse {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("eventList")
    @Expose
    private List<EventList> eventList = null;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<EventList> getEventList() {
        return eventList;
    }

    public void setEventList(List<EventList> eventList) {
        this.eventList = eventList;
    }

}
