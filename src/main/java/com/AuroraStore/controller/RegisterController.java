package com.AuroraStore.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;

import com.AuroraStore.model.UsersModel;
import com.AuroraStore.service.CustomerService;
import com.AuroraStore.util.PasswordUtil;
import com.AuroraStore.util.ValidationUtil;

/**
 * Servlet implementation class RegisterController
 * Handles user registration requests (GET and POST).
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/register" })
public class RegisterController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	/**
	 * Handles GET request to show the registration page.
	 * Forwards the request to the JSP view.
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.getRequestDispatcher("WEB-INF/pages/register.jsp").forward(request, response);
	}

	/**
	 * Handles POST request foruser registration.
	 * Validates input fields, encrypts password, creates a user model,
	 * and invokes the service to register the user.
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		
		String user_name = request.getParameter("firstName");
		String user_email=request.getParameter("email");
		String user_password=PasswordUtil.encrypt(user_email,   request.getParameter("password"));
		String contact_number = request.getParameter("phoneNumber");
		String created_at=LocalDate.now().toString();
		int role_id =Integer.parseInt(request.getParameter("usertype"));
		
		
		if(ValidationUtil.isNullOrEmpty(user_name)) {
			handleError(request, response, "Fill the Name field");
			return ;
		}
		if(ValidationUtil.isNullOrEmpty(user_email)) {
			handleError(request, response, "Fill the email field");
			return ;
		}
		if(ValidationUtil.isNullOrEmpty(user_password)) {
			handleError(request, response, "Fill the password field");
			return ;
		}
		if(ValidationUtil.isNullOrEmpty(contact_number)) {
			handleError(request, response, "Fill the phone number field");
			return ;
		}
		if(ValidationUtil.isNullOrEmpty(request.getParameter("usertype"))) {
			handleError(request, response, "Fill the user Type field");
			return ;
		}
		if (!ValidationUtil.doPasswordsMatch(request.getParameter("password"), request.getParameter("retypePassword"))){
			handleError(request, response,"Passwords do not match.");
			return ;
		}
		// Create user model with validated and processed data
		UsersModel user = new UsersModel(user_name, user_email, user_password, 
                contact_number, created_at, role_id);
		// Register the user through service class
		CustomerService service = new CustomerService();
		if(service.registerCustomer(user)) {
			request.setAttribute("success", "Sucessfully registered");
			request.getRequestDispatcher("/WEB-INF/pages/register.jsp").forward(request, response);	
		}
		}
		
	/**
	 * Helper method to handle validation errors.
	 * Sets error message and forwards back to registration page.
	 */
	private void handleError(HttpServletRequest req, HttpServletResponse resp, String message)
			throws ServletException, IOException {
		req.setAttribute("error", message);
		req.getRequestDispatcher("/WEB-INF/pages/register.jsp").forward(req, resp);
	}
		
}
