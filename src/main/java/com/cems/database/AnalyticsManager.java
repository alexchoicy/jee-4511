package com.cems.database;

import com.cems.Model.Analytics.BookingRate;
import com.cems.Model.Analytics.ChartData;
import com.cems.Model.Analytics.Dataset;

import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class AnalyticsManager extends DatabaseManager {

    public BookingRate getBookingRateInEquipment(int equipment_id, Timestamp start_date, Timestamp end_date) throws SQLException, IOException, ClassNotFoundException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        BookingRate bookingRateInEquipment = new BookingRate();
        ChartData chartData = new ChartData();
        String sql = "SELECT COUNT(*) AS booking_rate,DATE(reservation.start_time) AS booking_date,DATE(start_time) AS CreatedAt, equipment.equipment_id FROM reservation_items INNER JOIN reservation ON reservation_items.reservation_id = reservation.reservation_id INNER JOIN equipment_item ON reservation_items.equipment_item_id = equipment_item.equipment_item_id INNER JOIN equipment ON equipment_item.equipment_id = equipment.equipment_id WHERE equipment.equipment_id = ? AND reservation.start_time BETWEEN ? AND ? GROUP BY booking_date, equipment_id";
        ArrayList<Timestamp> timestampList = new ArrayList<>();
        Timestamp currentTimestamp = new Timestamp(start_date.getTime());

        while (!currentTimestamp.after(end_date)) {
            timestampList.add(currentTimestamp);
            currentTimestamp = new Timestamp(currentTimestamp.getTime() + (24 * 60 * 60 * 1000)); // Increment by one day (in milliseconds)
        }

        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            preparedStatement.setInt(1, equipment_id);
            preparedStatement.setTimestamp(2, start_date);
            preparedStatement.setTimestamp(3, end_date);

            preparedStatement.executeQuery();
            ResultSet resultSet = preparedStatement.getResultSet();
            int max = 0;
            int min = Integer.MAX_VALUE;
            ArrayList<Integer> bookingRate = new ArrayList<>();
            ArrayList<String> date = new ArrayList<>();

            for (Timestamp datestamp : timestampList) {
                boolean found = false;
                resultSet.beforeFirst();
                while (resultSet.next()) {
                    if (datestamp.equals(resultSet.getTimestamp("booking_date"))) {
                        int booking_rate = resultSet.getInt("booking_rate");
                        bookingRate.add(booking_rate);
                        date.add(sdf.format(resultSet.getTimestamp("CreatedAt")));
                        if (booking_rate > max) {
                            max = booking_rate;
                        }
                        if (booking_rate < min) {
                            min = booking_rate;
                        }
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    bookingRate.add(0);
                    date.add(sdf.format(datestamp));
                }
            }


            chartData.setLabels(date);
            Dataset dataset = new Dataset("Booking Rate", bookingRate, "borderColor", "backgroundColor");
            ArrayList<Dataset> datasets = new ArrayList<>();
            datasets.add(dataset);
            chartData.setDatasets(datasets);

            // Populate BookingRateInEquipment
            bookingRateInEquipment.setChartData(chartData);
            bookingRateInEquipment.setMaxBookingRate(max);
            bookingRateInEquipment.setMinBookingRate(min);

            return bookingRateInEquipment;
        }
    }

    public BookingRate getBookingRateInVenue(int location_id, Timestamp start_date, Timestamp end_date) throws SQLException, IOException, ClassNotFoundException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        BookingRate bookingRateInEquipment = new BookingRate();
        ChartData chartData = new ChartData();
        String sql = "SELECT COUNT(*) AS booking_rate,DATE(reservation.start_time) AS booking_date,DATE(start_time) AS CreatedAt, equipment.equipment_id FROM reservation_items INNER JOIN reservation ON reservation_items.reservation_id = reservation.reservation_id INNER JOIN equipment_item ON reservation_items.equipment_item_id = equipment_item.equipment_item_id INNER JOIN equipment ON equipment_item.equipment_id = equipment.equipment_id WHERE reservation.destination_id = ? AND reservation.start_time BETWEEN ? AND ? GROUP BY booking_date, equipment_id";
        ArrayList<Timestamp> timestampList = new ArrayList<>();
        Timestamp currentTimestamp = new Timestamp(start_date.getTime());
        while (!currentTimestamp.after(end_date)) {
            timestampList.add(currentTimestamp);
            currentTimestamp = new Timestamp(currentTimestamp.getTime() + (24 * 60 * 60 * 1000)); // Increment by one day (in milliseconds)
        }
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            preparedStatement.setInt(1, location_id);
            preparedStatement.setTimestamp(2, start_date);
            preparedStatement.setTimestamp(3, end_date);
            preparedStatement.executeQuery();
            ResultSet resultSet = preparedStatement.getResultSet();
            int max = 0;
            int min = Integer.MAX_VALUE;
            ArrayList<Integer> bookingRate = new ArrayList<>();
            ArrayList<String> date = new ArrayList<>();

            for (Timestamp datestamp : timestampList) {
                boolean found = false;
                resultSet.beforeFirst();
                while (resultSet.next()) {
                    if (datestamp.equals(resultSet.getTimestamp("booking_date"))) {
                        int booking_rate = resultSet.getInt("booking_rate");
                        bookingRate.add(booking_rate);
                        date.add(sdf.format(resultSet.getTimestamp("CreatedAt")));
                        if (booking_rate > max) {
                            max = booking_rate;
                        }
                        if (booking_rate < min) {
                            min = booking_rate;
                        }
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    bookingRate.add(0);
                    date.add(sdf.format(datestamp));
                }
            }
            chartData.setLabels(date);
            Dataset dataset = new Dataset("Booking Rate", bookingRate, "borderColor", "backgroundColor");
            ArrayList<Dataset> datasets = new ArrayList<>();
            datasets.add(dataset);
            chartData.setDatasets(datasets);

            // Populate BookingRateInEquipment
            bookingRateInEquipment.setChartData(chartData);
            bookingRateInEquipment.setMaxBookingRate(max);
            bookingRateInEquipment.setMinBookingRate(min);

            return bookingRateInEquipment;
        }

    }
}
