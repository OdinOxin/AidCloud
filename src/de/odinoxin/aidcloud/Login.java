package de.odinoxin.aidcloud;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebService
public class Login {
    @WebMethod
    public ServiceResult<Boolean> checkLogin(int userId, String pwd) {
        ServiceResult<Boolean> res = new ServiceResult<>();
        try {
            PreparedStatement statement = DBMgr.DB.prepareStatement("SELECT 'OK' FROM People WHERE ID = ? AND Pwd = ?");
            statement.setInt(1, userId);
            statement.setString(2, pwd);
            ResultSet dbRes = statement.executeQuery();
            if (dbRes.next())
                res = new ServiceResult<>(true);
        } catch (SQLException ex) {
            res = new ServiceResult<>(false, ex.getMessage());
        }
        return res;
    }
}
