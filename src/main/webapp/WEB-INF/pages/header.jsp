<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<div id="header">
    <header class="header">
        <!-- Left: Logo -->
        <div class="logo">
            <a href="${pageContext.request.contextPath}/welcome">
                <img src="${pageContext.request.contextPath}/resources/images/system/auroraLogo.png" alt="AuroraStore Logo" />
            </a>
        </div>

        <!-- Center: Main nav -->
        <nav class="center-nav">
            <ul class="main-nav">
                <li><a href="${pageContext.request.contextPath}/welcome">Welcome</a></li>
                <li><a href="${pageContext.request.contextPath}/products">Shop Products</a></li>
                <li><a href="${pageContext.request.contextPath}/aboutus">About Us</a></li>
                
                <c:if test="${not empty sessionScope.currentUser}">
                    <c:choose>
                        <c:when test="${sessionScope.currentUser.role_id == 1}">
                            <li><a href="${pageContext.request.contextPath}/dashboard">Dashboard</a></li>
                        </c:when>
                        <c:when test="${sessionScope.currentUser.role_id == 2}">
                            <li><a href="${pageContext.request.contextPath}/portfolio">Portfolio</a></li>
                        </c:when>
                    </c:choose>
                </c:if>
            </ul>
        </nav>

        <!-- Right: Auth nav -->
        <nav class="right-nav">
            <ul class="auth-nav">
                <c:choose>
                    <c:when test="${empty sessionScope.currentUser}">
                        <li><a href="${pageContext.request.contextPath}/register">Register</a></li>
                        <li><a href="${pageContext.request.contextPath}/login">Login</a></li>
                    </c:when>
                    <c:otherwise>
                        <li>
                            <span style="margin-right: 10px;">
                                Welcome, ${sessionScope.currentUser.user_name}!
                            </span>
                        </li>
                        <li><a href="${pageContext.request.contextPath}/logout">Logout</a></li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </nav>
    </header>
</div>
