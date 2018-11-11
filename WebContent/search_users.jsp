<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
	<title>CTM mkii - Search Users</title>
	<link rel="stylesheet" href="css/bootstrap.css">
</head>
<body>
<h1>Search Users</h1>
<div class="fluid-container">
	<form class="form-horizontal" method="post">
		<div class="form-group row">
			<div class="fieldset col-3">
				<input type="number" class="form-control" id="userID" name="userID" value="${userID}" placeholder="User ID">
			</div>
			<div class="fieldset col-3">
				<input type="number" class="form-control" id="employeeID" name="employeeID" value="${employeeID}" placeholder="Employee ID">
			</div>
			<div class="fieldset col-3">
				<input type="text" class="form-control" id="email" name="email" value="${email}" placeholder="Email">
			</div>
		</div>
		<div class="form-group row">
			<div class="fieldset col-3">
				<input type="text" class="form-control" id="firstName" name="firstName" value="${firstName}" placeholder="First Name">
			</div>
			<div class="fieldset col-3">
				<input type="text" class="form-control" id="lastName" name="lastName" value="${lastName}" placeholder="Last Name">
			</div>
			<div class="fieldset col-3">
				<input type="number" class="form-control" id="positionID" name="positionID" value="${positionID}" placeholder="Position ID">
			</div>
			<div class="fieldset col-3">
				<button type="submit" class="btn btn-info">Search</button>
			</div>
		</div>
	</form>
	<c:if test="${!empty userID1}">
		<div class="row">
			<div class="col-3">
				<p><b>UID / EID</b></p>
			</div>
			<div class="col-3">
				<p><b>Email</b></p>
			</div>
			<div class="col-3">
				<p><b>Name</b></p>
			</div>
			<div class="col-3">
				<p><b>Position Title</b></p>
			</div>
		</div>
		<div class="row">
			<div class="col-3">
				<p>${userID1} / ${employeeID1}</p>
			</div>
			<div class="col-3">
				<p>${email1}</p>
			</div>
			<div class="col-3">
				<p>${firstName1} ${lastName1}</p>
			</div>
			<div class="col-3">
				<p>${posTitle1}</p>
			</div>
		</div>
	</c:if>
</div>
</body>
</html>