package de.odinoxin.aidcloud.plugins.countries;

import de.odinoxin.aidcloud.plugins.RecordHandler;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@WebService
public class CountryProvider extends RecordHandler<Country> {

    @WebMethod
    public Country getCountry(@WebParam(name = "id") int id) {
        return super.get(id);
    }

    @WebMethod
    public Country saveCountry(@WebParam(name = "entity") Country entity) {
        return this.getCountry(super.save(entity));
    }

    @WebMethod
    public boolean deleteCountry(@WebParam(name = "id") int id) {
        return super.delete(id);
    }

    @WebMethod
    public List<Country> searchCountry(@WebParam(name = "expr") String[] expr) {
        return super.search(expr);
    }

    @Override
    protected List<Expression<String>> getSearchExpressions(Root<Country> root) {
        List<Expression<String>> expressions = new ArrayList<>();
        expressions.add(root.get(Country_.alpha2));
        expressions.add(root.get(Country_.alpha3));
        expressions.add(root.get(Country_.name));
        expressions.add(root.get(Country_.areaCode));
        return expressions;
    }
}
