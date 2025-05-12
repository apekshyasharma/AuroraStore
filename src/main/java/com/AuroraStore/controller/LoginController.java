package com.AuroraStore.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

import com.AuroraStore.model.UsersModel;
import com.AuroraStore.service.LoginService;
import com.AuroraStore.util.CookiesUtil;
import com.AuroraStore.util.SessionUtil;

@WebServlet(asyncSupported = true, urlPatterns = { "/login" })
public class LoginController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final LoginService loginService;

    public LoginController() {
        this.loginService = new LoginService();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/pages/login.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String remember = request.getParameter("remember");
        
        // Input validation
        if (email == null || email.trim().isEmpty() || 
            password == null || password.trim().isEmpty()) {
            handleError(request, response, "Please fill all fields");
            return;
        }

        // Attempt login
        UsersModel user = loginService.loginUser(email, password);

        if (user != null) {
            // Set session attributes
            HttpSession session = request.getSession(true);
            session.setAttribute("currentUser", user);
            session.setAttribute("userRole", String.valueOf(user.getRole_id()));
            
            // Set cookie expiry based on remember me checkbox
            int cookieAge = "on".equals(remember) ? 60 * 60 * 24 * 30 : -1; // 30 days or session only
            CookiesUtil.addCookie(response, "userRole", String.valueOf(user.getRole_id()), cookieAge);
            CookiesUtil.addCookie(response, "userEmail", user.getUser_email(), cookieAge);

            // Add success message to session
            session.setAttribute("success", "Login Successful!");

            // Redirect based on role
            String contextPath = request.getContextPath();
            if (user.getRole_id() == 1) { // Admin
                response.sendRedirect(contextPath + "/dashboard");
            } else if (user.getRole_id() == 2) { // Customer 
                response.sendRedirect(contextPath + "/portfolio");
            } else {
                response.sendRedirect(contextPath + "/welcome");
            }
        } else {
            handleError(request, response, "User not found. Please register your account first.");
        }
    }

    private void handleError(HttpServletRequest request, HttpServletResponse response, String message) 
            throws ServletException, IOException {
        request.setAttribute("error", message);
        request.getRequestDispatcher("/WEB-INF/pages/login.jsp").forward(request, response);
    }
}


