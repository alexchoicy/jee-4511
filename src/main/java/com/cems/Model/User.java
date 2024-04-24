package com.cems.Model;

import com.cems.Enums.UserRoles;

import java.io.Serializable;
import java.sql.ResultSet;

public class User implements Serializable {
    private int userId;
    private String username;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private String password;
    private UserRoles role;
    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public UserRoles getRole() {
        return role;
    }

    public void setRole(UserRoles role) {
        this.role = role;
    }

    public static User create(ResultSet resultSet) {
        User user = new User();
        try {
            user.setUserId(resultSet.getInt("user_id"));
            user.setUsername(resultSet.getString("username"));
            user.setPhoneNumber(resultSet.getString("phone_number"));
            user.setFirstName(resultSet.getString("first_name"));
            user.setLastName(resultSet.getString("last_name"));
            user.setRole(UserRoles.getRole(resultSet.getInt("role")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }
}
