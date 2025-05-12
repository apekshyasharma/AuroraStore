<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Register to Aurora</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/register.css"/>

</head>
<!-- Include jQuery and SweetAlert for alert popups -->
	<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
	<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
	
<!-- Display SweetAlert popup if there's an error message in request scope -->
<c:if test="${not empty error}">
	<script>
		$(document).ready(function() {
			Swal.fire("Error", "${error}", "error");
		});
	</script>
</c:if>

<!-- Display SweetAlert popup if registration is successful -->
<c:if test="${not empty success}">
	<script>
		$(document).ready(function() {
			Swal.fire("Success", "${success}", "success");
		});
	</script>
</c:if>

<script>
function validatePassword() {
    var password = document.getElementById('password').value;
    
    if(password.length < 8) {
        Swal.fire("Error", "Please enter password with minimum 8 characters.", "error");
        return false;
    }
    
    if(!/[@!#$%&]/.test(password)) {
        Swal.fire("Error", "Password must contain atleast one character like (@,!,#,$,%,&).", "error");
        return false;
    }
    
    if(!/[A-Z]/.test(password)) {
        Swal.fire("Error", "Password must contain atleast one Uppercase letter.", "error");
        return false;
    }
    
    return true;
}

function validateEmail(email) {
    const emailRegex = /^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$/;
    return emailRegex.test(email);
}

// Add this to the form validation
document.querySelector('form').onsubmit = function(e) {
    const email = document.getElementById('email').value;
    
    if (!validateEmail(email)) {
        Swal.fire("Error", "Please enter a valid email address.", "error");
        return false;
    }
    
    return validatePassword(); // Keep existing password validation
};
</script>

<body>
	<div class="container">
		<h1>Register to Aurora</h1>
				<!-- Display error message if available -->
		<!-- Registration form that submits to /register endpoint -->
		<form action="${pageContext.request.contextPath}/register" method="post" enctype="multipart/form-data">
			<div class="row">
				<div class="col">
				<!-- Name field -->
					<label for="firstName">Name:</label> 
					<input type="text" id="firstName" name="firstName" value="${firstName}" required>
				</div>
			</div>
			<div class="row">
				<div class="col">
				<!-- Email field -->
					<label for="email">Email:</label> 
					<input type="email" id="email" name="email" value="${email}" required 
						   pattern="^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$" 
						   title="Please enter a valid email address">
				</div>
			</div>

			<div class="row">
				<div class="col">
				<!-- Phone number -->
					<label for="phoneNumber">Phone Number:</label> 
					<input type="tel" id="phoneNumber" name="phoneNumber" required>
				</div>
				<div class="col">
					<label for="usertype">User Type: </label> 
					<select id="usertype" name="usertype" required>
						<option value="" disabled selected>Select role</option>
						<option value="1">Admin</option>
						<option value="2">Customer</option>
					</select>
				</div>
			</div>

			<div class="row">
				<div class="col">
				<!-- Password field -->
					<label for="password">Password:</label> 
					<input type="password" id="password" name="password" required>
				</div>
				<div class="col">
					<label for="retypePassword">Retype Password:</label> 
					<input type="password" id="retypePassword" name="retypePassword" required>
				</div>
			</div>
			<div class="row">
				<div class="col">
					<label for="image">Profile Picture:</label> 
					<input type="file" id="image" name="image" accept="image/*">
					<small>Supported formats: JPG, JPEG, PNG, GIF</small>
				</div>
			</div>		

			<!-- Submit Button -->
			<button type="submit">Submit</button>
		</form>
		
		<!-- Back button (navigates to 'welcome' page) -->
		<form action="welcome" method="get" style="margin-top: 10px;">
			<button>Back</button>
		</form>
		
	</div>
</body>
</html>