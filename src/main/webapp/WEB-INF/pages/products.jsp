 <%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Products - AuroraStore</title>
    
    <!-- Stylesheets -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/products.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/footer.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body>

<div class="page-container">

    <%@ include file="header.jsp" %>

    <main class="main-content">
        <section class="products-section">
            <h2 class="section-title">Our Stationery Products Collection</h2>
            <div class="products-grid">
                <!-- Product 1 -->
                <div class="product-card">
                    <img src="${pageContext.request.contextPath}/resources/images/system/notebook.png" 
                         alt="Premium Notebook Set"
                         onerror="this.src='https://via.placeholder.com/300x300?text=Notebook+Set'">
                    <h3 class="product-title">NoTes Notebook Set</h3>
                    <p class="product-price">Rs.250</p>
                    <button class="product-button">Add to Cart</button>
                </div>

                <!-- Product 2 -->
                <div class="product-card">
                    <img src="${pageContext.request.contextPath}/resources/images/system/pens.png" 
                         alt="Luxury Pen Set"
                         onerror="this.src='https://via.placeholder.com/300x300?text=Luxury+Pens'">
                    <h3 class="product-title">INK Pen Collection</h3>
                    <p class="product-price">Rs.50</p>
                    <button class="product-button">Add to Cart</button>
                </div>

                <!-- Product 3 -->
                <div class="product-card">
                    <img src="${pageContext.request.contextPath}/resources/images/system//artsupply.png" 
                         alt="Art Supplies Kit"
                         onerror="this.src='https://via.placeholder.com/300x300?text=Art+Kit'">
                    <h3 class="product-title">Artistic Art Supplies</h3>
                    <p class="product-price">Rs.500</p>
                    <button class="product-button">Add to Cart</button>
                </div>

                <!-- Product 4 -->
                <div class="product-card">
                    <img src="${pageContext.request.contextPath}/resources/images/system/organiser.png" 
                         alt="Desk Organizer"
                         onerror="this.src='https://via.placeholder.com/300x300?text=Desk+Organizer'">
                    <h3 class="product-title">Ikea Desk Organizer</h3>
                    <p class="product-price">Rs.5000</p>
                    <button class="product-button">Add to Cart</button>
                </div>

                <!-- Product 5 -->
                <div class="product-card">
                    <img src="${pageContext.request.contextPath}/resources/images/system/leatherplanner.png" 
                         alt="Leather Planner"
                         onerror="this.src='https://via.placeholder.com/300x300?text=Leather+Planner'">
                    <h3 class="product-title">Luxeria Leather Planner</h3>
                    <p class="product-price">Rs.1600</p>
                    <button class="product-button">Add to Cart</button>
                </div>

                <!-- Product 6 -->
                <div class="product-card">
                    <img src="${pageContext.request.contextPath}/resources/images/system/storage.jpg" 
                         alt="Decorative Storage Box"
                         onerror="this.src='https://via.placeholder.com/300x300?text=Storage+Box'">
                    <h3 class="product-title">Decorative Storage Box</h3>
                    <p class="product-price">Rs.1050</p>
                    <button class="product-button">Add to Cart</button>
                </div>
            </div>
        </section>
    </main>

    <%@ include file="footer.jsp" %>

</div>

</body>
</html>
