package com.chris.firebaseauth.scan.models;

public class History {
    private String barcode;

    public History() {

    }

    public History(String barcode) {
        this.barcode = barcode;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
}
