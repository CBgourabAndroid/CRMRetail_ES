
package com.crmretail_es.modelClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Ledger {

    @SerializedName("transaction_date")
    @Expose
    private String transactionDate;
    @SerializedName("transaction_type")
    @Expose
    private String transactionType;
    @SerializedName("transaction_no")
    @Expose
    private String transactionNo;
    @SerializedName("dr_amount")
    @Expose
    private String drAmount;
    @SerializedName("cr_amount")
    @Expose
    private String crAmount;
    @SerializedName("balance")
    @Expose
    private String balance;

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getTransactionNo() {
        return transactionNo;
    }

    public void setTransactionNo(String transactionNo) {
        this.transactionNo = transactionNo;
    }

    public String getDrAmount() {
        return drAmount;
    }

    public void setDrAmount(String drAmount) {
        this.drAmount = drAmount;
    }

    public String getCrAmount() {
        return crAmount;
    }

    public void setCrAmount(String crAmount) {
        this.crAmount = crAmount;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

}
