
package com.dminer.temperature.model;

import java.util.ArrayList;
import java.util.List;


public class Data {

    private List<String> temperature = new ArrayList<String>();
    private List<String> pop = new ArrayList<String>();
    private List<String> weather = new ArrayList<String>();
    private List<String> iconLink = new ArrayList<String>();
    private List<String> hazard = new ArrayList<String>();
    private List<String> hazardUrl = new ArrayList<String>();
    private List<String> text = new ArrayList<String>();

    public List<String> getTemperature() {
        return temperature;
    }

    public List<String> getPop() {
        return pop;
    }

    public List<String> getWeather() {
        return weather;
    }

    public List<String> getIconLink() {
        return iconLink;
    }

    public List<String> getHazard() {
        return hazard;
    }

    public List<String> getHazardUrl() {
        return hazardUrl;
    }

    public List<String> getText() {
        return text;
    }
}
