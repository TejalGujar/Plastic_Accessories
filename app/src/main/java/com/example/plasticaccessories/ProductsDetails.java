package com.example.plasticaccessories;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ProductsDetails implements Serializable {

    String productId;
    String productName;
    String productType;
    Float productPrice;

    //constructor
    public ProductsDetails(String productName, String productType, Float productPrice) {
        this.productName = productName;
        this.productType = productType;
        this.productPrice = productPrice;
    }

    //getter and setter
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public Float getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Float productPrice) {
        this.productPrice = productPrice;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> mMap = new HashMap<>();
        mMap.put("productName", this.productName);
        mMap.put("productType", this.productType);
        mMap.put("productPrice", this.productPrice);
        mMap.put("pImage"," ");

        return mMap;
    }
}
