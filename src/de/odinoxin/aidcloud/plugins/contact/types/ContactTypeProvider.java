package de.odinoxin.aidcloud.plugins.contact.types;

import de.odinoxin.aidcloud.plugins.RecordHandler;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public class ContactTypeProvider extends RecordHandler<ContactType> {
    @WebMethod
    public ContactType getContactType(@WebParam(name = "id") int id) {
        return super.get(id);
    }

    @WebMethod
    public ContactType saveContactType(@WebParam(name = "entity") ContactType entity) {
        return super.save(entity);
    }

    @WebMethod
    public boolean deleteContactType(@WebParam(name = "id") int id) {
        return super.delete(id);
    }
}
