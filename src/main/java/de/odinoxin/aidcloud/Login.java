package de.odinoxin.aidcloud;

import de.odinoxin.aidcloud.plugins.people.Person;
import de.odinoxin.aidcloud.plugins.people.Person_;
import org.hibernate.Session;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@WebService
public class Login {
    private static Login instance = new Login();

    public static boolean checkLogin(Person p) {
        return p == AidCloud.SYSTEM || Login.instance.checkLogin(p.getId(), p.getPwd());
    }

    @WebMethod
    public boolean checkLogin(@WebParam(name = "userId") int userId, @WebParam(name = "pwd") String pwd) {
        boolean access = false;
        Session session = DB.open();
        CriteriaBuilder builder = session.getEntityManagerFactory().getCriteriaBuilder();
        CriteriaQuery<Person> criteria = builder.createQuery(Person.class);
        Predicate predicates = builder.conjunction();
        Root<Person> root = criteria.from(Person.class);
        predicates = builder.and(predicates, builder.equal(root.get(Person_.id), userId));
        predicates = builder.and(predicates, builder.equal(root.get(Person_.pwd), pwd));
        criteria.where(predicates);
        List<Person> tmpList = session.getEntityManagerFactory().createEntityManager().createQuery(criteria).getResultList();
        if (tmpList != null && tmpList.size() == 1)
            access = true;
        session.close();
        return access;
    }

    @WebMethod
    public List<Person> searchLogin(@WebParam(name = "expr") String[] expr, @WebParam(name = "max") int max) {
        Session session = DB.open();
        List<Person> result = new ArrayList<>();
        CriteriaBuilder builder = session.getEntityManagerFactory().getCriteriaBuilder();
        CriteriaQuery<Person> criteria = builder.createQuery(Person.class);
        Predicate predicates = builder.conjunction();
        Root<Person> root = criteria.from(Person.class);
        if (expr != null && expr.length > 0) {
            for (int i = 0; i < expr.length; i++)
                expr[i] = "%" + expr[i].toLowerCase() + "%";
            predicates = builder.disjunction();
            for (int i = 0; i < expr.length; i++) {
                predicates = builder.or(predicates, builder.like(builder.lower(root.get(Person_.forename)), expr[i]));
                predicates = builder.or(predicates, builder.like(builder.lower(root.get(Person_.name)), expr[i]));
                predicates = builder.or(predicates, builder.like(builder.lower(root.get(Person_.code)), expr[i]));
            }
        }
        criteria.where(predicates);
        List<Person> tmpList = session.getEntityManagerFactory().createEntityManager().createQuery(criteria).setMaxResults(Math.max(0, max) + 1).getResultList();
        if (tmpList != null)
            for (Person tmp : tmpList)
                result.add(new Person(tmp.getId(), tmp.getName(), tmp.getForename(), tmp.getCode(), null, null, null));
        session.close();
        return result;
    }
}
