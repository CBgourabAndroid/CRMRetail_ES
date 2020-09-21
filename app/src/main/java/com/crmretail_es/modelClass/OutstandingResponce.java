
package com.crmretail_es.modelClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OutstandingResponce {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("outstanding")
    @Expose
    private Outstanding outstanding;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Outstanding getOutstanding() {
        return outstanding;
    }

    public void setOutstanding(Outstanding outstanding) {
        this.outstanding = outstanding;
    }

}
