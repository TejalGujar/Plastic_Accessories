package com.example.plasticaccessories;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class AddProduct implements Serializable
{

    String pName;
    String pType;
    Float pPrice;


    public AddProduct(){
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pId) {
        this.pName = pName;
    }

    public String getpType(){
        return pType;
    }

    public Float getpPrice(){
        return pPrice;
    }


    public Map<String,Object> toMap(){
        Map<String,Object> mMap = new HashMap<>();
        mMap.put("productName",this.pName);
        mMap.put("productType",this.pType);
        mMap.put("productPrice",this.pPrice);

        return mMap;
    }

}
