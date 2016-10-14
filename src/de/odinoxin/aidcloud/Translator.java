package de.odinoxin.aidcloud;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebService
public class Translator {

    @WebMethod
    public String getTranslation(@WebParam(name = "text")String text, @WebParam(name = "language")String language) {
        try {
            if (text == null)
                return null;
            if (language == null || language.isEmpty())
                language = "USA";
            PreparedStatement statement = DBMgr.DB.prepareStatement("SELECT " + language + " FROM Translations WHERE DEU LIKE ? OR USA LIKE ?");
            statement.setString(1, text);
            statement.setString(2, text);
            ResultSet dbRes = statement.executeQuery();
            if (dbRes.next()) {
                //return dbRes.getString(1);
                return "<" + dbRes.getString(1) + ">";
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return text;
    }

}
