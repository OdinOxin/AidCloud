package de.odinoxin.aidcloud.plugins.contact.types;

import de.odinoxin.aidcloud.plugins.RecordHandler;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;
import java.util.ArrayList;
import java.util.List;

@WebService
public class ContactTypeProvider extends RecordHandler<ContactType> {
    @WebMethod
    public ContactType getContactType(@WebParam(name = "id") int id) {
        return super.get(id);
    }

    @WebMethod
    public ContactType saveContactType(@WebParam(name = "entity") ContactType entity) {
        return this.getContactType(super.save(entity));
    }

    @WebMethod
    public boolean deleteContactType(@WebParam(name = "id") int id) {
        return super.delete(id);
    }

    @WebMethod
    public List<ContactType> searchContactType(@WebParam(name = "expr") String[] expr) {
        return super.search(expr);
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
            this.saveContactType(tel);
            ContactType mail = new ContactType();
            mail.setCode("Mail");
            mail.setName("E-Mail");
            this.saveContactType(mail);
        }
    }
}
