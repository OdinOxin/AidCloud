package de.odinoxin.aidcloud.plugins.people;

import de.odinoxin.aidcloud.DB;

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
    public PersonEntity getPerson(@WebParam(name = "id") int id) {
        try {
            PreparedStatement statement = DB.con.prepareStatement("SELECT * FROM People WHERE ID = ?");
            statement.setInt(1, id);
            ResultSet dbRes = statement.executeQuery();
            if (dbRes.next())
                return new PersonEntity(dbRes.getInt("ID"), dbRes.getString("Name"), dbRes.getString("Forename"), dbRes.getString("Code"), dbRes.getInt("Language"), dbRes.getInt("Address"));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @WebMethod
    public int savePerson(@WebParam(name = "item") PersonEntity item) {
        try {
            if (item.getId() == 0) {
                PreparedStatement insertStmt = DB.con.prepareStatement("INSERT INTO People VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                insertStmt.setString(1, item.getCode());
                insertStmt.setString(2, item.getName());
                insertStmt.setString(3, item.getForename());
                insertStmt.setString(4, item.getPwd() == null ? "" : item.getPwd());
                insertStmt.setInt(5, item.getLanguage());
                insertStmt.setObject(6, item.getAddressId() == 0 ? null : item.getAddressId());
                if (insertStmt.executeUpdate() == 1) {
                    ResultSet key = insertStmt.getGeneratedKeys();
                    if (key.next())
                        return key.getInt(1);
                }
            } else {
                PreparedStatement updateStmt = DB.con.prepareStatement("UPDATE People SET Code = ?, Name = ?, Forename = ?, Language = ?, Address = ? WHERE ID = ?");
                updateStmt.setString(1, item.getCode());
                updateStmt.setString(2, item.getName());
                updateStmt.setString(3, item.getForename());
                updateStmt.setInt(4, item.getLanguage());
                updateStmt.setObject(5, item.getAddressId() == 0 ? null : item.getAddressId());
                updateStmt.setInt(6, item.getId());
                if (updateStmt.executeUpdate() == 1)
                    return item.getId();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    @WebMethod
    public boolean deletePerson(@WebParam(name = "id") int id) {
        try {
            PreparedStatement deleteStmt = DB.con.prepareStatement("DELETE FROM People WHERE ID = ?");
            deleteStmt.setInt(1, id);
            return deleteStmt.executeUpdate() == 1;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @WebMethod
    public boolean changePwd(@WebParam(name = "id") int id, @WebParam(name = "oldPwd") String oldPwd, @WebParam(name = "newPwd") String newPwd) {
        if (id == 0)
            return false;
        try {
            PreparedStatement pwdStmt = DB.con.prepareStatement("UPDATE People SET Pwd = ? WHERE ID = ? AND Pwd = ?");
            pwdStmt.setString(1, newPwd);
            pwdStmt.setInt(2, id);
            pwdStmt.setString(3, oldPwd);
            return pwdStmt.executeUpdate() == 1;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
