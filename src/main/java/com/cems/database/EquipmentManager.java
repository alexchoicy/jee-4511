package com.cems.database;

import com.cems.Enums.ItemStatus;
import com.cems.Enums.UserRoles;
import com.cems.Exceptions.*;
import com.cems.Model.*;
import com.cems.Model.Display.EquipmentDisplay;
import com.cems.Model.Request.CreateEquipmentItem;
import com.cems.Model.Request.ReservationCart;
import com.cems.Utils.ParseUtil;
import com.cems.Utils.QueryBuilder;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class EquipmentManager extends DatabaseManager {

    public PagedResult<ArrayList<Equipment>> getEquipmentList(int userId, UserRoles roles, String searchText,
                                                              int locationId, boolean showWishlistOnly, boolean showStaffOnly, boolean isListed,
                                                              boolean showAvailableOnly, int page, int pageSize) {
        PagedResult<ArrayList<Equipment>> result = new PagedResult<>();
        int offset = (page - 1) * pageSize;

        QueryBuilder searchQuery = new QueryBuilder();
        QueryBuilder countQuery = new QueryBuilder();
        String availableQuantityQuery = "SELECT COALESCE(COUNT(equipment_id), 0) as available_quantity FROM equipment_item WHERE status = ? AND equipment_id = ?";
        searchQuery.select("e.*");
        searchQuery.select("CASE WHEN w.wishlist_id IS NOT NULL THEN TRUE ELSE FALSE END as isWishlisted");
        searchQuery.from("equipment e LEFT JOIN equipment_item i ON e.equipment_id = i.equipment_id " +
                "LEFT JOIN wishlist_items w ON e.equipment_id = w.equipment_id AND w.user_id = ?");

        countQuery.select("COUNT(*)");
        countQuery.from("equipment e INNER JOIN equipment_item i ON e.equipment_id = i.equipment_id " +
                "LEFT JOIN wishlist_items w ON e.equipment_id = w.equipment_id AND w.user_id = ?");

        if (showAvailableOnly) {
            searchQuery.where("i.status = ?");
            countQuery.where("i.status = ?");
        }

        if (roles != UserRoles.USER && showStaffOnly) {
            searchQuery.where("e.isStaffOnly = ?");
            countQuery.where("isStaffOnly = ?");
        }

        if (isListed) {
            searchQuery.where("e.isListed = ?");
            countQuery.where("isListed = ?");
        }

        if (searchText != null && !searchText.isEmpty()) {
            searchQuery.where("e.equipment_name LIKE ? OR i.serial_number LIKE ?");
            countQuery.where("equipment_name LIKE ? OR serial_number LIKE ?");
        }

        if (locationId > 0) {
            searchQuery.where("e.location_id = ?");
            countQuery.where("location_id = ?");
        }

        if (showWishlistOnly) {
            searchQuery.where("w.wishlist_id IS NOT NULL");
            countQuery.where("w.wishlist_id IS NOT NULL");
        }

        searchQuery.options("GROUP BY e.equipment_id , isWishlisted LIMIT ? OFFSET ?");

        try (Connection connection = getConnection()) {
            PreparedStatement countStatement = connection.prepareStatement(countQuery.build());
            PreparedStatement searchStatement = connection.prepareStatement(searchQuery.build());
            int index = 1;
            int countIndex = 1;

            searchStatement.setInt(index++, userId);
            countStatement.setInt(countIndex++, userId);

            if (showAvailableOnly) {
                searchStatement.setString(index++, ItemStatus.AVAILABLE.name());
                countStatement.setString(countIndex++, ItemStatus.AVAILABLE.name());
            }

            if (roles != UserRoles.USER && showStaffOnly) {
                searchStatement.setBoolean(index++, true);
                countStatement.setBoolean(countIndex++, true);
            }

            if (isListed) {
                searchStatement.setBoolean(index++, true);
                countStatement.setBoolean(countIndex++, true);
            }

            if (searchText != null && !searchText.isEmpty()) {
                searchStatement.setString(index++, "%" + searchText + "%");
                searchStatement.setString(index++, "%" + searchText + "%");
                countStatement.setString(countIndex++, "%" + searchText + "%");
                countStatement.setString(countIndex++, "%" + searchText + "%");
            }

            if (locationId > 0) {
                searchStatement.setInt(index++, locationId);
                countStatement.setInt(countIndex++, locationId);
            }

            searchStatement.setInt(index++, pageSize);
            searchStatement.setInt(index++, offset);


            try (ResultSet countResultSet = countStatement.executeQuery()) {
                if (countResultSet.next()) {
                    result.setTotal(countResultSet.getInt(1));
                    result.setPage(page);
                    result.setPageSize(pageSize);
                }
            }

            ArrayList<Equipment> data = new ArrayList<>();

            try (ResultSet resultSet = searchStatement.executeQuery()) {
                while (resultSet.next()) {
                    Equipment item = getEquipmentDataToBean(resultSet);
                    data.add(item);
                }
            }

            for (Equipment item : data) {
                PreparedStatement availableQuantityStatement = connection.prepareStatement(availableQuantityQuery);
                availableQuantityStatement.setInt(1, ItemStatus.AVAILABLE.getValue());
                availableQuantityStatement.setInt(2, item.getId());
                try (ResultSet resultSet = availableQuantityStatement.executeQuery()) {
                    if (resultSet.next()) {
                        item.setAvailableQuantity(resultSet.getInt("available_quantity"));
                    }
                }
            }

            result.setData(data);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public EquipmentDisplay getEquipmentDetail(int equipmentId) {
        String sql = "SELECT * FROM equipment LEFT JOIN equipment_item ON equipment.equipment_id = equipment_item.equipment_id LEFT JOIN location ON equipment_item.current_location = location.location_id WHERE equipment.equipment_id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, equipmentId);

            EquipmentDisplay equipmentDisplay = new EquipmentDisplay();
            Equipment equipment = new Equipment();
            int availableQuantity = 0;

            ArrayList<EquipmentItem> items = new ArrayList<>();
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    if (equipment.getId() == 0) {
                        equipment.setId(resultSet.getInt("equipment_id"));
                        equipment.setName(resultSet.getString("equipment_name"));
                        equipment.setDescription(resultSet.getString("description"));
                        equipment.setStaffOnly(resultSet.getBoolean("isStaffOnly"));
                        equipment.setListed(resultSet.getBoolean("isListed"));
                        equipment.setImagePath(resultSet.getString("image_path"));
                    }

                    EquipmentItem item = new EquipmentItem();
                    Location location = new Location();
                    item.setId(resultSet.getInt("equipment_item_id"));
                    item.setSerialNumber(resultSet.getString("serial_number"));
                    ItemStatus status = ItemStatus.getStatus(resultSet.getInt("status"));
                    item.setStatus(status);
                    item.setBorrowedTimes(resultSet.getInt("borrowed_time"));

                    location.setId(resultSet.getInt("location_id"));
                    location.setName(resultSet.getString("location_name"));
                    location.setAddress(resultSet.getString("location_address"));
                    item.setLocation(location);

                    if (status == ItemStatus.AVAILABLE) {
                        availableQuantity++;
                    }
                    items.add(item);
                }

            }
            equipment.setAvailableQuantity(availableQuantity);
            equipmentDisplay.setEquipment(equipment);
            equipmentDisplay.setItems(items);

            return equipmentDisplay;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<EquipmentItem> getEquipmentItemWithSearch(int equipmentID, String searchText) {
        ArrayList<EquipmentItem> items = new ArrayList<>();
        String sql = "SELECT * FROM equipment_item INNER JOIN location ON equipment_item.current_location = location.location_id WHERE equipment_id = ? AND serial_number LIKE ? OR equipment_item_id = ? ORDER BY equipment_item_id DESC";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, equipmentID);
            statement.setString(2, "%" + searchText + "%");
            statement.setInt(3, ParseUtil.tryParseInt(searchText, 0));
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    EquipmentItem item = new EquipmentItem();
                    item.setId(resultSet.getInt("equipment_item_id"));
                    item.setSerialNumber(resultSet.getString("serial_number"));
                    item.setBorrowedTimes(resultSet.getInt("borrowed_time"));
                    item.setStatus(ItemStatus.getStatus(resultSet.getInt("status")));
                    item.setEquipmentId(resultSet.getInt("equipment_id"));
                    Location location = new Location();
                    location.setId(resultSet.getInt("location_id"));
                    location.setName(resultSet.getString("location_name"));
                    location.setAddress(resultSet.getString("location_address"));
                    item.setLocation(location);
                    items.add(item);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return items;
    }

    public static Equipment getEquipmentDataToBean(ResultSet resultSet) {
        Equipment item = new Equipment();
        try {
            item.setId(resultSet.getInt("equipment_id"));
            item.setName(resultSet.getString("equipment_name"));
            item.setDescription(resultSet.getString("description"));
            item.setStaffOnly(resultSet.getBoolean("isStaffOnly"));
            item.setListed(resultSet.getBoolean("isListed"));
            item.setImagePath(resultSet.getString("image_path"));
            item.setWishListed(resultSet.getBoolean("isWishlisted"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return item;
    }

    public boolean deleteEquipmentItem(int equipmentId, int itemId) throws SQLException, IOException, ClassNotFoundException, ItemsInUseException, ItemNotFoundException {
        String deleteSql = "DELETE FROM equipment_item WHERE equipment_id = ? AND equipment_item_id = ?";
        String checkSql = "SELECT status FROM equipment_item WHERE equipment_id = ? AND equipment_item_id = ?";
        Connection connection = getConnection();
        PreparedStatement checkStatement = connection.prepareStatement(checkSql);
        checkStatement.setInt(1, equipmentId);
        checkStatement.setInt(2, itemId);
        ResultSet checkResultSet = checkStatement.executeQuery();
        if (!checkResultSet.next()) {
            throw new ItemNotFoundException(itemId);
        }
        ItemStatus status = ItemStatus.getStatus(checkResultSet.getInt(1));
        if (status != ItemStatus.AVAILABLE) {
            throw new ItemsInUseException(equipmentId, itemId);
        }
        PreparedStatement deleteStatement = connection.prepareStatement(deleteSql);
        deleteStatement.setInt(1, equipmentId);
        deleteStatement.setInt(2, itemId);
        deleteStatement.executeUpdate();
        return true;
    }

    public boolean deleteEquipment(int equipmentId) throws SQLException, IOException, ClassNotFoundException, HasItemsException, EquipmentNotFoundException {
        String countSql = "SELECT COUNT(*) FROM equipment_item WHERE equipment_id = ?";
        String deleteSql = "DELETE FROM equipment WHERE equipment_id = ?";
        Connection connection = getConnection();
        PreparedStatement countStatement = connection.prepareStatement(countSql);
        countStatement.setInt(1, equipmentId);
        ResultSet countResultSet = countStatement.executeQuery();
        countResultSet.next();
        if (countResultSet.getInt(1) > 0) {
            throw new HasItemsException(equipmentId);
        }
        PreparedStatement deleteStatement = connection.prepareStatement(deleteSql);
        deleteStatement.setInt(1, equipmentId);
        int result = deleteStatement.executeUpdate();
        if (result == 0) {
            throw new EquipmentNotFoundException(equipmentId);
        }
        return true;
    }

    public ArrayList<CreateEquipmentItem> createEquipmentItems(int equipmentId, ArrayList<CreateEquipmentItem> newItems) {
        String sql = "INSERT INTO equipment_item (equipment_id, serial_number, status, current_location, detail) VALUES (?, ?, ?, ?, ?)";
        String checkSql = "SELECT COUNT(*) FROM equipment_item WHERE equipment_id = ? AND serial_number = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            connection.setAutoCommit(false);
            Set<String> serialNumbers = new HashSet<>();
            ArrayList<CreateEquipmentItem> errorItems = new ArrayList<>();
            for (CreateEquipmentItem item : newItems) {
                if (item.getSerialNumber() == null || item.getSerialNumber().isEmpty()) {
                    item.setErrorMessages("Serial number is required");
                    errorItems.add(item);
                    continue;
                }

                PreparedStatement checkStatement = connection.prepareStatement(checkSql);
                checkStatement.setInt(1, equipmentId);
                checkStatement.setString(2, item.getSerialNumber());
                ResultSet checkResultSet = checkStatement.executeQuery();
                checkResultSet.next();
                if (checkResultSet.getInt(1) > 0) {
                    item.setErrorMessages("Serial number already exists");
                    errorItems.add(item);
                    continue;
                }

                if (!serialNumbers.add(item.getSerialNumber())) {
                    item.setErrorMessages("Duplicate serial number");
                    errorItems.add(item);
                    continue;
                }

                statement.setInt(1, equipmentId);
                statement.setString(2, item.getSerialNumber());
                statement.setInt(3, item.getStatus());
                statement.setInt(4, item.getLocation());
                statement.setString(5, "");
                statement.addBatch();
            }
            int[] result = statement.executeBatch();
            connection.commit();
            System.out.println("length" + result.length);
            for (int i = 0; i < result.length; i++) {
                System.out.println("result" + result[i]);
                if (result[i] == 0) {
                    newItems.get(i).setErrorMessages("Failed to insert into database");
                }
            }
            return errorItems;
        } catch (Exception e) {
            e.printStackTrace();
            // Set error message for all items in case of exception
            for (CreateEquipmentItem item : newItems) {
                item.setErrorMessages("Exception occurred: " + e.getMessage());
            }
            return newItems;
        }
    }

    public boolean editEquipment(int equipmentId, String name, String description, boolean isStaffOnly, boolean isListed) {
        String sql = "UPDATE equipment SET equipment_name = ?, description = ?, isStaffOnly = ?, isListed = ? WHERE equipment_id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setString(2, description);
            statement.setBoolean(3, isStaffOnly);
            statement.setBoolean(4, isListed);
            statement.setInt(5, equipmentId);
            int result = statement.executeUpdate();
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public Equipment CreateEquipment(Equipment equipment) {
        String sql = "INSERT INTO equipment (equipment_name, description, isStaffOnly, isListed, image_path) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, equipment.getName());
            statement.setString(2, equipment.getDescription());
            statement.setBoolean(3, equipment.isStaffOnly());
            statement.setBoolean(4, equipment.isListed());
            statement.setString(5, equipment.getImagePath());
            statement.executeUpdate();
            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    equipment.setId(resultSet.getInt(1));
                    return equipment;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
  public ReservationCart addItemsToCart(UserRoles role, int equipmentId, int quantity) throws StaffOnlyException, SQLException, IOException, ClassNotFoundException, NotEnoughItemException {
        String sql = "select equipment.equipment_id, equipment.equipment_name, equipment.isStaffOnly , COALESCE(COUNT(equipment_item.equipment_id), 0) as available_quantity FROM equipment LEFT JOIN `4511`.equipment_item on equipment.equipment_id = equipment_item.equipment_id" +
                " where equipment.equipment_id = ? GROUP BY equipment.equipment_id";
        ReservationCart cartItem = null;

        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, equipmentId);
        try (ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                boolean isStaffOnly = resultSet.getBoolean("isStaffOnly");
                if (role.equals(UserRoles.USER) && isStaffOnly) {
                    throw new StaffOnlyException(equipmentId);
                }
                cartItem = ReservationCart.create(resultSet, quantity);

                if (resultSet.getInt("available_quantity") < quantity) {
                    throw new NotEnoughItemException(equipmentId, cartItem.getQuantity(), quantity);
                }
            }
            return cartItem;
        }
    }
}
