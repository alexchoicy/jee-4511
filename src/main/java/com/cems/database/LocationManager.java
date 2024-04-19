package com.cems.database;

import com.cems.Model.Location;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class LocationManager extends DatabaseManager{

    public ArrayList<Location> getLocations() {
        ArrayList<Location> locations = new ArrayList<>();
        try {
            String query = "SELECT * FROM location";
            try (Connection connection = getConnection();
                 Statement statement = connection.createStatement()) {
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    Location location = new Location();
                    location.setId(resultSet.getInt("location_id"));
                    location.setName(resultSet.getString("location_name"));
                    locations.add(location);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return locations;
    }

    public Location getLocation(int locationID) {
        try {
            String query = "SELECT * FROM location WHERE location_ID = ?";
            try (Connection connect = getConnection()) {
                PreparedStatement statement = connect.prepareStatement(query);

                statement.setInt(1, locationID);

                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next())  {
                    Location location = new Location();
                    location.setId(resultSet.getInt("location_id"));
                    location.setName(resultSet.getString("location_name"));
                    return location;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
