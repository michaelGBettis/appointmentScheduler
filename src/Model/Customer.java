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
public class Customer extends LogInfo {

    private int customerID;
    private String customerName;
    private String address;
    private String address2;
    private String postalCode;
    private String phone;
    private String city;
    private String country;
    private int active;
    private int addressID;

    public Customer(int customerID, String customerName, String address, String 
            address2, String postalCode, String phone, String city, String country, 
            int active, int addressID, Timestamp createDate, String createdBy, Timestamp
                    lastUpdate, String lastUpdateBy) {
        
        super(createDate, createdBy, lastUpdate, lastUpdateBy);
        this.customerID = customerID;
        this.customerName = customerName;
        this.address = address;
        this.address2 = address2;
        this.postalCode = postalCode;
        this.phone = phone;
        this.city = city;
        this.country = country;
        this.active = active;
        this.addressID = addressID;
    }

    public Customer(Timestamp createDate, String createdBy, Timestamp lastUpdate,
            String lastUpdateBy, int customerID, String customerName, int active, int addressID) {
        super(createDate, createdBy, lastUpdate, lastUpdateBy);
        this.customerID = customerID;
        this.customerName = customerName;
        this.active = active;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
    
    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public int getAddressID() {
        return addressID;
    }

    public void setAddressID(int addressID) {
        this.addressID = addressID;
    }

}
