package com.AuroraStore.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

import com.AuroraStore.model.UsersModel;
import com.AuroraStore.util.CookiesUtil;
import com.AuroraStore.util.SessionUtil;

@WebFilter(asyncSupported = true, urlPatterns = "/*")
public class AuthenticationFilter implements Filter {

    private static final String[] PUBLIC_PATHS = {"/login", "/register", "/welcome", "/", "/css/", "/resources/", "/aboutus", "/products", "/js/"};
    private static final String[] ADMIN_PATHS = {"/dashboard", "/manage-users", "/manage-products"};
    private static final String[] CUSTOMER_PATHS = {"/portfolio"};
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
            
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String uri = req.getRequestURI().substring(req.getContextPath().length());

        // Allow access to public resources
        if (isPublicResource(uri)) {
            chain.doFilter(request, response);
            return;
        }

        // Check if user is logged in - check both session and cookies
        HttpSession session = req.getSession(false);
        UsersModel user = null;
        int userRoleId = -1;
        
        // First try to get user from session
        if (session != null && session.getAttribute("currentUser") != null) {
            user = (UsersModel) session.getAttribute("currentUser");
            userRoleId = user.getRole_id();
        }
        
        // If not in session, check for cookies as fallback
        if (user == null) {
            String userRole = CookiesUtil.getCookie(req, "userRole") != null ? 
                             CookiesUtil.getCookie(req, "userRole").getValue() : null;
                             
            if (userRole != null && !userRole.isEmpty()) {
                try {
                    userRoleId = Integer.parseInt(userRole);
                } catch (NumberFormatException e) {
                    userRoleId = -1;
                }
            }
        }

        // If no valid user found in session or cookies, redirect to login
        if (userRoleId == -1) {
            // Store the requested URI to redirect back after login
            if (session == null) {
                session = req.getSession(true);
            }
            session.setAttribute("requestedUri", uri);
            session.setAttribute("error", "You must log in to access this page.");
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // Handle admin access (role_id = 1)
        if (userRoleId == 1) {
            if (isCustomerOnlyPath(uri)) {
                // Redirect admin from customer-only pages
                resp.sendRedirect(req.getContextPath() + "/dashboard");
                return;
            }
            chain.doFilter(request, response);
        } 
        // Handle customer access (role_id = 2)
        else if (userRoleId == 2) {
            if (isAdminPath(uri)) {
                // Redirect customer from admin pages with error message
                if (session == null) {
                    session = req.getSession(true);
                }
                session.setAttribute("error", "You don't have permission to access the admin area.");
                resp.sendRedirect(req.getContextPath() + "/portfolio");
                return;
            }
            chain.doFilter(request, response);
        } else {
            // Invalid role - redirect to login
            if (session == null) {
                session = req.getSession(true);
            }
            session.setAttribute("error", "You don't have proper permissions. Please log in again.");
            resp.sendRedirect(req.getContextPath() + "/login");
        }
    }

    private boolean isPublicResource(String uri) {
        for (String path : PUBLIC_PATHS) {
            if (uri.startsWith(path)) return true;
        }
        return false;
    }

    private boolean isAdminPath(String uri) {
        for (String path : ADMIN_PATHS) {
            if (uri.startsWith(path)) return true;
        }
        return false;
    }

    private boolean isCustomerOnlyPath(String uri) {
        for (String path : CUSTOMER_PATHS) {
            if (uri.startsWith(path)) return true;
        }
        return false;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}
}
