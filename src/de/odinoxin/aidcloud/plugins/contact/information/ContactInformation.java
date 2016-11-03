package de.odinoxin.aidcloud.plugins.contact.information;

import de.odinoxin.aidcloud.DB;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@WebService
public class ContactInformation {
    private static final String TABLE = "ContactInformation";

    @WebMethod
    public ContactInformationEntity getContactInformation(@WebParam(name = "id") int id) {
        try {
            PreparedStatement statement = DB.con.prepareStatement("SELECT * FROM " + TABLE + " WHERE ID = ?");
            statement.setInt(1, id);
            ResultSet dbRes = statement.executeQuery();
            if (dbRes.next())
                return new ContactInformationEntity(dbRes.getInt("ID"), dbRes.getInt("ContactType"), dbRes.getString("Information"));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @WebMethod
    public int saveContactInformation(@WebParam(name = "item") ContactInformationEntity item) {
        try {
            if (item.getId() == 0) {
                PreparedStatement insertStmt = DB.con.prepareStatement("INSERT INTO " + TABLE + " VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
                insertStmt.setInt(1, item.getContactType());
                insertStmt.setString(2, item.getInformation());
                if (insertStmt.executeUpdate() == 1) {
                    ResultSet key = insertStmt.getGeneratedKeys();
                    if (key.next())
                        return key.getInt(1);
                }
            } else {
                PreparedStatement updateStmt = DB.con.prepareStatement("UPDATE " + TABLE + " SET ContactType = ?, Information = ? WHERE ID = ?");
                updateStmt.setInt(1, item.getContactType());
                updateStmt.setString(2, item.getInformation());
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
    public boolean deleteContactInformation(@WebParam(name = "id") int id) {
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
