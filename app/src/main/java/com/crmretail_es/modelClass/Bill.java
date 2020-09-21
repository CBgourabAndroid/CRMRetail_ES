
package com.crmretail_es.modelClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Bill {

    @SerializedName("transaction_no")
    @Expose
    private String transactionNo;
    @SerializedName("transaction_qty")
    @Expose
    private String transactionQty;
    @SerializedName("dr_amount")
    @Expose
    private String drAmount;
    @SerializedName("transaction_date")
    @Expose
    private String transactionDate;

    public String getTransactionNo() {
        return transactionNo;
    }

    public void setTransactionNo(String transactionNo) {
        this.transactionNo = transactionNo;
    }

    public String getTransactionQty() {
        return transactionQty;
    }

    public void setTransactionQty(String transactionQty) {
        this.transactionQty = transactionQty;
    }

    public String getDrAmount() {
        return drAmount;
    }

    public void setDrAmount(String drAmount) {
        this.drAmount = drAmount;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

}
