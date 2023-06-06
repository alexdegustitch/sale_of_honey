package com.example.medenjaci.beans;

public class OrderItem {

    private int id_item_order;
    private int id_item;
    private String id_order;

    private double item_price;
    private int quantity;
    private double total_price;
    private String name;

    public OrderItem() {

    }

    public OrderItem(int id_orderitem, int id_item, String id_order, int quantity, double item_price, double total_price, String item_name) {
        this.id_item_order = id_orderitem;
        this.id_item = id_item;
        this.id_order = id_order;
        this.quantity = quantity;
        this.item_price = item_price;
        this.total_price = total_price;
        this.name = item_name;
    }

    public int getId_item_order() {
        return id_item_order;
    }

    public void setId_item_order(int id_item_order) {
        this.id_item_order = id_item_order;
    }

    public double getItem_price() {
        return item_price;
    }

    public void setItem_price(double item_price) {
        this.item_price = item_price;
    }

    public int getId_item() {
        return id_item;
    }

    public void setId_item(int id_item) {
        this.id_item = id_item;
    }

    public String getId_order() {
        return id_order;
    }

    public void setId_order(String id_order) {
        this.id_order = id_order;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
