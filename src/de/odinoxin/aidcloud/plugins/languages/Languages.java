package de.odinoxin.aidcloud.plugins.languages;

import de.odinoxin.aidcloud.DB;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@WebService
public class Languages {

    @WebMethod
    public LanguageEntity getLanguage(@WebParam(name = "id") int id) {
        try {
            PreparedStatement statement = DB.con.prepareStatement("SELECT * FROM Languages WHERE ID = ?");
            statement.setInt(1, id);
            ResultSet dbRes = statement.executeQuery();
            if (dbRes.next())
                return new LanguageEntity(dbRes.getInt("ID"), dbRes.getString("Name"), dbRes.getString("Code"));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @WebMethod
    public int saveLanguage(@WebParam(name = "item") LanguageEntity item) {
        try {
            if (item.getId() == 0) {
                PreparedStatement insertStmt = DB.con.prepareStatement("INSERT INTO Languages VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
                insertStmt.setString(1, item.getName());
                insertStmt.setString(2, item.getCode());
                if (insertStmt.executeUpdate() == 1) {
                    ResultSet key = insertStmt.getGeneratedKeys();
                    if (key.next())
                        return key.getInt(1);
                }
            } else {
                PreparedStatement updateStmt = DB.con.prepareStatement("UPDATE Languages SET Name = ?, Code = ? WHERE ID = ?");
                updateStmt.setString(1, item.getName());
                updateStmt.setString(2, item.getCode());
                updateStmt.setInt(3, item.getId());
                if (updateStmt.executeUpdate() == 1)
                    return item.getId();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    @WebMethod
    public boolean deleteLanguage(@WebParam(name = "id") int id) {
        try {
            PreparedStatement deleteStmt = DB.con.prepareStatement("DELETE FROM Languages WHERE ID = ?");
            deleteStmt.setInt(1, id);
            return deleteStmt.executeUpdate() == 1;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
