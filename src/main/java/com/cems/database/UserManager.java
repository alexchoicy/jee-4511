package com.cems.database;

import com.cems.Enums.UserRoles;
import com.cems.Model.Users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class UserManager extends DatabaseManager {

    public ArrayList<Users> getUsers() {
        try (Connection connection = getConnection()) {
            String sql = "SELECT * FROM users";
            System.out.println("sql: " + sql);
            Statement statement = connection.createStatement();

            boolean results = statement.execute(sql);
            System.out.println("results: " + results);
            if (!results) {
                return null;
            }
            ResultSet resultSet = statement.getResultSet();
            ArrayList<Users> users = new ArrayList<>();
            while (resultSet.next()) {
                Users user = new Users();
                user.setUserId(resultSet.getInt("userId"));
                user.setUsername(resultSet.getString("username"));
                user.setPhoneNumber(resultSet.getString("phoneNumber"));
                user.setFirstName(resultSet.getString("firstName"));
                user.setLastName(resultSet.getString("lastName"));
                user.setRole(UserRoles.getRoles(resultSet.getInt("role")));
                users.add(user);
            }
            resultSet.close();
            statement.close();
            connection.close();
            return users;
        } catch (Exception e) {
//            e.printStackTrace();
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }

    public Users Login(String username, String password) {
        try (Connection connection = getConnection()) {
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?;";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();
            Users user = new Users();
            if (resultSet.next()) {
                user.setUserId(resultSet.getInt("userId"));
                user.setUsername(resultSet.getString("username"));
                user.setPhoneNumber(resultSet.getString("phoneNumber"));
                user.setFirstName(resultSet.getString("firstName"));
                user.setLastName(resultSet.getString("lastName"));
                user.setPassword(resultSet.getString("password"));
                user.setRole(UserRoles.getRoles(resultSet.getInt("role")));
            } else {
                return null;
            }
            return user;
        } catch (Exception e) {
//            e.printStackTrace();
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }

    public boolean updateUserInfo(Users user) {
        try (Connection connection = getConnection()) {
            String checkSql = "SELECT COUNT(*) FROM users WHERE username = ? AND userId != ?";
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

            String updateSql = "UPDATE users SET username = ?, firstName = ?, lastName = ?, password = ? WHERE userId = ?";
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
}
