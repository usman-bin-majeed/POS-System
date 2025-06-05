// File: org/example/src/MainClasses/HouseHold.java
package org.example.src.MainClasses;

import java.util.Date;

public class HouseHold extends Item {
    private String materialType;

    public HouseHold(String itemId, String itemName, double itemPrice, int itemQuantity, boolean availability, Date mfgDate, Date expDate, String materialType, double weight) {
        super(itemId, itemName, itemPrice, itemQuantity, availability, mfgDate, expDate, weight);
        this.materialType = materialType;
    }

    public String getMaterialType() {
        return materialType;
    }

    public void setMaterialType(String materialType) {
        this.materialType = materialType;
    }
}