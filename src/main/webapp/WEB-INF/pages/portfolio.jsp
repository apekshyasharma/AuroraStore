<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Your Profile | Aurora Store</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/portfolio.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/footer.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</head>
<body>
    <div class="page-container">
        <%@ include file="header.jsp" %>
        
        <main class="profile-main">
            <div class="profile-container">
                <div class="profile-header">
                    <h1>Welcome to your profile, ${user.user_name}!</h1>
                </div>
                
                <div class="profile-content">
                    <div class="profile-image-container">
                        <div class="profile-image">
                            <img src="${pageContext.request.contextPath}/${imagePath}" alt="Profile Image" 
                                 onerror="this.src='${pageContext.request.contextPath}/resources/images/system/default-profile.png'">
                        </div>
                    </div>
                    
                    <div class="profile-details">
                        <div class="card">
                            <div class="card-header">
                                <i class="fas fa-user-circle"></i>
                                <h2>Personal Information</h2>
                            </div>
                            <div class="card-content">
                                <div class="info-row">
                                    <div class="info-label">
                                        <i class="fas fa-user"></i>
                                        <span>Name</span>
                                    </div>
                                    <div class="info-value">${user.user_name}</div>
                                </div>
                                
                                <div class="info-row">
                                    <div class="info-label">
                                        <i class="fas fa-envelope"></i>
                                        <span>Email</span>
                                    </div>
                                    <div class="info-value">${user.user_email}</div>
                                </div>
                                
                                <div class="info-row">
                                    <div class="info-label">
                                        <i class="fas fa-phone"></i>
                                        <span>Contact Number</span>
                                    </div>
                                    <div class="info-value">${user.contact_number}</div>
                                </div>
                                
                                <div class="info-row">
                                    <div class="info-label">
                                        <i class="fas fa-calendar-alt"></i>
                                        <span>Member Since</span>
                                    </div>
                                    <div class="info-value">${user.created_at}</div>
                                </div>
                            </div>
                        </div>
                        
                        <div class="actions-container">
                            <button class="action-btn" onclick="window.location.href='${pageContext.request.contextPath}/products'">
                                <i class="fas fa-shopping-cart"></i> Continue Shopping
                            </button>
                            <button class="action-btn secondary" onclick="window.location.href='${pageContext.request.contextPath}/cart'">
                                <i class="fas fa-shopping-bag"></i> View Cart
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </main>
        
        <%@ include file="footer.jsp" %>
    </div>
    
    <script>
        // Check for success message in session
        <c:if test="${not empty sessionScope.success}">
            Swal.fire({
                icon: 'success',
                title: 'Success!',
                text: '${sessionScope.success}',
                confirmButtonColor: '#800040'
            });
            <% session.removeAttribute("success"); %>
        </c:if>
        
        // Check for error message in session
        <c:if test="${not empty sessionScope.error}">
            Swal.fire({
                icon: 'error',
                title: 'Error!',
                text: '${sessionScope.error}',
                confirmButtonColor: '#800040'
            });
            <% session.removeAttribute("error"); %>
        </c:if>
    </script>
</body>
</html>