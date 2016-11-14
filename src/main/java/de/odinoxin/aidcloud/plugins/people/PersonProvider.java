package de.odinoxin.aidcloud.plugins.people;

import de.odinoxin.aidcloud.AidCloud;
import de.odinoxin.aidcloud.DB;
import de.odinoxin.aidcloud.plugins.RecordHandler;
import de.odinoxin.aidcloud.plugins.addresses.Address;
import de.odinoxin.aidcloud.plugins.addresses.Address_;
import de.odinoxin.aidcloud.plugins.contact.information.ContactInformation;
import de.odinoxin.aidcloud.plugins.contact.information.ContactInformationProvider;
import de.odinoxin.aidcloud.plugins.countries.Country;
import de.odinoxin.aidcloud.plugins.countries.Country_;
import org.hibernate.Session;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.persistence.Query;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@WebService
public class PersonProvider extends RecordHandler<Person> {

    @WebMethod
    public Person getPerson(@WebParam(name = "id") int id, @WebParam(name = "auth") Person auth) {
        Person p = super.get(id, auth);
        if (p != null)
            p.setPwd(null);
        return p;
    }

    @WebMethod
    public Person savePerson(@WebParam(name = "entity") Person entity, @WebParam(name = "auth") Person auth) {
        Person current = this.get(entity.getId(), auth);
        if (current != null)
            entity.setPwd(current.getPwd());
        return this.getPerson(super.save(entity, auth), auth);
    }

    @WebMethod
    public boolean deletePerson(@WebParam(name = "id") int id, @WebParam(name = "auth") Person auth) {
        return super.delete(id, auth);
    }

    @WebMethod
    public List<Person> searchPerson(@WebParam(name = "expr") String[] expr, @WebParam(name = "max") int max, @WebParam(name = "auth") Person auth) {
        return super.search(expr, max, auth);
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
    protected Expression<Integer> getIdExpression(Root<Person> root) {
        return root.get(Person_.id);
    }

    @Override
    protected List<Expression<String>> getSearchExpressions(Root<Person> root) {
        List<Expression<String>> expressions = new ArrayList<>();
        Join<Person, Address> joinAddress = root.join(Person_.address, JoinType.LEFT);
        Join<Address, Country> joinCountry = joinAddress.join(Address_.country, JoinType.LEFT);
        expressions.add(root.get(Person_.forename));
        expressions.add(root.get(Person_.name));
        expressions.add(root.get(Person_.code));
        expressions.add(joinAddress.get(Address_.street));
        expressions.add(joinAddress.get(Address_.hsNo));
        expressions.add(joinAddress.get(Address_.zip));
        expressions.add(joinCountry.get(Country_.name));
        return expressions;
    }

    @Override
    protected void setFetchMode(Session session) {
        if (session != null)
            session.enableFetchProfile("JOIN_ContactInformation");
    }

    @Override
    public void generateDefaults() {
        if (!this.anyRecords()) {
            Person admin = new Person();
            admin.setForename("AidDesk");
            admin.setName("Admin");
            admin.setPwd("AidDesk");
            this.save(admin, AidCloud.SYSTEM);
        }
    }
}
