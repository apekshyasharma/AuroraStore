<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Login to Aurora</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/login.css"/>
<!-- Add jQuery and SweetAlert -->
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</head>
<body>
    <!-- Keep existing success message popup for logout -->
    <c:if test="${not empty sessionScope.success}">
        <script>
            $(document).ready(function() {
                Swal.fire({
                    title: "Success",
                    text: "${sessionScope.success}",
                    icon: "success"
                });
                <% session.removeAttribute("success"); %>
            });
        </script>
    </c:if>

    <!-- Add registration success message popup -->
    <c:if test="${not empty sessionScope.regSuccess}">
        <script>
            $(document).ready(function() {
                Swal.fire({
                    title: "Success",
                    text: "${sessionScope.regSuccess}",
                    icon: "success"
                });
                <% session.removeAttribute("regSuccess"); %>
            });
        </script>
    </c:if>

    <!-- Add error message popup -->
    <c:if test="${not empty error}">
        <script>
            $(document).ready(function() {
                Swal.fire({
                    title: "Error",
                    text: "${error}",
                    icon: "error"
                });
            });
        </script>
    </c:if>

    <div class="login-box">
        <h2>Login to Aurora Store</h2>
        <form action="${pageContext.request.contextPath}/login" method="post">
            <div class="row">
                <div class="col">
                    <label for="email">Email:</label> 
                    <input type="email" id="email" name="email" required>
                </div>
            </div>
            <div class="row">
                <div class="col">
                    <label for="password">Password:</label> 
                    <input type="password" id="password" name="password" required>
                </div>
            </div>
            <!-- Add Remember Me checkbox -->
            <div class="remember-me">
                <input type="checkbox" id="remember" name="remember">
                <label for="remember">Remember Me</label>
            </div>
            <button type="submit" class="login-button">Login</button>
        </form>
        <form action="welcome" method="get" style="margin-top: 10px;">
            <button type="submit">Back</button>
        </form>
    </div>    
</body>
</html>