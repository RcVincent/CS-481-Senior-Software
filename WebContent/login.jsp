<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
	<title>CTM mkii - Login</title>
	<link rel="stylesheet" href="css/bootstrap.css">
</head>
<body>
<h1>Login</h1>
<div class="fluid-container">
	<form  class="form-horizontal" method="post">
		<c:if test="${! empty errorMessage}">
			<div class="row">
				<div class="col">
					<p class="alert alert-warning">${errorMessage}</p>
				</div>
			</div>
		</c:if>
		<c:if test="${! empty successMessage}">
			<div class="row">
				<div class="col">
					<p class="alert alert-success">${successMessage}</p>
				</div>
			</div>
		</c:if>
		<div class="form-group row">
					<label for="email" class="control-label col-2">Email:</label>
					<input type="text" class="form-control col-2" id="email" name="email" value="${email}">
		</div>
		<div class="row">
					<label for="password" class="control-label col-2">Password:</label>
					<input type="password" class="form-control col-2" id="password" name="password" value="${password}">
		</div>
		<div class="row">
			<div class="col">
				<button type="submit" class="btn btn-info">Submit</button>
			</div>
		</div>
	</form>
	<p class="note">Don't have an account? <a href="./create_account">Create an Account</a></p>
	<p class="note">Forgot your password? <a href="./reset_password">Reset password</a></p>
</div>
</body></html>