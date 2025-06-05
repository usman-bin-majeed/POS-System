// File: org/example/src/MainClasses/Frozen.java
package org.example.src.MainClasses;

import java.util.Date;

public class Frozen extends Item {
    private double storageTemperature;

    public Frozen(String itemId, String itemName, double itemPrice, int itemQuantity, boolean availability, Date mfgDate, Date expDate, double storageTemperature, double weight) {
        super(itemId, itemName, itemPrice, itemQuantity, availability, mfgDate, expDate, weight);
        this.storageTemperature = storageTemperature;
    }

    public double getStorageTemperature() {
        return storageTemperature;
    }

    public void setStorageTemperature(double storageTemperature) {
        this.storageTemperature = storageTemperature;
    }
}