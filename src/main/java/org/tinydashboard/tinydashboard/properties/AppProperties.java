package org.tinydashboard.tinydashboard.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "api")
public class AppProperties {

    private Map<String,IndicatorProperties> indicator;

    public Map<String, IndicatorProperties> getIndicator() {
        return indicator;
    }

    public void setIndicator(Map<String, IndicatorProperties> indicator) {
        this.indicator = indicator;
    }
}
