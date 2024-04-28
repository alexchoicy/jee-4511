package com.cems.Model.Analytics;

public class BookingRate {
    private ChartData chartData;
    private int maxBookingRate;
    private int minBookingRate;
    private String SearchItemName;

    public ChartData getChartData() {
        return chartData;
    }

    public void setChartData(ChartData chartData) {
        this.chartData = chartData;
    }

    public int getMaxBookingRate() {
        return maxBookingRate;
    }

    public void setMaxBookingRate(int maxBookingRate) {
        this.maxBookingRate = maxBookingRate;
    }

    public int getMinBookingRate() {
        return minBookingRate;
    }

    public void setMinBookingRate(int minBookingRate) {
        this.minBookingRate = minBookingRate;
    }

    public String getSearchItemName() {
        return SearchItemName;
    }

    public void setSearchItemName(String searchItemName) {
        SearchItemName = searchItemName;
    }
}
