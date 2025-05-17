package com.AuroraStore.service;

import com.AuroraStore.config.DbConfig;
import com.AuroraStore.model.BrandsModel;
import com.AuroraStore.model.CategoriesModel;
import com.AuroraStore.model.ProductsModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class for product management operations
 */
public class AddProductsService {

    /**
     * Retrieves all categories from the database
     * @return List of CategoriesModel objects
     */
    public List<CategoriesModel> getAllCategories() {
        List<CategoriesModel> categories = new ArrayList<>();
        Connection dbConn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            dbConn = DbConfig.getDbConnection();
            String query = "SELECT category_id, category_name FROM categories ORDER BY category_name";
            
            stmt = dbConn.prepareStatement(query);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                CategoriesModel category = new CategoriesModel();
                category.setCategory_id(rs.getInt("category_id"));
                category.setCategory_name(rs.getString("category_name"));
                categories.add(category);
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error retrieving categories: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(rs, stmt, dbConn);
        }
        
        return categories;
    }
    
    /**
     * Retrieves all brands from the database
     * @return List of BrandsModel objects
     */
    public List<BrandsModel> getAllBrands() {
        List<BrandsModel> brands = new ArrayList<>();
        Connection dbConn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            dbConn = DbConfig.getDbConnection();
            String query = "SELECT brand_id, brand_name FROM brands ORDER BY brand_name";
            
            stmt = dbConn.prepareStatement(query);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                BrandsModel brand = new BrandsModel();
                brand.setBrand_id(rs.getInt("brand_id"));
                brand.setBrand_name(rs.getString("brand_name"));
                brands.add(brand);
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error retrieving brands: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(rs, stmt, dbConn);
        }
        
        return brands;
    }
    
    /**
     * Adds a new product to the database
     * @param product The product object containing product details
     * @param imageName The name of the uploaded image file (can be null)
     * @return The ID of the newly inserted product, or -1 if insertion failed
     */
    public int addProduct(ProductsModel product, String imageName) {
        Connection dbConn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int newProductId = -1;
        
        try {
            dbConn = DbConfig.getDbConnection();
            if (dbConn == null) {
                System.err.println("Database connection is null");
                return -1;
            }
            
            // Print debug info
            System.out.println("Attempting to add product: " + product.getProduct_name());
            System.out.println("Image name: " + imageName);
            
            String query = "INSERT INTO products (product_name, image, product_price, product_description, " +
                           "product_quantity, product_status, category_id, brand_id) " +
                           "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            
            stmt = dbConn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, product.getProduct_name());
            
            // Handle image name (can be null)
            if (imageName == null || imageName.trim().isEmpty()) {
                stmt.setNull(2, java.sql.Types.VARCHAR);
            } else {
                stmt.setString(2, imageName);
            }
            
            stmt.setDouble(3, product.getProduct_price());
            stmt.setString(4, product.getProduct_description());
            stmt.setInt(5, product.getProduct_quantity());
            stmt.setString(6, product.getProduct_status());
            stmt.setInt(7, product.getCategory_id());
            stmt.setInt(8, product.getBrand_id());
            
            // Print SQL debug
            System.out.println("Executing SQL: " + query);
            System.out.println("With values: name=" + product.getProduct_name() + 
                              ", image=" + imageName + 
                              ", price=" + product.getProduct_price() + 
                              ", desc=" + product.getProduct_description() + 
                              ", qty=" + product.getProduct_quantity() + 
                              ", status=" + product.getProduct_status() + 
                              ", catId=" + product.getCategory_id() + 
                              ", brandId=" + product.getBrand_id());
            
            int rowsAffected = stmt.executeUpdate();
            System.out.println("Rows affected: " + rowsAffected);
            
            if (rowsAffected > 0) {
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    newProductId = rs.getInt(1);
                    System.out.println("New product ID: " + newProductId);
                } else {
                    System.out.println("No generated keys returned");
                }
            } else {
                System.out.println("No rows affected by insert");
            }
        } catch (SQLException e) {
            System.err.println("SQL Error adding product: " + e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("Class not found adding product: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(rs, stmt, dbConn);
        }
        
        return newProductId;
    }
    
    /**
     * Tests the database connection
     * @return true if the connection is successful, false otherwise
     */
    public boolean testDatabaseConnection() {
        Connection dbConn = null;
        try {
            dbConn = DbConfig.getDbConnection();
            return dbConn != null;
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Database connection test failed: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            if (dbConn != null) {
                try {
                    dbConn.close();
                } catch (SQLException e) {
                    System.err.println("Error closing connection: " + e.getMessage());
                }
            }
        }
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
