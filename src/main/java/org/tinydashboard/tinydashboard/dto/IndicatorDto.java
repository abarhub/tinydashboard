package org.tinydashboard.tinydashboard.dto;

import java.util.List;
import java.util.Map;

public class IndicatorDto {
    private String id;
    private String title;

    private List<Map<String, Object>> data;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Map<String, Object>> getData() {
        return data;
    }

    public void setData(List<Map<String, Object>> data) {
        this.data = data;
    }
}
