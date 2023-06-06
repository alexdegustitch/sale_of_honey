package com.example.medenjaci.beans;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.time.LocalDateTime;

@IgnoreExtraProperties
public class Order {
    private int number_of_days;

    @Exclude
    private String id_order;

    private double total_price;
    private String username;
    private String date_of_order;
    private String status;
    private String date_of_shipping;


    public Order() {

    }

    public Order(int number_of_days, double total_price, String username, String date_of_order, String status, String date_of_shipping) {
        this.number_of_days = number_of_days;
        this.total_price = total_price;
        this.username = username;
        this.date_of_order = date_of_order;
        this.status = status;
        this.date_of_shipping = date_of_shipping;
    }

    public int getNumber_of_days() {
        return number_of_days;
    }

    public void setNumber_of_days(int number_of_days) {
        this.number_of_days = number_of_days;
    }

    public String getId_order() {
        return id_order;
    }

    public void setId_order(String id_order) {
        this.id_order = id_order;
    }

    public double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDate_of_order() {
        return date_of_order;
    }

    public void setDate_of_order(String date_of_order) {
        this.date_of_order = date_of_order;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate_of_shipping() {
        return date_of_shipping;
    }

    public void setDate_of_shipping(String date_of_shipping) {
        this.date_of_shipping = date_of_shipping;
    }
}
