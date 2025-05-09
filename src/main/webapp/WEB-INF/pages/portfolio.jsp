<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>User Profile - Aurora</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/header.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/portfolio.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/footer.css">
</head>
<body>
    <%@ include file="header.jsp" %>
    <div class="profile-container">
        <h2>Welcome, ${user.username}!</h2>
        
        <div class="profile-content">
            <div class="profile-picture">
                <img src="${pageContext.request.contextPath}/uploads/${user.profilePicture}" 
                     alt="Profile Picture" class="profile-img">
                <form action="../updatePicture" method="post" enctype="multipart/form-data">
                    <input type="file" name="newProfilePic" accept="image/*">
                    <button type="submit" class="update-btn">Update Picture</button>
                </form>
            </div>
            
            <div class="user-details">
                <div class="detail-row">
                    <span class="detail-label">Full Name:</span>
                    <span class="detail-value">${user.firstName} ${user.lastName}</span>
                </div>
                <div class="detail-row">
                    <span class="detail-label">Email:</span>
                    <span class="detail-value">${user.email}</span>
                </div>
                <div class="detail-row">
                    <span class="detail-label">Phone:</span>
                    <span class="detail-value">${user.phoneNumber}</span>
                </div>
                <div class="detail-row">
                    <span class="detail-label">Birthday:</span>
                    <span class="detail-value">${user.birthday}</span>
                </div>
                <div class="detail-row">
                    <span class="detail-label">Member Since:</span>
                    <span class="detail-value">${user.joinDate}</span>
                </div>
                <a href="edit-profile.jsp" class="edit-btn">Edit Profile</a>
            </div>
        </div>
    </div>
     <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
     <h1>Welcome, Customer!</h1>

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