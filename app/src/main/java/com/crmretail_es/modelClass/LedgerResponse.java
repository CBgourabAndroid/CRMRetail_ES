package com.crmretail_es.modelClass;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LedgerResponse {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("ledger")
    @Expose
    private List<Ledger> ledger = null;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<Ledger> getLedger() {
        return ledger;
    }

    public void setLedger(List<Ledger> ledger) {
        this.ledger = ledger;
    }

}
