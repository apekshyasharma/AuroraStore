package com.AuroraStore.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.AuroraStore.util.CookiesUtil;
import com.AuroraStore.util.SessionUtil;

@WebServlet(asyncSupported = true, urlPatterns = { "/logout" })
public class LogoutController extends HttpServlet {
    private static final long serialVersionUID = 1L;
       
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Delete all cookies
        CookiesUtil.deleteCookie(response, "userRole");
        CookiesUtil.deleteCookie(response, "userEmail");
        
        // Invalidate the session
        SessionUtil.invalidateSession(request);
        
        // Add success message
        request.getSession().setAttribute("success", "Successfully logged out!");
        
        // Redirect to welcome page
        response.sendRedirect(request.getContextPath() + "/welcome");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
}
