package com.crmretail_es.modelClass;

/**
 * Created by Parsania Hardik on 17-Apr-18.
 */

/**
 * Created by Parsania Hardik on 03-Jan-17.
 */
public class EditModel {

    private String editTextValue;

    private Integer productId;
    private String productName;
    private String productPrice;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getEditTextValue() {
        return editTextValue;
    }

    public void setEditTextValue(String editTextValue) {
        this.editTextValue = editTextValue;
    }
}