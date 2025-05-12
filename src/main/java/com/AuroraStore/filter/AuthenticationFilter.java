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
import java.io.IOException;

import com.AuroraStore.util.CookiesUtil;
import com.AuroraStore.util.SessionUtil;

@WebFilter(asyncSupported = true, urlPatterns = "/*")
public class AuthenticationFilter implements Filter {

    private static final String[] PUBLIC_PATHS = {"/login", "/register", "/welcome", "/", "/css/", "/resources/", "/aboutus", "/products"};
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

        // Check if user is logged in
        Object user = SessionUtil.getAttribute(req, "currentUser");
        String userRole = CookiesUtil.getCookie(req, "userRole") != null ? 
                         CookiesUtil.getCookie(req, "userRole").getValue() : null;

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // Handle admin access (role_id = 1)
        if ("1".equals(userRole)) {
            if (isCustomerOnlyPath(uri)) {
                // Redirect admin from customer-only pages
                resp.sendRedirect(req.getContextPath() + "/dashboard");
                return;
            }
            chain.doFilter(request, response);
        } 
        // Handle customer access (role_id = 2)
        else if ("2".equals(userRole)) {
            if (isAdminPath(uri)) {
                // Redirect customer from admin pages
                resp.sendRedirect(req.getContextPath() + "/portfolio");
                return;
            }
            chain.doFilter(request, response);
        } else {
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
