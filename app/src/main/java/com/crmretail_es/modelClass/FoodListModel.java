package com.crmretail_es.modelClass;

import java.io.File;

public class FoodListModel {

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(String foodPrice) {
        this.foodPrice = foodPrice;
    }


    private String foodName;
    private String foodPrice;

    public File getFoodPic() {
        return foodPic;
    }

    public void setFoodPic(File foodPic) {
        this.foodPic = foodPic;
    }

    private File foodPic;

}
