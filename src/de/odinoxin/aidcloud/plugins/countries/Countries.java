package de.odinoxin.aidcloud.plugins.countries;

import de.odinoxin.aidcloud.DBMgr;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@WebService
public class Countries {

    @WebMethod
    public CountryEntity getCountry(int countryId) {
        try {
            PreparedStatement statement = DBMgr.DB.prepareStatement("SELECT * FROM Countries WHERE ID = ?");
            statement.setInt(1, countryId);
            ResultSet dbRes = statement.executeQuery();
            if (dbRes.next())
                return new CountryEntity(dbRes.getInt("ID"), dbRes.getString("Alpha2"), dbRes.getString("Alpha3"), dbRes.getString("Name"), dbRes.getString("AreaCode"));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @WebMethod
    public int saveCountry(CountryEntity country) {
        try {
            if (country.getId() == 0) {
                PreparedStatement insertStmt = DBMgr.DB.prepareStatement("INSERT INTO Countries VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                insertStmt.setString(1, country.getAlpha2());
                insertStmt.setString(2, country.getAlpha3());
                insertStmt.setString(3, country.getName());
                insertStmt.setString(4, country.getAreaCode());
                if (insertStmt.executeUpdate() == 1) {
                    ResultSet key = insertStmt.getGeneratedKeys();
                    if (key.next())
                        return key.getInt(1);
                }
            } else {
                PreparedStatement updateStmt = DBMgr.DB.prepareStatement("UPDATE Countries SET Alpha2 = ?, Alpha3 = ?, Name = ?, AreaCode = ? WHERE ID = ?");
                updateStmt.setString(1, country.getAlpha2());
                updateStmt.setString(2, country.getAlpha3());
                updateStmt.setString(3, country.getName());
                updateStmt.setString(4, country.getAreaCode());
                updateStmt.setInt(5, country.getId());
                if (updateStmt.executeUpdate() == 1)
                    return country.getId();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    @WebMethod
    public boolean deleteCountry(int countryId) {
        try {
            PreparedStatement deleteStmt = DBMgr.DB.prepareStatement("DELETE FROM Countries WHERE ID = ?");
            deleteStmt.setInt(1, countryId);
            return deleteStmt.executeUpdate() == 1;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
