package com.crmretail_es.modelClass;

import java.io.File;

public class PrivateTravelModel {

    private String TravelType;
    private String Travelwith;
    private String privateTransKm;

    public File getSopportPic() {
        return sopportPic;
    }

    public void setSopportPic(File sopportPic) {
        this.sopportPic = sopportPic;
    }

    private File sopportPic;

    public String getTravelType() {
        return TravelType;
    }

    public void setTravelType(String travelType) {
        TravelType = travelType;
    }

    public String getTravelwith() {
        return Travelwith;
    }

    public void setTravelwith(String travelwith) {
        Travelwith = travelwith;
    }

    public String getPrivateTransKm() {
        return privateTransKm;
    }

    public void setPrivateTransKm(String privateTransKm) {
        this.privateTransKm = privateTransKm;
    }



}
