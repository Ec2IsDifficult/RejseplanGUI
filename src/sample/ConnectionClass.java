package sample;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionClass {

    public Connection connection;

    public Connection getConnection() {

        try {
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\rasse\\Desktop\\5 Semester\\sqlite/rejseplanenCasperRasmus.db");


        } catch (Exception e) {
            e.printStackTrace();
        }

        return connection;
    }

}
