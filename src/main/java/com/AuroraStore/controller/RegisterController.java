package com.AuroraStore.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

import com.AuroraStore.model.UsersModel;
import com.AuroraStore.service.CustomerService;
import com.AuroraStore.util.PasswordUtil;
import com.AuroraStore.util.ValidationUtil;

/**
 * Servlet implementation class RegisterController
 * Handles user registration requests (GET and POST).
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/register" })
public class RegisterController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	/**
	 * Handles GET request to show the registration page.
	 * Forwards the request to the JSP view.
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.getRequestDispatcher("WEB-INF/pages/register.jsp").forward(request, response);
	}

	/**
	 * Handles POST request for user registration.
	 * Validates input fields, encrypts password, creates a user model,
	 * and invokes the service to register the user.
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
	        throws ServletException, IOException {
	    
	    // Get form parameters
	    String userName = request.getParameter("firstName");
	    String userEmail = request.getParameter("email");
	    String password = request.getParameter("password");
	    String retypePassword = request.getParameter("retypePassword");
	    String contactNumber = request.getParameter("phoneNumber");
	    String userType = request.getParameter("usertype");
	    
	    // Handle image upload (optional)
	    String image = request.getParameter("image");
	    if (image == null || image.trim().isEmpty()) {
	        image = null; // Will use DB default or allow null
	    }
	    
	    // Validate inputs
	    if (ValidationUtil.isNullOrEmpty(userName)) {
	        handleError(request, response, "Name is required");
	        return;
	    }
	    
	    if (ValidationUtil.isNullOrEmpty(userEmail) || !ValidationUtil.isValidEmail(userEmail)) {
	        handleError(request, response, "Valid email is required");
	        return;
	    }
	    
	    if (!ValidationUtil.doPasswordsMatch(password, retypePassword)) {
	        handleError(request, response, "Passwords do not match");
	        return;
	    }
	    
	    if (ValidationUtil.isNullOrEmpty(contactNumber)) {
	        handleError(request, response, "Contact number is required");
	        return;
	    }
	    
	    // Create user model
	    UsersModel user = new UsersModel();
	    user.setUser_name(userName);
	    user.setUser_email(userEmail);
	    user.setUser_password(password); // Will be encrypted in service layer
	    user.setContact_number(contactNumber);
	    user.setRole_id(Integer.parseInt(userType));
	    user.setImage(image);
	    
	    // Register user
	    CustomerService service = new CustomerService();
	    if (service.registerCustomer(user)) {
	        request.setAttribute("success", "Registration successful! Please login.");
	        response.sendRedirect(request.getContextPath() + "/login");
	    } else {
	        handleError(request, response, "Registration failed. Please try again.");
	    }
	}
		
	/**
	 * Helper method to handle validation errors.
	 * Sets error message and forwards back to registration page.
	 */
	private void handleError(HttpServletRequest req, HttpServletResponse resp, String message)
			throws ServletException, IOException {
		req.setAttribute("error", message);
		req.getRequestDispatcher("/WEB-INF/pages/register.jsp").forward(req, resp);
	}
		
	/**
	 * Registers a customer in the database.
	 * Encrypts the password and inserts user details into the database.
	 * Returns true if registration is successful, false otherwise.
	 */
	public boolean registerCustomer(UsersModel user) {
	    Connection dbConn = null; // Assume this is initialized elsewhere
	    if (dbConn == null) {
	        System.err.println("Database connection is not available.");
	        return false;
	    }

	    String query = "INSERT INTO users (user_name, user_email, user_password, contact_number, created_at, role_id, image) " +
	                   "VALUES (?, ?, ?, ?, NOW(), ?, ?)";

	    try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
	        stmt.setString(1, user.getUser_name());
	        stmt.setString(2, user.getUser_email());
	        stmt.setString(3, PasswordUtil.encrypt(user.getUser_email(), user.getUser_password()));
	        stmt.setString(4, user.getContact_number());
	        stmt.setInt(5, user.getRole_id());
	        stmt.setString(6, user.getImage() != null ? user.getImage() : "default.jpg");
	        int result = stmt.executeUpdate();
	        return result > 0;
	    } catch (SQLException e) {
	        System.err.println("Error during user registration: " + e.getMessage());
	        e.printStackTrace();
	        return false;
	    }
	}
}
