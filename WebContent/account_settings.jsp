<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
	<title>CTM mkii - Account Settings</title>
	<link rel="stylesheet" href="css/bootstrap.css">
</head>
<body>
<h1>Account Settings</h1>
<div class="fluid-container">
	<form class="form-horizontal" method="post">
		<div class="row">
			<div class="col">
				<h3>Change Email</h3>
			</div>
		</div>
		<div class="row">
			<div class="col">
				<div class="row">
					<div class="col">
						Current Email:
					</div>
					<div class="col">
						${email}
					</div>
				</div>
			</div>
			<div class="col">
				<c:if test="${! empty changeEmailError}">
					<p class="alert alert-warning">${changeEmailError}</p>
				</c:if>
			</div>
		</div>
		<div class="row">
			<div class="col">
				<div class="form-group row">
					<label for="newEmail" class="control-label col-2">New Email:</label>
					<input type="email" class="form-control col-10" id="newEmail" name="newEmail" value="${newEmail}">
				</div>
			</div>
			<div class="col">
				<div class="form-group row">
					<label for="newEmailConfirm" class="control-label col-2">Confirm Email:</label>
					<input type="email" class="form-control col-10" id="newEmailConfirm" name="newEmailConfirm" value="${newEmailConfirm}">
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col">
				
			</div>
			<div class="col-6">
				<div class="form-group row">
					<label for="emailPass" class="control-label col-2">Password:</label>
					<input type="password" class="form-control col-10" id="emailPass" name="emailPass" value="${emailPass}">
				</div>
			</div>
			<div class="col">
				
			</div>
		</div>
		<div class="row">
			<div class="col">
				<button type="submit" name="doStuff" value="changeEmail" class="btn btn-info">Change Email</button>
			</div>
		</div>
	</form>
</div>
</body>
</html>