package de.odinoxin.aidcloud.plugins.addresses;

import de.odinoxin.aidcloud.DB;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@WebService
public class Addresses {
    @WebMethod
    public AddressEntity getAddress(@WebParam(name = "id") int id) {
        try {
            PreparedStatement statement = DB.con.prepareStatement("SELECT * FROM Addresses WHERE ID = ?");
            statement.setInt(1, id);
            ResultSet dbRes = statement.executeQuery();
            if (dbRes.next())
                return new AddressEntity(dbRes.getInt("ID"), dbRes.getString("Street"), dbRes.getString("HsNo"), dbRes.getString("Zip"), dbRes.getString("City"), dbRes.getInt("Country"));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @WebMethod
    public int saveAddress(@WebParam(name = "item") AddressEntity item) {
        try {
            if (item.getId() == 0) {
                PreparedStatement insertStmt = DB.con.prepareStatement("INSERT INTO Addresses VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                insertStmt.setObject(1, item.getCountry() == 0 ? null : item.getCountry());
                insertStmt.setString(2, item.getZip());
                insertStmt.setString(3, item.getCity());
                insertStmt.setString(4, item.getStreet());
                insertStmt.setString(5, item.getHsNo());
                if (insertStmt.executeUpdate() == 1) {
                    ResultSet key = insertStmt.getGeneratedKeys();
                    if (key.next())
                        return key.getInt(1);
                }
            } else {
                PreparedStatement updateStmt = DB.con.prepareStatement("UPDATE Addresses SET Country = ?, Zip = ?, City = ?, Street = ?, HsNo = ? WHERE ID = ?");
                updateStmt.setObject(1, item.getCountry() == 0 ? null : item.getCountry());
                updateStmt.setString(2, item.getZip());
                updateStmt.setString(3, item.getCity());
                updateStmt.setString(4, item.getStreet());
                updateStmt.setString(5, item.getHsNo());
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
    public boolean deleteAddress(@WebParam(name = "id") int id) {
        try {
            PreparedStatement deleteStmt = DB.con.prepareStatement("DELETE FROM Addresses WHERE ID = ?");
            deleteStmt.setInt(1, id);
            return deleteStmt.executeUpdate() == 1;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
