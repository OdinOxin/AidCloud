package de.odinoxin.aidcloud.plugins.contact.information;

import de.odinoxin.aidcloud.plugins.RecordHandler;
import de.odinoxin.aidcloud.plugins.contact.types.ContactType;
import de.odinoxin.aidcloud.plugins.contact.types.ContactType_;
import de.odinoxin.aidcloud.plugins.people.Person;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@WebService
public class ContactInformationProvider extends RecordHandler<ContactInformation> {

    @WebMethod
    public ContactInformation getContactInformation(@WebParam(name = "id") int id, @WebParam(name = "auth") Person auth) {
        return super.get(id, auth);
    }

    @WebMethod
    public ContactInformation saveContactInformation(@WebParam(name = "entity") ContactInformation entity, @WebParam(name = "auth") Person auth) {
        return this.getContactInformation(super.save(entity, auth), auth);
    }

    @WebMethod
    public boolean deleteContactInformation(@WebParam(name = "id") int id, @WebParam(name = "auth") Person auth) {
        return super.delete(id, auth);
    }

    @WebMethod
    public List<ContactInformation> searchContactInformation(@WebParam(name = "expr") String[] expr, @WebParam(name = "max") int max, @WebParam(name = "auth") Person auth) {
        return super.search(expr, max, auth);
    }

    @Override
    protected Expression<Integer> getIdExpression(Root<ContactInformation> root) {
        return root.get(ContactInformation_.id);
    }

    @Override
    protected List<Expression<String>> getSearchExpressions(Root<ContactInformation> root) {
        List<Expression<String>> expressions = new ArrayList<>();
        Join<ContactInformation, ContactType> joinContactType = root.join(ContactInformation_.contactType, JoinType.LEFT);
        expressions.add(root.get(ContactInformation_.information));
        expressions.add(joinContactType.get(ContactType_.name));
        expressions.add(joinContactType.get(ContactType_.code));
        return expressions;
    }
}
