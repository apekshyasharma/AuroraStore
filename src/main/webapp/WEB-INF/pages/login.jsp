<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Login to Aurora</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/login.css"/>

</head>
<body>
	<div class="login-box">
		<h2>Login to Aurora Store</h2>
		<form action="">
			<div class="row">
				<div class="col">
					<label for="username">Username:</label> <input type="text"
						id="username" name="username" required>
				</div>
			</div>
			<div class="row">
				<div class="col">
					<label for="password">Password:</label> <input type="password"
						id="password" name="password" required>
				</div>
			</div>
			<button type="submit" class="login-button">Login</button>
		</form>
		<form action="welcome" method="get" style="margin-top: 10px;">
			<button type="submit">Back</button>
		</form>
	</div>	
</body>
</html>