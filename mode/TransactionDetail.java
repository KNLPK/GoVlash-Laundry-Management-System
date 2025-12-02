package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import database.Connect;

public class TransactionDetail {
    public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	private String transactionId;
    private String serviceId;

    public TransactionDetail(String transactionId, String serviceId) {
        this.transactionId = transactionId;
        this.serviceId = serviceId;
    }

    public boolean insertDetail() {
        Connect con = Connect.getInstance();
        String query = "INSERT INTO TransactionDetail (transactionId, serviceId) VALUES (?, ?)";
        PreparedStatement ps = con.prepareStatement(query);
        try {
            ps.setString(1, this.transactionId);
            ps.setString(2, this.serviceId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static ArrayList<MsService> getServicesByTransactionId(String transId) {
        ArrayList<MsService> services = new ArrayList<>();
        Connect con = Connect.getInstance();
        String query = "SELECT s.* FROM TransactionDetail td " +
                       "JOIN MsService s ON td.serviceId = s.serviceId " +
                       "WHERE td.transactionId = '" + transId + "'";
        
        ResultSet rs = con.execQuery(query);
        try {
            while(rs.next()) {
                services.add(new MsService(
                    rs.getString("serviceId"),
                    rs.getString("serviceName"),
                    rs.getInt("serviceDuration"),
                    rs.getInt("servicePrice"),
                    rs.getString("serviceDesc")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return services;
    }
}