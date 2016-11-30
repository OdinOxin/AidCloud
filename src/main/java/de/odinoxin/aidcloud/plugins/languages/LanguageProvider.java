package de.odinoxin.aidcloud.plugins.languages;

import de.odinoxin.aidcloud.ConcurrentFault;
import de.odinoxin.aidcloud.plugins.RecordHandler;

import javax.annotation.Resource;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import javax.xml.ws.WebServiceContext;
import java.util.ArrayList;
import java.util.List;

@WebService
public class LanguageProvider extends RecordHandler<Language> {

    @Resource
    WebServiceContext wsCtx;

    @WebMethod
    public Language getLanguage(@WebParam(name = "id") int id) {
        return super.get(id, this.wsCtx);
    }

    @WebMethod
    public Language saveLanguage(@WebParam(name = "entity") Language entity, @WebParam(name = "original") Language original) throws ConcurrentFault {
        return this.getLanguage(super.save(entity, original, this.wsCtx));
    }

    @WebMethod
    public boolean deleteLanguage(@WebParam(name = "id") int id) {
        return super.delete(id, this.wsCtx);
    }

    @WebMethod
    public List<Language> searchLanguage(@WebParam(name = "expr") String[] expr, @WebParam(name = "max") int max) {
        return super.search(expr, max, this.wsCtx);
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
            this.generate(german);
            Language english = new Language();
            english.setName("English");
            english.setCode("USA");
            this.generate(english);
        }
    }
}
