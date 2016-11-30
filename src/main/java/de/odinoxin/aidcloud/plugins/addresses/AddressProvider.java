package de.odinoxin.aidcloud.plugins.addresses;

import de.odinoxin.aidcloud.ConcurrentFault;
import de.odinoxin.aidcloud.plugins.RecordHandler;
import de.odinoxin.aidcloud.plugins.countries.Country;
import de.odinoxin.aidcloud.plugins.countries.Country_;

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
public class AddressProvider extends RecordHandler<Address> {

    @Resource
    private WebServiceContext wsCtx;

    @WebMethod
    public Address getAddress(@WebParam(name = "id") int id) {
        return super.get(id, this.wsCtx);
    }

    @WebMethod
    public Address saveAddress(@WebParam(name = "entity") Address entity, @WebParam(name = "original") Address original) throws ConcurrentFault {
        return this.getAddress(super.save(entity, original, this.wsCtx));
    }

    @WebMethod
    public boolean deleteAddress(@WebParam(name = "id") int id) {
        return super.delete(id, this.wsCtx);
    }

    @WebMethod
    public List<Address> searchAddress(@WebParam(name = "expr") String[] expr, @WebParam(name = "max") int max) {
        return super.search(expr, max, this.wsCtx);
    }

    @Override
    protected Expression<Integer> getIdExpression(Root<Address> root) {
        return root.get(Address_.id);
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
