package com.example.laba10.model;

import java.io.Serializable;

public class Rental implements Serializable {
    private int id;
    private String clientName;
    private String phoneNumber;
    private String rentalDate;
    private String itemName;
    private double price;
    private String duration;

    public Rental() {
    }

    public Rental(int id, String clientName, String phoneNumber, String rentalDate,
                  String itemName, double price, String duration) {
        this.id = id;
        this.clientName = clientName;
        this.phoneNumber = phoneNumber;
        this.rentalDate = rentalDate;
        this.itemName = itemName;
        this.price = price;
        this.duration = duration;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getClientName() { return clientName; }
    public void setClientName(String clientName) { this.clientName = clientName; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getRentalDate() { return rentalDate; }
    public void setRentalDate(String rentalDate) { this.rentalDate = rentalDate; }
    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }
}