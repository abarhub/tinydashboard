package org.tinydashboard.tinydashboard.dto;

import java.util.List;
import java.util.Map;

public class IndicatorDto {
    private String id;
    private String title;

    private TableDto table;

    private List<String> list;

    private Map<String, String> cardStyle;

    private Map<String, String> records;

    private Map<String, Object> tree;

    private String minwidth;

    private String type;

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

    public TableDto getTable() {
        return table;
    }

    public void setTable(TableDto table) {
        this.table = table;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public Map<String, String> getCardStyle() {
        return cardStyle;
    }

    public void setCardStyle(Map<String, String> cardStyle) {
        this.cardStyle = cardStyle;
    }

    public String getMinwidth() {
        return minwidth;
    }

    public void setMinwidth(String minwidth) {
        this.minwidth = minwidth;
    }

    public Map<String, String> getRecords() {
        return records;
    }

    public void setRecords(Map<String, String> records) {
        this.records = records;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, Object> getTree() {
        return tree;
    }

    public void setTree(Map<String, Object> tree) {
        this.tree = tree;
    }
}
