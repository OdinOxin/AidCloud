package de.odinoxin.aidcloud;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {
    public static Connection con;
    private static SessionFactory sessionFactory;

    static {
        DB.sessionFactory = new Configuration().configure().buildSessionFactory();
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            DB.con = DriverManager.getConnection("jdbc:sqlserver://localhost;databaseName=AidDesk", "USER", "PWD");
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public static Session open() {
        return DB.sessionFactory.openSession();
    }
}
