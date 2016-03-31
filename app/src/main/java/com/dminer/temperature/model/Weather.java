
package com.dminer.temperature.model;



public class Weather {

   
    private String operationalMode;
   
    private String srsName;
   
    private String creationDate;
  
    private String creationDateLocal;

    private String productionCenter;

    private String credit;

    private String moreInformation;

    private Location location;

    private Time time;

    private Data data;

    private Currentobservation currentobservation;

    public String getOperationalMode() {
        return operationalMode;
    }

    public String getSrsName() {
        return srsName;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public String getCreationDateLocal() {
        return creationDateLocal;
    }

    public String getProductionCenter() {
        return productionCenter;
    }

    public String getCredit() {
        return credit;
    }

    public String getMoreInformation() {
        return moreInformation;
    }

    public Location getLocation() {
        return location;
    }

    public Time getTime() {
        return time;
    }

    public Data getData() {
        return data;
    }

    public Currentobservation getCurrentobservation() {
        return currentobservation;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "operationalMode='" + operationalMode + '\'' +
                ", srsName='" + srsName + '\'' +
                ", creationDate='" + creationDate + '\'' +
                ", creationDateLocal='" + creationDateLocal + '\'' +
                ", productionCenter='" + productionCenter + '\'' +
                ", credit='" + credit + '\'' +
                ", moreInformation='" + moreInformation + '\'' +
                ", location=" + location +
                ", time=" + time +
                ", data=" + data +
                ", currentobservation=" + currentobservation +
                '}';
    }
}
