package de.odinoxin.aidcloud.plugins.languages;

import de.odinoxin.aidcloud.AidCloud;
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
public class LanguageProvider extends RecordHandler<Language> {

    @WebMethod
    public Language getLanguage(@WebParam(name = "id") int id, @WebParam(name = "auth") Person auth) {
        return super.get(id, auth);
    }

    @WebMethod
    public Language saveLanguage(@WebParam(name = "entity") Language entity, @WebParam(name = "auth") Person auth) {
        return this.getLanguage(super.save(entity, auth), auth);
    }

    @WebMethod
    public boolean deleteLanguage(@WebParam(name = "id") int id, @WebParam(name = "auth") Person auth) {
        return super.delete(id, auth);
    }

    @WebMethod
    public List<Language> searchLanguage(@WebParam(name = "expr") String[] expr, @WebParam(name = "max") int max, @WebParam(name = "auth") Person auth) {
        return super.search(expr, max, auth);
    }

    @Override
    protected Expression<Integer> getIdExpression(Root<Language> root) {
        return root.get(Language_.id);
    }

    @Override
    protected List<Expression<String>> getSearchExpressions(Root<Language> root) {
        List<Expression<String>> expressions = new ArrayList<>();
        expressions.add(root.get(Language_.name));
        expressions.add(root.get(Language_.code));
        return expressions;
    }

    @Override
    public void generateDefaults() {
        if (!this.anyRecords()) {
            Language german = new Language();
            german.setName("Deutsch");
            german.setCode("DEU");
            this.saveLanguage(german, AidCloud.SYSTEM);
            Language english = new Language();
            english.setName("English");
            english.setCode("USA");
            this.saveLanguage(english, AidCloud.SYSTEM);
        }
    }
}
