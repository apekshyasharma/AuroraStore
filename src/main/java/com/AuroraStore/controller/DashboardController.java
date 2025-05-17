package com.AuroraStore.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

import com.AuroraStore.model.ProductsModel;
import com.AuroraStore.model.UsersModel;
import com.AuroraStore.service.DashboardService;
import com.AuroraStore.util.ValidationUtil;

/**
 * Servlet implementation class DashboardController
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/dashboard" })
public class DashboardController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private DashboardService dashboardService;
    
    /**
     * Constructor initializes the DashboardService
     */
    @Override
    public void init() throws ServletException {
        super.init();
        this.dashboardService = new DashboardService();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("DashboardController: doGet method called");
        
        // Check authentication - only admin should access
        HttpSession session = request.getSession(false);
        
        if (session != null && session.getAttribute("currentUser") != null) {
            UsersModel currentUser = (UsersModel) session.getAttribute("currentUser");
            System.out.println("Current user: " + currentUser.getUser_name() + ", Role: " + currentUser.getRole_id());
            
            if (currentUser.getRole_id() != 1) {
                // If not admin, redirect to welcome page
                System.out.println("Access denied: User is not an admin");
                session.setAttribute("error", "You don't have permission to access the admin area.");
                response.sendRedirect(request.getContextPath() + "/welcome");
                return;
            }
            
            // Set admin name for welcome message
            request.setAttribute("adminName", currentUser.getUser_name());
            
            try {
                // Get all users and products
                System.out.println("Fetching users from database...");
                List<UsersModel> usersList = dashboardService.getAllUsers();
                System.out.println("Retrieved " + usersList.size() + " users");
                
                System.out.println("Fetching products from database...");
                List<ProductsModel> productsList = dashboardService.getAllProducts();
                System.out.println("Retrieved " + productsList.size() + " products");
                
                // Get counts for statistics
                System.out.println("Fetching counts and totals...");
                int userCount = dashboardService.getUserCount();
                int productCount = dashboardService.getProductCount();
                double totalInventoryValue = dashboardService.getTotalInventoryValue();
                System.out.println("User count: " + userCount);
                System.out.println("Product count: " + productCount);
                System.out.println("Inventory value: " + totalInventoryValue);
                
                // Set attributes for JSP
                request.setAttribute("usersList", usersList);
                request.setAttribute("productsList", productsList);
                request.setAttribute("userCount", userCount);
                request.setAttribute("productCount", productCount);
                request.setAttribute("totalInventory", totalInventoryValue);
                
                // Forward to dashboard page
                System.out.println("Forwarding to dashboard.jsp");
                request.getRequestDispatcher("/WEB-INF/pages/dashboard.jsp").forward(request, response);
            } catch (Exception e) {
                System.err.println("Error in DashboardController: " + e.getMessage());
                e.printStackTrace();
                
                // Handle error case
                session.setAttribute("error", "Failed to load dashboard data. Please try again. Error: " + e.getMessage());
                response.sendRedirect(request.getContextPath() + "/welcome");
            }
        } else {
            // If not authenticated, redirect to login page
            System.out.println("Access denied: User not logged in");
            session = request.getSession();
            session.setAttribute("error", "You must be logged in to access the dashboard.");
            response.sendRedirect(request.getContextPath() + "/login");
        }
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("DashboardController: doPost method called");
        
        HttpSession session = request.getSession(false);
        
        // Check if the user is logged in and is an admin
        if (session == null || session.getAttribute("currentUser") == null) {
            System.out.println("Access denied: User not logged in");
            session = request.getSession();
            session.setAttribute("error", "You must be logged in to perform this action.");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        UsersModel currentUser = (UsersModel) session.getAttribute("currentUser");
        if (currentUser.getRole_id() != 1) {
            System.out.println("Access denied: User is not an admin");
            session.setAttribute("error", "You don't have permission to perform this action.");
            response.sendRedirect(request.getContextPath() + "/welcome");
            return;
        }
        
        // Get the action parameter
        String action = request.getParameter("action");
        
        if (action != null) {
            try {
                if (action.equals("edit")) {
                    // Handle edit user action
                    String userIdStr = request.getParameter("userId");
                    String email = request.getParameter("email");
                    String phone = request.getParameter("phone");
                    
                    // Validate input
                    if (ValidationUtil.isNullOrEmpty(userIdStr) || ValidationUtil.isNullOrEmpty(email) || ValidationUtil.isNullOrEmpty(phone)) {
                        session.setAttribute("error", "All fields are required.");
                        response.sendRedirect(request.getContextPath() + "/dashboard");
                        return;
                    }
                    
                    // Validate email format
                    if (!ValidationUtil.isValidEmail(email)) {
                        session.setAttribute("error", "Invalid email format.");
                        response.sendRedirect(request.getContextPath() + "/dashboard");
                        return;
                    }
                    
                    // Validate phone number format
                    if (!ValidationUtil.isNumericOnly(phone) || !ValidationUtil.isPhoneNumberLength10(phone)) {
                        session.setAttribute("error", "Phone number must be 10 digits.");
                        response.sendRedirect(request.getContextPath() + "/dashboard");
                        return;
                    }
                    
                    int userId = Integer.parseInt(userIdStr);
                    
                    // Get the user by ID
                    UsersModel user = dashboardService.getUserById(userId);
                    
                    // Check if the user exists and is an admin
                    if (user == null) {
                        session.setAttribute("error", "User not found.");
                        response.sendRedirect(request.getContextPath() + "/dashboard");
                        return;
                    }
                    
                    if (user.getRole_id() != 1) {
                        session.setAttribute("error", "Only admin users can be edited.");
                        response.sendRedirect(request.getContextPath() + "/dashboard");
                        return;
                    }
                    
                    // Check if email is already taken by another user
                    if (dashboardService.isEmailTaken(email, userId)) {
                        session.setAttribute("error", "Email address is already in use by another user.");
                        response.sendRedirect(request.getContextPath() + "/dashboard");
                        return;
                    }
                    
                    // Check if phone number is already taken by another user
                    if (dashboardService.isPhoneNumberTaken(phone, userId)) {
                        session.setAttribute("error", "Phone number is already in use by another user.");
                        response.sendRedirect(request.getContextPath() + "/dashboard");
                        return;
                    }
                    
                    // Update the user
                    boolean updated = dashboardService.updateUserDetails(userId, email, phone);
                    
                    if (updated) {
                        // If the edited user is the current user, update the session
                        if (userId == currentUser.getUser_id()) {
                            currentUser.setUser_email(email);
                            currentUser.setContact_number(phone);
                            session.setAttribute("currentUser", currentUser);
                        }
                        
                        session.setAttribute("success", "User details updated successfully.");
                    } else {
                        session.setAttribute("error", "Failed to update user details.");
                    }
                    
                } else if (action.equals("delete")) {
                    // Handle delete user action
                    String userIdStr = request.getParameter("userId");
                    
                    if (ValidationUtil.isNullOrEmpty(userIdStr)) {
                        session.setAttribute("error", "User ID is required.");
                        response.sendRedirect(request.getContextPath() + "/dashboard");
                        return;
                    }
                    
                    int userId = Integer.parseInt(userIdStr);
                    
                    // Prevent admin from deleting themselves
                    if (userId == currentUser.getUser_id()) {
                        session.setAttribute("error", "You cannot delete your own account.");
                        response.sendRedirect(request.getContextPath() + "/dashboard");
                        return;
                    }
                    
                    // Get the user by ID
                    UsersModel user = dashboardService.getUserById(userId);
                    
                    // Check if the user exists and is an admin
                    if (user == null) {
                        session.setAttribute("error", "User not found.");
                        response.sendRedirect(request.getContextPath() + "/dashboard");
                        return;
                    }
                    
                    if (user.getRole_id() != 1) {
                        session.setAttribute("error", "Only admin users can be deleted.");
                        response.sendRedirect(request.getContextPath() + "/dashboard");
                        return;
                    }
                    
                    // Delete the user
                    boolean deleted = dashboardService.deleteUser(userId);
                    
                    if (deleted) {
                        session.setAttribute("success", "User deleted successfully.");
                    } else {
                        session.setAttribute("error", "Failed to delete user.");
                    }
                } else if (action.equals("deleteProduct")) {
                    // Handle delete product action
                    String productIdStr = request.getParameter("productId");
                    
                    if (ValidationUtil.isNullOrEmpty(productIdStr)) {
                        session.setAttribute("error", "Product ID is required.");
                        response.sendRedirect(request.getContextPath() + "/dashboard");
                        return;
                    }
                    
                    int productId = Integer.parseInt(productIdStr);
                    
                    // Delete the product
                    boolean deleted = dashboardService.deleteProduct(productId);
                    
                    if (deleted) {
                        session.setAttribute("success", "Product deleted successfully.");
                    } else {
                        session.setAttribute("error", "Failed to delete product.");
                    }
                }
            } catch (NumberFormatException e) {
                System.err.println("Invalid number format: " + e.getMessage());
                session.setAttribute("error", "Invalid ID format.");
            } catch (Exception e) {
                System.err.println("Error processing request: " + e.getMessage());
                e.printStackTrace();
                session.setAttribute("error", "An error occurred: " + e.getMessage());
            }
        }
        
        // Redirect back to the dashboard
        response.sendRedirect(request.getContextPath() + "/dashboard");
    }
}
