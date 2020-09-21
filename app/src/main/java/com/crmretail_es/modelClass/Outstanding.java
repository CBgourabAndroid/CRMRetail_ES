
package com.crmretail_es.modelClass;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Outstanding {

    @SerializedName("totalDues")
    @Expose
    private Integer totalDues;
    @SerializedName("Upto 30 Days")
    @Expose
    private Integer upto30Days;
    @SerializedName("31 to 45 Days")
    @Expose
    private Integer _31To45Days;
    @SerializedName("46 to 60 Days")
    @Expose
    private Integer _46To60Days;
    @SerializedName("61 to 90 Days")
    @Expose
    private Integer _61To90Days;
    @SerializedName("More than 90 Days")
    @Expose
    private Integer moreThan90Days;
    @SerializedName("dealerDues")
    @Expose
    private List<Object> dealerDues = null;

    public Integer getTotalDues() {
        return totalDues;
    }

    public void setTotalDues(Integer totalDues) {
        this.totalDues = totalDues;
    }

    public Integer getUpto30Days() {
        return upto30Days;
    }

    public void setUpto30Days(Integer upto30Days) {
        this.upto30Days = upto30Days;
    }

    public Integer get31To45Days() {
        return _31To45Days;
    }

    public void set31To45Days(Integer _31To45Days) {
        this._31To45Days = _31To45Days;
    }

    public Integer get46To60Days() {
        return _46To60Days;
    }

    public void set46To60Days(Integer _46To60Days) {
        this._46To60Days = _46To60Days;
    }

    public Integer get61To90Days() {
        return _61To90Days;
    }

    public void set61To90Days(Integer _61To90Days) {
        this._61To90Days = _61To90Days;
    }

    public Integer getMoreThan90Days() {
        return moreThan90Days;
    }

    public void setMoreThan90Days(Integer moreThan90Days) {
        this.moreThan90Days = moreThan90Days;
    }

    public List<Object> getDealerDues() {
        return dealerDues;
    }

    public void setDealerDues(List<Object> dealerDues) {
        this.dealerDues = dealerDues;
    }

}
