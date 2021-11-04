package com.vaibhavmojidra.wheresthefood.DataRetrivalClasses;

public class CartItem {
    private String EmailAdd,FoodID,Foodname,Price;
    private int Quantity;

    public CartItem(String emailAdd, String foodID, String foodname, String price, int quantity) {
        EmailAdd = emailAdd;
        FoodID = foodID;
        Foodname = foodname;
        Price = price;
        Quantity = quantity;
    }

    public String getEmailAdd() {
        return EmailAdd;
    }

    public String getFoodID() {
        return FoodID;
    }

    public String getFoodname() {
        return Foodname;
    }

    public String getPrice() {
        return Price;
    }

    public int getQuantity() {
        return Quantity;
    }
}
