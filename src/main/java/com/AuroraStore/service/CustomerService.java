package com.AuroraStore.service;

import java.sql.*;

import com.AuroraStore.config.DbConfig;
import com.AuroraStore.model.UsersModel;
import com.AuroraStore.util.PasswordUtil;

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
		
		String query = "INSERT INTO users (user_name, user_email, user_password, contact_number, created_at, role_id) "
				+ "VALUES (?, ?, ?, ?, ?, ?)";
		
		
		try {
			PreparedStatement Stmt = dbConn.prepareStatement(query);
			// Insert student details
			Stmt.setString(1, user.getUser_name());
			Stmt.setString(2, user.getUser_email() );
			Stmt.setString(3, user.getUser_password() );
			Stmt.setString(4,   user.getContact_number());
			Stmt.setString(5, user.getCreated_at());
			Stmt.setInt(6, user.getRole_id() );

			return Stmt.executeUpdate() > 0;
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
}
