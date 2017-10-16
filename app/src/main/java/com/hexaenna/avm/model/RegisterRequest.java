package com.hexaenna.avm.model;

/**
 * Created by admin on 10/16/2017.
 */

public class RegisterRequest {
   private String name,comp_name,city,pincode,mobile,email;

    public RegisterRequest(String name,String comp_name,String city,String pincode,String mobile,String email  ){
        this.name = name;
        this.comp_name = comp_name;
        this.city = city;
        this.pincode = pincode;
        this.mobile = mobile;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComp_name() {
        return comp_name;
    }

    public void setComp_name(String comp_name) {
        this.comp_name = comp_name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
