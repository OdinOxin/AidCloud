package de.odinoxin.aidcloud.plugins.people;

import de.odinoxin.aidcloud.DBMgr;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@WebService
public class People {

    @WebMethod
    public PersonEntity getPerson(@WebParam(name = "personId") int personId) {
        try {
            PreparedStatement statement = DBMgr.DB.prepareStatement("SELECT * FROM People WHERE ID = ?");
            statement.setInt(1, personId);
            ResultSet dbRes = statement.executeQuery();
            if (dbRes.next())
                return new PersonEntity(dbRes.getInt("ID"), dbRes.getString("Name"), dbRes.getString("Forename"), dbRes.getString("Code"), dbRes.getString("Language"), dbRes.getInt("Address"));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @WebMethod
    public int savePerson(@WebParam(name = "p") PersonEntity p) {
        try {
            if (p.getId() == 0) {
                PreparedStatement insertStmt = DBMgr.DB.prepareStatement("INSERT INTO People VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                insertStmt.setString(1, p.getCode());
                insertStmt.setString(2, p.getName());
                insertStmt.setString(3, p.getForename());
                insertStmt.setString(4, p.getPwd() == null ? "" : p.getPwd());
                insertStmt.setString(5, p.getLanguage());
                insertStmt.setInt(6, p.getAddressId());
                if (insertStmt.executeUpdate() == 1) {
                    ResultSet key = insertStmt.getGeneratedKeys();
                    if (key.next())
                        return key.getInt(1);
                }
            } else {
                PreparedStatement updateStmt = DBMgr.DB.prepareStatement("UPDATE People SET Code = ?, Name = ?, Forename = ?, Language = ?, Address = ? WHERE ID = ?");
                updateStmt.setString(1, p.getCode());
                updateStmt.setString(2, p.getName());
                updateStmt.setString(3, p.getForename());
                updateStmt.setString(4, p.getLanguage());
                updateStmt.setInt(5, p.getAddressId());
                updateStmt.setInt(6, p.getId());
                if (updateStmt.executeUpdate() == 1)
                    return p.getId();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    @WebMethod
    public boolean deletePerson(@WebParam(name = "personId") int personId) {
        try {
            PreparedStatement deleteStmt = DBMgr.DB.prepareStatement("DELETE FROM People WHERE ID = ?");
            deleteStmt.setInt(1, personId);
            return deleteStmt.executeUpdate() == 1;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @WebMethod
    public boolean changePwd(@WebParam(name = "personId") int personId, @WebParam(name = "oldPwd") String oldPwd, @WebParam(name = "newPwd") String newPwd) {
        if (personId == 0)
            return false;
        try {
            PreparedStatement pwdStmt = DBMgr.DB.prepareStatement("UPDATE People SET Pwd = ? WHERE ID = ? AND Pwd = ?");
            pwdStmt.setString(1, newPwd);
            pwdStmt.setInt(2, personId);
            pwdStmt.setString(3, oldPwd);
            return pwdStmt.executeUpdate() == 1;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
