package com.cems.database;

import com.cems.Model.Equipment;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class WishListManager extends DatabaseManager {
    public ArrayList<Equipment> getWishListed(int userID) throws SQLException, IOException, ClassNotFoundException {
        ArrayList<Equipment> equipmentList = new ArrayList<>();
        String sql = "SELECT * FROM wishlist_items WHERE user_id = ?";
        String getEquipment = "SELECT * FROM equipment WHERE equipment_id = ?";

        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userID);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                PreparedStatement preparedStatement1 = connection.prepareStatement(getEquipment);
                preparedStatement1.setInt(1, resultSet.getInt("equipment_id"));
                ResultSet resultSet1 = preparedStatement1.executeQuery();

                while (resultSet1.next()) {
                    Equipment item = new Equipment();
                    item.setId(resultSet1.getInt("equipment_id"));
                    item.setName(resultSet1.getString("equipment_name"));
                    item.setDescription(resultSet1.getString("description"));
                    item.setStaffOnly(resultSet1.getBoolean("isStaffOnly"));
                    item.setListed(resultSet1.getBoolean("isListed"));
                    item.setImagePath(resultSet1.getString("image_path"));
                    equipmentList.add(item);
                }
            }
        }

        return equipmentList;
    }

    public void removeWishListed(int user_id, int equipment_id) {
        String sql = "DELETE FROM wishlist_items WHERE equipment_id = ? AND user_id = ?";
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, equipment_id);
            preparedStatement.setInt(2, user_id);
            preparedStatement.executeUpdate();
        } catch (SQLException | IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void addWishList(int userId, int equipmentId) {
        String sql = "INSERT INTO wishlist_items (user_id, equipment_id) VALUES (?, ?)";
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, equipmentId);
            preparedStatement.executeUpdate();
        } catch (SQLException | IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
