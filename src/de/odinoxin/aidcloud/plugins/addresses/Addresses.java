package de.odinoxin.aidcloud.plugins.addresses;

import de.odinoxin.aidcloud.DBMgr;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@WebService
public class Addresses {
    @WebMethod
    public AddressEntity getAddress(int addressId) {
        try {
            PreparedStatement statement = DBMgr.DB.prepareStatement("SELECT * FROM Addresses WHERE ID = ?");
            statement.setInt(1, addressId);
            ResultSet dbRes = statement.executeQuery();
            if (dbRes.next())
                return new AddressEntity(dbRes.getInt("ID"), dbRes.getString("Street"), dbRes.getString("HsNo"), dbRes.getString("Zip"), dbRes.getString("City"), dbRes.getInt("Country"));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @WebMethod
    public int saveAddress(AddressEntity adr) {
        try {
            if (adr.getId() == 0) {
                PreparedStatement insertStmt = DBMgr.DB.prepareStatement("INSERT INTO Addresses VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                insertStmt.setInt(1, adr.getCountry());
                insertStmt.setString(2, adr.getZip());
                insertStmt.setString(3, adr.getCity());
                insertStmt.setString(4, adr.getStreet());
                insertStmt.setString(5, adr.getHsNo());
                if (insertStmt.executeUpdate() == 1) {
                    ResultSet key = insertStmt.getGeneratedKeys();
                    if (key.next())
                        return key.getInt(1);
                }
            } else {
                PreparedStatement updateStmt = DBMgr.DB.prepareStatement("UPDATE Addresses SET Country = ?, Zip = ?, City = ?, Street = ?, HsNo = ? WHERE ID = ?");
                updateStmt.setInt(1, adr.getCountry());
                updateStmt.setString(2, adr.getZip());
                updateStmt.setString(3, adr.getCity());
                updateStmt.setString(4, adr.getStreet());
                updateStmt.setString(5, adr.getHsNo());
                updateStmt.setInt(6, adr.getId());
                if (updateStmt.executeUpdate() == 1)
                    return adr.getId();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    @WebMethod
    public boolean deleteAddress(int addressId) {
        try {
            PreparedStatement deleteStmt = DBMgr.DB.prepareStatement("DELETE FROM Addresses WHERE ID = ?");
            deleteStmt.setInt(1, addressId);
            return deleteStmt.executeUpdate() == 1;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
