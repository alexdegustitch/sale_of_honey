package com.example.medenjaci.beans;

public class Item {
    private int id_item;
    private double price;
    private String use_desc;
    private String name;
    private String desc;

    public Item(){

    }
    public Item(int id_item, double price, String use_desc, String name, String desc) {
        this.id_item = id_item;
        this.price = price;
        this.use_desc = use_desc;
        this.name = name;
        this.desc = desc;
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

    public String getUse_desc() {
        return use_desc;
    }

    public void setUse_desc(String use_desc) {
        this.use_desc = use_desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
