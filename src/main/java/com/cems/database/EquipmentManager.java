package com.cems.database;

import com.cems.Enums.ItemStatus;
import com.cems.Enums.UserRoles;
import com.cems.Model.*;
import com.cems.Utils.ParseUtil;
import com.cems.Utils.QueryBuilder;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class EquipmentManager extends DatabaseManager {

    public PagedResult<ArrayList<Equipment>> getEquipmentList(int userId, UserRoles roles, String searchText,
            int locationId, boolean showWishlistOnly, boolean showStaffOnly, boolean isListed,
            boolean showAvailableOnly, int page, int pageSize) {
        PagedResult<ArrayList<Equipment>> result = new PagedResult<>();
        int offset = (page - 1) * pageSize;

        QueryBuilder searchQuery = new QueryBuilder();
        QueryBuilder countQuery = new QueryBuilder();
        searchQuery.select("e.*");
        searchQuery.select("COALESCE(COUNT(i.equipment_id), 0) as available_quantity");
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

            System.out.println(searchStatement.toString());
            System.out.println(countStatement.toString());

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
                    Equipment item = GetEquipmentDataToBean(resultSet);
                    data.add(item);
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
        String sql = "SELECT * FROM equipment_item INNER JOIN location ON equipment_item.current_location = location.location_id WHERE equipment_id = ? AND serial_number LIKE ? OR equipment_item_id = ?";
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

    private Equipment GetEquipmentDataToBean(ResultSet resultSet) {
        Equipment item = new Equipment();
        try {
            item.setId(resultSet.getInt("equipment_id"));
            item.setName(resultSet.getString("equipment_name"));
            item.setDescription(resultSet.getString("description"));
            item.setAvailableQuantity(resultSet.getInt("available_quantity"));
            item.setStaffOnly(resultSet.getBoolean("isStaffOnly"));
            item.setListed(resultSet.getBoolean("isListed"));
            item.setImagePath(resultSet.getString("image_path"));
            item.setWishListed(resultSet.getBoolean("isWishlisted"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return item;
    }
}
