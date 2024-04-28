package com.cems.Model.Analytics;

import java.util.List;

public class Dataset {
    private String label;
    private List<Integer> data;
    private String borderColor;
    private String backgroundColor;

    public Dataset(String label, List<Integer> data, String borderColor, String backgroundColor) {
        this.label = label;
        this.data = data;
        this.borderColor = borderColor;
        this.backgroundColor = backgroundColor;
    }

    // Getters and setters
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<Integer> getData() {
        return data;
    }

    public void setData(List<Integer> data) {
        this.data = data;
    }

    public String getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(String borderColor) {
        this.borderColor = borderColor;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}
