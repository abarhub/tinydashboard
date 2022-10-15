package org.tinydashboard.tinydashboard.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tinydashboard.tinydashboard.controler.MainControler;
import org.tinydashboard.tinydashboard.properties.AppProperties;
import org.tinydashboard.tinydashboard.service.IndicatorService;

@Configuration
@EnableConfigurationProperties(AppProperties.class)
public class ConfigurationService {

    @Bean
    public IndicatorService indicatorService(AppProperties appProperties){
        return new IndicatorService(appProperties);
    }

}
