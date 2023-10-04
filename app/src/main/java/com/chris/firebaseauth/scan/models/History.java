package com.chris.firebaseauth.scan.models;

public class History {
    private String barcode;
    private String productName;

    public History(String barcode, String productName) {
        this.barcode = barcode;
        this.productName = productName;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
