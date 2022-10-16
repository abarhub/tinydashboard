package org.tinydashboard.tinydashboard.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private DefaultProperties defaultConfig;
    private ApiProperties api;

    public DefaultProperties getDefault() {
        return defaultConfig;
    }

    public void setDefault(DefaultProperties defaultConfig) {
        this.defaultConfig = defaultConfig;
    }

    public ApiProperties getApi() {
        return api;
    }

    public void setApi(ApiProperties api) {
        this.api = api;
    }
}
