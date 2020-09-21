package com.crmretail_es.modelClass;

import java.io.File;

public class PublicTravelModel {


    public String getTravelType() {
        return TravelType;
    }

    public void setTravelType(String travelType) {
        TravelType = travelType;
    }

    private String TravelType;
    private String PublicTravelWith;
    private String PublicStartLocation;
    private String PublicEndLocation;
    private String PublicAmt;

    public File getSupportingPic() {
        return supportingPic;
    }

    public void setSupportingPic(File supportingPic) {
        this.supportingPic = supportingPic;
    }

    private File supportingPic;

    public String getPublicTravelWith() {
        return PublicTravelWith;
    }

    public void setPublicTravelWith(String publicTravelWith) {
        PublicTravelWith = publicTravelWith;
    }

    public String getPublicStartLocation() {
        return PublicStartLocation;
    }

    public void setPublicStartLocation(String publicStartLocation) {
        PublicStartLocation = publicStartLocation;
    }

    public String getPublicEndLocation() {
        return PublicEndLocation;
    }

    public void setPublicEndLocation(String publicEndLocation) {
        PublicEndLocation = publicEndLocation;
    }

    public String getPublicAmt() {
        return PublicAmt;
    }

    public void setPublicAmt(String publicAmt) {
        PublicAmt = publicAmt;
    }



}