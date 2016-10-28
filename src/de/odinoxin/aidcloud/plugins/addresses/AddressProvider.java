package de.odinoxin.aidcloud.plugins.addresses;

import de.odinoxin.aidcloud.plugins.RecordHandler;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public class AddressProvider extends RecordHandler<Address> {

    @WebMethod
    public Address getAddress(@WebParam(name = "id") int id) {
        return super.get(id);
    }

    @WebMethod
    public Address saveAddress(@WebParam(name = "entity") Address entity) {
        return super.save(entity);
    }

    @WebMethod
    public boolean deleteAddress(@WebParam(name = "id") int id) {
        return super.delete(id);
    }
}
