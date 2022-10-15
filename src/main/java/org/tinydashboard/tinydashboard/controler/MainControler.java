package org.tinydashboard.tinydashboard.controler;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.tinydashboard.tinydashboard.dto.IndicatorDto;
import org.tinydashboard.tinydashboard.dto.IndicatorListDto;
import org.tinydashboard.tinydashboard.service.IndicatorService;

@RestController()
public class MainControler {

    private final IndicatorService indicatorService;

    public MainControler(IndicatorService indicatorService) {
        this.indicatorService = indicatorService;
    }

    @GetMapping("/indicator")
    IndicatorListDto listAllIndicators() {
        return indicatorService.listAllIndicators();
    }

    @GetMapping("/indicator/{id}")
    IndicatorDto indicator(@PathVariable String id) {
        return indicatorService.getIndicator(id);
    }

}
