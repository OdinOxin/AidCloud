package de.odinoxin.aidcloud.plugins.contact.types;

import de.odinoxin.aidcloud.plugins.RecordHandler;

import javax.annotation.Resource;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import javax.xml.ws.WebServiceContext;
import java.util.ArrayList;
import java.util.List;

@WebService
public class ContactTypeProvider extends RecordHandler<ContactType> {

    @Resource
    WebServiceContext wsCtx;

    @WebMethod
    public ContactType getContactType(@WebParam(name = "id") int id) {
        return super.get(id, this.wsCtx);
    }

    @WebMethod
    public ContactType saveContactType(@WebParam(name = "entity") ContactType entity) {
        return this.getContactType(super.save(entity, this.wsCtx));
    }

    @WebMethod
    public boolean deleteContactType(@WebParam(name = "id") int id) {
        return super.delete(id, this.wsCtx);
    }

    @WebMethod
    public List<ContactType> searchContactType(@WebParam(name = "expr") String[] expr, @WebParam(name = "max") int max) {
        return super.search(expr, max, this.wsCtx);
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
            this.generate(tel);
            ContactType mail = new ContactType();
            mail.setCode("Mail");
            mail.setName("E-Mail");
            this.generate(mail);
        }
    }
}
