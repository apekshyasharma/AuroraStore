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
            
            String query = "SELECT u.user_id, u.user_name, u.user_email, u.contact_number, u.created_at, " +
                          "u.role_id, r.role_type FROM users u " +
                          "JOIN user_roles r ON u.role_id = r.role_id " +
                          "ORDER BY u.user_id";
            
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
}
