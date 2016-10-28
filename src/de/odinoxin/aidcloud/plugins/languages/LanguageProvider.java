package de.odinoxin.aidcloud.plugins.languages;

import de.odinoxin.aidcloud.plugins.RecordHandler;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public class LanguageProvider extends RecordHandler<Language> {

    @WebMethod
    public Language getLanguage(@WebParam(name = "id") int id) {
        return super.get(id);
    }

    @WebMethod
    public Language saveLanguage(@WebParam(name = "entity") Language entity) {
        return super.save(entity);
    }

    @WebMethod
    public boolean deleteLanguage(@WebParam(name = "id") int id) {
        return super.delete(id);
    }
}
