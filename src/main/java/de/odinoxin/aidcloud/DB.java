package de.odinoxin.aidcloud;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class DB {
    private static SessionFactory sessionFactory;

    public static void setSessionFactory(SessionFactory factory) {
        if (sessionFactory != null)
            sessionFactory.close();
        sessionFactory = factory;
    }

    public static Session open() {
        if (DB.sessionFactory == null)
            throw new IllegalStateException("SessionFactory cannot be null!");
        Session session = DB.sessionFactory.openSession();
        if (session == null)
            throw new IllegalStateException("Could not open session!");
        return session;
    }
}
