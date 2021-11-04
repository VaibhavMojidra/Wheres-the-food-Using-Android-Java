package com.vaibhavmojidra.wheresthefood.DataRetrivalClasses;

public class FoodItem {
    private String FoodID,FoodName,Price,imageURL;

    public FoodItem(String foodID, String foodName, String price, String imageURL) {
        FoodID = foodID;
        FoodName = foodName;
        Price = price;
        this.imageURL = imageURL;
    }

    public String getFoodID() {
        return FoodID;
    }

    public String getFoodName() {
        return FoodName;
    }

    public String getPrice() {
        return Price;
    }

    public String getImageURL() {
        return imageURL;
    }
}
