package com.AuroraStore.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.util.List;

import com.AuroraStore.model.ProductsModel;
import com.AuroraStore.model.BrandsModel;
import com.AuroraStore.model.CategoriesModel;
import com.AuroraStore.model.UsersModel;
import com.AuroraStore.service.AddProductsService;
import com.AuroraStore.util.ValidationUtil;
import com.AuroraStore.util.ImagesUtil;

/**
 * Servlet implementation class AddProducts
 * Handles adding new products to the database
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/addproducts" })
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024, // 1 MB
    maxFileSize = 1024 * 1024 * 5,   // 5 MB
    maxRequestSize = 1024 * 1024 * 10 // 10 MB
)
public class AddProductsController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private AddProductsService addProductsService;
    private ImagesUtil imageUtil;
    
    @Override
    public void init() throws ServletException {
        super.init();
        this.addProductsService = new AddProductsService();
        this.imageUtil = new ImagesUtil();
    }
       
    /**
     * Displays the form for adding a new product
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("AddProductsController: doGet method called");
        
        // Check if user is admin
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("currentUser") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        UsersModel currentUser = (UsersModel) session.getAttribute("currentUser");
        if (currentUser.getRole_id() != 1) {
            session.setAttribute("error", "You don't have permission to access this page");
            response.sendRedirect(request.getContextPath() + "/welcome");
            return;
        }
        
        try {
            // Fetch all categories and brands for the form dropdowns
            List<CategoriesModel> categories = addProductsService.getAllCategories();
            List<BrandsModel> brands = addProductsService.getAllBrands();
            
            // Set data for the view
            request.setAttribute("categories", categories);
            request.setAttribute("brands", brands);
            
            // Forward to the add products page
            request.getRequestDispatcher("/WEB-INF/pages/addProducts.jsp").forward(request, response);
        } catch (Exception e) {
            System.err.println("Error in AddProductsController.doGet: " + e.getMessage());
            e.printStackTrace();
            session.setAttribute("error", "Failed to load form data: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/dashboard");
        }
    }

    /**
     * Processes the form submission to add a new product
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("AddProductsController: doPost method called");
        
        HttpSession session = request.getSession();
        
        // Check if user is admin
        if (session.getAttribute("currentUser") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        UsersModel currentUser = (UsersModel) session.getAttribute("currentUser");
        if (currentUser.getRole_id() != 1) {
            response.sendRedirect(request.getContextPath() + "/welcome");
            return;
        }
        
        try {
            // Get form parameters
            String productName = request.getParameter("productName");
            String priceStr = request.getParameter("productPrice");
            String quantityStr = request.getParameter("productQuantity");
            String productDescription = request.getParameter("productDescription");
            String productStatus = request.getParameter("productStatus");
            String categoryIdStr = request.getParameter("categoryId");
            String brandIdStr = request.getParameter("brandId");
            
            // Set form values as request attributes for form repopulation in case of error
            request.setAttribute("productName", productName);
            request.setAttribute("productPrice", priceStr);
            request.setAttribute("productQuantity", quantityStr);
            request.setAttribute("productDescription", productDescription);
            request.setAttribute("productStatus", productStatus);
            request.setAttribute("selectedCategoryId", categoryIdStr);
            request.setAttribute("selectedBrandId", brandIdStr);
            
            // Validate required fields
            if (ValidationUtil.isNullOrEmpty(productName) || 
                ValidationUtil.isNullOrEmpty(priceStr) || 
                ValidationUtil.isNullOrEmpty(quantityStr) || 
                ValidationUtil.isNullOrEmpty(productDescription) || 
                ValidationUtil.isNullOrEmpty(productStatus) || 
                ValidationUtil.isNullOrEmpty(categoryIdStr) || 
                ValidationUtil.isNullOrEmpty(brandIdStr)) {
                
                handleError(request, response, "All fields are required");
                return;
            }
            
            // Parse numeric values
            double price;
            int quantity, categoryId, brandId;
            
            try {
                price = Double.parseDouble(priceStr);
                quantity = Integer.parseInt(quantityStr);
                categoryId = Integer.parseInt(categoryIdStr);
                brandId = Integer.parseInt(brandIdStr);
                
                if (price < 0 || quantity < 1) {
                    handleError(request, response, "Price and quantity must be positive values");
                    return;
                }
            } catch (NumberFormatException e) {
                handleError(request, response, "Invalid number format");
                return;
            }
            
            // Handle file upload
            Part filePart = request.getPart("productImage");
            String imageName = null;
            
            if (filePart != null && filePart.getSize() > 0) {
                System.out.println("File part size: " + filePart.getSize());
                if (!ValidationUtil.isValidImageExtension(filePart)) {
                    handleError(request, response, "Invalid image format. Only jpg, jpeg, png, and gif allowed.");
                    return;
                }
                
                // Get filename from the Part
                imageName = imageUtil.getImageNameFromPart(filePart);
                System.out.println("Image name extracted: " + imageName);
            }
            
            // Create product object
            ProductsModel product = new ProductsModel();
            product.setProduct_name(productName);
            product.setProduct_price(price);
            product.setProduct_quantity(quantity);
            product.setProduct_description(productDescription);
            product.setProduct_status(productStatus);
            product.setCategory_id(categoryId);
            product.setBrand_id(brandId);
            
            // Add product to database
            System.out.println("Adding product to database: " + product.getProduct_name());
            int productId = addProductsService.addProduct(product, imageName);
            System.out.println("Product ID returned: " + productId);
            
            if (productId > 0) {
                // If file exists, save it
                if (filePart != null && filePart.getSize() > 0) {
                    System.out.println("Uploading image for product ID: " + productId);
                    boolean uploadSuccess = imageUtil.uploadProductImage(filePart, request.getServletContext().getRealPath("/"));
                    System.out.println("Image upload success: " + uploadSuccess);
                    
                    if (!uploadSuccess) {
                        // Continue even if image upload fails - product is already in DB
                        System.err.println("Image upload failed, but product was added to database");
                    }
                }
                
                // Set success message and redirect
                session.setAttribute("success", "Product added successfully!");
                response.sendRedirect(request.getContextPath() + "/dashboard");
            } else {
                System.out.println("Product ID is negative or zero: " + productId);
                handleError(request, response, "Failed to add product to database. Check database connection.");
            }
        } catch (Exception e) {
            System.err.println("Error in AddProductsController.doPost: " + e.getMessage());
            e.printStackTrace();
            handleError(request, response, "An unexpected error occurred: " + e.getMessage());
        }
    }
    
    /**
     * Helper method to handle errors
     */
    private void handleError(HttpServletRequest request, HttpServletResponse response, String errorMessage) 
            throws ServletException, IOException {
        
        // Re-fetch categories and brands for the form
        List<CategoriesModel> categories = addProductsService.getAllCategories();
        List<BrandsModel> brands = addProductsService.getAllBrands();
        
        // Set attributes
        request.setAttribute("categories", categories);
        request.setAttribute("brands", brands);
        request.setAttribute("error", errorMessage);
        
        // Preserve form values
        request.setAttribute("productName", request.getParameter("productName"));
        request.setAttribute("productPrice", request.getParameter("productPrice"));
        request.setAttribute("productQuantity", request.getParameter("productQuantity"));
        request.setAttribute("productDescription", request.getParameter("productDescription"));
        request.setAttribute("productStatus", request.getParameter("productStatus"));
        request.setAttribute("selectedCategoryId", request.getParameter("categoryId"));
        request.setAttribute("selectedBrandId", request.getParameter("brandId"));
        
        // Forward back to the form
        request.getRequestDispatcher("/WEB-INF/pages/addProducts.jsp").forward(request, response);
    }
}
