
package com.crmretail_es.modelClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderList {

    @SerializedName("order_id")
    @Expose
    private Integer orderId;
    @SerializedName("customer_id")
    @Expose
    private Integer customerId;
    @SerializedName("total_amount")
    @Expose
    private String totalAmount;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("dealerApproved")
    @Expose
    private String dealerApproved;
    @SerializedName("orgApproved")
    @Expose
    private String orgApproved;
    @SerializedName("received")
    @Expose
    private String received;
    @SerializedName("received_date")
    @Expose
    private String receivedDate;
    @SerializedName("products")
    @Expose
    private String products;
    @SerializedName("qualtities")
    @Expose
    private String qualtities;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getDealerApproved() {
        return dealerApproved;
    }

    public void setDealerApproved(String dealerApproved) {
        this.dealerApproved = dealerApproved;
    }

    public String getOrgApproved() {
        return orgApproved;
    }

    public void setOrgApproved(String orgApproved) {
        this.orgApproved = orgApproved;
    }

    public String getReceived() {
        return received;
    }

    public void setReceived(String received) {
        this.received = received;
    }

    public String getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(String receivedDate) {
        this.receivedDate = receivedDate;
    }

    public String getProducts() {
        return products;
    }

    public void setProducts(String products) {
        this.products = products;
    }

    public String getQualtities() {
        return qualtities;
    }

    public void setQualtities(String qualtities) {
        this.qualtities = qualtities;
    }

}
