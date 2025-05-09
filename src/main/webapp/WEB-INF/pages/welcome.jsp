<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Welcome to AuroraStore</title>
    
    <!-- Stylesheets -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/welcome.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/footer.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body>

<div class="page-container">

    <%@ include file="header.jsp" %>

    <main class="main-content">
        <!-- First Stationery Section -->
        <section class="stationery-section">
            <div class="stationery-image">
                <img src="${pageContext.request.contextPath}/resources/images/system/home.png" 
                     alt="Stationery Items"
                     onerror="this.src='https://via.placeholder.com/500x300?text=Stationery+Collection'">
            </div>
            <div class="stationery-info">
                <h3>Explore Our Stationery Collection</h3>
                <p>From colorful notebooks to premium pens — we've got everything to light up your creative world.</p>
                <a href="${pageContext.request.contextPath}/products" class="shop-button">Shop Now</a>
            </div>
        </section>

        <!-- Second Stationery Section -->
        <section class="snd_stationery-section">
            <div class="snd_stationery-image">
                <img src="${pageContext.request.contextPath}/resources/images/system/Sales.png" 
                     alt="Sales Items"
                     onerror="this.src='https://via.placeholder.com/500x300?text=Exclusive+Sales'">
            </div>
            <div class="snd_stationery-info">
                <h3>Exclusive Sales</h3>
                <p>Explore our products on seasonal sales.</p>
                <a href="products" class="snd_shop-button">Shop Now</a>
            </div>
        </section>

        <!-- Third Stationery Section -->
        <section class="third-stationery-section">
            <div class="third-stationery-info">
                <h3>New Arrivals</h3>
                <p>Discover the latest additions to our collection – newest designs and vibrant colors just for you.</p>
                <a href="products" class="third-shop-button">Shop Now</a>
            </div>
            <div class="third-stationery-image">
                <img src="${pageContext.request.contextPath}/resources/images/system/newarrivals.png" 
                     alt="New Arrivals"
                     onerror="this.src='https://via.placeholder.com/500x300?text=New+Arrivals'">
            </div>
        </section>
    </main>

    <%@ include file="footer.jsp" %>

</div>

</body>
</html>