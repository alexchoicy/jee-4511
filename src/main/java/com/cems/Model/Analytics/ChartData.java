package com.cems.Model.Analytics;

import java.util.ArrayList;
import java.util.List;

public class ChartData {
    private List<String> labels;
    private List<Dataset> datasets;



    // Getters and setters
    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public List<Dataset> getDatasets() {
        return datasets;
    }

    public void setDatasets(List<Dataset> datasets) {
        this.datasets = datasets;
    }
}
