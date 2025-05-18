package com.AuroraStore.service;

import com.AuroraStore.config.DbConfig;
import com.AuroraStore.model.UsersModel;
import com.AuroraStore.util.PasswordUtil;
import java.sql.*;

public class CustomerService {

	
	private Connection dbConn;

	/**
	 * Constructor initializes the database connection.
	 */
	public CustomerService() {
		try {
			this.dbConn = DbConfig.getDbConnection();
		} catch (SQLException | ClassNotFoundException ex) {
			System.err.println("Database connection error: " + ex.getMessage());
			ex.printStackTrace();
		}
	}
	
	public boolean registerCustomer(UsersModel user) {
	    if (dbConn == null) {
	        System.err.println("Database connection is not available.");
	        return false;
	    }
	    
	    String query = "INSERT INTO users (user_name, user_email, user_password, contact_number, created_at, role_id, image) " +
	                  "VALUES (?, ?, ?, ?, NOW(), ?, ?)";
	    
	    try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
	        stmt.setString(1, user.getUser_name());
	        stmt.setString(2, user.getUser_email());
	        stmt.setString(3, PasswordUtil.encrypt(user.getUser_email(), user.getUser_password())); 
	        stmt.setString(4, user.getContact_number());
	        stmt.setInt(5, user.getRole_id());
	        // If image is null or empty, set to null (so DB default applies), else use provided value
	        String image = user.getImage();
	        if (image == null || image.trim().isEmpty()) {
	            stmt.setNull(6, java.sql.Types.VARCHAR);
	        } else {
	            stmt.setString(6, image);
	        }
	        
	        int result = stmt.executeUpdate();
	        return result > 0;
	    } catch (SQLException e) {
	        System.err.println("Error during user registration: " + e.getMessage());
	        e.printStackTrace();
	        return false;
	    }
	}
	
		public UsersModel login(String email, String password) {
			UsersModel customer = null;
			try {
				String query = "SELECT * FROM user WHERE email = ?";

				PreparedStatement pst = dbConn.prepareStatement(query);
				pst.setString(1, email);

				ResultSet set = pst.executeQuery();

				if (set.next()) {
					customer = new UsersModel();

					customer.setUser_id(set.getInt("userid"));

					// data from db
					String name = set.getString("name");
					// set to user object
					customer.setUser_name(name);
					customer.setUser_email(set.getString("email"));
					customer.setContact_number(set.getString("number"));
					String password1 = set.getString("password");
					String decryptPassword = PasswordUtil.decrypt(password1, email);
					customer.setUser_password(decryptPassword);

					customer.setCreated_at(set.getString("created_at"));
					customer.setRole_id(set.getInt("role_id"));
					if (decryptPassword != null && set.getString("email").equals(email)
							&& decryptPassword.equals(password)) {
						return customer;
					}

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		{

	}
	
	/**
	 * Checks if an email already exists in the database
	 * @param email Email to check
	 * @return true if email exists, false otherwise
	 */
	public boolean emailExists(String email) {
	    if (dbConn == null) {
	        System.err.println("Database connection is not available.");
	        return false;
	    }
	    
	    String query = "SELECT COUNT(*) FROM users WHERE user_email = ?";
	    
	    try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
	        stmt.setString(1, email);
	        ResultSet rs = stmt.executeQuery();
	        
	        if (rs.next()) {
	            return rs.getInt(1) > 0;
	        }
	    } catch (SQLException e) {
	        System.err.println("Error checking email existence: " + e.getMessage());
	        e.printStackTrace();
	    }
	    
	    return false;
	}
}
