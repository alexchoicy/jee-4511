package com.cems.database;

import com.cems.Enums.DamagedStatus;
import com.cems.Enums.ItemStatus;
import com.cems.Model.Display.DamagesReportDisplay;
import com.cems.Model.User;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DamagesReportManager extends DatabaseManager {

    public DamagesReportDisplay getCreateDamagesDetail(int itemID, int recordID) {
        DamagesReportDisplay damagesReportDisplay = new DamagesReportDisplay();
        String sql = "select * from reservation INNER JOIN user ON reservation.user_id = user.user_id INNER JOIN reservation_items ON reservation.reservation_id = reservation_items.reservation_id INNER JOIN equipment_item ON equipment_item.equipment_item_id = reservation_items.equipment_item_id INNER JOIN equipment ON equipment_item.equipment_id = equipment.equipment_id WHERE reservation.reservation_id = ? AND reservation_items.equipment_item_id = ?";
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, recordID);
            statement.setInt(2, itemID);
            statement.executeQuery();

            ResultSet resultSet = statement.getResultSet();
            if (resultSet.next()) {
                damagesReportDisplay.setRecordID(resultSet.getInt("reservation_id"));
                damagesReportDisplay.setBorrowerID(resultSet.getInt("user_id"));
                damagesReportDisplay.setBorrowerName(
                        String.format("%s %s", resultSet.getString("first_name"), resultSet.getString("last_name")));
                damagesReportDisplay.setItemID(resultSet.getInt("equipment_item_id"));
                damagesReportDisplay.setItemName(resultSet.getString("equipment_name"));
            }
        } catch (SQLException | ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
        return damagesReportDisplay;
    }

    public boolean createDamageDetail(int itemID, User user, int recordID, String damageDetail) {
        String sql = "INSERT INTO damaged_record ( description, reported_date, related_reservation, recorded_by, item_id, damaged_status) VALUES (?, ?, ?, ?, ?, ?)";
        String updateItemStatus = "UPDATE equipment_item SET status = ? WHERE equipment_item_id = ?";

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, damageDetail);
            statement.setTimestamp(2, new java.sql.Timestamp(System.currentTimeMillis()));
            statement.setInt(3, recordID);
            statement.setInt(4, user.getUserId());
            statement.setInt(5, itemID);
            statement.setInt(6, DamagedStatus.REPORTED.getValue());

            statement.executeUpdate();

            PreparedStatement statement2 = connection.prepareStatement(updateItemStatus);

            statement2.setInt(1, ItemStatus.UNDER_MAINTENANCE.getValue());
            statement2.setInt(2, itemID);
            statement2.executeUpdate();

            return true;
        } catch (SQLException | ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<DamagesReportDisplay> getDamageReports() {
        String sql = "SELECT * FROM damaged_record INNER JOIN reservation ON damaged_record.related_reservation = reservation.reservation_id INNER JOIN user ON reservation.user_id = user.user_id INNER JOIN equipment_item ON damaged_record.item_id = equipment_item.equipment_item_id INNER JOIN equipment ON equipment_item.equipment_id = equipment.equipment_id";
        ArrayList<DamagesReportDisplay> damagesReportDisplays = new ArrayList<>();

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.executeQuery();
            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()) {
                DamagesReportDisplay damagesReportDisplay = new DamagesReportDisplay();
                damagesReportDisplay.setRecordID(resultSet.getInt("damaged_record_id"));
                damagesReportDisplay.setBorrowerID(resultSet.getInt("user_id"));
                damagesReportDisplay.setBorrowerName(
                        String.format("%s %s", resultSet.getString("first_name"), resultSet.getString("last_name")));
                damagesReportDisplay.setItemID(resultSet.getInt("equipment_item_id"));
                damagesReportDisplay.setItemName(resultSet.getString("equipment_name"));
                damagesReportDisplay.setLogDatetime(resultSet.getTimestamp("reported_date"));
                damagesReportDisplay.setDamagedStatus(DamagedStatus.valueOf(resultSet.getInt("damaged_status")));
                damagesReportDisplay.setDamageDetail(resultSet.getString("description"));
                damagesReportDisplays.add(damagesReportDisplay);
            }
        } catch (SQLException | ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
        return damagesReportDisplays;
    }

    public DamagesReportDisplay getDamageReport(int recordID) {
        String sql = "SELECT *, reported_by.first_name AS reported_by_first_name, reported_by.last_name AS reported_by_last_name, related_user.first_name AS related_user_first_name, related_user.last_name AS related_user_last_name FROM damaged_record LEFT JOIN reservation ON damaged_record.related_reservation = reservation.reservation_id INNER JOIN equipment_item ON damaged_record.item_id = equipment_item.equipment_item_id INNER JOIN equipment ON equipment_item.equipment_id = equipment.equipment_id LEFT JOIN user AS reported_by ON reported_by.user_id = damaged_record.recorded_by LEFT JOIN user AS related_user ON related_user.user_id = reservation.user_id WHERE damaged_record_id = ?";

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, recordID);
            statement.executeQuery();
            ResultSet resultSet = statement.getResultSet();
            if (resultSet.next()) {
                DamagesReportDisplay damagesReportDisplay = new DamagesReportDisplay();
                damagesReportDisplay.setRecordID(resultSet.getInt("damaged_record_id"));
                damagesReportDisplay.setBorrowerID(resultSet.getInt("user_id"));
                damagesReportDisplay.setBorrowerName(String.format("%s %s",
                        resultSet.getString("related_user_first_name"), resultSet.getString("related_user_last_name")));
                damagesReportDisplay.setLoggerID(resultSet.getString("recorded_by"));
                damagesReportDisplay.setLoggerName(String.format("%s %s", resultSet.getString("reported_by_first_name"),
                        resultSet.getString("reported_by_last_name")));
                damagesReportDisplay.setItemID(resultSet.getInt("equipment_item_id"));
                damagesReportDisplay.setItemName(resultSet.getString("equipment_name"));
                damagesReportDisplay.setLogDatetime(resultSet.getTimestamp("reported_date"));
                damagesReportDisplay.setDamagedStatus(DamagedStatus.valueOf(resultSet.getInt("damaged_status")));
                damagesReportDisplay.setDamageDetail(resultSet.getString("description"));
                return damagesReportDisplay;
            }
        } catch (SQLException | ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
        return null;

    }

    public boolean confirmDamageReport(int recordID) {
        String sql = "UPDATE damaged_record SET damaged_status = ? WHERE damaged_record_id = ?";
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, DamagedStatus.CONFIRMED.getValue());
            statement.setInt(2, recordID);
            statement.executeUpdate();
            return true;
        } catch (SQLException | ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean discardDamageReport(int recordID) {
        String sql = "UPDATE damaged_record SET damaged_status = ? WHERE damaged_record_id = ?";
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, DamagedStatus.DISCARDED.getValue());
            statement.setInt(2, recordID);
            statement.executeUpdate();
            return true;
        } catch (SQLException | ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean repairedDamageReport(int recordID) {
        String sql = "UPDATE damaged_record SET damaged_status = ? WHERE damaged_record_id = ?";
        String sql2 = "UPDATE equipment_item SET status = ? WHERE equipment_item_id = (SELECT item_id FROM damaged_record WHERE damaged_record_id = ?)";
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, DamagedStatus.REPAIRED.getValue());
            statement.setInt(2, recordID);
            statement.executeUpdate();

            PreparedStatement statement2 = connection.prepareStatement(sql2);
            statement2.setInt(1, ItemStatus.AVAILABLE.getValue());
            statement2.setInt(2, recordID);
            statement2.executeUpdate();

            return true;
        } catch (SQLException | ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
