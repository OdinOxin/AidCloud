package de.odinoxin.aidcloud.refbox;

import de.odinoxin.aidcloud.DBMgr;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebService
public class RefBox {

    @WebMethod
    public RefBoxEntity getItem(String view, int id) {
        try {
            PreparedStatement stmt = DBMgr.DB.prepareStatement("SELECT * FROM " + view + " WHERE ID = ?");
            stmt.setInt(1, id);
            ResultSet dbRes = stmt.executeQuery();
            if (dbRes.next())
                return new RefBoxEntity(dbRes.getInt("ID"), dbRes.getString("Text"), dbRes.getString("SubText"));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @WebMethod
    public List<RefBoxEntity> search(@WebParam(name = "view") String view, @WebParam(name = "expr") String[] expr) {
        List<RefBoxEntity> res = new ArrayList<>();
        String dbViewSelect = "SELECT * FROM " + view;
        if (expr != null && expr.length > 0) {
            dbViewSelect += " WHERE";
            for (int i = 0; i < expr.length; i++) {
                dbViewSelect += " ID LIKE ? OR Text LIKE ? OR SubText LIKE ?";

                if (i < expr.length - 1)
                    dbViewSelect += " OR";
            }
        }
        try {
            PreparedStatement stmt = DBMgr.DB.prepareStatement(dbViewSelect);
            if (expr != null)
                for (int i = 0; i < expr.length; i++) {
                    stmt.setString(i * 3 + 1, "%" + expr[i] + "%");
                    stmt.setString(i * 3 + 2, "%" + expr[i] + "%");
                    stmt.setString(i * 3 + 3, "%" + expr[i] + "%");
                }
            ResultSet dbRes = stmt.executeQuery();
            while (dbRes.next()) {
                RefBoxEntity item = new RefBoxEntity(dbRes.getInt("ID"), dbRes.getString("Text"), dbRes.getString("SubText"));
                res.add(item);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return res;
    }

}
