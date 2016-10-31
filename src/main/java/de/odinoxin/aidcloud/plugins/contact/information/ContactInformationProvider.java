package de.odinoxin.aidcloud.plugins.contact.information;

import de.odinoxin.aidcloud.plugins.RecordHandler;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public class ContactInformationProvider extends RecordHandler<ContactInformation> {

    @WebMethod
    public ContactInformation getContactInformation(@WebParam(name = "id") int id) {
        return super.get(id);
    }

    @WebMethod
    public ContactInformation saveContactInformation(@WebParam(name = "entity") ContactInformation entity) {
        return super.save(entity);
    }

    @WebMethod
    public boolean deleteContactInformation(@WebParam(name = "id") int id) {
        return super.delete(id);
    }
}
