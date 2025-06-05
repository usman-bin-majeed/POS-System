// File: org/example/src/MainClasses/BakeryProducts.java
package org.example.src.MainClasses;

import java.util.Date;

public class BakeryProducts extends Item {
    private boolean isSugarFree;

    public BakeryProducts(String itemId, String itemName, double itemPrice, int itemQuantity, boolean availability, Date mfgDate, Date expDate, boolean isSugarFree, double weight) {
        super(itemId, itemName, itemPrice, itemQuantity, availability, mfgDate, expDate, weight);
        this.isSugarFree = isSugarFree;
    }

    public boolean isSugarFree() {
        return isSugarFree;
    }

    public void setSugarFree(boolean isSugarFree) {
        this.isSugarFree = isSugarFree;
    }
}