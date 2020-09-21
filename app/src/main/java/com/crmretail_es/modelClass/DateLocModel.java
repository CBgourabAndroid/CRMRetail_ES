package com.crmretail_es.modelClass;

import java.util.ArrayList;

public class DateLocModel {

    private String date;
    private ArrayList<String> loc;
    private ArrayList<String> locName;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<String> getLoc() {
        return loc;
    }

    public void setLoc(ArrayList<String> loc) {
        this.loc = loc;
    }

    public ArrayList<String> getLocName() {
        return locName;
    }

    public void setLocName(ArrayList<String> locName) {
        this.locName = locName;
    }
}
