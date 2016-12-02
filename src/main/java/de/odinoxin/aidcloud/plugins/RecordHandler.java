package de.odinoxin.aidcloud.plugins;

import de.odinoxin.aidcloud.*;
import org.hibernate.Session;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import javax.ws.rs.NotAuthorizedException;
import javax.xml.ws.WebServiceContext;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

public abstract class RecordHandler<T extends Recordable> extends Provider {

    public RecordHandler() {
        generateDefaults();
    }

    protected T get(int id, WebServiceContext wsCtx) {
        if (!Login.checkSession(wsCtx))
            throw new NotAuthorizedException(AidCloud.INVALID_SESSION);
        T entity = null;
        try (Session session = DB.open()) {
            this.setFetchMode(session);
            entity = session.get((Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0], id);
        }
        return entity;
    }

    protected int save(T entity, T original, WebServiceContext wsCtx) throws ConcurrentFault {
        if (!Login.checkSession(wsCtx))
            throw new NotAuthorizedException(AidCloud.INVALID_SESSION);

        if (entity != null && entity.getId() != 0) {
            if (original == null)
                throw new IllegalArgumentException("Original entity cannot be null on update!");
            if (entity.getId() != original.getId())
                throw new IllegalArgumentException("Entity is different from original entity!");
            T current = this.get(original.getId(), wsCtx);
            if (!original.equals(current))
                throw new ConcurrentFault("Entity was edited in the meantime!");
        }
        return this.generate(entity);
    }

    protected int generate(T entity) {
        if (entity == null)
            throw new IllegalArgumentException("Entity cannot be null!");
        int id = entity.getId();
        try (Session session = DB.open()) {
            session.beginTransaction();
            if (id == 0)
                id = (int) session.save(entity);
            else
                session.update(entity);
            session.getTransaction().commit();
        }
        return id;
    }

    protected boolean delete(int id, WebServiceContext wsCtx) {
        if (!Login.checkSession(wsCtx))
            throw new NotAuthorizedException(AidCloud.INVALID_SESSION);
        T entity = this.get(id, wsCtx);
        try (Session session = DB.open()) {
            session.beginTransaction();
            session.delete(entity);
            session.getTransaction().commit();
        }
        return true;
    }

    protected List<T> search(String[] expressions, int max, WebServiceContext wsCtx) {
        if (!Login.checkSession(wsCtx))
            throw new NotAuthorizedException(AidCloud.INVALID_SESSION);
        Session session = DB.open();
        CriteriaBuilder builder = session.getEntityManagerFactory().getCriteriaBuilder();
        CriteriaQuery<T> criteria = builder.createQuery((Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
        Predicate predicates = builder.conjunction();
        Root<T> root = criteria.from((Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
        if (expressions != null && expressions.length > 0) {
            predicates = builder.disjunction();
            Expression<Integer> dbIdExpression = getIdExpression(root);
            List<Expression<String>> dbExpressions = getSearchExpressions(root);
            for (String expr : expressions) {
                if (dbIdExpression != null && expr.matches("-?\\d+"))
                    predicates = builder.or(predicates, builder.equal(dbIdExpression, Integer.parseInt(expr)));
                if (dbExpressions != null)
                    for (Expression<String> dbExpr : dbExpressions) {
                        predicates = builder.or(predicates, builder.like(builder.lower(dbExpr), "%" + expr.toLowerCase() + "%"));
                    }
            }
        }
        criteria.where(predicates);
        EntityManager em = session.getEntityManagerFactory().createEntityManager();
        List<T> tmpList = em.createQuery(criteria).setMaxResults(Math.max(0, max) + 1).getResultList();
        List<T> result = new ArrayList<>();
        for (T tmp : tmpList)
            result.add((T) tmp.clone());
        em.close();
        session.close();
        return result;
    }

    protected Expression<Integer> getIdExpression(Root<T> root) {
        return null;
    }

    protected List<Expression<String>> getSearchExpressions(Root<T> root) {
        return null;
    }

    protected void setFetchMode(Session session) {

    }

    protected boolean anyRecords() {
        boolean anyRecords = false;
        try (Session session = DB.open()) {
            CriteriaBuilder builder = session.getEntityManagerFactory().getCriteriaBuilder();
            CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
            criteria.select(builder.count(criteria.from((Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0])));
            anyRecords = session.getEntityManagerFactory().createEntityManager().createQuery(criteria).getResultList().get(0) > 0;
        }
        return anyRecords;
    }
}