package com.AuroraStore.service;

import com.AuroraStore.config.DbConfig;
import com.AuroraStore.model.UsersModel;
import com.AuroraStore.util.PasswordUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginService {
    private Connection dbConn;
    private boolean isConnectionError = false;

    public LoginService() {
        try {
            dbConn = DbConfig.getDbConnection();
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            isConnectionError = true;
        }
    }

    public UsersModel loginUser(String email, String password) {
        if (isConnectionError) {
            System.out.println("Connection Error!");
            return null;
        }

        String query = "SELECT * FROM users WHERE user_email = ? AND role_id IN (1, 2)";
        try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet result = stmt.executeQuery();

            if (result.next()) {
                String dbPassword = result.getString("user_password");
                int roleId = result.getInt("role_id");
                
                // For admin (role_id = 1), do direct password comparison
                // For customer (role_id = 2), decrypt password
                boolean passwordMatch = false;
                if (roleId == 1) {
                    passwordMatch = dbPassword.equals(password);
                } else {
                    String decryptedPassword = PasswordUtil.decrypt(dbPassword, email);
                    passwordMatch = decryptedPassword != null && decryptedPassword.equals(password);
                }

                if (passwordMatch) {
                    UsersModel user = new UsersModel();
                    user.setUser_id(result.getInt("user_id"));
                    user.setUser_name(result.getString("user_name"));
                    user.setUser_email(email);
                    user.setContact_number(result.getString("contact_number"));
                    user.setCreated_at(result.getString("created_at"));
                    user.setRole_id(roleId);
                    user.setImage(result.getString("image"));
                    return user;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
