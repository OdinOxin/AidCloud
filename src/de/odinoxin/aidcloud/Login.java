package de.odinoxin.aidcloud;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebService
public class Login {
    @WebMethod
    public boolean checkLogin(int userId, String pwd) {
        try {
            PreparedStatement statement = DBMgr.DB.prepareStatement("SELECT 'OK' FROM People WHERE ID = ? AND Pwd = ?");
            statement.setInt(1, userId);
            statement.setString(2, pwd);
            ResultSet dbRes = statement.executeQuery();
            if (dbRes.next())
                return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
