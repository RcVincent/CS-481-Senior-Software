<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
	<title>CTM mkii - Managers Only</title>
	<link rel="stylesheet" href="css/bootstrap.css">
</head>
<p class="note">Want to return to the Homepage? <a href="./user_home">Home</a></p>
<body>
<h1>Manage Users</h1>
<div class="fluid-container">
	<div class="row">
		<div class="col">
			<c:if test="${! empty successMessage}">
				<p class="alert alert-success">${successMessage}</p>
			</c:if>
		</div>
	</div>
	
	<form class="form-horizontal" method="post">
		<input type="hidden" name="user_id" value="${userID}">
		<div class="row">
			<div class="col-1">
			</div>
			<div class="col-1">
			</div>	
		</div>
		
		<div class="row">
			<div class="col">
				<div class="form-group row">
					<label for="newTitle" class="control-label col-2">USer ID:</label>
					<input type="number" class="form-control col-5" id="user_ID" name="user_ID" value="${user_ID}">
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col">
				<button type="submit" name="managerEdit" value="addSubordinate" class="btn btn-info">Add Subordinate</button>
			</div>
		</div>
	</form>
	
	 
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
				<button type="submit" name="managerEdit" value="assignSOP" class="btn btn-info">Assign SOP</button>
			</div>
		</div>
	</form> 
	
	<form class="form-horizontal" method="post">
		<input type="hidden" name="user_id" value="${userID}">
		<div class="row">
			<div class="col-1">
			
			</div>
			<div class="col-5">
				<c:if test="${!empty SubjectError}">
					<p class="alert alert-warning">${SubjectError}</p>
				</c:if>
			</div>
			<div class="col-1">
			</div>	
		</div>
		
		<div class="row">
			<div class="col">
				<div class="form-group row">
					<label for="newTitle" class="control-label col-2">Message Subject:</label>
					<input type="text" class="form-control col-5" id="subject" name="subject" value="${subject}">
				</div>
			</div>
		</div>
		
		<div class="row">
			<div class="col">
				<div class="form-group row">
					<label for="newTitle" class="control-label col-2">Message Contents:</label>
					<textarea rows = "4" cols = "50" name = "newDescription"></textarea>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col">
				<button type="submit" name="managerEdit" value="messageSubordinate" class="btn btn-info">Message User</button>
			</div>
		</div>
	</form> 
	
</div>
</body>
</html>