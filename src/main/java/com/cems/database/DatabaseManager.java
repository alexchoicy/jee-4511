package com.cems.database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class DatabaseManager {

    protected Connection getConnection() throws SQLException, IOException, ClassNotFoundException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Class.forName("com.mysql.cj.jdbc.Driver");
        System.out.println("URL: " + com.cems.secret.DatabaseInfo.url + " Username: " + com.cems.secret.DatabaseInfo.username + " Password: " + com.cems.secret.DatabaseInfo.password);
        String url = com.cems.secret.DatabaseInfo.url;
        String username = com.cems.secret.DatabaseInfo.username;
        String password = com.cems.secret.DatabaseInfo.password;
        return DriverManager.getConnection(url, username, password);
    }

}
