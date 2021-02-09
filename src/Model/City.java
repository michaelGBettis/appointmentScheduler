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
public class City extends LogInfo {
    private int cityID;
    private String cityName;
    private int countryId;
    
    public City(Timestamp createDate, String createdBy, Timestamp lastUpdate, String lastUpdateBy, int cityID, String cityName, int countryID) {
      super(createDate, createdBy, lastUpdate, lastUpdateBy);
      this.cityID = cityID;  
      this.cityName = cityName;  
      this.countryId = countryId;
    }

    public int getCityID() {
        return cityID;
    }

    public void setCityID(int cityID) {
        this.cityID = cityID;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }
}
