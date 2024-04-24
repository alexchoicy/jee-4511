package com.cems.database;

import com.cems.Enums.ItemStatus;
import com.cems.Enums.ReservationItemStatus;
import com.cems.Enums.ReservationStatus;
import com.cems.Enums.UserRoles;
import com.cems.Model.Display.ReservationDisplay;
import com.cems.Model.EquipmentItem;
import com.cems.Model.Request.ReservationCart;
import com.cems.Model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;

public class ReservationManager extends DatabaseManager {

    //TODO : try-with-resources for items
    //return error message
    public ArrayList<String> createReservation(User user, ArrayList<ReservationCart> cart, int locationID, Timestamp startDateTime, Timestamp endDateTime){
        ArrayList<String> errorsItems = new ArrayList<>();
        //i am lazy to create another object
        ArrayList<Integer> items = new ArrayList<Integer>();
        ArrayList<ReservationItemStatus> items_status = new ArrayList<>();
        String searchQuery = "SELECT * FROM equipment_item INNER JOIN equipment ON equipment.equipment_id = ? WHERE equipment_item.equipment_id = ? AND status = 1 ORDER BY CASE WHEN current_location = ? THEN 0 ELSE 1 END, current_location DESC";

        try (Connection connection = getConnection()){
            connection.setAutoCommit(false);
            for (ReservationCart cartItem : cart){
                PreparedStatement statement = connection.prepareStatement(searchQuery);

                statement.setInt(1, cartItem.getEquipmentID());
                statement.setInt(2, cartItem.getEquipmentID());
                statement.setInt(3, locationID);

                ResultSet resultSet = statement.executeQuery();
                ArrayList<EquipmentItem> equipments = new ArrayList<>();

                while (resultSet.next()) {
                    EquipmentItem item = new EquipmentItem();
                    item.setId(resultSet.getInt("equipment_item_id"));
                    item.setSerialNumber(resultSet.getString("serial_number"));
                    item.setBorrowedTimes(resultSet.getInt("borrowed_time"));
                    item.setEquipmentId(resultSet.getInt("equipment_id"));
                    item.setStaffOnly(resultSet.getBoolean("isStaffOnly"));
                    item.setListed(resultSet.getBoolean("isListed"));
                    item.setCurrentLocation(resultSet.getInt("current_location"));
                    equipments.add(item);
                }

                if(equipments.size() < cartItem.getQuantity()) {
                    errorsItems.add(String.format("Item %s don't have Enough Request Quantity", cartItem.getEquipmentID()));
                    continue;
                }
                int itemcount = 0;
                for (EquipmentItem equipment : equipments) {
                    itemcount ++;
                    System.out.println("itemCount :" + itemcount + " , Request Quantity: " + cartItem.getQuantity());
                    if (!equipment.isListed()) {
                        errorsItems.add(String.format("Item %s is not Listed to reservation", cartItem.getEquipmentID()));
                        continue;
                    }

                    if (equipment.isStaffOnly() && user.getRole() == UserRoles.USER) {
                        errorsItems.add(String.format("Item %s is staff only", cartItem.getEquipmentID()));
                        continue;
                    }

                    String changeItemStatusQuery = "UPDATE equipment_item SET status = ? WHERE equipment_item_id = ?";

                    PreparedStatement changeStatusStatement = connection.prepareStatement(changeItemStatusQuery);
                    changeStatusStatement.setInt(1, ItemStatus.BOOKED.getValue());
                    changeStatusStatement.setInt(2, equipment.getId());

                    changeStatusStatement.executeUpdate();
                    items.add(equipment.getId());
                    items_status.add(locationID == equipment.getCurrentLocation() ? ReservationItemStatus.ARRIVED : ReservationItemStatus.NEED_DELIVERY);
                    if (itemcount == cartItem.getQuantity()) {
                        break;
                    }
                }
                connection.commit();
            }
            if (!errorsItems.isEmpty()) {
                return errorsItems;
            }

            String createReservationQuery = "INSERT INTO reservation (user_id, start_time, end_time, status, destination_id) VALUES (?, ?, ?, ?, ?)";

            PreparedStatement createReservationStatement = connection.prepareStatement(createReservationQuery, PreparedStatement.RETURN_GENERATED_KEYS);

            createReservationStatement.setInt(1, user.getUserId());
            createReservationStatement.setTimestamp(2, startDateTime);
            createReservationStatement.setTimestamp(3, endDateTime);
            createReservationStatement.setInt(4, ReservationStatus.PENDING.getValue());
            createReservationStatement.setInt(5, locationID);
            int newId = 0;

            int createReservationStatus = createReservationStatement.executeUpdate();

            try (ResultSet createReservationResultSet = createReservationStatement.getGeneratedKeys()) {
                if (createReservationResultSet.next()) {
                    newId = createReservationResultSet.getInt(1);
                }
            }
            connection.commit();
            for (int i = 0; i < items.size(); i++) {
                int itemID = items.get(i);
                int item_status_value = items_status.get(i).getValue();
                String createReservationItemQuery = "INSERT INTO reservation_items (reservation_id, equipment_item_id, reservation_items_status) VALUES (?, ?, ?)";
                PreparedStatement createReservationItemStatement = connection.prepareStatement(createReservationItemQuery);

                createReservationItemStatement.setInt(1, newId);
                createReservationItemStatement.setInt(2, itemID);
                createReservationItemStatement.setInt(3, item_status_value);
                createReservationItemStatement.executeUpdate();
            }
            connection.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return errorsItems;
    }

    public ReservationDisplay getReservations(User user) {
        String sql = "SELECT * FROM reservation INNER JOIN user ON user.user_id = reservation.user_id WHERE user_id = ? INNER JOIN location ON location.location_id = reservation.destination_id";
        ReservationDisplay reservationDisplay = new ReservationDisplay();
        ArrayList<ReservationDisplay> reservations = new ArrayList<>();

        return reservationDisplay;
    }
}
