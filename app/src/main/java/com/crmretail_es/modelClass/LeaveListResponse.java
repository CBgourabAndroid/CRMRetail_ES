
package com.crmretail_es.modelClass;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LeaveListResponse {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("leaveList")
    @Expose
    private List<LeaveList> leaveList = null;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<LeaveList> getLeaveList() {
        return leaveList;
    }

    public void setLeaveList(List<LeaveList> leaveList) {
        this.leaveList = leaveList;
    }

}
