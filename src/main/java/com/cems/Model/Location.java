package com.cems.Model;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Location implements Serializable {
    private int id;
    private String name;
    private String address;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public static Location create(ResultSet resultSet) throws SQLException {
        Location location = new Location();
        location.setId(resultSet.getInt("location_id"));
        location.setName(resultSet.getString("location_name"));
        location.setAddress(resultSet.getString("location_address"));
        return location;
    }
}
