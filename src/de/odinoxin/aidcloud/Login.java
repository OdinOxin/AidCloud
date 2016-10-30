package de.odinoxin.aidcloud;

import de.odinoxin.aidcloud.plugins.people.Person;
import org.hibernate.Session;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.persistence.criteria.CriteriaQuery;
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
        if (expr != null && expr.length > 0) {
            for (int i = 0; i < expr.length; i++)
                expr[i] = expr[i].toLowerCase();

            CriteriaQuery<Person> criteria = session.getEntityManagerFactory().getCriteriaBuilder().createQuery(Person.class);
            Root<Person> root = criteria.from(Person.class);
            criteria.select(root);

            result.addAll(session.getEntityManagerFactory().createEntityManager().createQuery(criteria).getResultList());

//            Query q = session.createQuery("FROM Person WHERE LOWER(name) IN(:expr) OR LOWER(forename) IN(:expr) OR LOWER(code) IN(:expr)");
//            q.setParameter("expr", Arrays.asList(expr));
//            List<Person> tmpList = (List<Person>) q.getResultList();
//            for (Person tmp : tmpList)
//                result.add(new Person(tmp.getId(), tmp.getName(), tmp.getForename(), tmp.getCode(), tmp.getLanguage(), tmp.getAddress(), tmp.getContactInformation()));
        } else {
            List<Person> tmpList = (List<Person>) session.createQuery("FROM Person").getResultList();
            for (Person tmp : tmpList)
                result.add(new Person(tmp.getId(), tmp.getName(), tmp.getForename(), tmp.getCode(), tmp.getLanguage(), tmp.getAddress(), tmp.getContactInformation()));
        }
        session.close();
        return result;
    }
}
