package com.AuroraStore.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.AuroraStore.config.DbConfig;
import com.AuroraStore.model.UsersModel;

/**
 * Service class for handling user portfolio-related operations, 
 * including profile management and database interactions.
 */
public class PortfolioService {
	private Connection dbConn;

	/**
	 * Constructor initializes the database connection.
	 */
	public PortfolioService() {
		try {
			this.dbConn = DbConfig.getDbConnection();
		} catch (SQLException | ClassNotFoundException ex) {
			System.err.println("Database connection error: " + ex.getMessage());
			ex.printStackTrace();
		}
	}
	/**
     * Retrieves a user's profile details from the database.
     * 
     * @param userId The unique ID of the user whose profile is being requested
     * @return UsersModel object populated with profile data, or null if not found
     */
	public UsersModel getUserProfile(int userId) {
	    UsersModel user = null;
	    // SQL query to fetch user details with role name using JOIN
	    String query = "SELECT u.user_name, u.user_email, u.contact_number, r.role_name " +
	                   "FROM users u " +
	                   "JOIN user_roles r ON u.role_id = r.role_id " +
	                   "WHERE u.user_id = ?";
	    
	    try (PreparedStatement pstmt = dbConn.prepareStatement(query)) {
	    	// Set the user_id parameter in the prepared statement
	        pstmt.setInt(1, userId);
	        ResultSet rs = pstmt.executeQuery();

	        if (rs.next()) {
	        	// Process the result set if a matching user is found
	            user = new UsersModel();
	            user.setUser_name(rs.getString("user_name"));
	            user.setUser_email(rs.getString("user_email"));
	            user.setContact_number(rs.getString("contact_number"));
	        }
	    } catch (SQLException e) {
	        System.err.println("Profile error: " + e.getMessage());
	    }
	    return user;
	}
}
