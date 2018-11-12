<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
	<title>CTM mkii - Search Users</title>
	<link rel="stylesheet" href="css/bootstrap.css">
</head>
<body>
<h1>Search Users</h1>
<p class="note">Want to return to the Homepage? <a href="./user_home">Home</a></p>
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
	<!-- TODO: Find a better way -->
	<c:if test="${!empty userID1}">
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
	<c:if test="${!empty userID2}">
		<div class="row">
			<div class="col-3">
				<p>${userID2} / ${employeeID2}</p>
			</div>
			<div class="col-3">
				<p>${email2}</p>
			</div>
			<div class="col-3">
				<p>${firstName2} ${lastName2}</p>
			</div>
			<div class="col-3">
				<p>${posTitle2}</p>
			</div>
		</div>
	</c:if>
	<c:if test="${!empty userID3}">
		<div class="row">
			<div class="col-3">
				<p>${userID3} / ${employeeID3}</p>
			</div>
			<div class="col-3">
				<p>${email3}</p>
			</div>
			<div class="col-3">
				<p>${firstName3} ${lastName3}</p>
			</div>
			<div class="col-3">
				<p>${posTitle3}</p>
			</div>
		</div>
	</c:if>
	<c:if test="${!empty userID4}">
		<div class="row">
			<div class="col-3">
				<p>${userID4} / ${employeeID4}</p>
			</div>
			<div class="col-3">
				<p>${email4}</p>
			</div>
			<div class="col-3">
				<p>${firstName4} ${lastName4}</p>
			</div>
			<div class="col-3">
				<p>${posTitle4}</p>
			</div>
		</div>
	</c:if>
	<c:if test="${!empty userID5}">
		<div class="row">
			<div class="col-3">
				<p>${userID5} / ${employeeID5}</p>
			</div>
			<div class="col-3">
				<p>${email5}</p>
			</div>
			<div class="col-3">
				<p>${firstName5} ${lastName5}</p>
			</div>
			<div class="col-3">
				<p>${posTitle5}</p>
			</div>
		</div>
	</c:if>
	<c:if test="${!empty userID6}">
		<div class="row">
			<div class="col-3">
				<p>${userID6} / ${employeeID6}</p>
			</div>
			<div class="col-3">
				<p>${email6}</p>
			</div>
			<div class="col-3">
				<p>${firstName6} ${lastName6}</p>
			</div>
			<div class="col-3">
				<p>${posTitle6}</p>
			</div>
		</div>
	</c:if>
	<c:if test="${!empty userID7}">
		<div class="row">
			<div class="col-3">
				<p>${userID7} / ${employeeID7}</p>
			</div>
			<div class="col-3">
				<p>${email7}</p>
			</div>
			<div class="col-3">
				<p>${firstName7} ${lastName7}</p>
			</div>
			<div class="col-3">
				<p>${posTitle7}</p>
			</div>
		</div>
	</c:if>
	<c:if test="${!empty userID8}">
		<div class="row">
			<div class="col-3">
				<p>${userID8} / ${employeeID8}</p>
			</div>
			<div class="col-3">
				<p>${email8}</p>
			</div>
			<div class="col-3">
				<p>${firstName8} ${lastName8}</p>
			</div>
			<div class="col-3">
				<p>${posTitle8}</p>
			</div>
		</div>
	</c:if>
	<c:if test="${!empty userID9}">
		<div class="row">
			<div class="col-3">
				<p>${userID9} / ${employeeID9}</p>
			</div>
			<div class="col-3">
				<p>${email9}</p>
			</div>
			<div class="col-3">
				<p>${firstName9} ${lastName9}</p>
			</div>
			<div class="col-3">
				<p>${posTitle9}</p>
			</div>
		</div>
	</c:if>
	<c:if test="${!empty userID10}">
		<div class="row">
			<div class="col-3">
				<p>${userID10} / ${employeeID10}</p>
			</div>
			<div class="col-3">
				<p>${email10}</p>
			</div>
			<div class="col-3">
				<p>${firstName10} ${lastName10}</p>
			</div>
			<div class="col-3">
				<p>${posTitle10}</p>
			</div>
		</div>
	</c:if>
</div>
</body>
</html>