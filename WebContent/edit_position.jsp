<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
	<title>CTM mkii - Edit Position</title>
	<link rel="stylesheet" href="css/bootstrap.css">
</head>
<body>
<p class="note">Want to return to the Homepage? <a href="./user_home">Home</a></p>

<form class="form-horizontal" method="post">
		<div class="row">
			<div class="col">
				<h3>Search for Position</h3>
			</div>
		</div>
		<div class="col">
				<div class="form-group row">
						<input type="number" class="form-control" id="position_id" name="position_id" value="${position_id}" placeholder="Position ID">				
				</div>
			</div>
		<div class="row">
			<div class="col">
				<button type="submit" class="btn btn-info">Search Position</button>
			</div>
		</div>
	</form>

<h1>Position Details</h1>
<div class="fluid-container">
	<form class="form-horizontal" method="post">
		<div class="row">
			<div class="col">
				<h3>Change Title</h3>
			</div>
		</div>
		<div class="row">
			<div class="col">
				<div class="row">
					<div class="col">
						Current Title:
					</div>
					<div class="col">
						${title}
					</div>
				</div>
			</div>
			<div class="col">
				<c:choose>
					<c:when test="${! empty changeTitleError}">
						<p class="alert alert-warning">${changeTitleError}</p>
					</c:when>
					<c:when test="${! empty SuccessMessage}">
						<p class="alert alert-success">${SuccessMessage}</p>
					</c:when>
				</c:choose>
			</div>
		</div>
		<div class="row">
			<div class="col">
				<div class="form-group row">
					<label for="newTitle" class="control-label col-2">New Title:</label>
					<input type="text" class="form-control col-10" id="newTitle" name="newTitle" value="${newTitle}">
				</div>
			</div>
			<div class="col">
				<div class="form-group row">
					<label for="newTitleConfirm" class="control-label col-2">Confirm Title:</label>
					<input type="text" class="form-control col-10" id="newTitleConfirm" name="newTitleConfirm" value="${newTitleConfirm}">
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col">
				<button type="submit" name="doStuff" value="changeTitle" class="btn btn-info">Change Title</button>
			</div>
		</div>
	</form>
	
	<form class="form-horizontal" method="post">
		<div class="row">
			<div class="col">
				<h3>Change Description</h3>
			</div>
		</div>
		<div class="row">
					<div class="col">
						Current Description:
					</div>
					<div class="col">
						${description}
					</div>
				</div>
		<div class="row">
			<div class="col">
				<c:choose>
					<c:when test="${! empty changeDescriptionError}">
						<p class="alert alert-warning">${changeDescriptionError}</p>
					</c:when>
					<c:when test="${! empty SuccessMessage}">
						<p class="alert alert-success">${SuccessMessage}</p>
					</c:when>
				</c:choose>
			</div>
		</div>
		<div class="row">
			<div class="col">
				<div class="form-group row">
					<label for="newDescription" class="control-label col-2">New Description: </label>
					<textarea rows = "4" cols = "50" name = "newDescription"></textarea>
				</div>
			</div>
			<div class="col">
				<div class="form-group row">
					<label for="newDescriptionConfirmation" class="control-label col-2">Confirm Description:</label>
					<textarea rows = "4" cols = "50" name = "newDescriptionConfirmation"></textarea>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col">
				
			</div>
	
			<div class="col">
				
			</div>
		</div>
		<div class="row">
			<div class="col">
				<button type="submit" name="doStuff" value="changeDescription" class="btn btn-info">Change Description</button>
			</div>
		</div>
	</form>
	
	<form class="form-horizontal" method="post">
		<div class="row">
			<div class="col">
				<h3>Change Priority</h3>
			</div>
		</div>
		<div class="row">
				<div class="col">
					Current Priority:
				</div>
				<div class="col">
					${priority}
				</div>
		</div>
		
		<div class="row">
			<div class="col">
				<c:choose>
					<c:when test="${! empty priorityError}">
						<p class="alert alert-warning">${priorityError}</p>
					</c:when>
					<c:when test="${! empty SuccessMessage}">
						<p class="alert alert-success">${SuccessMessage}</p>
					</c:when>
				</c:choose>
			</div>
		</div>
		
		<div class="row">
			<div class="col">
				<div class="form-group row">
					<label for="newPriority" class="control-label col-2">New Priority:</label>
					<input type="number" class="form-control col-10" id="newPriority" name="newPriority" value="${newPriority}">
				</div>
			</div>
			<div class="col">
				<div class="form-group row">
					<label for="newTitleConfirm" class="control-label col-2">Confirm Priority :</label>
					<input type="number" class="form-control col-10" id="newPriorityConfirmation" name="newPriorityConfirmation" value="${newPriorityConfirmation}">
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col">
				<button type="submit" name="doStuff" value="changePriority" class="btn btn-info">Change Priority</button>
			</div>
		</div>

	</form>
	
	<form class="form-horizontal" method="post">
		<div class="row">
			<div class="col">
				<h3>Delete Position</h3>
			</div>
		</div>
		
		<div class="row">
			<div class="col">
				<button type="submit" name="doStuff" value="deletePosition" class="btn btn-info">Delete Position</button>
			</div>
		</div>
	</form>
	
</div>
</body>
</html>