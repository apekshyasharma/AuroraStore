package com.AuroraStore.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

import com.AuroraStore.model.UsersModel;
import com.AuroraStore.service.CustomerService;
import com.AuroraStore.util.ImagesUtil;
import com.AuroraStore.util.PasswordUtil;
import com.AuroraStore.util.ValidationUtil;

/**
 * Servlet implementation class RegisterController
 * Handles user registration requests (GET and POST).
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/register" })
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2,  // 2MB
    maxFileSize = 1024 * 1024 * 10,       // 10MB
    maxRequestSize = 1024 * 1024 * 50     // 50MB
)
public class RegisterController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final ImagesUtil imageUtil = new ImagesUtil();
       
    /**
     * Handles GET request to show the registration page.
     * Forwards the request to the JSP view.
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("WEB-INF/pages/register.jsp").forward(request, response);
    }

    /**
     * Handles POST request for user registration.
     * Validates input fields, encrypts password, creates a user model,
     * and invokes the service to register the user.
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
    
        // First, clear any existing logout messages
        request.getSession().removeAttribute("success");

        // Handle image upload using Part
        Part imagePart = request.getPart("image");
        String imagePath = null;
        
        if (imagePart != null && imagePart.getSize() > 0) {
            if (!ValidationUtil.isValidImageExtension(imagePart)) {
                handleError(request, response, "Invalid image format. Only jpg, jpeg, png, and gif allowed.");
                return;
            }
            imagePath = imageUtil.getImageNameFromPart(imagePart);
        }
    
        // Get form parameters
        String userName = request.getParameter("firstName");
        String userEmail = request.getParameter("email");
        String password = request.getParameter("password");
        String retypePassword = request.getParameter("retypePassword");
        String contactNumber = request.getParameter("phoneNumber");
        String userType = request.getParameter("usertype");
        
        // Validate inputs
        if (ValidationUtil.isNullOrEmpty(userName)) {
            handleError(request, response, "Name is required");
            return;
        }
        
        if (ValidationUtil.isNullOrEmpty(userEmail) || !ValidationUtil.isValidEmail(userEmail)) {
            handleError(request, response, "Please enter a valid email address.");
            return;
        }
        
        if (!ValidationUtil.doPasswordsMatch(password, retypePassword)) {
            handleError(request, response, "Passwords do not match");
            return;
        }

        if (!ValidationUtil.hasMinimumLength(password)) {
            handleError(request, response, "Please enter password with minimum 8 characters.");
            return;
        }

        if (!ValidationUtil.hasSpecialCharacter(password)) {
            handleError(request, response, "Password must contain atleast one character like (@,!,#,$,%,&).");
            return;
        }

        if (!ValidationUtil.hasUpperCase(password)) {
            handleError(request, response, "Password must contain atleast one Uppercase letter.");
            return;
        }
        
        if (ValidationUtil.isNullOrEmpty(contactNumber)) {
            handleError(request, response, "Contact number is required");
            return;
        }
        
        // Add phone number validation checks
        if (!ValidationUtil.isNumericOnly(contactNumber)) {
            handleError(request, response, "Please do not provide non numeric characters in the phone number textfield.");
            return;
        }
        
        if (!ValidationUtil.isPhoneNumberLength10(contactNumber)) {
            handleError(request, response, "Contact number is expected to be of 10 digits.");
            return;
        }
        
        // Create CustomerService instance
        CustomerService service = new CustomerService();
        
        // Check if email already exists
        if (service.emailExists(userEmail)) {
            handleError(request, response, "Email address already registered. Please use a different email or login to your existing account.");
            return;
        }
        
        // Create user model
        UsersModel user = new UsersModel();
        user.setUser_name(userName);
        user.setUser_email(userEmail);
        user.setUser_password(password);
        user.setContact_number(contactNumber);
        user.setRole_id(Integer.parseInt(userType));
        user.setImage(imagePath);
        
        // Register user
        if (service.registerCustomer(user)) {
            // Upload image if provided
            if (imagePath != null) {
                if (!imageUtil.uploadImage(imagePart, request.getServletContext().getRealPath("/"))) {
                    handleError(request, response, "Failed to upload image");
                    return;
                }
            }
            
            // Then set our registration success message with a different attribute name
            request.getSession().setAttribute("regSuccess", "Successful Registration");
            
            // Redirect to login page
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
        // Retain form values on error
        req.setAttribute("firstName", req.getParameter("firstName"));
        req.setAttribute("email", req.getParameter("email"));
        req.setAttribute("phoneNumber", req.getParameter("phoneNumber"));
        req.setAttribute("usertype", req.getParameter("usertype"));
        req.getRequestDispatcher("/WEB-INF/pages/register.jsp").forward(req, resp);
    }
}
