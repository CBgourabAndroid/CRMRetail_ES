
package com.crmretail_es.modelClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Discussion {

    @SerializedName("discussion")
    @Expose
    private String discussion;
    @SerializedName("dis_date")
    @Expose
    private String disDate;

    public String getDiscussion() {
        return discussion;
    }

    public void setDiscussion(String discussion) {
        this.discussion = discussion;
    }

    public String getDisDate() {
        return disDate;
    }

    public void setDisDate(String disDate) {
        this.disDate = disDate;
    }

}
