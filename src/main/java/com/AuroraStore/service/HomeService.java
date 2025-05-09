package com.AuroraStore.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.AuroraStore.config.DbConfig;
import com.AuroraStore.model.ProductsModel;

public class HomeService {
	private Connection dbConn;

	/**
	 * Constructor initializes the database connection.
	 */
	public HomeService() {
		try {
			this.dbConn = DbConfig.getDbConnection();
		} catch (SQLException | ClassNotFoundException ex) {
			System.err.println("Database connection error: " + ex.getMessage());
			ex.printStackTrace();
		}
	}
	/**
     * Fetches all products marked as "New Arrivals" (category_id=2).
     * 
     * @return List of ProductsModel objects containing product details
     */
		public List<ProductsModel> getNewArrivals() {
		    List<ProductsModel> products = new ArrayList<>();
		    String query = "SELECT product_id, product_name, price, product_description " +
		                   "FROM products WHERE category_id = 2"; // Category ID 2 = "New Arrivals"
		    
		    try (Statement stmt = dbConn.createStatement();
		         ResultSet rs = stmt.executeQuery(query)) {
		        
		        while (rs.next()) {
		            ProductsModel product = new ProductsModel();
		            product.setProduct_id(rs.getInt("product_id"));
		            product.setProduct_name(rs.getString("product_name"));
		            product.setProduct_price(rs.getDouble("price"));
		            product.setProduct_description(rs.getString("product_description"));
		            products.add(product);
		        }
		    } catch (SQLException e) {
		        System.err.println("New Arrivals error: " + e.getMessage());
		    }
		    return products;
		}
		 /**
	     * Fetches all products marked as "Most Purchased" (category_id=3).
	     * 
	     * @return List of ProductsModel objects containing product details
	     */
		public List<ProductsModel> getMostPurchased() {
		    List<ProductsModel> products = new ArrayList<>();
		    String query = "SELECT product_id, product_name, price, product_description " +
		                   "FROM products WHERE category_id = 3"; 
		    
		    try (Statement stmt = dbConn.createStatement();
		         ResultSet rs = stmt.executeQuery(query)) {
		       
		    } catch (SQLException e) {
		        System.err.println("Most Purchased error: " + e.getMessage());
		    }
		    return products;
		}
	}
