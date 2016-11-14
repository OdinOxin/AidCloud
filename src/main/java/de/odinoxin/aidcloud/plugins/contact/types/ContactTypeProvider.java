package de.odinoxin.aidcloud.plugins.contact.types;

import de.odinoxin.aidcloud.AidCloud;
import de.odinoxin.aidcloud.plugins.RecordHandler;
import de.odinoxin.aidcloud.plugins.people.Person;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@WebService
public class ContactTypeProvider extends RecordHandler<ContactType> {
    @WebMethod
    public ContactType getContactType(@WebParam(name = "id") int id, @WebParam(name = "auth") Person auth) {
        return super.get(id, auth);
    }

    @WebMethod
    public ContactType saveContactType(@WebParam(name = "entity") ContactType entity, @WebParam(name = "auth") Person auth) {
        return this.getContactType(super.save(entity, auth), auth);
    }

    @WebMethod
    public boolean deleteContactType(@WebParam(name = "id") int id, @WebParam(name = "auth") Person auth) {
        return super.delete(id, auth);
    }

    @WebMethod
    public List<ContactType> searchContactType(@WebParam(name = "expr") String[] expr, @WebParam(name = "max") int max, @WebParam(name = "auth") Person auth) {
        return super.search(expr, max, auth);
    }

    @Override
    protected Expression<Integer> getIdExpression(Root<ContactType> root) {
        return root.get(ContactType_.id);
    }

    @Override
    protected List<Expression<String>> getSearchExpressions(Root<ContactType> root) {
        List<Expression<String>> expressions = new ArrayList<>();

        expressions.add(root.get(ContactType_.name));
        expressions.add(root.get(ContactType_.code));
        return expressions;
    }

    @Override
    public void generateDefaults() {
        if (!this.anyRecords()) {
            ContactType tel = new ContactType();
            tel.setCode("Tel");
            tel.setName("Telephone");
            this.saveContactType(tel, AidCloud.SYSTEM);
            ContactType mail = new ContactType();
            mail.setCode("Mail");
            mail.setName("E-Mail");
            this.saveContactType(mail, AidCloud.SYSTEM);
        }
    }
}
