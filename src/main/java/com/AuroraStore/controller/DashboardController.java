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
        doGet(request, response);
    }
}
