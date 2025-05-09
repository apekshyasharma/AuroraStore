package com.AuroraStore.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import com.AuroraStore.model.UsersModel;
import com.AuroraStore.service.CustomerService;

/**
 * Servlet implementation class LoginController
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/login" })
public class LoginController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.getRequestDispatcher("WEB-INF/pages/login.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
		String email = request.getParameter("email");
		String password =request.getParameter("password");

		// 2. Validate (Optional: can be expanded later)
		if (email == null || password.isEmpty()) {
		    
			// Redirect back if any field missing
			request.setAttribute("error", "Please fill all fields.");
			request.getRequestDispatcher("/WEB-INF/pages/login.jsp").forward(request, response);
			return;
		}

		
		CustomerService userServices = new CustomerService();
		UsersModel user = userServices.login(email,password);
	
		if (user == null) {
			HttpSession errorSession = request.getSession();
			errorSession.setAttribute("invalidCustomer", "Invalid Email ");
			request.setAttribute("error", "User not found please check credentials");
			request.getRequestDispatcher("/WEB-INF/pages/login.jsp").forward(request, response);

		} else if (user != null) {
		    HttpSession session = request.getSession();
		    session.setAttribute("currentUser", user);
		    request.setAttribute("success", "Login Success");

		    // Create a cookie for the user role
		    Cookie roleCookie = new Cookie("userRole", String.valueOf(user.getRole_id()));
		    roleCookie.setMaxAge(60 * 60 * 24 * 7); // 7 days
		    roleCookie.setPath("/"); // accessible across the whole app
		    response.addCookie(roleCookie);

		    // Forward to homepage or dashboard
		    request.getRequestDispatcher("/WEB-INF/pages/home.jsp").forward(request, response);
	}
		
		//Page redirection acc to role
		int roleId = user.getRole_id();
        if (roleId == 1) {
            // Admin
            request.getRequestDispatcher("/WEB-INF/pages/adminDashboard.jsp").forward(request, response);
        } else if (roleId == 2) {
            // Customer
            request.getRequestDispatcher("/WEB-INF/pages/portfolio.jsp").forward(request, response);
        } else {
            // Default or unknown role
            request.getRequestDispatcher("/WEB-INF/pages/welcome.jsp").forward(request, response);
        }

}

	
	}


