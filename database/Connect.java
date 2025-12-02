package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Connect {
    
    private final String USERNAME = "root"; 
    private final String PASSWORD = ""; 
    private final String DATABASE = "go_vlash_laundry";
    private final String HOST = "localhost:3306"; 
    private final String CONNECTION = String.format("jdbc:mysql://%s/%s", HOST, DATABASE);
    
    private Connection con;
    private Statement st;
    private static Connect instance;
    
    public static Connect getInstance() {
        if(instance == null) {
            instance = new Connect();
        }
        return instance;
    }
    
    private Connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(CONNECTION, USERNAME, PASSWORD);
            st = con.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public ResultSet execQuery(String query) {
        try {
            st.executeQuery(query);
            return st.getResultSet();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public void execUpdate(String query) {
        try {
            st.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public PreparedStatement prepareStatement(String query) {
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ps;
    }
}