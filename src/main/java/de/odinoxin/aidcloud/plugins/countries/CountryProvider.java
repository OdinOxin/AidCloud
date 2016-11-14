package de.odinoxin.aidcloud.plugins.countries;

import de.odinoxin.aidcloud.plugins.RecordHandler;
import de.odinoxin.aidcloud.plugins.people.Person;

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
    public Country getCountry(@WebParam(name = "id") int id, @WebParam(name = "auth") Person auth) {
        return super.get(id, auth);
    }

    @WebMethod
    public Country saveCountry(@WebParam(name = "entity") Country entity, @WebParam(name = "auth") Person auth) {
        return this.getCountry(super.save(entity, auth), auth);
    }

    @WebMethod
    public boolean deleteCountry(@WebParam(name = "id") int id, @WebParam(name = "auth") Person auth) {
        return super.delete(id, auth);
    }

    @WebMethod
    public List<Country> searchCountry(@WebParam(name = "expr") String[] expr, @WebParam(name = "max") int max, @WebParam(name = "auth") Person auth) {
        return super.search(expr, max, auth);
    }

    @Override
    protected Expression<Integer> getIdExpression(Root<Country> root) {
        return root.get(Country_.id);
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
