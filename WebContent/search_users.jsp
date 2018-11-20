<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

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
		<div class="row">
			<div class="col-1">
			
			</div>
			<div class="col">
				Showing results ${(page*displaySize) + 1} - ${fn:length(users) lt (((page+1)*displaySize) - 1)?fn:length(users):(((page+1)*displaySize) - 1)} of ${fn:length(users)}
			</div>
		</div>
		<div class="row">
			<div class="col-1">
			
			</div>
			<div class="col-2 text-center">
				<p><b>UID / EID</b></p>
			</div>
			<div class="col-3 text-center">
				<p><b>Email</b></p>
			</div>
			<div class="col-3 text-center">
				<p><b>Name</b></p>
			</div>
			<div class="col-3 text-center">
				<p><b>Position Title</b></p>
			</div>
		</div>
		<c:forEach begin="${page*displaySize}" end="${((page+1)*displaySize) - 1}" items="${users}" var="current">
			<div class="row">
				<div class="col-1 text-center">
					<p><a href="edit_user?userID=${current.ID}">Edit</a>
				</div>
				<div class="col-2 text-center">
					<p>${current.ID} / ${current.employeeID}</p>
				</div>
				<div class="col-3 text-center">
					<p>${current.email}</p>
				</div>
				<div class="col-3 text-center">
					<p>${current.firstName} ${current.lastName}</p>
				</div>
				<div class="col-3 text-center">
					<p>${current.position.title}</p>
				</div>
			</div>
		</c:forEach>
		<input type="hidden" name="page" value="${page}">
		<input type="hidden" name="displaySize" value="${displaySize}">
		<div class="row">
			<div class="col-2 text-center">
				<c:if test="${page gt 0}">
					<button type="submit" name="changePage" value="prev" class="btn btn-info">Prev Page</button>
				</c:if>
			</div>
			<div class="col-2 text-right">
				Results Per Page:
			</div>
			<c:if test="${displaySize != 5}">
				<div class="col-1">
						<button type="submit" name="changeDisplaySize" value="5" class="btn btn-info">5</button>
				</div>
			</c:if>
			<c:if test="${displaySize != 10}">
				<div class="col-1">
						<button type="submit" name="changeDisplaySize" value="10" class="btn btn-info">10</button>
				</div>
			</c:if>
			<c:if test="${displaySize != 25}">
				<div class="col-1">
						<button type="submit" name="changeDisplaySize" value="25" class="btn btn-info">25</button>
				</div>
			</c:if>
			<c:if test="${displaySize != 50}">
				<div class="col-1">
						<button type="submit" name="changeDisplaySize" value="50" class="btn btn-info">50</button>
				</div>
			</c:if>
			<c:if test="${displaySize != 100}">
				<div class="col-1">
						<button type="submit" name="changeDisplaySize" value="100" class="btn btn-info">100</button>
				</div>
			</c:if>
			<div class="col-2 text-center">
				<c:if test="${fn:length(users) gt (displaySize*(page + 1))}">
					<button type="submit" name="changePage" value="next" class="btn btn-info">Next Page</button>
				</c:if>
			</div>
		</div>
	</form>
</div>
</body>
</html>