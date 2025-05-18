package com.AuroraStore.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.io.File;

import com.AuroraStore.model.UsersModel;
import com.AuroraStore.service.UserPortfolioService;
import com.AuroraStore.util.ImagesUtil;
import com.AuroraStore.util.SessionUtil;
import com.AuroraStore.util.ValidationUtil;

/**
 * Servlet implementation class UserPortfolioController
 * Handles displaying and updating the user profile
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/portfolio" })
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2,  // 2MB
    maxFileSize = 1024 * 1024 * 10,       // 10MB
    maxRequestSize = 1024 * 1024 * 50)    // 50MB
public class UserPortfolioController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserPortfolioService portfolioService;
    private ImagesUtil imagesUtil;
       
    /**
     * Initialize services
     */
    public UserPortfolioController() {
        super();
        portfolioService = new UserPortfolioService();
        imagesUtil = new ImagesUtil();
    }

    /**
     * Handles GET requests to display the user portfolio/profile page.
     * Validates user is logged in and retrieves their information from session.
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        // Check if user is logged in
        if (session == null || session.getAttribute("currentUser") == null) {
            // Set error message and redirect to login
            session = request.getSession();
            session.setAttribute("error", "Please login to view your profile");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // Get the current user from session
        UsersModel user = (UsersModel) session.getAttribute("currentUser");
        
        // Verify the user has role_id = 2 (Customer)
        if (user.getRole_id() != 2) {
            response.sendRedirect(request.getContextPath() + "/welcome");
            return;
        }
        
        // Prepare the image path
        String imagePath = "";
        if (user.getImage() != null && !user.getImage().isEmpty()) {
            imagePath = "resources/images/users/" + user.getImage();
            
            // Verify the image exists on disk
            String realPath = getServletContext().getRealPath("/");
            File imageFile = new File(realPath + imagePath);
            
            if (!imageFile.exists()) {
                // If image file doesn't exist, use default
                imagePath = "resources/images/system/default-profile.png";
            }
        } else {
            // No image in database, use default
            imagePath = "resources/images/system/default-profile.png";
        }
        
        // Set attributes for JSP
        request.setAttribute("user", user);
        request.setAttribute("imagePath", imagePath);
        
        // Log debugging information
        System.out.println("UserPortfolioController: Forwarding to portfolio.jsp");
        System.out.println("User data: name=" + user.getUser_name() + ", email=" + user.getUser_email());
        System.out.println("Image path: " + imagePath);
        
        // Forward to the portfolio.jsp
        request.getRequestDispatcher("/WEB-INF/pages/portfolio.jsp").forward(request, response);
    }

    /**
     * Handles POST requests for updating user profile information and/or image
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("currentUser") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // Get current user from session
        UsersModel currentUser = (UsersModel) session.getAttribute("currentUser");
        
        // Determine action type
        String action = request.getParameter("action");
        
        if ("updateInfo".equals(action)) {
            // Handle updating user information
            handleUserInfoUpdate(request, response, currentUser, session);
        } else if ("updateImage".equals(action)) {
            // Handle updating profile image
            handleProfileImageUpdate(request, response, currentUser, session);
        } else {
            // Unknown action, redirect back to profile
            response.sendRedirect(request.getContextPath() + "/portfolio");
        }
    }
    
    /**
     * Handle updating user information
     */
    private void handleUserInfoUpdate(HttpServletRequest request, HttpServletResponse response, 
            UsersModel currentUser, HttpSession session) throws IOException {
        
        // Get form parameters
        String name = request.getParameter("user_name");
        String email = request.getParameter("user_email");
        String contactNumber = request.getParameter("contact_number");
        
        // Validate input
        boolean hasError = false;
        
        if (ValidationUtil.isNullOrEmpty(name)) {
            session.setAttribute("error", "Name cannot be empty");
            hasError = true;
        } else if (!ValidationUtil.isAlphabetic(name)) {
            session.setAttribute("error", "Name should contain only letters");
            hasError = true;
        }
        
        if (ValidationUtil.isNullOrEmpty(email)) {
            session.setAttribute("error", "Email cannot be empty");
            hasError = true;
        } else if (!ValidationUtil.isValidEmail(email)) {
            session.setAttribute("error", "Invalid email format");
            hasError = true;
        }
        
        if (ValidationUtil.isNullOrEmpty(contactNumber)) {
            session.setAttribute("error", "Contact number cannot be empty");
            hasError = true;
        } else if (!ValidationUtil.isValidPhoneNumber(contactNumber)) {
            session.setAttribute("error", "Contact number must be 10 digits starting with 98");
            hasError = true;
        }
        
        if (hasError) {
            response.sendRedirect(request.getContextPath() + "/portfolio");
            return;
        }
        
        // Update user model
        currentUser.setUser_name(name);
        currentUser.setUser_email(email);
        currentUser.setContact_number(contactNumber);
        
        // Update in database
        boolean updated = portfolioService.updateUserInfo(currentUser);
        
        if (updated) {
            // Update session with new user information
            session.setAttribute("currentUser", currentUser);
            session.setAttribute("success", "Profile information updated successfully");
        } else {
            session.setAttribute("error", "Failed to update profile information");
        }
        
        response.sendRedirect(request.getContextPath() + "/portfolio");
    }
    
    /**
     * Handle updating user profile image
     */
    private void handleProfileImageUpdate(HttpServletRequest request, HttpServletResponse response, 
            UsersModel currentUser, HttpSession session) throws IOException, ServletException {
        
        // Get the uploaded file
        Part filePart = request.getPart("profile_image");
        
        if (filePart == null || filePart.getSize() <= 0) {
            session.setAttribute("error", "Please select an image to upload");
            response.sendRedirect(request.getContextPath() + "/portfolio");
            return;
        }
        
        // Check file type
        String fileName = imagesUtil.getImageNameFromPart(filePart);
        if (fileName == null) {
            session.setAttribute("error", "Invalid file format. Only images are allowed.");
            response.sendRedirect(request.getContextPath() + "/portfolio");
            return;
        }
        
        // Get real path to the web application
        String realPath = getServletContext().getRealPath("/");
        
        // Delete old image if exists
        portfolioService.deleteOldImage(currentUser.getImage(), realPath);
        
        // Generate unique image name (using timestamp)
        String uniqueImageName = System.currentTimeMillis() + "_" + fileName;
        
        // Create directory if not exists
        String uploadPath = realPath + "resources/images/users/";
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        
        // Save image to disk
        filePart.write(uploadPath + uniqueImageName);
        
        // Update image in database
        boolean updated = portfolioService.updateUserImage(currentUser.getUser_id(), uniqueImageName);
        
        if (updated) {
            // Update user model in session
            currentUser.setImage(uniqueImageName);
            session.setAttribute("currentUser", currentUser);
            session.setAttribute("success", "Profile image updated successfully");
        } else {
            // If database update fails, delete the uploaded image
            File uploadedFile = new File(uploadPath + uniqueImageName);
            if (uploadedFile.exists()) {
                uploadedFile.delete();
            }
            session.setAttribute("error", "Failed to update profile image");
        }
        
        response.sendRedirect(request.getContextPath() + "/portfolio");
    }
}
