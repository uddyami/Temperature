
package com.dminer.temperature.model;

import java.util.ArrayList;
import java.util.List;


public class Time {

    private String layoutKey;
    private List<String> startPeriodName = new ArrayList<String>();
    private List<String> startValidTime = new ArrayList<String>();
    private List<String> tempLabel = new ArrayList<String>();

    public String getLayoutKey() {
        return layoutKey;
    }

    public List<String> getStartPeriodName() {
        return startPeriodName;
    }

    public List<String> getStartValidTime() {
        return startValidTime;
    }

    public List<String> getTempLabel() {
        return tempLabel;
    }
}
