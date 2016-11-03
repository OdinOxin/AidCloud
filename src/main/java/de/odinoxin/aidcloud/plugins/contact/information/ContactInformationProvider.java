package de.odinoxin.aidcloud.plugins.contact.information;

import de.odinoxin.aidcloud.plugins.RecordHandler;
import de.odinoxin.aidcloud.plugins.contact.types.ContactType;
import de.odinoxin.aidcloud.plugins.contact.types.ContactType_;

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
    public ContactInformation getContactInformation(@WebParam(name = "id") int id) {
        return super.get(id);
    }

    @WebMethod
    public ContactInformation saveContactInformation(@WebParam(name = "entity") ContactInformation entity) {
        return this.getContactInformation(super.save(entity));
    }

    @WebMethod
    public boolean deleteContactInformation(@WebParam(name = "id") int id) {
        return super.delete(id);
    }

    @WebMethod
    public List<ContactInformation> searchContactInformation(@WebParam(name = "expr") String[] expr) {
        return super.search(expr);
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
