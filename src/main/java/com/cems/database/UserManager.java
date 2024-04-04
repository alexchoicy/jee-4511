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
                user.setPassword(resultSet.getString("password"));
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
                user.setPassword(resultSet.getString("password"));
                user.setPhoneNumber(resultSet.getString("phoneNumber"));
                user.setFirstName(resultSet.getString("firstName"));
                user.setLastName(resultSet.getString("lastName"));
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
}
