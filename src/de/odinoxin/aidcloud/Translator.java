package de.odinoxin.aidcloud;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebService
public class Translator {

    private static Translator instance;

    public static Translator get() {
        if (instance == null)
            instance = new Translator();
        return instance;
    }

    @WebMethod
    public String getTranslation(@WebParam(name = "text") String text, @WebParam(name = "language") String language) {
        try {
            if (text == null)
                return null;
            if (language == null || language.isEmpty())
                language = "USA";
            PreparedStatement statement = DB.con.prepareStatement("SELECT " + language + " FROM Translations WHERE SYS LIKE ?");
            statement.setString(1, text);
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
