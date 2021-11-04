package com.vaibhavmojidra.wheresthefood.DataRetrivalClasses;

public class OrderItem {
    private String FoodName,Price;
    private int Quantity,TotalPrice;
    private String Token;

    public OrderItem(String foodName, String price, int quantity, int totalPrice, String token) {
        FoodName = foodName;
        Price = price;
        Quantity = quantity;
        TotalPrice = totalPrice;
        Token = token;
    }

    public String getFoodName() {
        return FoodName;
    }

    public String getPrice() {
        return Price;
    }

    public int getQuantity() {
        return Quantity;
    }

    public int getTotalPrice() {
        return TotalPrice;
    }

    public String getToken() {
        return Token;
    }
}
