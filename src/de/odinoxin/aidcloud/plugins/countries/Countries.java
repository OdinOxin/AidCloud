package de.odinoxin.aidcloud.plugins.countries;

import de.odinoxin.aidcloud.DB;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@WebService
public class Countries {

    @WebMethod
    public CountryEntity getCountry(@WebParam(name = "id") int id) {
        try {
            PreparedStatement statement = DB.con.prepareStatement("SELECT * FROM Countries WHERE ID = ?");
            statement.setInt(1, id);
            ResultSet dbRes = statement.executeQuery();
            if (dbRes.next())
                return new CountryEntity(dbRes.getInt("ID"), dbRes.getString("Alpha2"), dbRes.getString("Alpha3"), dbRes.getString("Name"), dbRes.getString("AreaCode"));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @WebMethod
    public int saveCountry(@WebParam(name = "item") CountryEntity item) {
        try {
            if (item.getId() == 0) {
                PreparedStatement insertStmt = DB.con.prepareStatement("INSERT INTO Countries VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                insertStmt.setString(1, item.getAlpha2());
                insertStmt.setString(2, item.getAlpha3());
                insertStmt.setString(3, item.getName());
                insertStmt.setString(4, item.getAreaCode());
                if (insertStmt.executeUpdate() == 1) {
                    ResultSet key = insertStmt.getGeneratedKeys();
                    if (key.next())
                        return key.getInt(1);
                }
            } else {
                PreparedStatement updateStmt = DB.con.prepareStatement("UPDATE Countries SET Alpha2 = ?, Alpha3 = ?, Name = ?, AreaCode = ? WHERE ID = ?");
                updateStmt.setString(1, item.getAlpha2());
                updateStmt.setString(2, item.getAlpha3());
                updateStmt.setString(3, item.getName());
                updateStmt.setString(4, item.getAreaCode());
                updateStmt.setInt(5, item.getId());
                if (updateStmt.executeUpdate() == 1)
                    return item.getId();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    @WebMethod
    public boolean deleteCountry(@WebParam(name = "id") int id) {
        try {
            PreparedStatement deleteStmt = DB.con.prepareStatement("DELETE FROM Countries WHERE ID = ?");
            deleteStmt.setInt(1, id);
            return deleteStmt.executeUpdate() == 1;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
