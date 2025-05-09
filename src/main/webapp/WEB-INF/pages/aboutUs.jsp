<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>About Aurora Stationery</title>
    
    <!-- Stylesheets -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/aboutUs.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/footer.css">
</head>
<body>

<div class="page-container">

    <%@ include file="header.jsp" %>

    <main class="main-content">
        <section class="simple-about">
            <div class="about-store-image">
                <img src="${pageContext.request.contextPath}/resources/images/system/stationery.png" 
                     alt="Aurora Stationery Store"
                     onerror="this.src='https://via.placeholder.com/800x400?text=Our+Store'">
            </div>
            
            <div class="about-text-content">
                <h1>About Aurora Stationery</h1>
                
                <div class="about-history">
                    <p>Established in 2008 AD in Dhapakhel, Lalitpur, Aurora Stationery has been serving our community with quality stationery products for over 15 years. What began as a small neighborhood shop has grown into a trusted destination for students, professionals, and artists alike.</p>
                    
                    <p>We take pride in offering:</p>
                    <ul class="simple-list">
                        <li>✓ Quality products at fair prices</li>
                        <li>✓ Friendly and knowledgeable service</li>
                        <li>✓ Wide range of stationery essentials</li>
                        <li>✓ Specialized art supplies</li>
                        <li>✓ Regular new arrivals</li>
                    </ul>
                </div>

                <div class="store-philosophy">
                    <h2>Our Promise</h2>
                    <p>At Aurora Stationery, we believe that great tools inspire great work. Whether you're a student preparing for exams, an office professional, or a creative artist, we're committed to providing you with the reliable tools you need to express yourself and get the job done.</p>
                    
                    <p>Visit us at our Dhapakhel location and experience the difference that comes with 15 years of stationery expertise.</p>
                    <p>Contact Us: +977238453854, +97745652419, +97735869489</p>
                </div>
            </div>
        </section>
    </main>

    <%@ include file="footer.jsp" %>

</div>

</body>
</html>