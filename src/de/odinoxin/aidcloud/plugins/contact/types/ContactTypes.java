package de.odinoxin.aidcloud.plugins.contact.types;

import de.odinoxin.aidcloud.DB;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@WebService
public class ContactTypes {

    private static final String TABLE = "ContactTypes";

    @WebMethod
    public ContactTypeEntity getContactType(@WebParam(name = "id") int id) {
        try {
            PreparedStatement statement = DB.con.prepareStatement("SELECT * FROM " + TABLE + " WHERE ID = ?");
            statement.setInt(1, id);
            ResultSet dbRes = statement.executeQuery();
            if (dbRes.next())
                return new ContactTypeEntity(dbRes.getInt("ID"), dbRes.getString("Name"), dbRes.getString("Code"), dbRes.getString("Regex"));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @WebMethod
    public int saveContactType(@WebParam(name = "item") ContactTypeEntity item) {
        try {
            if (item.getId() == 0) {
                PreparedStatement insertStmt = DB.con.prepareStatement("INSERT INTO " + TABLE + " VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                insertStmt.setString(1, item.getName());
                insertStmt.setString(2, item.getCode());
                insertStmt.setString(3, item.getRegex());
                if (insertStmt.executeUpdate() == 1) {
                    ResultSet key = insertStmt.getGeneratedKeys();
                    if (key.next())
                        return key.getInt(1);
                }
            } else {
                PreparedStatement updateStmt = DB.con.prepareStatement("UPDATE " + TABLE + " SET Name = ?, Code = ?, Regex = ? WHERE ID = ?");
                updateStmt.setString(1, item.getName());
                updateStmt.setString(2, item.getCode());
                updateStmt.setString(3, item.getRegex());
                updateStmt.setInt(4, item.getId());
                if (updateStmt.executeUpdate() == 1)
                    return item.getId();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    @WebMethod
    public boolean deleteContactType(@WebParam(name = "id") int id) {
        try {
            PreparedStatement deleteStmt = DB.con.prepareStatement("DELETE FROM " + TABLE + " WHERE ID = ?");
            deleteStmt.setInt(1, id);
            return deleteStmt.executeUpdate() == 1;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
