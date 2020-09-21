
package com.crmretail_es.modelClass;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AreaInfo {

    @SerializedName("zone_id")
    @Expose
    private Integer zoneId;
    @SerializedName("zone_name")
    @Expose
    private String zoneName;
    @SerializedName("state_id")
    @Expose
    private Integer stateId;
    @SerializedName("state_name")
    @Expose
    private String stateName;
    @SerializedName("zone_array")
    @Expose
    private List<ZoneArray> zoneArray = null;

    public Integer getZoneId() {
        return zoneId;
    }

    public void setZoneId(Integer zoneId) {
        this.zoneId = zoneId;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public Integer getStateId() {
        return stateId;
    }

    public void setStateId(Integer stateId) {
        this.stateId = stateId;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public List<ZoneArray> getZoneArray() {
        return zoneArray;
    }

    public void setZoneArray(List<ZoneArray> zoneArray) {
        this.zoneArray = zoneArray;
    }

}
