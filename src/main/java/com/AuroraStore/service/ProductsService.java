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
