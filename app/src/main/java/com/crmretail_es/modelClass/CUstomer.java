
package com.crmretail_es.modelClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CUstomer {

    @SerializedName("customer_id")
    @Expose
    private Integer customerId;
    @SerializedName("parent_id")
    @Expose
    private Integer parentId;
    @SerializedName("customer_name")
    @Expose
    private String customerName;
    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("customer_address")
    @Expose
    private String customerAddress;
    @SerializedName("customer_district")
    @Expose
    private Integer customerDistrict;
    @SerializedName("customer_gst")
    @Expose
    private String customerGst;
    @SerializedName("customer_owner")
    @Expose
    private String customerOwner;
    @SerializedName("customer_contact")
    @Expose
    private String customerContact;
    @SerializedName("customer_secondary_email")
    @Expose
    private String customerSecondaryEmail;
    @SerializedName("customer_contact_person")
    @Expose
    private String customerContactPerson;

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public Integer getCustomerDistrict() {
        return customerDistrict;
    }

    public void setCustomerDistrict(Integer customerDistrict) {
        this.customerDistrict = customerDistrict;
    }

    public String getCustomerGst() {
        return customerGst;
    }

    public void setCustomerGst(String customerGst) {
        this.customerGst = customerGst;
    }

    public String getCustomerOwner() {
        return customerOwner;
    }

    public void setCustomerOwner(String customerOwner) {
        this.customerOwner = customerOwner;
    }

    public String getCustomerContact() {
        return customerContact;
    }

    public void setCustomerContact(String customerContact) {
        this.customerContact = customerContact;
    }

    public String getCustomerSecondaryEmail() {
        return customerSecondaryEmail;
    }

    public void setCustomerSecondaryEmail(String customerSecondaryEmail) {
        this.customerSecondaryEmail = customerSecondaryEmail;
    }

    public String getCustomerContactPerson() {
        return customerContactPerson;
    }

    public void setCustomerContactPerson(String customerContactPerson) {
        this.customerContactPerson = customerContactPerson;
    }

}
