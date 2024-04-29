/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cems.database;

import com.cems.Enums.ReservationItemStatus;
import com.cems.Enums.UserRoles;
import com.cems.Model.Display.DeliveryDisplay;
import com.cems.Model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author MarkYu
 */
public class DeliveryManager extends DatabaseManager {

    public ArrayList<DeliveryDisplay> getDelivery() {
        try (Connection connection = getConnection()) {
            String sql = "SELECT reservation_items.*,reservation.*,equipment_item.*,equipment.*,from_location.location_name AS from_location_name,to_location.location_name AS to_location_name FROM reservation_items INNER JOIN reservation ON reservation.reservation_id = reservation_items.reservation_id INNER JOIN equipment_item ON equipment_item.equipment_item_id = reservation_items.equipment_item_id INNER JOIN equipment ON equipment_item.equipment_id = equipment.equipment_id LEFT JOIN location AS  from_location ON equipment_item.current_location = from_location.location_id LEFT JOIN location AS to_location ON reservation.destination_id = to_location.location_id WHERE reservation_items_status = "
                    + ReservationItemStatus.NEED_DELIVERY.getValue()
                    + " ORDER BY equipment_item.current_location, reservation.destination_id;";
            Statement statement = connection.createStatement();

            boolean results = statement.execute(sql);
            if (!results) {
                return null;
            }
            ResultSet resultSet = statement.getResultSet();
            ArrayList<DeliveryDisplay> deliveryDisplay = new ArrayList<>();
            while (resultSet.next()) {
                DeliveryDisplay delivery = GetDeliveryDataToBean(resultSet);
                deliveryDisplay.add(delivery);
            }
            resultSet.close();
            statement.close();
            connection.close();
            return deliveryDisplay;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }

    public ArrayList<DeliveryDisplay> getDeliveryRecods() {
        try (Connection connection = getConnection()) {
            String sql = "SELECT * FROM delivery_record;";
            Statement statement = connection.createStatement();

            boolean results = statement.execute(sql);
            if (!results) {
                return null;
            }
            ResultSet resultSet = statement.getResultSet();
            ArrayList<DeliveryDisplay> deliveryDisplay = new ArrayList<>();
            while (resultSet.next()) {
                DeliveryDisplay delivery = GetDeliveryRecodsDataToBean(resultSet);
                deliveryDisplay.add(delivery);
            }
            resultSet.close();
            statement.close();
            connection.close();
            return deliveryDisplay;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }

    private DeliveryDisplay GetDeliveryDataToBean(ResultSet resultSet) throws SQLException {
        DeliveryDisplay deliveryDisplay = new DeliveryDisplay();
        deliveryDisplay.setItemId(resultSet.getInt("reservation_items_id"));
        deliveryDisplay.setSerialNumber(resultSet.getInt("serial_number"));
        deliveryDisplay.setItemName(resultSet.getString("equipment_name"));
        deliveryDisplay.setLoctionName(resultSet.getString("from_location_name"));
        deliveryDisplay.setDestionName(resultSet.getString("to_location_name"));
        deliveryDisplay.setStartTime(resultSet.getString("start_time"));
        deliveryDisplay.setEndTime(resultSet.getString("end_time"));
        return deliveryDisplay;
    }

    private DeliveryDisplay GetDeliveryRecodsDataToBean(ResultSet resultSet) throws SQLException {
        DeliveryDisplay deliveryDisplay = new DeliveryDisplay();
        deliveryDisplay.setDeliveryBy(resultSet.getInt("delivery_by"));
        deliveryDisplay.setDeliveryId(resultSet.getInt("delivery_id"));
        deliveryDisplay.setPickupDateTime(resultSet.getString("pickup_datetime"));
        deliveryDisplay.setArriveDateTime(resultSet.getString("arrive_datetime"));
        deliveryDisplay.setDeadline(resultSet.getString("delivery_date"));
        return deliveryDisplay;
    }

    public boolean delegate(DeliveryDisplay newDelivery) {
        try (Connection connection = getConnection()) {
            String sql = "INSERT INTO delivery_record (delivery_by, delivery_date) VALUES (?, ?)";
            PreparedStatement updateOrderStatusStatement = connection.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);
            updateOrderStatusStatement.setInt(1, newDelivery.getCourier());
            updateOrderStatusStatement.setString(2, newDelivery.getDeadline());
            int rowsAffected = updateOrderStatusStatement.executeUpdate();

            ResultSet generatedKeys = updateOrderStatusStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int generatedUserId = generatedKeys.getInt(1);
                newDelivery.setDeliveryId(generatedUserId);
            }

            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updatePickupTime(DeliveryDisplay newDelivery) {
        try (Connection connection = getConnection()) {
            String sql = "UPDATE delivery_record SET pickup_datetime = ? WHERE delivery_id = ?";
            PreparedStatement updateOrderStatusStatement = connection.prepareStatement(sql);
            updateOrderStatusStatement.setString(1, newDelivery.getPickupDateTime());
            updateOrderStatusStatement.setInt(2, newDelivery.getDeliveryId());
            int rowsAffected = updateOrderStatusStatement.executeUpdate();

            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateArriveTime(DeliveryDisplay newDelivery) {
        try (Connection connection = getConnection()) {
            String sql = "UPDATE delivery_record SET arrive_datetime = ? WHERE delivery_id = ?";
            PreparedStatement updateOrderStatusStatement = connection.prepareStatement(sql);
            updateOrderStatusStatement.setString(1, newDelivery.getArriveDateTime());
            updateOrderStatusStatement.setInt(2, newDelivery.getDeliveryId());
            int rowsAffected = updateOrderStatusStatement.executeUpdate();

            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
