package de.odinoxin.aidcloud.plugins;

import de.odinoxin.aidcloud.DB;
import de.odinoxin.aidcloud.Provider;
import org.hibernate.Session;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
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
        T entity = this.get(id);
        Session session = DB.open();
        session.beginTransaction();
        session.delete(entity);
        session.getTransaction().commit();
        session.close();
        return true;
    }

    protected List<T> search(String[] expressions) {
        Session session = DB.open();
        CriteriaBuilder builder = session.getEntityManagerFactory().getCriteriaBuilder();
        CriteriaQuery<T> criteria = builder.createQuery((Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
        Predicate predicates = builder.conjunction();
        Root<T> root = criteria.from((Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
        if (expressions != null && expressions.length > 0) {
            for (int i = 0; i < expressions.length; i++)
                expressions[i] = "%" + expressions[i].toLowerCase() + "%";
            predicates = builder.disjunction();
            List<Expression<String>> dbExpressions = getSearchExpressions(root);
            if (dbExpressions != null)
                for (String expr : expressions)
                    for (Expression<String> dbExpr : dbExpressions)
                        predicates = builder.or(predicates, builder.like(builder.lower(dbExpr), expr));
        }
        criteria.where(predicates);
        EntityManager em = session.getEntityManagerFactory().createEntityManager();
        List<T> tmpList = em.createQuery(criteria).getResultList();
        List<T> result = new ArrayList<>();
        for (T tmp : tmpList)
            result.add((T) tmp.clone());
        em.close();
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