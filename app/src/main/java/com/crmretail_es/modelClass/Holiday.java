
package com.crmretail_es.modelClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Holiday {

    @SerializedName("holiday_date")
    @Expose
    private String holidayDate;
    @SerializedName("purpose")
    @Expose
    private String purpose;

    public String getHolidayDate() {
        return holidayDate;
    }

    public void setHolidayDate(String holidayDate) {
        this.holidayDate = holidayDate;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

}
