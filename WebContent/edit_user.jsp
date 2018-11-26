<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
	<title>CTM mkii - Edit User</title>
	<link rel="stylesheet" href="css/bootstrap.css">
</head>
<body>
<p class="note">Want to return to the Homepage? <a href="./user_home">Home</a></p>
<div class="fluid-container">
	<div class="row">
		<div class="col">
			<c:if test="${! empty successMessage}">
				<p class="alert alert-success">${successMessage}</p>
			</c:if>
		</div>
	</div>
	<div class="row">
		<div class="col-4">
			<h1>Current User Details</h1>
		</div>
		<div class="col-4 text-right">
			<form class="form-horizontal" method="post">
				<input type="hidden" name="user_id" value="${userID}">
				<!-- TODO: Check archived vs not archived -->
				<c:if test="${archived == false}">
					<button type="submit" name="editType" value="archiveUser" class="btn btn-danger">Archive User</button>
				</c:if>

				<c:if test="${archived == true}">
					<button type="submit" name="editType" value="unarchiveUser" class="btn btn-danger">Unarchive User</button>
				</c:if>
			</form>
			
		</div>
		<div class = col-1>
		</div>
		<!-- set manual lock out conditions -->
		<div class="col-6 text-right">
			<form class="form-horizontal" method="post">
				<input type="hidden" name="user_id" value="${userID}">
				<!-- Check if the user is locked out -->
				<c:if test="${locked_out == true}">
					<button type="submit" name="editType" value="overTurnLockout" class="btn btn-danger">Unlock User</button>
				</c:if>
			</form>
		</div>
	</div>
	<div class="row">
		<div class="col">
			ID: ${userID}
		</div>
		<div class="col">
			Email: ${email}
		</div>
		<div class="col">
			Firstname: ${firstname}
		</div>
		<div class="col">
			Lastname: ${lastname}
		</div>
		<div class="col">
			Position Title: ${position_title}
		</div>
		
		<div class = "col">
			Lockout Status: ${locked_out}
		</div>
	</div>
	
	<h2>Advanced Admin Functions</h2>
	<form class="form-horizontal" method="post">
		<input type="hidden" name="user_id" value="${userID}">
		<div class="row">
			<div class="col-1">
			
			</div>
			<div class="col-5">
				<c:if test="${!empty sopIDError}">
					<p class="alert alert-warning">${sopIDError}</p>
				</c:if>
			</div>
			<div class="col-1">
			</div>	
		</div>
		
		<div class="row">
			<div class="col">
				<div class="form-group row">
					<label for="newTitle" class="control-label col-2">SOP ID:</label>
					<input type="number" class="form-control col-5" id="sop_ID" name="sop_ID" value="${sop_ID}">
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col">
				<button type="submit" name="editType" value="assignSOP" class="btn btn-info">Assign SOP</button>
			</div>
		</div>
	</form>
	
	<div class = col 3>
	</div>
	<form class="form-horizontal" method="post">
		<input type="hidden" name="user_id" value="${userID}">
		<div class="row">
			<div class="col-1">
			
			</div>
			<div class="col-5">
				<c:if test="${!empty positionIDError}">
					<p class="alert alert-warning">${positionIDError}</p>
				</c:if>
			</div>
			<div class="col-1">
			
			</div>
		</div>
		<div class="row">
			<div class="col">
				<div class="form-group row">
					<label for="newPositionID" class="control-label col-2">New Position ID:</label>
					<input type="number" class="form-control col-5" id="newPositionID" name="newPositionID" value="${newPositionID}">
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col">
				<button type="submit" name="editType" value="changePosition" class="btn btn-info">Change Position</button>
			</div>
		</div>
	</form>
</div>
</body>
</html>