// File: org/example/src/MainClasses/Beverages.java
package org.example.src.MainClasses;

import java.util.Date;

public class Beverages extends Item {
    private boolean isAlcoholic;

    public Beverages(String itemId, String itemName, double itemPrice, int itemQuantity, boolean availability, Date mfgDate, Date expDate, boolean isAlcoholic, double weight) {
        super(itemId, itemName, itemPrice, itemQuantity, availability, mfgDate, expDate, weight);
        this.isAlcoholic = isAlcoholic;
    }

    public boolean isAlcoholic() {
        return isAlcoholic;
    }

    public void setAlcoholic(boolean isAlcoholic) {
        this.isAlcoholic = isAlcoholic;
    }
}