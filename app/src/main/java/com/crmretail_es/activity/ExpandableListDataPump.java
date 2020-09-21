package com.crmretail_es.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListDataPump {
    public static HashMap<String, List<String>> getData() {
        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();



        List<String> CEMENT = new ArrayList<String>();
        CEMENT.add("ULTRATECH");
        CEMENT.add("NOVOCO");
        CEMENT.add("ACC");
        CEMENT.add("AMBUJA");
        CEMENT.add("BALMIA");
        CEMENT.add("OTHERS");

        List<String> TMT = new ArrayList<String>();
        TMT.add("JSW");
        TMT.add("SHYAM STEEL");
        TMT.add("SRMB");
        TMT.add("CAPTAIN");
        TMT.add("ELCTROL STEEL");
        TMT.add("MAGADH");
        TMT.add("KAMDHENU");
        TMT.add("BALMUKUND");
        TMT.add("MAITHON");
        TMT.add("SUPER");
        TMT.add("MONGIA");
        TMT.add("OTHERS");

        expandableListDetail.put("CEMENT", CEMENT);
        expandableListDetail.put("TMT", TMT);
        return expandableListDetail;
    }
}
