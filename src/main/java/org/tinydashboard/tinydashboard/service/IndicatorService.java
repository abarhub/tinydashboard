package org.tinydashboard.tinydashboard.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;
import org.tinydashboard.tinydashboard.dto.IndicatorDto;
import org.tinydashboard.tinydashboard.dto.IndicatorListDto;
import org.tinydashboard.tinydashboard.model.Indicator;
import org.tinydashboard.tinydashboard.properties.AppProperties;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class IndicatorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndicatorService.class);

    private final AppProperties appProperties;

    private final List<Indicator> listIndicator;

    public IndicatorService(AppProperties appProperties) {
        this.appProperties = appProperties;
        listIndicator = new ArrayList<>();
    }

    @PostConstruct
    public void init() {
        if (appProperties != null && !CollectionUtils.isEmpty(appProperties.getIndicator())) {
            for (var entry : appProperties.getIndicator().entrySet()) {
                Indicator indicator = new Indicator();
                indicator.setId(entry.getKey());
                indicator.setName(entry.getValue().getName());
                indicator.setUrl(entry.getValue().getUrl());
                listIndicator.add(indicator);
            }
        }
    }

    public IndicatorListDto listAllIndicators() {
        IndicatorListDto indicatorListDto = new IndicatorListDto();
        indicatorListDto.setIndicatorDtoList(new ArrayList<>());
        for (var indicator : listIndicator) {
            var indicatorDto = new IndicatorDto();
            indicatorDto.setTitle(indicator.getName());
            indicatorDto.setId(indicator.getId());
            indicatorListDto.getIndicatorDtoList().add(indicatorDto);
        }
        return indicatorListDto;
    }

    public IndicatorDto getIndicator(String id) {
        var itemOpt=listIndicator.stream().filter(x-> Objects.equals(x.getId(),id)).findFirst();
        if(itemOpt.isPresent()) {
            var item=itemOpt.get();
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response
                    = restTemplate.getForEntity(item.getUrl(), String.class);
            LOGGER.info("res: {}",response.getStatusCode());
            LOGGER.info("res body: {}",response.getBody());
            IndicatorDto indicatorDto=new IndicatorDto();
            return indicatorDto;
        } else {
            return null;
        }
    }
}
