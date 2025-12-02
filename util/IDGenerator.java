package util;

import java.sql.ResultSet;
import java.sql.SQLException;
import database.Connect;

public class IDGenerator {

    public static String generateID(String colName, String tableName, String prefix) {
        String lastID = null;
        String query = String.format("SELECT %s FROM %s ORDER BY %s DESC LIMIT 1", colName, tableName, colName);
        
        Connect con = Connect.getInstance();
        ResultSet rs = con.execQuery(query);
        
        try {
            if (rs.next()) {
                lastID = rs.getString(colName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        if (lastID == null) {
            return prefix + "001";
        }
        
        String numberPart = lastID.substring(prefix.length()); 
        int number = Integer.parseInt(numberPart); 
        number++; 
        
        String newNumberPart = String.format("%03d", number);
        
        return prefix + newNumberPart; 
    }
}