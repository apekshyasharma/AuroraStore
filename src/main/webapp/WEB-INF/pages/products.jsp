<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Our Products - Aurora Store</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/products.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/footer.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <!-- Add jQuery and SweetAlert scripts -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</head>
<body>
    <%@ include file="header.jsp" %>
    
    <div class="container">
        <div class="products-header">
            <c:choose>
                <c:when test="${not empty selectedCategory}">
                    <h1>${selectedCategory} Products</h1>
                    <p>Explore our collection of high-quality ${selectedCategory} stationery and office supplies.</p>
                </c:when>
                <c:otherwise>
                    <h1>Discover Our Products</h1>
                    <p>Explore our collection of high-quality stationery and office supplies for all your needs.</p>
                </c:otherwise>
            </c:choose>
        </div>
        
        <c:choose>
            <c:when test="${empty productsList}">
                <div class="no-products">
                    <i class="fas fa-box-open fa-3x"></i>
                    <p>No products available at the moment.</p>
                </div>
            </c:when>
            <c:otherwise>
                <div class="products-grid">
                    <c:forEach items="${productsList}" var="product">
                        <div class="product-card">
                            <div class="product-image">
                                <c:choose>
                                    <c:when test="${not empty product.image}">
                                        <img src="${pageContext.request.contextPath}/resources/images/products/${product.image}" 
                                             alt="${product.product_name}"
                                             onerror="this.src='${pageContext.request.contextPath}/resources/images/system/placeholder.png'">
                                    </c:when>
                                    <c:otherwise>
                                        <img src="${pageContext.request.contextPath}/resources/images/system/placeholder.png" 
                                             alt="Product Image Placeholder">
                                    </c:otherwise>
                                </c:choose>
                            </div>
                            <div class="product-details">
                                <h3 class="product-name">${product.product_name}</h3>
                                <div class="product-price">Rs. <fmt:formatNumber value="${product.product_price}" type="number" pattern="#,##0.00" /></div>
                                <p class="product-description">${product.product_description}</p>
                                <div class="product-meta">
                                    <span class="product-category">${product.category_name}</span>
                                    <span class="product-brand">${product.brand_name}</span>
                                </div>
                                <div class="product-actions">
                                    <a href="${pageContext.request.contextPath}/product-details?id=${product.product_id}" class="view-details">
                                        <i class="fas fa-eye"></i> View Details
                                    </a>
                                    <button class="add-to-cart" onclick="addToCart(${product.product_id})">
                                        <i class="fas fa-cart-plus"></i> Add to Cart
                                    </button>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
    
    <%@ include file="footer.jsp" %>
    
    <script>
        function addToCart(productId) {
            // Check if user is logged in by looking for the currentUser attribute in session
            <c:choose>
                <c:when test="${empty sessionScope.currentUser}">
                    /* User is not logged in, show login prompt */
                    Swal.fire({
                        title: 'Login Required',
                        text: 'Please login to add items to your cart',
                        icon: 'info',
                        showCancelButton: true,
                        confirmButtonColor: '#800040',
                        cancelButtonColor: '#6c757d',
                        confirmButtonText: 'Login Now',
                        cancelButtonText: 'Cancel'
                    }).then((result) => {
                        if (result.isConfirmed) {
                            // Redirect to login page with a return URL parameter
                            window.location.href = '${pageContext.request.contextPath}/login';
                        }
                    });
                </c:when>
                <c:otherwise>
                    /* User is logged in, implement actual cart functionality */
                    Swal.fire({
                        title: 'Success!',
                        text: 'Product added to cart!',
                        icon: 'success',
                        confirmButtonColor: '#800040'
                    });
                    // Here you would add actual cart functionality, like an AJAX call to add the item
                </c:otherwise>
            </c:choose>
        }
    </script>
</body>
</html>