<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Add New Product - Aurora Store</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/addProducts.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/footer.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</head>
<body>
    <%@ include file="header.jsp" %>
    
    <div class="container">
        <div class="add-product-container">
            <div class="header-section">
                <h1><i class="fas fa-plus-circle"></i> Add New Product</h1>
                <p>Enter the details below to add a new product to your inventory</p>
            </div>
            
            <form action="${pageContext.request.contextPath}/addproducts" method="post" enctype="multipart/form-data" class="product-form">
                <div class="form-row">
                    <div class="form-group">
                        <label for="productName">Product Name <span class="required">*</span></label>
                        <input type="text" id="productName" name="productName" value="${productName}" required>
                    </div>
                    <div class="form-group">
                        <label for="productPrice">Price (Rs.) <span class="required">*</span></label>
                        <input type="number" id="productPrice" name="productPrice" step="0.01" min="0" value="${productPrice}" required>
                    </div>
                </div>
                
                <div class="form-row">
                    <div class="form-group">
                        <label for="productQuantity">Quantity <span class="required">*</span></label>
                        <input type="number" id="productQuantity" name="productQuantity" min="1" value="${productQuantity}" required>
                    </div>
                    <div class="form-group">
                        <label for="productStatus">Status <span class="required">*</span></label>
                        <select id="productStatus" name="productStatus" required>
                            <option value="active" ${productStatus == 'active' ? 'selected' : ''}>Active</option>
                            <option value="inactive" ${productStatus == 'inactive' ? 'selected' : ''}>Inactive</option>
                        </select>
                    </div>
                </div>
                
                <div class="form-row">
                    <div class="form-group">
                        <label for="category">Category <span class="required">*</span></label>
                        <select id="category" name="categoryId" required>
                            <option value="" disabled selected>Select Category</option>
                            <c:forEach items="${categories}" var="category">
                                <option value="${category.category_id}" ${selectedCategoryId == category.category_id ? 'selected' : ''}>${category.category_name}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="brand">Brand <span class="required">*</span></label>
                        <select id="brand" name="brandId" required>
                            <option value="" disabled selected>Select Brand</option>
                            <c:forEach items="${brands}" var="brand">
                                <option value="${brand.brand_id}" ${selectedBrandId == brand.brand_id ? 'selected' : ''}>${brand.brand_name}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                
                <div class="form-group full-width">
                    <label for="productDescription">Description <span class="required">*</span></label>
                    <textarea id="productDescription" name="productDescription" rows="4" required>${productDescription}</textarea>
                </div>
                
                <div class="form-group full-width">
                    <label for="productImage">Product Image</label>
                    <div class="image-upload-container">
                        <div id="imagePreview" class="image-preview">
                            <i class="fas fa-image"></i>
                            <p>No image selected</p>
                        </div>
                        <div class="image-upload-controls">
                            <input type="file" id="productImage" name="productImage" accept="image/*" onchange="previewImage(this)" style="display: none;">
                            <label for="productImage" class="custom-file-upload">
                                <i class="fas fa-upload"></i> Choose Image
                            </label>
                            <small>Maximum file size: 5MB. Supported formats: JPG, JPEG, PNG, GIF</small>
                        </div>
                    </div>
                </div>
                
                <div class="form-actions">
                    <a href="${pageContext.request.contextPath}/dashboard" class="cancel-btn">
                        <i class="fas fa-times"></i> Cancel
                    </a>
                    <button type="submit" class="submit-btn">
                        <i class="fas fa-save"></i> Save Product
                    </button>
                </div>
            </form>
        </div>
    </div>
    
    <%@ include file="footer.jsp" %>
    
    <script>
        function previewImage(input) {
            const preview = document.getElementById('imagePreview');
            
            if (input.files && input.files[0]) {
                const reader = new FileReader();
                
                reader.onload = function(e) {
                    preview.innerHTML = `<img src="${e.target.result}" alt="Product Image Preview">`;
                };
                
                reader.readAsDataURL(input.files[0]);
            } else {
                preview.innerHTML = `
                    <i class="fas fa-image"></i>
                    <p>No image selected</p>
                `;
            }
        }
    </script>
    
    <!-- Add this script right after the form -->
    <script>
        // Verify form has proper enctype for file uploads
        document.addEventListener('DOMContentLoaded', function() {
            const form = document.querySelector('.product-form');
            if (form.enctype !== 'multipart/form-data') {
                console.error('Form missing proper enctype for file uploads!');
                form.enctype = 'multipart/form-data';
                console.log('Fixed form enctype to: ' + form.enctype);
            } else {
                console.log('Form enctype is correctly set to: ' + form.enctype);
            }
        });
    </script>
    
    <c:if test="${not empty success}">
        <script>
            Swal.fire("Success", "${success}", "success");
            <% session.removeAttribute("success"); %>
        </script>
    </c:if>
    
    <c:if test="${not empty error}">
        <script>
            Swal.fire("Error", "${error}", "error");
            <% session.removeAttribute("error"); %>
        </script>
    </c:if>
    
    <script>
        // Form validation
        document.querySelector('.product-form').addEventListener('submit', function(event) {
            console.log('Form submission initiated');
            let isValid = true;
            const productName = document.getElementById('productName');
            const productPrice = document.getElementById('productPrice');
            const productQuantity = document.getElementById('productQuantity');
            const productDescription = document.getElementById('productDescription');
            const category = document.getElementById('category');
            const brand = document.getElementById('brand');
            
            // Clear previous error messages
            document.querySelectorAll('.error-message').forEach(el => el.remove());
            document.querySelectorAll('.error-field').forEach(el => el.classList.remove('error-field'));
            
            // Validate product name (not empty and not too long)
            if (!productName.value.trim() || productName.value.length > 100) {
                showError(productName, 'Product name is required and must be less than 100 characters');
                isValid = false;
            }
            
            // Validate price (must be a positive number)
            if (!productPrice.value || parseFloat(productPrice.value) <= 0) {
                showError(productPrice, 'Price must be a positive number');
                isValid = false;
            }
            
            // Validate quantity (must be a positive integer)
            if (!productQuantity.value || parseInt(productQuantity.value) < 1) {
                showError(productQuantity, 'Quantity must be at least 1');
                isValid = false;
            }
            
            // Validate description (not empty and not too long)
            if (!productDescription.value.trim() || productDescription.value.length > 100) {
                showError(productDescription, 'Description is required and must be less than 100 characters');
                isValid = false;
            }
            
            // Validate category selection
            if (category.value === "" || category.value === null) {
                showError(category, 'Please select a category');
                isValid = false;
            }
            
            // Validate brand selection
            if (brand.value === "" || brand.value === null) {
                showError(brand, 'Please select a brand');
                isValid = false;
            }
            
            if (!isValid) {
                console.log('Form validation failed');
                event.preventDefault();
            } else {
                console.log('Form validation passed, submitting');
            }
        });
        
        function showError(inputElement, message) {
            inputElement.classList.add('error-field');
            const errorDiv = document.createElement('div');
            errorDiv.className = 'error-message';
            errorDiv.textContent = message;
            inputElement.parentNode.appendChild(errorDiv);
        }
    </script>
</body>
</html>