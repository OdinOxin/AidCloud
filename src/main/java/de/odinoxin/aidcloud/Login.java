package de.odinoxin.aidcloud;

import de.odinoxin.aidcloud.plugins.people.Person;
import de.odinoxin.aidcloud.plugins.people.Person_;
import org.hibernate.Session;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebService
public class Login {
    @WebMethod
    public boolean checkLogin(int userId, String pwd) {
        try {
            PreparedStatement statement = DB.con.prepareStatement("SELECT 'OK' FROM Person WHERE ID = ? AND Pwd LIKE ?");
            statement.setInt(1, userId);
            statement.setString(2, pwd);
            ResultSet dbRes = statement.executeQuery();
            if (dbRes.next())
                return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @WebMethod
    public List<Person> searchLogin(@WebParam(name = "expr") String[] expr) {
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
        List<Person> tmpList = session.getEntityManagerFactory().createEntityManager().createQuery(criteria).getResultList();
        if (tmpList != null)
            for (Person tmp : tmpList)
                result.add(new Person(tmp.getId(), tmp.getName(), tmp.getForename(), tmp.getCode(), tmp.getLanguage(), tmp.getAddress(), tmp.getContactInformation()));
        session.close();
        return result;
    }
}
