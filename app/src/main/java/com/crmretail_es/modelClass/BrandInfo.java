
package com.crmretail_es.modelClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BrandInfo {

    @SerializedName("current_brand")
    @Expose
    private String currentBrand;
    @SerializedName("yearly_lifting")
    @Expose
    private String yearlyLifting;

    public String getCurrentBrand() {
        return currentBrand;
    }

    public void setCurrentBrand(String currentBrand) {
        this.currentBrand = currentBrand;
    }

    public String getYearlyLifting() {
        return yearlyLifting;
    }

    public void setYearlyLifting(String yearlyLifting) {
        this.yearlyLifting = yearlyLifting;
    }

}
