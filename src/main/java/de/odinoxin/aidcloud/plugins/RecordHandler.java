package de.odinoxin.aidcloud.plugins;

import de.odinoxin.aidcloud.DB;
import de.odinoxin.aidcloud.Provider;
import org.hibernate.Session;

import javax.persistence.criteria.*;
import java.lang.reflect.ParameterizedType;
import java.util.List;

public abstract class RecordHandler<T extends Recordable> extends Provider {

    public RecordHandler() {
        generateDefaults();
    }

    protected T get(int id) {
        Session session = DB.open();
        T entity = session.get((Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0], id);
        session.close();
        return entity;
    }

    protected int save(T entity) {
        Session session = DB.open();
        session.beginTransaction();
        int id = entity.getId();
        if (id == 0)
            id = (int) session.save(entity);
        else
            session.update(entity);
        session.getTransaction().commit();
        session.close();
        return id;
    }

    protected boolean delete(int id) {
        Session session = DB.open();
        session.beginTransaction();
        session.delete(this.get(id));
        session.getTransaction().commit();
        session.close();
        return true;
    }

    protected List<T> search(String[] expr) {
        Session session = DB.open();
        CriteriaBuilder builder = session.getEntityManagerFactory().getCriteriaBuilder();
        CriteriaQuery<T> criteria = builder.createQuery((Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
        Predicate predicates = builder.conjunction();
        Root<T> root = criteria.from((Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
        if (expr != null && expr.length > 0) {
            for (int i = 0; i < expr.length; i++)
                expr[i] = "%" + expr[i].toLowerCase() + "%";
            predicates = builder.disjunction();
            List<Expression<String>> expressions = getSearchExpressions(root);
            if (expressions != null)
                for (int i = 0; i < expr.length; i++)
                    for (int j = 0; j < expressions.size(); j++)
                        predicates = builder.or(predicates, builder.like(builder.lower(expressions.get(j)), expr[i]));
        }
        criteria.where(predicates);
        List<T> result = session.getEntityManagerFactory().createEntityManager().createQuery(criteria).getResultList();
        session.close();
        return result;
    }

    protected List<Expression<String>> getSearchExpressions(Root<T> root) {
        return null;
    }

    protected boolean anyRecords() {
        Session session = DB.open();
        CriteriaBuilder builder = session.getEntityManagerFactory().getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        criteria.select(builder.count(criteria.from((Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0])));
        return session.getEntityManagerFactory().createEntityManager().createQuery(criteria).getResultList().get(0) > 0;
    }
}