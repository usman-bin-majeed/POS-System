// File: org/example/src/MainClasses/Item.java
package org.example.src.MainClasses;

import java.util.Date;

public abstract class Item {
    private String itemId;
    private String itemName;
    private double itemPrice;
    private int itemQuantity;
    private boolean availability;
    private Date mfgDate;
    private Date expDate;
    private double weight;

    public Item(String itemId, String itemName, double itemPrice, int itemQuantity, boolean availability, Date mfgDate, Date expDate, double weight) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemQuantity = itemQuantity;
        this.availability = availability;
        this.mfgDate = mfgDate;
        this.expDate = expDate;
        this.weight = weight;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public int getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(int itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public boolean isAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public Date getMfgDate() {
        return mfgDate;
    }

    public void setMfgDate(Date mfgDate) {
        this.mfgDate = mfgDate;
    }

    public Date getExpDate() {
        return expDate;
    }

    public void setExpDate(Date expDate) {
        this.expDate = expDate;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public boolean isExpired() {
        return new Date().after(expDate);
    }
}
