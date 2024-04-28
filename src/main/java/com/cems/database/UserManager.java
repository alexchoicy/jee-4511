package com.cems.database;

import com.cems.Enums.UserRoles;
import com.cems.Model.User;

import java.sql.*;
import java.util.ArrayList;

public class UserManager extends DatabaseManager {

    public ArrayList<User> getUsers() {
        try (Connection connection = getConnection()) {
            String sql = "SELECT * FROM user";
            System.out.println("sql: " + sql);
            Statement statement = connection.createStatement();

            boolean results = statement.execute(sql);
            System.out.println("results: " + results);
            if (!results) {
                return null;
            }
            ResultSet resultSet = statement.getResultSet();
            ArrayList<User> users = new ArrayList<>();
            while (resultSet.next()) {
                User user = GetUserDataToBean(resultSet);
                users.add(user);
            }
            resultSet.close();
            statement.close();
            connection.close();
            return users;
        } catch (Exception e) {
            // e.printStackTrace();
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }

    public ArrayList<User> getDeliveryName() {
        try (Connection connection = getConnection()) {
            String sql = "SELECT * FROM user WHERE role = 4";
            System.out.println("sql: " + sql);
            Statement statement = connection.createStatement();

            boolean results = statement.execute(sql);
            System.out.println("results: " + results);
            if (!results) {
                return null;
            }
            ResultSet resultSet = statement.getResultSet();
            ArrayList<User> users = new ArrayList<>();
            while (resultSet.next()) {
                User user = GetUserDataToBean(resultSet);
                users.add(user);
            }
            resultSet.close();
            statement.close();
            connection.close();
            return users;
        } catch (Exception e) {
            // e.printStackTrace();
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }

    public User Login(String username, String password) {
        try (Connection connection = getConnection()) {
            String sql = "SELECT * FROM user WHERE username = ? AND password = ?;";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();
            User user;
            if (resultSet.next()) {
                user = GetUserDataToBean(resultSet);
                return user;
            }
        } catch (Exception e) {
            // e.printStackTrace();
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }

    public boolean updateUserInfo(User user) {
        try (Connection connection = getConnection()) {
            String checkSql = "SELECT COUNT(*) FROM user WHERE username = ? AND user_id != ?;";
            PreparedStatement checkStatement = connection.prepareStatement(checkSql);
            checkStatement.setString(1, user.getUsername());
            checkStatement.setInt(2, user.getUserId());
            ResultSet resultSet = checkStatement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                if (count > 0) {
                    return false;
                }
            }

            String updateSql = "UPDATE user SET username = ?, first_name = ?, last_name = ?, password = ? WHERE user_id = ?";
            PreparedStatement updateStatement = connection.prepareStatement(updateSql);
            updateStatement.setString(1, user.getUsername());
            updateStatement.setString(2, user.getFirstName());
            updateStatement.setString(3, user.getLastName());
            updateStatement.setString(4, user.getPassword());
            updateStatement.setInt(5, user.getUserId());
            int rowsAffected = updateStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean CreateUser(User user) {
        try (Connection connection = getConnection()) {
            String checkSql = "SELECT COUNT(*) FROM user WHERE username = ? AND user_id != ?;";
            PreparedStatement checkStatement = connection.prepareStatement(checkSql);
            checkStatement.setString(1, user.getUsername());
            checkStatement.setInt(2, user.getUserId());
            ResultSet resultSet = checkStatement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                if (count > 0) {
                    return false;
                }
            }

            String createSql = "INSERT INTO user (username, password, phone_number, first_name, last_name, role) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement createStatement = connection.prepareStatement(createSql, Statement.RETURN_GENERATED_KEYS);
            createStatement.setString(1, user.getUsername());
            createStatement.setString(2, user.getPassword());
            createStatement.setString(3, "");
            createStatement.setString(4, user.getFirstName());
            createStatement.setString(5, user.getLastName());
            createStatement.setInt(6, user.getRole().getValue());
            int rowsAffected = createStatement.executeUpdate();

            ResultSet generatedKeys = createStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int generatedUserId = generatedKeys.getInt(1);
                user.setUserId(generatedUserId);
            }

            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean EditUser(User user) {
        try (Connection connection = getConnection()) {
            String checkSql = "SELECT COUNT(*) FROM user WHERE username = ? AND user_id != ?;;";
            PreparedStatement checkStatement = connection.prepareStatement(checkSql);
            checkStatement.setString(1, user.getUsername());
            checkStatement.setInt(2, user.getUserId());
            ResultSet resultSet = checkStatement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                if (count > 0) {
                    return false;
                }
            }

            String editSql = "UPDATE user SET username = ?, first_name = ?, last_name = ?, password = ?, role = ? WHERE user_id = ?";
            PreparedStatement editStatement = connection.prepareStatement(editSql);
            editStatement.setString(1, user.getUsername());
            editStatement.setString(2, user.getFirstName());
            editStatement.setString(3, user.getLastName());
            editStatement.setString(4, user.getPassword());
            editStatement.setInt(5, user.getRole().getValue());
            editStatement.setInt(6, user.getUserId());
            int rowsAffected = editStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private User GetUserDataToBean(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setUserId(resultSet.getInt("user_id"));
        user.setUsername(resultSet.getString("username"));
        user.setPhoneNumber(resultSet.getString("phone_number"));
        user.setFirstName(resultSet.getString("first_name"));
        user.setLastName(resultSet.getString("last_name"));
        user.setPassword(resultSet.getString("password"));
        user.setRole(UserRoles.getRole(resultSet.getInt("role")));
        return user;
    }

    public boolean RemoveUser(User user) {
        try (Connection connection = getConnection()) {
            String removeSql = "DELETE FROM user WHERE username = ?;";
            PreparedStatement removeStatement = connection.prepareStatement(removeSql);
            removeStatement.setString(1, user.getUsername());
            int rowsAffected = removeStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
