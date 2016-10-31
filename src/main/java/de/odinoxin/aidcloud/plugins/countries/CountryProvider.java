package de.odinoxin.aidcloud.plugins.countries;

import de.odinoxin.aidcloud.plugins.RecordHandler;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public class CountryProvider extends RecordHandler<Country> {

    @WebMethod
    public Country getCountry(@WebParam(name = "id") int id) {
        return super.get(id);
    }

    @WebMethod
    public Country saveCountry(@WebParam(name = "entity") Country entity) {
        return super.save(entity);
    }

    @WebMethod
    public boolean deleteCountry(@WebParam(name = "id") int id) {
        return super.delete(id);
    }
}
