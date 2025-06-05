// File: org/example/src/MainClasses/DairyProducts.java
package org.example.src.MainClasses;

import java.util.Date;

public class DairyProducts extends Item {
    private double fatContent;

    public DairyProducts(String itemId, String itemName, double itemPrice, int itemQuantity, boolean availability, Date mfgDate, Date expDate, double fatContent, double weight) {
        super(itemId, itemName, itemPrice, itemQuantity, availability, mfgDate, expDate, weight);
        this.fatContent = fatContent;
    }

    public double getFatContent() {
        return fatContent;
    }

    public void setFatContent(double fatContent) {
        this.fatContent = fatContent;
    }
}