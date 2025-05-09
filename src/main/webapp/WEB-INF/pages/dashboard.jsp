<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <title>Admin Dashboard</title>
      <!-- Stylesheets -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/footer.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body>
	<%@ include file="header.jsp" %>
    <div class="dashboard-container">
        <h1>Admin Dashboard</h1>

        <!-- Summary Cards -->
        <div class="summary-cards">
            <div class="card">
                <h2>Total Users</h2>
                <p>1,234</p>
            </div>
            <div class="card">
                <h2>Total Products</h2>
                <p>98</p>
            </div>
        </div>

        <!-- User Table -->
        <div class="section">
            <div class="section-header">
                <h2>All Users</h2>
                <button>Add User</button>
            </div>
            <table class="data-table">
                <thead>
                    <tr>
                        <th>User ID</th>
                        <th>Full Name</th>
                        <th>Email</th>
                        <th>Role</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    <!-- Sample rows -->
                    <tr>
                        <td>U001</td>
                        <td>Jane Doe</td>
                        <td>jane@example.com</td>
                        <td>Customer</td>
                        <td><button class="delete-btn">Delete</button></td>
                    </tr>
                </tbody>
            </table>
        </div>

        <!-- Product Table -->
        <div class="section">
            <div class="section-header">
                <h2>All Products</h2>
                <div class="actions">
                    <select>
                        <option>Sort by</option>
                        <option>Name</option>
                        <option>Price</option>
                        <option>Stock</option>
                    </select>
                    <button>Add Product</button>
                </div>
            </div>
            <table class="data-table">
                <thead>
                    <tr>
                        <th>Product ID</th>
                        <th>Name</th>
                        <th>Price</th>
                        <th>Stock</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    <!-- Sample rows -->
                    <tr>
                        <td>P001</td>
                        <td>Notebook</td>
                        <td>$2.99</td>
                        <td>150</td>
                        <td><button class="delete-btn">Delete</button></td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
    <h1>Welcome, Admin!</h1>
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <c:if test="${not empty success}">
        <script>
            Swal.fire("Success", "${success}", "success");
            // Remove the success message from the session after displaying
            <% session.removeAttribute("success"); %>
        </script>
    </c:if>
    <%@ include file="footer.jsp" %>
</body>
</html>