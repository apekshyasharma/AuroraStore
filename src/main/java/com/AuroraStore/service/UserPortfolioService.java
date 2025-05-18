package com.AuroraStore.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.io.File;

import com.AuroraStore.config.DbConfig;
import com.AuroraStore.model.UsersModel;
import com.AuroraStore.util.ImagesUtil;

public class UserPortfolioService {
    private Connection dbConn;
    private boolean isConnectionError = false;

    public UserPortfolioService() {
        try {
            dbConn = DbConfig.getDbConnection();
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            isConnectionError = true;
        }
    }

    /**
     * Updates user information in the database
     * 
     * @param user The updated user model
     * @return true if update successful, false otherwise
     */
    public boolean updateUserInfo(UsersModel user) {
        if (isConnectionError) {
            System.out.println("Connection Error!");
            return false;
        }

        String query = "UPDATE users SET user_name = ?, user_email = ?, contact_number = ? WHERE user_id = ?";
        
        try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
            stmt.setString(1, user.getUser_name());
            stmt.setString(2, user.getUser_email());
            stmt.setString(3, user.getContact_number());
            stmt.setInt(4, user.getUser_id());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Updates user profile image in the database
     * 
     * @param userId The ID of the user
     * @param imageName The name of the uploaded image
     * @return true if update successful, false otherwise
     */
    public boolean updateUserImage(int userId, String imageName) {
        if (isConnectionError) {
            System.out.println("Connection Error!");
            return false;
        }

        String query = "UPDATE users SET image = ? WHERE user_id = ?";
        
        try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
            stmt.setString(1, imageName);
            stmt.setInt(2, userId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Deletes old profile image if it exists
     * 
     * @param oldImageName The name of the old image
     * @param realPath The real path to the web application root
     */
    public void deleteOldImage(String oldImageName, String realPath) {
        if (oldImageName != null && !oldImageName.isEmpty()) {
            // Path to images folder
            String imagePath = realPath + "resources/images/users/" + oldImageName;
            File oldImage = new File(imagePath);
            
            if (oldImage.exists()) {
                oldImage.delete();
                System.out.println("Old image deleted: " + imagePath);
            }
        }
    }
}
