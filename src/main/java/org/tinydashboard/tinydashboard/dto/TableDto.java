package org.tinydashboard.tinydashboard.dto;

import java.util.List;

public class TableDto {

    private List<String> headers;
    private List<List<String>> rows;

    public List<String> getHeaders() {
        return headers;
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    public List<List<String>> getRows() {
        return rows;
    }

    public void setRows(List<List<String>> rows) {
        this.rows = rows;
    }
}
