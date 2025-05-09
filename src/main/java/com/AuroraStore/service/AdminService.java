package com.AuroraStore.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.AuroraStore.config.DbConfig;
import com.AuroraStore.model.ProductsModel;

public class AdminService {
	private Connection dbConn;

	/**
	 * Constructor initializes the database connection.
	 */
	public AdminService() {
		try {
			this.dbConn = DbConfig.getDbConnection();
		} catch (SQLException | ClassNotFoundException ex) {
			System.err.println("Database connection error: " + ex.getMessage());
			ex.printStackTrace();
		}
	}
	/**
     * Adds a new product to the database (Admin Product Page).
     * 
     * @param product The ProductsModel object containing product details
     * @return true if the product is added successfully, false otherwise
     */
	public boolean addProduct(ProductsModel product) {
	    String query = "INSERT INTO products (product_name, price, product_description, category_id, brand_id) " +
	                   "VALUES (?, ?, ?, ?, ?)";
	    
	    try (PreparedStatement pstmt = dbConn.prepareStatement(query)) {
	        pstmt.setString(1, product.getProduct_name());
	        pstmt.setDouble(2, product.getProduct_price());
	        pstmt.setString(3, product.getProduct_description());
	        pstmt.setInt(4, product.getCategory_id()); // e.g., 2 for "New Arrivals"
	        pstmt.setInt(5, product.getBrand_id());     // e.g., 1 for "NoTes"
	        
	        return pstmt.executeUpdate() > 0;
	    } catch (SQLException e) {
	        System.err.println("Add product error: " + e.getMessage());
	        return false;
	    }
	}
	/**
     * Retrieves all products from the database with category and brand names (Admin Dashboard).
     * 
     * @return List of ProductsModel objects with product details
     */
	public List<ProductsModel> getAllProducts() {
	    List<ProductsModel> products = new ArrayList<>();
	    String query = "SELECT p.product_id, p.product_name, p.price, c.category_name, b.brand_name " +
	                   "FROM products p " +
	                   "JOIN categories c ON p.category_id = c.category_id " +
	                   "JOIN brands b ON p.brand_id = b.brand_id";
	    
	    try (Statement stmt = dbConn.createStatement();
	         ResultSet rs = stmt.executeQuery(query)) {
	        
	        while (rs.next()) {
	            ProductsModel product = new ProductsModel();
	            product.setProduct_id(rs.getInt("product_id"));
	            product.setProduct_name(rs.getString("product_name"));
	            product.setProduct_price(rs.getDouble("price"));
	            product.setCategory_name(rs.getString("category_name"));
	            product.setBrand_name(rs.getString("brand_name"));
	            products.add(product);
	        }
	    } catch (SQLException e) {
	        System.err.println("Fetch products error: " + e.getMessage());
	    }
	    return products;
	}

}
