package de.odinoxin.aidcloud.plugins.contact.information;

import de.odinoxin.aidcloud.ConcurrentFault;
import de.odinoxin.aidcloud.plugins.RecordHandler;
import de.odinoxin.aidcloud.plugins.contact.types.ContactType;
import de.odinoxin.aidcloud.plugins.contact.types.ContactType_;

import javax.annotation.Resource;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import javax.xml.ws.WebServiceContext;
import java.util.ArrayList;
import java.util.List;

@WebService
public class ContactInformationProvider extends RecordHandler<ContactInformation> {

    @Resource
    WebServiceContext wsCtx;

    @WebMethod
    public ContactInformation getContactInformation(@WebParam(name = "id") int id) {
        return super.get(id, this.wsCtx);
    }

    @WebMethod
    public ContactInformation saveContactInformation(@WebParam(name = "entity") ContactInformation entity, @WebParam(name = "original") ContactInformation original) throws ConcurrentFault {
        return this.getContactInformation(super.save(entity, original, this.wsCtx));
    }

    @WebMethod
    public boolean deleteContactInformation(@WebParam(name = "id") int id) {
        return super.delete(id, this.wsCtx);
    }

    @WebMethod
    public List<ContactInformation> searchContactInformation(@WebParam(name = "expr") String[] expr, @WebParam(name = "max") int max) {
        return super.search(expr, max, this.wsCtx);
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
