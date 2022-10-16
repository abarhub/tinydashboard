package org.tinydashboard.tinydashboard.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.tinydashboard.tinydashboard.dto.IndicatorDto;
import org.tinydashboard.tinydashboard.dto.IndicatorListDto;
import org.tinydashboard.tinydashboard.dto.TableDto;
import org.tinydashboard.tinydashboard.model.Indicator;
import org.tinydashboard.tinydashboard.properties.AppProperties;

import javax.annotation.PostConstruct;
import java.util.*;

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
        if (appProperties != null) {

            var defaultSize = 5;
            var minWidth = "16em";
            var maxWidth = "";
            if (appProperties.getDefault() != null) {
                var defaultConfig = appProperties.getDefault();
                if (defaultConfig.getSize() > 0) {
                    defaultSize = defaultConfig.getSize();
                }
                if (defaultConfig.getMinWidth() != null) {
                    minWidth = defaultConfig.getMinWidth();
                }
                if (defaultConfig.getMaxWidth() != null) {
                    maxWidth = defaultConfig.getMaxWidth();
                }
            }
            if (!CollectionUtils.isEmpty(appProperties.getApi().getIndicator())) {
                for (var entry : appProperties.getApi().getIndicator().entrySet()) {
                    Indicator indicator = new Indicator();
                    indicator.setId(entry.getKey());
                    var value = entry.getValue();
                    indicator.setName(value.getName());
                    indicator.setUrl(value.getUrl());
                    indicator.setType(value.getType());
                    if (value.getRowSize() > 0) {
                        indicator.setRowSize(value.getRowSize());
                    } else {
                        indicator.setRowSize(defaultSize);
                    }
                    if (!CollectionUtils.isEmpty(value.getColumns())) {
                        indicator.setColumns(value.getColumns());
                    }
                    indicator.setMinWidth(minWidth);
                    indicator.setMaxWidth(maxWidth);
                    listIndicator.add(indicator);
                }
            }
        }
        LOGGER.info("{} indicators", listIndicator.size());
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
        var itemOpt = listIndicator.stream().filter(x -> Objects.equals(x.getId(), id)).findFirst();
        if (itemOpt.isPresent()) {
            var item = itemOpt.get();
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response
                    = restTemplate.getForEntity(item.getUrl(), String.class);
            LOGGER.info("res: {}", response.getStatusCode());
            LOGGER.info("res body: {}", response.getBody());
            IndicatorDto indicatorDto = new IndicatorDto();
            indicatorDto.setType(item.getType());

            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(response.getBody());
                if(Objects.equals(item.getType(),"springBootActuator")){
//                    indicatorDto.setRecords(new LinkedHashMap<>());
//                    if(root.has("status")){
//                        var status=root.get("status").textValue();
//                        indicatorDto.getRecords().put("status",status);
//                        if(root.has("components")){
//                            var components=root.get("components");
//                            var iter=components.fieldNames();
//                            while(iter.hasNext()){
//                                var name=iter.next();
//                                var tmp=components.get(name);
//                                if(tmp.has("status")) {
//                                    var status2 = tmp.get("status").textValue();
//                                    indicatorDto.getRecords().put(name+".status",status2);
//                                }
//                            }
//                        }
//                    }
                    indicatorDto.setTree(new LinkedHashMap<>());
                    if(root.has("status")){
                        var status=root.get("status").textValue();
                        indicatorDto.getTree().put("status",status);
                        if(root.has("components")){
                            var components=root.get("components");
                            var iter=components.fieldNames();
                            while(iter.hasNext()){
                                var name=iter.next();
                                var data=new LinkedHashMap<String,String>();
                                indicatorDto.getTree().put(name,data);
                                var tmp=components.get(name);
                                if(tmp.has("status")) {
                                    data.put("status",tmp.get("status").textValue());
                                    if(tmp.has("details")){
                                        var tmp2=tmp.get("details");
                                        var iter2=tmp2.fieldNames();
                                        while(iter2.hasNext()){
                                            name=iter2.next();
                                            String s=null;
                                            var tmp3=tmp2.get(name);
                                            if(tmp3!=null){
                                                if(tmp3.isTextual()){
                                                    s= tmp3.textValue();
                                                } else if(tmp3.isNumber()){
                                                    s=tmp3.asText();
                                                } else if(tmp3.isBoolean()){
                                                    s=tmp3.asText();
                                                }
                                            }
                                            data.put(name,s);
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    TableDto tableDto = new TableDto();
                    if (root != null && root.isArray()) {
                        for (int i = 0; i < root.size() && i < item.getRowSize(); i++) {
                            var tmp = root.get(i);
                            if (tableDto.getHeaders() == null) {
                                tableDto.setHeaders(new ArrayList<>());
                            }
                            var line = new ArrayList<String>();
                            if (tableDto.getRows() == null) {
                                tableDto.setRows(new ArrayList<>());
                            }
                            tableDto.getRows().add(line);
                            var iter = tmp.fieldNames();
                            while (iter.hasNext()) {
                                var name = iter.next();

                                boolean ignoreColumn = false;
                                if (!CollectionUtils.isEmpty(item.getColumns())) {
                                    if (!item.getColumns().contains(name)) {
                                        ignoreColumn = true;
                                    }
                                }
                                if (!ignoreColumn) {
                                    String s = null;
                                    var tmp2 = tmp.get(name);
                                    if (tmp2.isTextual()) {
                                        s = tmp2.asText();
                                    } else if (tmp2.isInt()) {
                                        s = tmp2.asText();
                                    }
                                    if (s != null) {
                                        var pos = -1;
                                        if (!tableDto.getHeaders().contains(name)) {
                                            tableDto.getHeaders().add(name);
                                            pos = tableDto.getHeaders().size() - 1;
                                        } else {
                                            pos = tableDto.getHeaders().indexOf(name);
                                        }
                                        while (line.size() < pos + 1) {
                                            line.add(null);
                                        }
                                        line.set(pos, s);
                                    }
                                }
                            }
                        }
                    }
                    if (!CollectionUtils.isEmpty(tableDto.getHeaders())) {
                        indicatorDto.setTable(tableDto);
                    }
                }
                if (StringUtils.hasLength(item.getMinWidth())) {
                    if (indicatorDto.getCardStyle() == null) {
                        indicatorDto.setCardStyle(new HashMap<>());
                    }
                    indicatorDto.getCardStyle().put("min-width", item.getMinWidth());
                    indicatorDto.setMinwidth(item.getMinWidth());
                }
                if (StringUtils.hasLength(item.getMaxWidth())) {
                    if (indicatorDto.getCardStyle() == null) {
                        indicatorDto.setCardStyle(new HashMap<>());
                    }
                    indicatorDto.getCardStyle().put("max-width", item.getMaxWidth());
                }
            } catch (Exception e) {
                LOGGER.error("Erreur", e);
            }

            return indicatorDto;
        } else {
            return null;
        }
    }
}
