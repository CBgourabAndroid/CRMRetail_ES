
package com.crmretail_es.modelClass.attandence;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AttendanceResponce {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("duties")
    @Expose
    private List<Duty> duties = null;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<Duty> getDuties() {
        return duties;
    }

    public void setDuties(List<Duty> duties) {
        this.duties = duties;
    }

}
