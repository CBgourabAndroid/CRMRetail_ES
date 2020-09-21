
package com.crmretail_es.modelClass;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BillResponce {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("bills")
    @Expose
    private List<Bill> bills = null;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<Bill> getBills() {
        return bills;
    }

    public void setBills(List<Bill> bills) {
        this.bills = bills;
    }

}
