package org.tinydashboard.tinydashboard.properties;

import java.util.Map;

public class ApiProperties {

    private Map<String,IndicatorProperties> indicator;

    public Map<String, IndicatorProperties> getIndicator() {
        return indicator;
    }

    public void setIndicator(Map<String, IndicatorProperties> indicator) {
        this.indicator = indicator;
    }
}
