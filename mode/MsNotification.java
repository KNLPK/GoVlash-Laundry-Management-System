package model;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import database.Connect;

public class MsNotification {
    public String getNotifId() {
		return notifId;
	}

	public void setNotifId(String notifId) {
		this.notifId = notifId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getNotifContent() {
		return notifContent;
	}

	public void setNotifContent(String notifContent) {
		this.notifContent = notifContent;
	}

	public Date getNotifDate() {
		return notifDate;
	}

	public void setNotifDate(Date notifDate) {
		this.notifDate = notifDate;
	}

	public boolean isRead() {
		return isRead;
	}

	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}

	private String notifId;
    private String userId;
    private String notifContent;
    private Date notifDate;
    private boolean isRead;

    public MsNotification(String notifId, String userId, String notifContent, Date notifDate, boolean isRead) {
        this.notifId = notifId;
        this.userId = userId;
        this.notifContent = notifContent;
        this.notifDate = notifDate;
        this.isRead = isRead;
    }

    public static ArrayList<MsNotification> getNotificationsByUser(String userId) {
        ArrayList<MsNotification> notifs = new ArrayList<>();
        Connect con = Connect.getInstance();
        String query = "SELECT * FROM MsNotification WHERE userId = '" + userId + "' ORDER BY notifDate DESC";
        ResultSet rs = con.execQuery(query);

        try {
            while (rs.next()) {
                notifs.add(new MsNotification(
                    rs.getString("notifId"),
                    rs.getString("userId"),
                    rs.getString("notifContent"),
                    rs.getDate("notifDate"),
                    rs.getBoolean("isRead")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notifs;
    }

    public boolean sendNotification() {
        Connect con = Connect.getInstance();
        String query = "INSERT INTO MsNotification VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ps = con.prepareStatement(query);
        try {
            ps.setString(1, this.notifId);
            ps.setString(2, this.userId);
            ps.setString(3, this.notifContent);
            ps.setDate(4, this.notifDate);
            ps.setBoolean(5, false); 
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean markAsRead(String notifId) {
        Connect con = Connect.getInstance();
        String query = "UPDATE MsNotification SET isRead = true WHERE notifId = ?";
        PreparedStatement ps = con.prepareStatement(query);
        try {
            ps.setString(1, notifId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteNotification() {
        Connect con = Connect.getInstance();
        String query = "DELETE FROM MsNotification WHERE notifId = ?";
        PreparedStatement ps = con.prepareStatement(query);
        try {
            ps.setString(1, this.notifId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}