package com.AuroraStore.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import com.AuroraStore.model.ProductsModel;
import com.AuroraStore.service.ProductsService;

/**
 * Servlet implementation class ProductsController
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/products" })
public class ProductsController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ProductsService productsService;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProductsController() {
        super();
    }
    
    @Override
    public void init() throws ServletException {
        super.init();
        this.productsService = new ProductsService();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Get all active products
            List<ProductsModel> productsList = productsService.getAllActiveProducts();
            
            // Set data for the view
            request.setAttribute("productsList", productsList);
            
            // Forward to the products page
            request.getRequestDispatcher("/WEB-INF/pages/products.jsp").forward(request, response);
        } catch (Exception e) {
            System.err.println("Error in ProductsController.doGet: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/welcome");
        }
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
