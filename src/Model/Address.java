/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.sql.Timestamp;

/**
 *
 * @author Myque
 */
public class Address extends LogInfo{
    
    private int addressID;
    private String address;
    private String address2;
    private String postalCode;
    private String phone;
    private int cityID;
    
    public Address(Timestamp createDate, String createdBy, Timestamp lastUpdate, String lastUpdateBy, int addressID, String address, String address2, String postalCode, String phone, int cityID) {
            super(createDate, createdBy, lastUpdate, lastUpdateBy);
            this.addressID= addressID;
            this.address = address;
            this.address2 = address2;
            this.postalCode = postalCode;
            this.phone = phone;
            this.cityID = cityID;
        
    }

    public int getAddressID() {
        return addressID;
    }

    public void setAddressID(int addressID) {
        this.addressID = addressID;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getCityID() {
        return cityID;
    }

    public void setCityID(int cityID) {
        this.cityID = cityID;
    }

    
}
