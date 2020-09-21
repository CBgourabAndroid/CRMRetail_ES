
package com.crmretail_es.modelClass;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FeedbackDetailsResponse {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("regInfo")
    @Expose
    private List<RegInfo> regInfo = null;
    @SerializedName("brandInfo")
    @Expose
    private List<BrandInfo> brandInfo = null;
    @SerializedName("discussion")
    @Expose
    private List<Discussion> discussion = null;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<RegInfo> getRegInfo() {
        return regInfo;
    }

    public void setRegInfo(List<RegInfo> regInfo) {
        this.regInfo = regInfo;
    }

    public List<BrandInfo> getBrandInfo() {
        return brandInfo;
    }

    public void setBrandInfo(List<BrandInfo> brandInfo) {
        this.brandInfo = brandInfo;
    }

    public List<Discussion> getDiscussion() {
        return discussion;
    }

    public void setDiscussion(List<Discussion> discussion) {
        this.discussion = discussion;
    }

}
