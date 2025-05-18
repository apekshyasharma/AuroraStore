package com.AuroraStore.service;

import com.AuroraStore.config.DbConfig;
import com.AuroraStore.model.ProductsModel;
import com.AuroraStore.model.UsersModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DashboardService {
    
    /**
     * Retrieves all users from the database except password and image fields
     * @return List of UsersModel objects with user information
     */
    public List<UsersModel> getAllUsers() {
        List<UsersModel> usersList = new ArrayList<>();
        Connection dbConn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            // Get DB connection
            dbConn = DbConfig.getDbConnection();
            System.out.println("Connected to database successfully for users query");
            
            String query = "SELECT u.user_id, u.user_name, u.user_email, u.contact_number, " +
                           "DATE_FORMAT(u.created_at, '%Y-%m-%d %H:%i:%s') as created_at, " +
                           "u.role_id, r.role_type FROM users u " +
                           "JOIN user_roles r ON u.role_id = r.role_id " +
                           "ORDER BY u.created_at DESC";
            
            stmt = dbConn.prepareStatement(query);
            rs = stmt.executeQuery();
            
            System.out.println("Executing query for users: " + query);
            
            while (rs.next()) {
                UsersModel user = new UsersModel();
                user.setUser_id(rs.getInt("user_id"));
                user.setUser_name(rs.getString("user_name"));
                user.setUser_email(rs.getString("user_email"));
                user.setContact_number(rs.getString("contact_number"));
                user.setCreated_at(rs.getString("created_at"));
                user.setRole_id(rs.getInt("role_id"));
                // Store role_type in image field for display purposes
                user.setImage(rs.getString("role_type"));
                
                usersList.add(user);
            }
            
            System.out.println("Retrieved " + usersList.size() + " users from database");
            
        } catch (SQLException e) {
            System.err.println("SQL Error retrieving users: " + e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("Driver Error retrieving users: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Close resources in reverse order
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (dbConn != null) dbConn.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
        
        return usersList;
    }
    
    /**
     * Retrieves all products from the database
     * @return List of ProductsModel objects with product information
     */
    public List<ProductsModel> getAllProducts() {
        List<ProductsModel> productsList = new ArrayList<>();
        Connection dbConn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            // Get DB connection
            dbConn = DbConfig.getDbConnection();
            System.out.println("Connected to database successfully for products query");
            
            String query = "SELECT p.product_id, p.product_name, p.product_price, p.product_description, " +
                          "p.product_quantity, p.product_status, p.category_id, p.brand_id, " +
                          "c.category_name, b.brand_name FROM products p " +
                          "JOIN categories c ON p.category_id = c.category_id " +
                          "JOIN brands b ON p.brand_id = b.brand_id " +
                          "ORDER BY p.product_id";
            
            stmt = dbConn.prepareStatement(query);
            rs = stmt.executeQuery();
            
            System.out.println("Executing query for products: " + query);
            
            while (rs.next()) {
                ProductsModel product = new ProductsModel();
                product.setProduct_id(rs.getInt("product_id"));
                product.setProduct_name(rs.getString("product_name"));
                product.setProduct_price(rs.getDouble("product_price"));
                product.setProduct_description(rs.getString("product_description"));
                product.setProduct_quantity(rs.getInt("product_quantity"));
                product.setProduct_status(rs.getString("product_status"));
                product.setCategory_id(rs.getInt("category_id"));
                product.setBrand_id(rs.getInt("brand_id"));
                product.setCategory_name(rs.getString("category_name"));
                product.setBrand_name(rs.getString("brand_name"));
                
                productsList.add(product);
            }
            
            System.out.println("Retrieved " + productsList.size() + " products from database");
            
        } catch (SQLException e) {
            System.err.println("SQL Error retrieving products: " + e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("Driver Error retrieving products: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Close resources in reverse order
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (dbConn != null) dbConn.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
        
        return productsList;
    }
    
    /**
     * Gets the total count of users in the system
     * @return Total number of users
     */
    public int getUserCount() {
        Connection dbConn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int count = 0;
        
        try {
            dbConn = DbConfig.getDbConnection();
            String query = "SELECT COUNT(*) FROM users";
            
            stmt = dbConn.prepareStatement(query);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error counting users: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (dbConn != null) dbConn.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
        
        return count;
    }
    
    /**
     * Gets the total count of products in the system
     * @return Total number of products
     */
    public int getProductCount() {
        Connection dbConn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int count = 0;
        
        try {
            dbConn = DbConfig.getDbConnection();
            String query = "SELECT COUNT(*) FROM products";
            
            stmt = dbConn.prepareStatement(query);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error counting products: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (dbConn != null) dbConn.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
        
        return count;
    }
    
    /**
     * Calculates the total inventory value
     * @return Total inventory value
     */
    public double getTotalInventoryValue() {
        Connection dbConn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        double totalValue = 0.0;
        
        try {
            dbConn = DbConfig.getDbConnection();
            String query = "SELECT SUM(product_price * product_quantity) as total FROM products";
            
            stmt = dbConn.prepareStatement(query);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                totalValue = rs.getDouble("total");
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error calculating inventory value: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (dbConn != null) dbConn.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
        
        return totalValue;
    }
    
    /**
     * Retrieves a user by ID
     * @param userId The ID of the user to retrieve
     * @return UsersModel object with user information, or null if not found
     */
    public UsersModel getUserById(int userId) {
        Connection dbConn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        UsersModel user = null;
        
        try {
            dbConn = DbConfig.getDbConnection();
            String query = "SELECT u.user_id, u.user_name, u.user_email, u.contact_number, " +
                           "DATE_FORMAT(u.created_at, '%Y-%m-%d %H:%i:%s') as created_at, " +
                           "u.role_id, r.role_type FROM users u " +
                           "JOIN user_roles r ON u.role_id = r.role_id " +
                           "WHERE u.user_id = ?";
            
            stmt = dbConn.prepareStatement(query);
            stmt.setInt(1, userId);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                user = new UsersModel();
                user.setUser_id(rs.getInt("user_id"));
                user.setUser_name(rs.getString("user_name"));
                user.setUser_email(rs.getString("user_email"));
                user.setContact_number(rs.getString("contact_number"));
                user.setCreated_at(rs.getString("created_at"));
                user.setRole_id(rs.getInt("role_id"));
                user.setImage(rs.getString("role_type")); // Role type
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error retrieving user by ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(rs, stmt, dbConn);
        }
        
        return user;
    }
    
    /**
     * Updates a user's email and phone number
     * @param userId The ID of the user to update
     * @param email The new email address
     * @param phone The new phone number
     * @return true if update was successful, false otherwise
     */
    public boolean updateUserDetails(int userId, String email, String phone) {
        Connection dbConn = null;
        PreparedStatement stmt = null;
        boolean success = false;
        
        try {
            dbConn = DbConfig.getDbConnection();
            String query = "UPDATE users SET user_email = ?, contact_number = ? WHERE user_id = ? AND role_id = 1";
            
            stmt = dbConn.prepareStatement(query);
            stmt.setString(1, email);
            stmt.setString(2, phone);
            stmt.setInt(3, userId);
            
            int rowsAffected = stmt.executeUpdate();
            success = (rowsAffected > 0);
            
            System.out.println("User update result: " + (success ? "Success" : "Failed") + 
                              ", Rows affected: " + rowsAffected);
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error updating user: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(null, stmt, dbConn);
        }
        
        return success;
    }
    
    /**
     * Deletes a user from the database
     * @param userId The ID of the user to delete
     * @return true if deletion was successful, false otherwise
     */
    public boolean deleteUser(int userId) {
        Connection dbConn = null;
        PreparedStatement stmt = null;
        boolean success = false;
        
        try {
            dbConn = DbConfig.getDbConnection();
            // Only delete if the user is an admin (role_id = 1)
            String query = "DELETE FROM users WHERE user_id = ? AND role_id = 1";
            
            stmt = dbConn.prepareStatement(query);
            stmt.setInt(1, userId);
            
            int rowsAffected = stmt.executeUpdate();
            success = (rowsAffected > 0);
            
            System.out.println("User deletion result: " + (success ? "Success" : "Failed") + 
                              ", Rows affected: " + rowsAffected);
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error deleting user: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(null, stmt, dbConn);
        }
        
        return success;
    }
    
    /**
     * Deletes a product from the database
     * @param productId The ID of the product to delete
     * @return true if deletion was successful, false otherwise
     */
    public boolean deleteProduct(int productId) {
        Connection dbConn = null;
        PreparedStatement stmt = null;
        boolean success = false;
        
        try {
            dbConn = DbConfig.getDbConnection();
            String query = "DELETE FROM products WHERE product_id = ?";
            
            stmt = dbConn.prepareStatement(query);
            stmt.setInt(1, productId);
            
            int rowsAffected = stmt.executeUpdate();
            success = (rowsAffected > 0);
            
            System.out.println("Product deletion result: " + (success ? "Success" : "Failed") + 
                              ", Rows affected: " + rowsAffected);
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error deleting product: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(null, stmt, dbConn);
        }
        
        return success;
    }
    
    /**
     * Checks if an email already exists in the database for a different user
     * @param email Email to check
     * @param userId User ID to exclude from the check
     * @return true if the email exists for another user, false otherwise
     */
    public boolean isEmailTaken(String email, int userId) {
        Connection dbConn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean isTaken = false;
        
        try {
            dbConn = DbConfig.getDbConnection();
            String query = "SELECT COUNT(*) FROM users WHERE user_email = ? AND user_id != ?";
            
            stmt = dbConn.prepareStatement(query);
            stmt.setString(1, email);
            stmt.setInt(2, userId);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                isTaken = rs.getInt(1) > 0;
            }
            
            System.out.println("Email availability check: " + email + " is " + 
                              (isTaken ? "already taken" : "available") + 
                              " (excluding user ID: " + userId + ")");
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error checking email uniqueness: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(rs, stmt, dbConn);
        }
        
        return isTaken;
    }
    
    /**
     * Checks if a phone number already exists in the database for a different user
     * @param phone Phone number to check
     * @param userId User ID to exclude from the check
     * @return true if the phone number exists for another user, false otherwise
     */
    public boolean isPhoneNumberTaken(String phone, int userId) {
        Connection dbConn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean isTaken = false;
        
        try {
            dbConn = DbConfig.getDbConnection();
            String query = "SELECT COUNT(*) FROM users WHERE contact_number = ? AND user_id != ?";
            
            stmt = dbConn.prepareStatement(query);
            stmt.setString(1, phone);
            stmt.setInt(2, userId);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                isTaken = rs.getInt(1) > 0;
            }
            
            System.out.println("Phone number availability check: " + phone + " is " + 
                              (isTaken ? "already taken" : "available") + 
                              " (excluding user ID: " + userId + ")");
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error checking phone number uniqueness: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(rs, stmt, dbConn);
        }
        
        return isTaken;
    }
    
    /**
     * Helper method to close database resources
     */
    private void closeResources(ResultSet rs, PreparedStatement stmt, Connection conn) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            System.err.println("Error closing resources: " + e.getMessage());
        }
    }
}
