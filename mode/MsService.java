package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import database.Connect;

public class MsService {
    public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public int getServiceDuration() {
		return serviceDuration;
	}

	public void setServiceDuration(int serviceDuration) {
		this.serviceDuration = serviceDuration;
	}

	public int getServicePrice() {
		return servicePrice;
	}

	public void setServicePrice(int servicePrice) {
		this.servicePrice = servicePrice;
	}

	public String getServiceDesc() {
		return serviceDesc;
	}

	public void setServiceDesc(String serviceDesc) {
		this.serviceDesc = serviceDesc;
	}

	private String serviceId;
    private String serviceName;
    private int serviceDuration;
    private int servicePrice;
    private String serviceDesc;

    public MsService(String serviceId, String serviceName, int serviceDuration, int servicePrice, String serviceDesc) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.serviceDuration = serviceDuration;
        this.servicePrice = servicePrice;
        this.serviceDesc = serviceDesc;
    }

    public static ArrayList<MsService> getAllServices() {
        ArrayList<MsService> services = new ArrayList<>();
        Connect con = Connect.getInstance();
        String query = "SELECT * FROM MsService";
        ResultSet rs = con.execQuery(query);

        try {
            while (rs.next()) {
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
    
    public static MsService getServiceById(String id) {
        Connect con = Connect.getInstance();
        String query = "SELECT * FROM MsService WHERE serviceId = '" + id + "'";
        ResultSet rs = con.execQuery(query);
        try {
            if (rs.next()) {
                return new MsService(
                    rs.getString("serviceId"),
                    rs.getString("serviceName"),
                    rs.getInt("serviceDuration"),
                    rs.getInt("servicePrice"),
                    rs.getString("serviceDesc")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static MsService getServiceByName(String name) {
        Connect con = Connect.getInstance();
        String query = "SELECT * FROM MsService WHERE serviceName = ?";
        PreparedStatement ps = con.prepareStatement(query);
        try {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new MsService(
                    rs.getString("serviceId"),
                    rs.getString("serviceName"),
                    rs.getInt("serviceDuration"),
                    rs.getInt("servicePrice"),
                    rs.getString("serviceDesc")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean insertService() {
        Connect con = Connect.getInstance();
        String query = "INSERT INTO MsService VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ps = con.prepareStatement(query);
        try {
            ps.setString(1, this.serviceId);
            ps.setString(2, this.serviceName);
            ps.setInt(3, this.serviceDuration);
            ps.setInt(4, this.servicePrice);
            ps.setString(5, this.serviceDesc);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateService() {
        Connect con = Connect.getInstance();
        String query = "UPDATE MsService SET serviceName=?, serviceDuration=?, servicePrice=?, serviceDesc=? WHERE serviceId=?";
        PreparedStatement ps = con.prepareStatement(query);
        try {
            ps.setString(1, this.serviceName);
            ps.setInt(2, this.serviceDuration);
            ps.setInt(3, this.servicePrice);
            ps.setString(4, this.serviceDesc);
            ps.setString(5, this.serviceId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteService() {
        Connect con = Connect.getInstance();
        String query = "DELETE FROM MsService WHERE serviceId=?";
        PreparedStatement ps = con.prepareStatement(query);
        try {
            ps.setString(1, this.serviceId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String toString() {
        return this.serviceName;
    }
}