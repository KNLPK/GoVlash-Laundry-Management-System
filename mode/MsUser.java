package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Date;

import database.Connect;

public class MsUser {
    public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	private String userId;
    private String username;
    private String password;
    private String email;
    private String gender;
    private Date dob;
    private String role;

    public MsUser(String userId, String username, String password, String email, String gender, Date dob, String role) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.gender = gender;
        this.dob = dob;
        this.role = role;
    }

    public static MsUser login(String username, String password) {
        Connect con = Connect.getInstance();
        String query = "SELECT * FROM MsUser WHERE username = ? AND password = ?";
        PreparedStatement ps = con.prepareStatement(query);
        
        try {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return new MsUser(
                    rs.getString("userId"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("email"),
                    rs.getString("gender"),
                    rs.getDate("dob"),
                    rs.getString("role")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; 
    }

    public boolean register() {
        Connect con = Connect.getInstance();
        String query = "INSERT INTO MsUser (userId, username, password, email, gender, dob, role) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = con.prepareStatement(query);
        
        try {
            ps.setString(1, this.userId);
            ps.setString(2, this.username);
            ps.setString(3, this.password);
            ps.setString(4, this.email);
            ps.setString(5, this.gender);
            ps.setDate(6, this.dob);
            ps.setString(7, this.role);
            
            int result = ps.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static ArrayList<MsUser> getAllEmployees() {
        ArrayList<MsUser> employees = new ArrayList<>();
        Connect con = Connect.getInstance();
        String query = "SELECT * FROM MsUser WHERE role IN ('Laundry Staff', 'Receptionist')";
        ResultSet rs = con.execQuery(query);
        
        try {
            while (rs.next()) {
                employees.add(new MsUser(
                    rs.getString("userId"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("email"),
                    rs.getString("gender"),
                    rs.getDate("dob"),
                    rs.getString("role")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }
    
    public static boolean isUsernameUnique(String username) {
        Connect con = Connect.getInstance();
        String query = "SELECT * FROM MsUser WHERE username = '" + username + "'";
        ResultSet rs = con.execQuery(query);
        try {
            return !rs.next(); 
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}