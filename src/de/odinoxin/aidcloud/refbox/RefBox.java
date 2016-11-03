package de.odinoxin.aidcloud.refbox;

import de.odinoxin.aidcloud.DB;
import de.odinoxin.aidcloud.Translator;

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
    public RefBoxEntity getItem(@WebParam(name = "name") String name, @WebParam(name = "id") int id) {
        try {
            PreparedStatement viewStmt = DB.con.prepareStatement("SELECT ViewName FROM RefBoxViews WHERE NAME LIKE ?");
            viewStmt.setString(1, name);
            ResultSet viewRes = viewStmt.executeQuery();
            if (viewRes.next()) {
                PreparedStatement stmt = DB.con.prepareStatement("SELECT * FROM " + viewRes.getString("ViewName") + " WHERE ID = ?");
                stmt.setInt(1, id);
                ResultSet dbRes = stmt.executeQuery();
                if (dbRes.next())
                    return new RefBoxEntity(dbRes.getInt("ID"), dbRes.getString("Text"), dbRes.getString("SubText"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @WebMethod
    public List<RefBoxEntity> search(@WebParam(name = "name") String name, @WebParam(name = "expr") String[] expr, @WebParam(name = "language") String language) {
        List<RefBoxEntity> res = new ArrayList<>();
        try {
            PreparedStatement viewStmt = DB.con.prepareStatement("SELECT ViewName FROM RefBoxViews WHERE NAME LIKE ?");
            viewStmt.setString(1, name);
            ResultSet viewRes = viewStmt.executeQuery();
            if (viewRes.next()) {
                String dbViewSelect = "SELECT * FROM " + viewRes.getString("ViewName");

                PreparedStatement stmt = DB.con.prepareStatement(dbViewSelect);
                if (language == null) {
                    if (expr != null && expr.length > 0) {
                        dbViewSelect += " WHERE";
                        for (int i = 0; i < expr.length; i++) {
                            dbViewSelect += " ID LIKE ? OR Text LIKE ? OR SubText LIKE ?";

                            if (i < expr.length - 1)
                                dbViewSelect += " OR";
                        }
                    }
                    stmt = DB.con.prepareStatement(dbViewSelect);
                    if (expr != null)
                        for (int i = 0; i < expr.length; i++) {
                            stmt.setString(i * 3 + 1, "%" + expr[i] + "%");
                            stmt.setString(i * 3 + 2, "%" + expr[i] + "%");
                            stmt.setString(i * 3 + 3, "%" + expr[i] + "%");
                        }
                }
                ResultSet dbRes = stmt.executeQuery();
                while (dbRes.next()) {
                    RefBoxEntity item;
                    if (language == null) {
                        item = new RefBoxEntity(dbRes.getInt("ID"), dbRes.getString("Text"), dbRes.getString("SubText"));
                        res.add(item);
                    } else {
                        item = new RefBoxEntity(dbRes.getInt("ID"), Translator.get().getTranslation(dbRes.getString("Text"), language), Translator.get().getTranslation(dbRes.getString("SubText"), language));
                        if (expr != null && expr.length > 0) {
                            String lowExpr;
                            for (int i = 0; i < expr.length; i++) {
                                lowExpr = expr[i].toLowerCase();
                                if (String.valueOf(item.getId()).contains(lowExpr)
                                        || item.getText().toLowerCase().contains(lowExpr)
                                        || item.getSubText().toLowerCase().contains(lowExpr)) {
                                    res.add(item);
                                    break;
                                }
                            }
                        } else
                            res.add(item);
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return res;
    }

}
