
package com.crmretail_es.modelClass;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FeedbackResponse {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("notclosed")
    @Expose
    private List<Notclosed> notclosed = null;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<Notclosed> getNotclosed() {
        return notclosed;
    }

    public void setNotclosed(List<Notclosed> notclosed) {
        this.notclosed = notclosed;
    }

}
