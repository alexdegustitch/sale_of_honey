package com.example.medenjaci.beans;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Cart {

    private int id_item;
    private double price;
    private int quantity;
    private String user;
    private double item_price;
    private String item_name;

    @Exclude
    private String id_cart;

    public Cart() {

    }

    public Cart(int id_item, double price, double item_price, int quantity, String username, String item_name) {

        this.id_item = id_item;
        this.item_price = item_price;
        this.price = price;
        this.quantity = quantity;
        this.user = username;
        this.item_name = item_name;
    }


    public int getId_item() {
        return id_item;
    }

    public void setId_item(int id_item) {
        this.id_item = id_item;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public double getItem_price() {
        return item_price;
    }

    public void setItem_price(double item_price) {
        this.item_price = item_price;
    }

    public String getId_cart() {
        return id_cart;
    }

    public void setId_cart(String id_cart) {
        this.id_cart = id_cart;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }
}
