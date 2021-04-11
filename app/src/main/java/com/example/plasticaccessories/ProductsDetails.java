package com.example.plasticaccessories;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ProductsDetails implements Serializable {

    String pId;
    String pName;
    String pType;
    Float pPrice;

    public ProductsDetails() {
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getpName() {
        return pName;
    }

//    public void setpName(String pId) {
//        this.pName = pName;
//    }

    public String getpType() {
        return pType;
    }

    public Float getpPrice() {
        return pPrice;
    }


    public Map<String, Object> toMap() {
        Map<String, Object> mMap = new HashMap<>();
        mMap.put("productName", this.pName);
        mMap.put("productType", this.pType);
        mMap.put("productPrice", this.pPrice);
        mMap.put("pImage"," ");

        return mMap;
    }
}
