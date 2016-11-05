package de.odinoxin.aidcloud.plugins.languages;

import de.odinoxin.aidcloud.plugins.RecordHandler;

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
    public Language getLanguage(@WebParam(name = "id") int id) {
        return super.get(id);
    }

    @WebMethod
    public Language saveLanguage(@WebParam(name = "entity") Language entity) {
        return this.getLanguage(super.save(entity));
    }

    @WebMethod
    public boolean deleteLanguage(@WebParam(name = "id") int id) {
        return super.delete(id);
    }

    @WebMethod
    public List<Language> searchLanguage(@WebParam(name = "expr") String[] expr) {
        return super.search(expr);
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
            this.saveLanguage(german);
            Language english = new Language();
            english.setName("English");
            english.setCode("USA");
            this.saveLanguage(english);
        }
    }
}
