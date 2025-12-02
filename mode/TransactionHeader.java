package model;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import database.Connect;

public class TransactionHeader {
    public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getStaffId() {
		return staffId;
	}

	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}

	public String getReceptionistId() {
		return receptionistId;
	}

	public void setReceptionistId(String receptionistId) {
		this.receptionistId = receptionistId;
	}

	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	public String getTransactionStatus() {
		return transactionStatus;
	}

	public void setTransactionStatus(String transactionStatus) {
		this.transactionStatus = transactionStatus;
	}

	private String transactionId;
    private String customerId;      
    private String staffId;
    private String receptionistId;
    private Date transactionDate;
    private String transactionStatus;

    public TransactionHeader(String transactionId, String customerId, String staffId, String receptionistId, Date transactionDate, String transactionStatus) {
        this.transactionId = transactionId;
        this.customerId = customerId;
        this.staffId = staffId;
        this.receptionistId = receptionistId;
        this.transactionDate = transactionDate;
        this.transactionStatus = transactionStatus;
    }

    // 1. Insert Transaction Header (Customer Order)
    public boolean insertTransaction() {
        Connect con = Connect.getInstance();
        String query = "INSERT INTO TransactionHeader (transactionId, customerId, transactionDate, transactionStatus) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = con.prepareStatement(query);
        try {
            ps.setString(1, this.transactionId);
            ps.setString(2, this.customerId);
            ps.setDate(3, this.transactionDate);
            ps.setString(4, "Pending"); 
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 2. Admin: Lihat Semua Transaksi
    public static ArrayList<TransactionHeader> getAllTransactions() {
        return fetchTransactions("SELECT * FROM TransactionHeader ORDER BY transactionDate DESC");
    }

    // 3. Receptionist: Liat Transaksi Pending
    public static ArrayList<TransactionHeader> getPendingTransactions() {
        return fetchTransactions("SELECT * FROM TransactionHeader WHERE transactionStatus = 'Pending' ORDER BY transactionDate DESC");
    }

    // 4. Laundry Staff: Liat Transaksi yang Ditugaskan ke Dia
    public static ArrayList<TransactionHeader> getAssignedTransactions(String staffId) {
        return fetchTransactions("SELECT * FROM TransactionHeader WHERE staffId = '" + staffId + "' AND transactionStatus != 'Finished' ORDER BY transactionDate DESC");
    }

    // 5. Customer: Liat History Sendiri
    public static ArrayList<TransactionHeader> getCustomerHistory(String customerId) {
        return fetchTransactions("SELECT * FROM TransactionHeader WHERE customerId = '" + customerId + "' ORDER BY transactionDate DESC");
    }

    private static ArrayList<TransactionHeader> fetchTransactions(String query) {
        ArrayList<TransactionHeader> list = new ArrayList<>();
        Connect con = Connect.getInstance();
        ResultSet rs = con.execQuery(query);
        try {
            while (rs.next()) {
                list.add(new TransactionHeader(
                    rs.getString("transactionId"),
                    rs.getString("customerId"),
                    rs.getString("staffId"), 
                    rs.getString("receptionistId"), 
                    rs.getDate("transactionDate"),
                    rs.getString("transactionStatus")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 6. Receptionist: Assign Staff (Update staffId & receptionistId)
    public static boolean assignStaff(String transId, String staffId, String receptId) {
        Connect con = Connect.getInstance();
        String query = "UPDATE TransactionHeader SET staffId = ?, receptionistId = ?, transactionStatus = 'In Progress' WHERE transactionId = ?";
        PreparedStatement ps = con.prepareStatement(query);
        try {
            ps.setString(1, staffId);
            ps.setString(2, receptId);
            ps.setString(3, transId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 7. Staff: Finish Order (Update Status jadi Finished)
    public static boolean finishTransaction(String transId) {
        Connect con = Connect.getInstance();
        String query = "UPDATE TransactionHeader SET transactionStatus = 'Finished' WHERE transactionId = ?";
        PreparedStatement ps = con.prepareStatement(query);
        try {
            ps.setString(1, transId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}