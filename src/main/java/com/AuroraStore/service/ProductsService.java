package com.AuroraStore.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.AuroraStore.config.DbConfig;
import com.AuroraStore.model.ProductsModel;

public class ProductsService {
    
    /**
     * Retrieves all active products from the database
     * @return List of ProductsModel objects
     */
    public List<ProductsModel> getAllActiveProducts() {
        List<ProductsModel> products = new ArrayList<>();
        Connection dbConn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            dbConn = DbConfig.getDbConnection();
            String query = "SELECT p.product_id, p.product_name, p.image, p.product_price, " +
                           "p.product_description, p.product_quantity, p.product_status, " +
                           "c.category_name, b.brand_name " +
                           "FROM products p " +
                           "LEFT JOIN categories c ON p.category_id = c.category_id " +
                           "LEFT JOIN brands b ON p.brand_id = b.brand_id " +
                           "WHERE p.product_status = 'active' " +
                           "ORDER BY p.product_id DESC";
            
            stmt = dbConn.prepareStatement(query);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                ProductsModel product = new ProductsModel();
                product.setProduct_id(rs.getInt("product_id"));
                product.setProduct_name(rs.getString("product_name"));
                product.setProduct_price(rs.getDouble("product_price"));
                product.setProduct_description(rs.getString("product_description"));
                product.setProduct_quantity(rs.getInt("product_quantity"));
                product.setProduct_status(rs.getString("product_status"));
                product.setCategory_name(rs.getString("category_name"));
                product.setBrand_name(rs.getString("brand_name"));
                
                // Get image or set null
                String imageName = rs.getString("image");
                if (imageName != null && !imageName.trim().isEmpty()) {
                    product.setImage(imageName);
                }
                
                products.add(product);
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error retrieving products: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(rs, stmt, dbConn);
        }
        
        return products;
    }
    
    /**
     * Retrieves all active products from the database by category name
     * @param categoryName The name of the category
     * @return List of ProductsModel objects
     */
    public List<ProductsModel> getProductsByCategory(String categoryName) {
        List<ProductsModel> products = new ArrayList<>();
        Connection dbConn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            dbConn = DbConfig.getDbConnection();
            
            // Debug statement
            System.out.println("Executing query for category: " + categoryName);
            
            // Print all available categories to debug
            PreparedStatement catStmt = dbConn.prepareStatement("SELECT category_id, category_name FROM categories");
            ResultSet catRs = catStmt.executeQuery();
            System.out.println("Available categories:");
            while (catRs.next()) {
                System.out.println("- " + catRs.getString("category_name") + " (ID: " + catRs.getInt("category_id") + ")");
            }
            catRs.close();
            catStmt.close();
            
            // Using LIKE operator with TRIM for more flexible matching
            String query = "SELECT p.product_id, p.product_name, p.image, p.product_price, " +
                           "p.product_description, p.product_quantity, p.product_status, " +
                           "c.category_name, b.brand_name " +
                           "FROM products p " +
                           "LEFT JOIN categories c ON p.category_id = c.category_id " +
                           "LEFT JOIN brands b ON p.brand_id = b.brand_id " +
                           "WHERE p.product_status = 'active' AND TRIM(c.category_name) = TRIM(?) " +
                           "ORDER BY p.product_id DESC";
            
            stmt = dbConn.prepareStatement(query);
            stmt.setString(1, categoryName);
            
            // Print the actual SQL for debugging
            System.out.println("SQL Query: " + query.replace("?", "'" + categoryName + "'"));
            
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                ProductsModel product = new ProductsModel();
                product.setProduct_id(rs.getInt("product_id"));
                product.setProduct_name(rs.getString("product_name"));
                product.setProduct_price(rs.getDouble("product_price"));
                product.setProduct_description(rs.getString("product_description"));
                product.setProduct_quantity(rs.getInt("product_quantity"));
                product.setProduct_status(rs.getString("product_status"));
                product.setCategory_name(rs.getString("category_name"));
                product.setBrand_name(rs.getString("brand_name"));
                
                String imageName = rs.getString("image");
                if (imageName != null && !imageName.trim().isEmpty()) {
                    product.setImage(imageName);
                }
                
                products.add(product);
            }
            
            // If no products found with exact match, try with LIKE
            if (products.isEmpty()) {
                System.out.println("No products found with exact match, trying with LIKE");
                String fuzzyQuery = "SELECT p.product_id, p.product_name, p.image, p.product_price, " +
                                   "p.product_description, p.product_quantity, p.product_status, " +
                                   "c.category_name, b.brand_name " +
                                   "FROM products p " +
                                   "LEFT JOIN categories c ON p.category_id = c.category_id " +
                                   "LEFT JOIN brands b ON p.brand_id = b.brand_id " +
                                   "WHERE p.product_status = 'active' AND LOWER(c.category_name) LIKE LOWER(?) " +
                                   "ORDER BY p.product_id DESC";
                
                PreparedStatement fuzzyStmt = dbConn.prepareStatement(fuzzyQuery);
                fuzzyStmt.setString(1, "%" + categoryName + "%");
                
                ResultSet fuzzyRs = fuzzyStmt.executeQuery();
                
                while (fuzzyRs.next()) {
                    ProductsModel product = new ProductsModel();
                    product.setProduct_id(fuzzyRs.getInt("product_id"));
                    product.setProduct_name(fuzzyRs.getString("product_name"));
                    product.setProduct_price(fuzzyRs.getDouble("product_price"));
                    product.setProduct_description(fuzzyRs.getString("product_description"));
                    product.setProduct_quantity(fuzzyRs.getInt("product_quantity"));
                    product.setProduct_status(fuzzyRs.getString("product_status"));
                    product.setCategory_name(fuzzyRs.getString("category_name"));
                    product.setBrand_name(fuzzyRs.getString("brand_name"));
                    
                    String imageName = fuzzyRs.getString("image");
                    if (imageName != null && !imageName.trim().isEmpty()) {
                        product.setImage(imageName);
                    }
                    
                    products.add(product);
                }
                
                fuzzyRs.close();
                fuzzyStmt.close();
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error retrieving products by category: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(rs, stmt, dbConn);
        }
        
        return products;
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
