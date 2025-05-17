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
            String categoryName = request.getParameter("category");
            List<ProductsModel> productsList;

            // Debug statement to check what category is being received
            System.out.println("Requested category: " + categoryName);

            if (categoryName != null && !categoryName.trim().isEmpty()) {
                // URL decoding to handle spaces properly
                categoryName = java.net.URLDecoder.decode(categoryName, "UTF-8");
                productsList = productsService.getProductsByCategory(categoryName);
                
                // Debug statement to check products count
                System.out.println("Found " + productsList.size() + " products for category: " + categoryName);
            } else {
                productsList = productsService.getAllActiveProducts();
            }
            
            request.setAttribute("productsList", productsList);
            request.setAttribute("selectedCategory", categoryName); // To highlight active category
            
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
