package de.odinoxin.aidcloud;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class DB {
    private static SessionFactory sessionFactory;

    static {
        DB.sessionFactory = new Configuration().configure().buildSessionFactory();
    }

    public static Session open() {
        if (DB.sessionFactory == null)
            throw new IllegalStateException("SessionFactory could not be initialized!");
        Session session = DB.sessionFactory.openSession();
        if (session == null)
            throw new IllegalStateException("Could not open session!");
        return session;
    }
}
