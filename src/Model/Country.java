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
public class Country extends LogInfo{

    private int countryID;
    private String countryName;

    public Country(Timestamp createDate, String createdBy, Timestamp lastUpdate, 
            String lastUpdateBy, int countryID, String countryName) {
        super(createDate, createdBy, lastUpdate, lastUpdateBy);
        this.countryID = countryID;
        this.countryName = countryName;   
    }
    
    public Country(Timestamp createDate, String createdBy, Timestamp lastUpdate, 
            String lastUpdateBy) {
        super(createDate, createdBy, lastUpdate, lastUpdateBy);
    }

    public int getCountryID() {
        return countryID;
    }

    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
    
    
}
