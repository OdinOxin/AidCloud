package de.odinoxin.aidcloud;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBMgr {
    public static Connection DB;

    static {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            DBMgr.DB = DriverManager.getConnection("jdbc:sqlserver://localhost;databaseName=AidDesk", "GoBabyGo", "comeback");
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }
}
