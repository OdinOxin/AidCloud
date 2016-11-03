package de.odinoxin.aidcloud.plugins.addresses;

import de.odinoxin.aidcloud.plugins.RecordHandler;
import de.odinoxin.aidcloud.plugins.countries.Country;
import de.odinoxin.aidcloud.plugins.countries.Country_;

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
public class AddressProvider extends RecordHandler<Address> {

    @WebMethod
    public Address getAddress(@WebParam(name = "id") int id) {
        return super.get(id);
    }

    @WebMethod
    public Address saveAddress(@WebParam(name = "entity") Address entity) {
        return this.getAddress(super.save(entity));
    }

    @WebMethod
    public boolean deleteAddress(@WebParam(name = "id") int id) {
        return super.delete(id);
    }

    @WebMethod
    public List<Address> searchAddress(@WebParam(name = "expr") String[] expr) {
        return super.search(expr);
    }

    @Override
    protected List<Expression<String>> getSearchExpressions(Root<Address> root) {
        List<Expression<String>> expressions = new ArrayList<>();
        Join<Address, Country> joinCountry = root.join(Address_.country, JoinType.LEFT);
        expressions.add(root.get(Address_.street));
        expressions.add(root.get(Address_.hsNo));
        expressions.add(root.get(Address_.zip));
        expressions.add(root.get(Address_.city));
        expressions.add(joinCountry.get(Country_.name));
        return expressions;
    }
}
