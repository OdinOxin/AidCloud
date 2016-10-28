package de.odinoxin.aidcloud;

import de.odinoxin.aidcloud.plugins.people.Person;
import org.hibernate.Session;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.persistence.Query;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebService
public class Login {
    @WebMethod(action = "checkLogin")
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

    @WebMethod(action = "searchLogin")
    public List<Person> searchLogin(@WebParam(name = "expr") String[] expr) {
        Session session = DB.open();
        List<Person> result = new ArrayList<>();
        if (expr != null && expr.length > 0)
            for (int i = 0; i < expr.length; i++) {
                Query q = session.createQuery("FROM Person WHERE id LIKE :expr OR name LIKE :expr OR forename LIKE :expr OR code LIKE :expr");
                q.setParameter("expr", expr);
                result.addAll(q.getResultList());
            }
        else
            result.addAll(session.createQuery("FROM Person").getResultList());
        session.close();
        return result;
    }
}
