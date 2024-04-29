package com.cems.database;

import com.cems.Enums.ItemStatus;
import com.cems.Enums.ReservationItemStatus;
import com.cems.Enums.ReservationStatus;
import com.cems.Enums.UserRoles;
import com.cems.Model.Display.ReservationDisplay;
import com.cems.Model.EquipmentItem;
import com.cems.Model.Location;
import com.cems.Model.Request.ReservationCart;
import com.cems.Model.Reservations;
import com.cems.Model.User;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class ReservationManager extends DatabaseManager {

    // TODO : try-with-resources for items
    // return error message
    public ArrayList<String> createReservation(User user, ArrayList<ReservationCart> cart, int locationID,
            Timestamp startDateTime, Timestamp endDateTime) {
        ArrayList<String> errorsItems = new ArrayList<>();
        // i am lazy to create another object
        ArrayList<Integer> items = new ArrayList<Integer>();
        ArrayList<ReservationItemStatus> items_status = new ArrayList<>();
        String searchQuery = "SELECT * FROM equipment_item INNER JOIN equipment ON equipment.equipment_id = ? WHERE equipment_item.equipment_id = ? AND status = 1 ORDER BY CASE WHEN current_location = ? THEN 0 ELSE 1 END, current_location DESC";

        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false);
            for (ReservationCart cartItem : cart) {
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

                if (equipments.size() < cartItem.getQuantity()) {
                    errorsItems.add(
                            String.format("Item %s don't have Enough Request Quantity", cartItem.getEquipmentID()));
                    continue;
                }
                int itemcount = 0;
                for (EquipmentItem equipment : equipments) {
                    itemcount++;
                    if (!equipment.isListed()) {
                        errorsItems
                                .add(String.format("Item %s is not Listed to reservation", cartItem.getEquipmentID()));
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
                    items_status.add(locationID == equipment.getCurrentLocation() ? ReservationItemStatus.ARRIVED
                            : ReservationItemStatus.NEED_DELIVERY);
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

            PreparedStatement createReservationStatement = connection.prepareStatement(createReservationQuery,
                    PreparedStatement.RETURN_GENERATED_KEYS);

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
                PreparedStatement createReservationItemStatement = connection
                        .prepareStatement(createReservationItemQuery);

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
        String sql = "SELECT * FROM reservation INNER JOIN user ON user.user_id = reservation.user_id INNER JOIN location ON location.location_id = reservation.destination_id WHERE reservation.user_id = ?";
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, user.getUserId());
            ResultSet resultSet = statement.executeQuery();
            return getReservationDisplay(resultSet);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ReservationDisplay getReservationAdmin() {
        String sql = "SELECT * FROM reservation INNER JOIN user ON user.user_id = reservation.user_id INNER JOIN location ON location.location_id = reservation.destination_id";
        try (Connection connection = getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            return getReservationDisplay(resultSet);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ReservationDisplay getReservationDisplay(ResultSet resultSet) throws SQLException {
        ArrayList<Reservations> waitingTOoApprove = new ArrayList<>();
        ArrayList<Reservations> approved = new ArrayList<>();
        ArrayList<Reservations> active = new ArrayList<>();
        ArrayList<Reservations> completed = new ArrayList<>();

        while (resultSet.next()) {
            Reservations reservations = Reservations.create(resultSet);
            switch (reservations.getStatus()) {
                case PENDING:
                    waitingTOoApprove.add(reservations);
                    break;
                case APPROVED:
                    approved.add(reservations);
                    break;
                case ACTIVE:
                    active.add(reservations);
                    break;
                default:
                    completed.add(reservations);
            }
        }
        ReservationDisplay reservationDisplay = new ReservationDisplay();
        reservationDisplay.setActive(active);
        reservationDisplay.setWaitingToApprove(waitingTOoApprove);
        reservationDisplay.setApproved(approved);
        reservationDisplay.setCompleted(completed);
        return reservationDisplay;
    }

    public Reservations getReservation(int recordID) throws SQLException, IOException, ClassNotFoundException {
        String sql = "SELECT * FROM reservation INNER JOIN user ON user.user_id = reservation.user_id INNER JOIN location ON location.location_id = reservation.destination_id INNER JOIN reservation_items ON reservation.reservation_id = reservation_items.reservation_id INNER JOIN equipment_item ON reservation_items.equipment_item_id = equipment_item.equipment_item_id INNER JOIN equipment ON equipment_item.equipment_id = equipment.equipment_id WHERE reservation.reservation_id = ?";
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, recordID);
            ResultSet resultSet = statement.executeQuery();
            Reservations reservation = new Reservations();
            ArrayList<EquipmentItem> items = new ArrayList<>();
            while (resultSet.next()) {
                reservation.setId(resultSet.getInt("reservation_id"));
                reservation.setStartTime(resultSet.getTimestamp("start_time"));
                reservation.setEndTime(resultSet.getTimestamp("end_time"));
                reservation.setCreatedAt(resultSet.getTimestamp("CreatedAt"));
                reservation.setStatus(ReservationStatus.getStatus(resultSet.getInt("status")));
                reservation.setCheckin_time(resultSet.getTimestamp("checkin_time"));
                reservation.setCheckout_time(resultSet.getTimestamp("checkout_time"));
                reservation.setDestination(Location.create(resultSet));
                reservation.setUser(User.create(resultSet));
                EquipmentItem item = new EquipmentItem();
                item.setId(resultSet.getInt("equipment_id"));
                item.setEquipmentName(resultSet.getString("equipment_name"));
                item.setEquipmentId(resultSet.getInt("equipment_item_id"));
                item.setSerialNumber(resultSet.getString("serial_number"));
                items.add(item);
            }
            reservation.setItems(items);
            return reservation;
        }
    }

    public boolean approveReservation(int recordID) {
        String sql = "UPDATE reservation SET status = ? WHERE reservation_id = ?";
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, ReservationStatus.APPROVED.getValue());
            statement.setInt(2, recordID);
            statement.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean declineReservation(int recordID) {
        String sql = "UPDATE reservation SET status = ? WHERE reservation_id = ?";
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, ReservationStatus.REJECTED.getValue());
            statement.setInt(2, recordID);
            statement.executeUpdate();
            releaseItem(recordID);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean checkInReservation(int recordID) {
        String sql = "UPDATE reservation SET status = ?, checkin_time = ? WHERE reservation_id = ?";
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, ReservationStatus.ACTIVE.getValue());
            statement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            statement.setInt(3, recordID);
            statement.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean checkOutReservation(int recordID) {
        String sql = "UPDATE reservation SET status = ?, checkout_time = ? WHERE reservation_id = ?";
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, ReservationStatus.FINISHED.getValue());
            statement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            statement.setInt(3, recordID);
            statement.executeUpdate();

            releaseItem(recordID);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void releaseItem(int recordID) {
        String getItemsQuery = "SELECT equipment_item_id FROM reservation_items WHERE reservation_id = ?";
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(getItemsQuery);
            statement.setInt(1, recordID);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int itemID = resultSet.getInt("equipment_item_id");
                String releaseItemQuery = "UPDATE equipment_item SET status = ? WHERE equipment_item_id = ?";
                PreparedStatement releaseItemStatement = connection.prepareStatement(releaseItemQuery);
                releaseItemStatement.setInt(1, ItemStatus.AVAILABLE.getValue());
                releaseItemStatement.setInt(2, itemID);
                releaseItemStatement.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
