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
                        <input type="text" id="productName" name="productName" required>
                    </div>
                    <div class="form-group">
                        <label for="productPrice">Price (Rs.) <span class="required">*</span></label>
                        <input type="number" id="productPrice" name="productPrice" step="0.01" min="0" required>
                    </div>
                </div>
                
                <div class="form-row">
                    <div class="form-group">
                        <label for="productQuantity">Quantity <span class="required">*</span></label>
                        <input type="number" id="productQuantity" name="productQuantity" min="1" required>
                    </div>
                    <div class="form-group">
                        <label for="productStatus">Status <span class="required">*</span></label>
                        <select id="productStatus" name="productStatus" required>
                            <option value="active">Active</option>
                            <option value="inactive">Inactive</option>
                        </select>
                    </div>
                </div>
                
                <div class="form-row">
                    <div class="form-group">
                        <label for="category">Category <span class="required">*</span></label>
                        <select id="category" name="categoryId" required>
                            <option value="" disabled selected>Select Category</option>
                            <c:forEach items="${categories}" var="category">
                                <option value="${category.category_id}">${category.category_name}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="brand">Brand <span class="required">*</span></label>
                        <select id="brand" name="brandId" required>
                            <option value="" disabled selected>Select Brand</option>
                            <c:forEach items="${brands}" var="brand">
                                <option value="${brand.brand_id}">${brand.brand_name}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                
                <div class="form-group full-width">
                    <label for="productDescription">Description <span class="required">*</span></label>
                    <textarea id="productDescription" name="productDescription" rows="4" required></textarea>
                </div>
                
                <div class="form-group full-width">
                    <label for="productImage">Product Image</label>
                    <div class="image-upload-container">
                        <div class="image-preview" id="imagePreview">
                            <i class="fas fa-image"></i>
                            <p>No image selected</p>
                        </div>
                        <div class="image-upload-controls">
                            <input type="file" id="productImage" name="productImage" accept="image/*" onchange="previewImage(this)">
                            <label for="productImage" class="custom-file-upload">
                                <i class="fas fa-upload"></i> Choose Image
                            </label>
                            <small>Supported formats: JPG, JPEG, PNG, GIF (Max: 5MB)</small>
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
</body>
</html>