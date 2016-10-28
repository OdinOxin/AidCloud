package de.odinoxin.aidcloud.plugins;

import de.odinoxin.aidcloud.DB;
import de.odinoxin.aidcloud.Provider;
import org.hibernate.Session;

import java.lang.reflect.ParameterizedType;

public class RecordHandler<T> extends Provider {

    public RecordHandler() {
        generateDefaults();
    }

    public <T> T get(int id) {
        Session session = DB.open();
        T entity = session.get((Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0], id);
        session.close();
        return entity;
    }

    public T save(T entity) {
        Session session = DB.open();
        session.beginTransaction();
        int id = (int) session.save(entity);
        session.getTransaction().commit();
        session.close();
        return get(id);
    }

    public boolean delete(int id) {
        Session session = DB.open();
        session.beginTransaction();
        session.delete(this.get(id));
        session.getTransaction().commit();
        session.close();
        return true;
    }
}