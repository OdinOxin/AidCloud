package de.odinoxin.aidcloud.plugins.people;

import de.odinoxin.aidcloud.DB;
import de.odinoxin.aidcloud.plugins.RecordHandler;
import org.hibernate.Session;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@WebService
public class PersonProvider extends RecordHandler<Person> {

    @WebMethod
    public Person getPerson(@WebParam(name = "id") int id) {
        Person p = super.get(id);
        if (p != null)
            p.setPwd(null);
        return p;
    }

    @WebMethod
    public Person savePerson(@WebParam(name = "entity") Person entity) {
        Person current = this.get(entity.getId());
        if (current != null)
            entity.setPwd(current.getPwd());
        return super.save(entity);
    }

    @WebMethod
    public boolean deletePerson(@WebParam(name = "id") int id) {
        return super.delete(id);
    }

    @WebMethod
    public List<Person> searchPerson(@WebParam(name = "expr") String[] expr) {
        if (expr == null || expr.length <= 0)
            return null;
        Session session = DB.open();
        List<Person> result = new ArrayList<>();
        for (int i = 0; i < expr.length; i++) { //Very naiv, cause of duplicates!
            Query q = session.createQuery("FROM Person WHERE id LIKE :expr OR name LIKE :expr OR forename LIKE :expr OR code LIKE :expr");
            q.setParameter("expr", expr[i]);
            result.addAll(q.getResultList());
        }
        return result;
    }

    @WebMethod
    public boolean changePwd(@WebParam(name = "id") int id, @WebParam(name = "currentPwd") String currentPwd, @WebParam(name = "newPwd") String newPwd) {
        if (id == 0)
            return false;
        Session session = DB.open();
        session.beginTransaction();
        Query q = session.createQuery("UPDATE Person SET Pwd = :NewPwd WHERE ID = :ID AND Pwd LIKE :CurrentPwd");
        q.setParameter("ID", id);
        q.setParameter("CurrentPwd", currentPwd);
        q.setParameter("NewPwd", newPwd);
        boolean success = q.executeUpdate() == 1;
        session.getTransaction().commit();
        session.close();
        return success;
    }

    @Override
    public void generateDefaults() {
        Session session = DB.open();
        Query qAdmin = session.createQuery("SELECT 'Admin' FROM Person WHERE Name LIKE :Name");
        qAdmin.setParameter("Name", "Admin");
        boolean exists = qAdmin.getResultList().size() >= 1;
        session.close();
        if (!exists) {
            Person admin = new Person();
            admin.setName("Admin");
            admin.setForename("AidDesk");
            admin.setCode("ADMIN");
            admin.setPwd("AidDesk");
            this.save(admin);
        }
//        { // V_Login
//            session = DB.open();
//            session.beginTransaction();
//            Query qDrop = session.createNativeQuery("DROP VIEW V_Login");
//            qDrop.executeUpdate();
//            Query qCreate = session.createNativeQuery("CREATE VIEW V_Login AS SELECT ID, Forename + ' ' + Name AS Text, Code AS SubText FROM Person");
//            qCreate.executeUpdate();
//            session.getTransaction().commit();
//            session.close();
//        }
//        { // V_Person
//            session = DB.open();
//            session.beginTransaction();
//            Query qDrop = session.createNativeQuery("DROP VIEW V_Person");
//            qDrop.executeUpdate();
//            Query qCreate = session.createNativeQuery("CREATE VIEW V_Person AS SELECT P.ID AS ID, '' AS Text, '' AS SubText FROM Person");
//            qCreate.executeUpdate();
//            session.getTransaction().commit();
//            session.close();
//        }
    }
}
