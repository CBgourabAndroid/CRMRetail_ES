
package com.crmretail_es.modelClass;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DutyResponce {

    @SerializedName("duties")
    @Expose
    private List<Duty> duties = null;

    public List<Duty> getDuties() {
        return duties;
    }

    public void setDuties(List<Duty> duties) {
        this.duties = duties;
    }

}
