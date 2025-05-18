package com.AuroraStore.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.File;

import com.AuroraStore.model.UsersModel;
import com.AuroraStore.util.SessionUtil;

/**
 * Servlet implementation class UserPortfolioController
 * Handles displaying the user profile page with personal information
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/portfolio" })
public class UserPortfolioController extends HttpServlet {
    private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserPortfolioController() {
        super();
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
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // For now, just redirect to doGet
        doGet(request, response);
    }
}
