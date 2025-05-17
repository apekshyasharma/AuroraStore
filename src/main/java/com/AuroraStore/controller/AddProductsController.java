package com.AuroraStore.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Collection;

import com.AuroraStore.model.ProductsModel;
import com.AuroraStore.model.BrandsModel;
import com.AuroraStore.model.CategoriesModel;
import com.AuroraStore.model.UsersModel;
import com.AuroraStore.service.AddProductsService;
import com.AuroraStore.util.ValidationUtil;
import com.AuroraStore.util.ImagesUtil;

@WebServlet(asyncSupported = true, urlPatterns = { "/addproducts" })
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024,    // 1 MB
    maxFileSize = 1024 * 1024 * 10,     // 10 MB
    maxRequestSize = 1024 * 1024 * 15   // 15 MB
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
       
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("currentUser") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        UsersModel currentUser = (UsersModel) session.getAttribute("currentUser");
        if (currentUser.getRole_id() != 1) { // Assuming role_id 1 is Admin
            response.sendRedirect(request.getContextPath() + "/welcome");
            return;
        }

        List<CategoriesModel> categories = addProductsService.getAllCategories();
        List<BrandsModel> brands = addProductsService.getAllBrands();
        
        request.setAttribute("categories", categories);
        request.setAttribute("brands", brands);
        
        System.out.println("AddProductsController: doGet method called, forwarding to addProducts.jsp");
        request.getRequestDispatcher("/WEB-INF/pages/addProducts.jsp").forward(request, response);
    }

    private String getStringValueFromPart(Part part) throws IOException {
        if (part == null) {
            System.out.println("getStringValueFromPart: part is null");
            return null;
        }
        String submittedFileName = part.getSubmittedFileName();
        // A part is a form field if its submittedFileName is null or empty.
        if (submittedFileName == null || submittedFileName.trim().isEmpty()) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(part.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder value = new StringBuilder();
                char[] buffer = new char[1024];
                int length;
                while ((length = reader.read(buffer)) > 0) {
                    value.append(buffer, 0, length);
                }
                String result = value.toString().trim();
                System.out.println("getStringValueFromPart: Part Name='" + part.getName() + "', Value='" + result + "'");
                return result;
            }
        }
        System.out.println("getStringValueFromPart: Part Name='" + part.getName() + "' is a file part (filename: " + submittedFileName + ")");
        return null; 
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("AddProductsController: doPost method called");
        System.out.println("Content Type: " + request.getContentType());
        
        System.out.println("Request Parts Details (Iterating all parts):");
        try {
            Collection<Part> allParts = request.getParts();
            if (allParts != null && !allParts.isEmpty()) {
                for (Part p : allParts) {
                    System.out.println("  Part Name: " + p.getName() + 
                                       ", Submitted FileName: " + p.getSubmittedFileName() +
                                       ", Size: " + p.getSize() +
                                       ", ContentType: " + p.getContentType());
                }
            } else {
                System.out.println("  No parts found in the request via getParts().");
            }
        } catch (Exception e) {
            System.err.println("  Error iterating request.getParts(): " + e.getMessage());
            // e.printStackTrace(); // Potentially too verbose for regular log
        }
        
        HttpSession session = request.getSession(false);
        
        if (session == null || session.getAttribute("currentUser") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        UsersModel currentUser = (UsersModel) session.getAttribute("currentUser");
        if (currentUser.getRole_id() != 1) {
            response.sendRedirect(request.getContextPath() + "/welcome");
            return;
        }
        
        String productName = null;
        String priceStr = null;
        String quantityStr = null;
        String productDescription = null;
        String productStatus = null;
        String categoryIdStr = null;
        String brandIdStr = null;
        Part filePart = null;

        try {
            System.out.println("Attempting to get parts by name:");
            productName = getStringValueFromPart(request.getPart("productName"));
            priceStr = getStringValueFromPart(request.getPart("productPrice"));
            quantityStr = getStringValueFromPart(request.getPart("productQuantity"));
            productDescription = getStringValueFromPart(request.getPart("productDescription"));
            productStatus = getStringValueFromPart(request.getPart("productStatus"));
            categoryIdStr = getStringValueFromPart(request.getPart("categoryId"));
            brandIdStr = getStringValueFromPart(request.getPart("brandId"));
            filePart = request.getPart("productImage");

            System.out.println("Retrieved values: productName='" + productName + "', priceStr='" + priceStr + 
                               "', quantityStr='" + quantityStr + "', productDescription='" + productDescription + 
                               "', productStatus='" + productStatus + "', categoryIdStr='" + categoryIdStr + 
                               "', brandIdStr='" + brandIdStr + "'");
            if (filePart != null) {
                System.out.println("Retrieved filePart: name='" + filePart.getName() + "', submittedFileName='" + filePart.getSubmittedFileName() + "', size=" + filePart.getSize());
            } else {
                System.out.println("filePart is null");
            }

            request.setAttribute("productName", productName);
            request.setAttribute("productPrice", priceStr);
            request.setAttribute("productQuantity", quantityStr);
            request.setAttribute("productDescription", productDescription);
            request.setAttribute("productStatus", productStatus);
            request.setAttribute("selectedCategoryId", categoryIdStr);
            request.setAttribute("selectedBrandId", brandIdStr);
            
            StringBuilder missingFields = new StringBuilder();
            if (ValidationUtil.isNullOrEmpty(productName)) missingFields.append("Product Name, ");
            if (ValidationUtil.isNullOrEmpty(priceStr)) missingFields.append("Price, ");
            if (ValidationUtil.isNullOrEmpty(quantityStr)) missingFields.append("Quantity, ");
            if (ValidationUtil.isNullOrEmpty(productDescription)) missingFields.append("Description, ");
            if (ValidationUtil.isNullOrEmpty(productStatus)) missingFields.append("Status, ");
            if (ValidationUtil.isNullOrEmpty(categoryIdStr)) missingFields.append("Category, ");
            if (ValidationUtil.isNullOrEmpty(brandIdStr)) missingFields.append("Brand, ");

            if (missingFields.length() > 0) {
                String errorMessage = "Missing required fields: " + missingFields.substring(0, missingFields.length() - 2);
                System.out.println("Validation Error (Server-side): " + errorMessage);
                handleError(request, response, errorMessage);
                return;
            }
            
            double price;
            int quantity, categoryId, brandId;
            
            try {
                price = Double.parseDouble(priceStr);
                quantity = Integer.parseInt(quantityStr);
                categoryId = Integer.parseInt(categoryIdStr);
                brandId = Integer.parseInt(brandIdStr);
                
                if (price < 0) {
                    handleError(request, response, "Price must be a non-negative value.");
                    return;
                }
                if (quantity < 0) {
                     handleError(request, response, "Quantity must be a non-negative value.");
                     return;
                }
            } catch (NumberFormatException e) {
                System.out.println("Validation Error (Server-side): Invalid number format. " + e.getMessage());
                handleError(request, response, "Invalid number format for price, quantity, category ID, or brand ID.");
                return;
            }
            
            String imageName = null;
            if (filePart != null && filePart.getSize() > 0) {
                if (!ValidationUtil.isValidImageFile(filePart)) {
                    System.out.println("Validation Error (Server-side): Invalid image file.");
                    handleError(request, response, "Invalid image file. Check extension (JPG, JPEG, PNG, GIF) and size (max 10MB)."); // Updated message
                    return; // Ensure execution stops here if image is invalid
                }
                imageName = imageUtil.getImageNameFromPart(filePart);
                System.out.println("Image name extracted: " + imageName);
            }
            
            ProductsModel product = new ProductsModel();
            product.setProduct_name(productName);
            product.setProduct_price(price);
            product.setProduct_quantity(quantity);
            product.setProduct_description(productDescription);
            product.setProduct_status(productStatus);
            product.setCategory_id(categoryId);
            product.setBrand_id(brandId);
            product.setImage(imageName); 
            
            System.out.println("Attempting to add product to database: " + product.getProduct_name());
            int productId = addProductsService.addProduct(product, imageName);
            System.out.println("Product ID from service: " + productId);
            
            if (productId > 0) {
                if (imageName != null && filePart != null && filePart.getSize() > 0) {
                    System.out.println("Attempting to upload image for product ID: " + productId);
                    boolean uploadSuccess = imageUtil.uploadProductImage(filePart, request.getServletContext().getRealPath("/"));
                    System.out.println("Image upload success: " + uploadSuccess);
                    
                    if (!uploadSuccess) {
                        System.err.println("Image upload failed, but product (ID: " + productId + ") was added to database.");
                        session.setAttribute("warning", "Product added, but image upload failed. Please try updating the product image later.");
                    }
                }
                session.setAttribute("success", "Product added successfully!");
                response.sendRedirect(request.getContextPath() + "/dashboard");
            } else {
                System.out.println("Failed to add product. Service returned ID: " + productId);
                handleError(request, response, "Failed to add product to database. Please check logs or database connection.");
            }
        } catch (IOException ioe) {
            System.err.println("IOException in AddProductsController.doPost (likely reading parts): " + ioe.getMessage());
            ioe.printStackTrace();
            handleError(request, response, "Error processing form data: " + ioe.getMessage());
        } 
        catch (ServletException se) {
            System.err.println("ServletException in AddProductsController.doPost (likely from getParts): " + se.getMessage());
            se.printStackTrace();
            handleError(request, response, "Error processing request: " + se.getMessage());
        }
        catch (Exception e) {
            System.err.println("General Exception in AddProductsController.doPost: " + e.getMessage());
            e.printStackTrace();
            handleError(request, response, "An unexpected error occurred: " + e.getMessage());
        }
    }
    
    private void handleError(HttpServletRequest request, HttpServletResponse response, String errorMessage) 
            throws ServletException, IOException {
        List<CategoriesModel> categories = addProductsService.getAllCategories();
        List<BrandsModel> brands = addProductsService.getAllBrands();
        
        request.setAttribute("categories", categories);
        request.setAttribute("brands", brands);
        request.setAttribute("error", errorMessage);
        
        System.out.println("Handling error: \"" + errorMessage + "\", forwarding to addProducts.jsp");
        request.getRequestDispatcher("/WEB-INF/pages/addProducts.jsp").forward(request, response);
    }
}
