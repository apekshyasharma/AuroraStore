<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<div id="header">
    <header class="header">
        <!-- Left: Logo -->
        <div class="logo">
            <a href="${pageContext.request.contextPath}/pages/welcome.jsp">
                <img src="${pageContext.request.contextPath}/resources/images/system/auroraLogo.png" alt="AuroraStore Logo" />
            </a>
        </div>

        <!-- Center: Main nav -->
        <nav class="center-nav">
            <ul class="main-nav">
                <li><a href="${pageContext.request.contextPath}/welcome">Welcome</a></li>
                <li><a href="${pageContext.request.contextPath}/products">Shop Products</a></li>
                <li><a href="${pageContext.request.contextPath}/aboutus">About Us</a></li>
                <li><a href="${pageContext.request.contextPath}/portfolio">Portfolio</a></li>
                <li><a href="${pageContext.request.contextPath}/dashboard">Dashboard</a></li>
            </ul>
        </nav>

        <!-- Right: Auth nav -->
        <nav class="right-nav">
            <ul class="auth-nav">
                <li><a href="${pageContext.request.contextPath}/register">Register</a></li>
                <li><a href="${pageContext.request.contextPath}/login">Login</a></li>
            </ul>
        </nav>
    </header>
</div>
   