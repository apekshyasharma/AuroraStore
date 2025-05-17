<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
    <title>Admin Dashboard - Aurora Stationery</title>
    <!-- Stylesheets -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/footer.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</head>
<body>
    <!-- Debug info - will be hidden in production -->
    <div style="display:none;">
        <p>Debug - Users list size: ${usersList.size()}</p>
        <p>Debug - Products list size: ${productsList.size()}</p>
    </div>

    <%@ include file="header.jsp" %>
    
    <div class="dashboard-container">
        <!-- Welcome Banner -->
        <div class="welcome-banner">
            <div class="welcome-text">
                <h1>Welcome Back, <span class="admin-name">${adminName}</span>!</h1>
                <p>Here's what's happening in your store today</p>
            </div>
            <div class="date-display">
                <i class="fas fa-calendar-alt"></i>
                <span id="current-date">Today's Date</span>
            </div>
        </div>
        
        <!-- Summary Cards -->
        <div class="summary-cards">
            <div class="card users-card">
                <i class="fas fa-users card-icon"></i>
                <div class="card-content">
                    <h2>Total Users</h2>
                    <p>${userCount}</p>
                </div>
            </div>
            
            <div class="card products-card">
                <i class="fas fa-box-open card-icon"></i>
                <div class="card-content">
                    <h2>Total Products</h2>
                    <p>${productCount}</p>
                </div>
            </div>
            
            <div class="card sales-card">
                <i class="fas fa-shopping-cart card-icon"></i>
                <div class="card-content">
                    <h2>Inventory Value</h2>
                    <p>Rs. <fmt:formatNumber type="number" pattern="#,##0.00" value="${totalInventory}" /></p>
                </div>
            </div>
        </div>

        <!-- User Table Section -->
        <div class="section user-section">
            <div class="section-header">
                <h2><i class="fas fa-user-friends"></i> All Users</h2>
            </div>
            
            <div class="table-container">
                <table class="data-table" id="usersTable">
                    <thead>
                        <tr>
                            <th>User ID</th>
                            <th>Full Name</th>
                            <th>Email</th>
                            <th>Phone Number</th>
                            <th>Created Date</th>
                            <th>Role ID</th>
                            <th>Role Type</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:if test="${empty usersList}">
                            <tr>
                                <td colspan="8" style="text-align: center;">No users found</td>
                            </tr>
                        </c:if>
                        <c:forEach items="${usersList}" var="user">
                            <tr>
                                <td>${user.user_id}</td>
                                <td>${user.user_name}</td>
                                <td>${user.user_email}</td>
                                <td>${user.contact_number}</td>
                                <td>${user.created_at}</td>
                                <td>${user.role_id}</td>
                                <td>${user.image}</td> <!-- Role type is stored in image field temporarily -->
                                <td><button class="delete-btn">Delete</button></td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>

        <!-- Product Table Section -->
        <div class="section product-section">
            <div class="section-header">
                <h2><i class="fas fa-box"></i> All Products</h2>
                <div class="action-buttons">
                    <button class="add-btn">
                        <i class="fas fa-plus"></i> Add Product
                    </button>
                </div>
            </div>
            
            <div class="table-container">
                <table class="data-table" id="productsTable">
                    <thead>
                        <tr>
                            <th>Product ID</th>
                            <th>Name</th>
                            <th>Description</th>
                            <th>Price (Rs.)</th>
                            <th>Quantity</th>
                            <th>Status</th>
                            <th>Category</th>
                            <th>Brand</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:if test="${empty productsList}">
                            <tr>
                                <td colspan="9" style="text-align: center;">No products found</td>
                            </tr>
                        </c:if>
                        <c:forEach items="${productsList}" var="product">
                            <tr>
                                <td>${product.product_id}</td>
                                <td>${product.product_name}</td>
                                <td>${product.product_description}</td>
                                <td><fmt:formatNumber value="${product.product_price}" type="number" pattern="#,##0.00" /></td>
                                <td>${product.product_quantity}</td>
                                <td>${product.product_status}</td>
                                <td>${product.category_name}</td>
                                <td>${product.brand_name}</td>
                                <td><button class="delete-btn">Delete</button></td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <script>
        // Display current date
        document.addEventListener('DOMContentLoaded', function() {
            const now = new Date();
            const options = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' };
            document.getElementById('current-date').textContent = now.toLocaleDateString('en-US', options);
        });
    </script>

    <c:if test="${not empty success}">
        <script>
            Swal.fire("Success", "${success}", "success");
            // Remove the success message from the session after displaying
            <% session.removeAttribute("success"); %>
        </script>
    </c:if>
    
    <c:if test="${not empty error}">
        <script>
            Swal.fire("Error", "${error}", "error");
            // Remove the error message from the session after displaying
            <% session.removeAttribute("error"); %>
        </script>
    </c:if>
    <%@ include file="footer.jsp" %>
</body>
</html>