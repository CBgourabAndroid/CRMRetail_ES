
package com.crmretail_es.modelClass.attandence;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Duty {

    @SerializedName("duty_date")
    @Expose
    private String dutyDate;
    @SerializedName("duty_start")
    @Expose
    private String dutyStart;
    @SerializedName("duty_end")
    @Expose
    private Object dutyEnd;
    @SerializedName("duty_time")
    @Expose
    private String dutyTime;
    @SerializedName("remarks")
    @Expose
    private String remarks;

    public String getDutyDate() {
        return dutyDate;
    }

    public void setDutyDate(String dutyDate) {
        this.dutyDate = dutyDate;
    }

    public String getDutyStart() {
        return dutyStart;
    }

    public void setDutyStart(String dutyStart) {
        this.dutyStart = dutyStart;
    }

    public Object getDutyEnd() {
        return dutyEnd;
    }

    public void setDutyEnd(Object dutyEnd) {
        this.dutyEnd = dutyEnd;
    }

    public String getDutyTime() {
        return dutyTime;
    }

    public void setDutyTime(String dutyTime) {
        this.dutyTime = dutyTime;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

}
