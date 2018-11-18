<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
	<title>CTM mkii - Edit SOP</title>
	<link rel="stylesheet" href="css/bootstrap.css">
</head>
<body>
<p class="note">Want to return to the Homepage? <a href="./user_home">Home</a></p>
<div class="fluid-container">
	<div class="row">
		<div class="col-6">
			<h1>Current SOP Details</h1>
		</div>
		<div class="col-6 text-right">
			<form class="form-horizontal" method="post">
				<!-- TODO: Check archived vs not archived -->
				<button type="submit" name="doStuff" value="archiveSOP" class="btn btn-danger">Archive SOP</button>
			</form>
		</div>
	</div>
	<div class="row">
		<div class="col">
			ID: ${oldID}
		</div>
		<div class="col">
			Title: ${oldTitle}
		</div>
		<div class="col">
			Version: ${oldVersion}
		</div>
		<div class="col">
			Author ID: ${authorID}
		</div>
		<div class="col">
			Priority: ${oldPriority}
		</div>
	</div>
	<div class="row">
		<div class="col-2">
			Description: 
		</div>
		<div class="col-10">
			${oldDescription}
		</div>
	</div>
	<h2>Change Basic Details</h2>
	<form class="form-horizontal" method="post">
		<div class="row">
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
				<c:choose>
					<c:when test="${! empty priorityError}">
						<p class="alert alert-warning">${priorityError}</p>
					</c:when>
					<c:when test="${! empty priorityValueError}">
						<p class="alert alert-warning">${priorityValueError}</p>
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
				<c:choose>
					<c:when test="${! empty versionError}">
						<p class="alert alert-warning">${versionError}</p>
					</c:when>
					<c:when test="${! empty versionError}">
						<p class="alert alert-warning">${versionValueError}</p>
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
					<label for="newVersion" class="control-label col-2">New Version:</label>
					<input type="number" class="form-control col-10" id="newVersion" name="newVersion" value="${newVersion}">
				</div>
			</div>
			<div class="col">
				<div class="form-group row">
					<label for="newVersionConfirmation" class="control-label col-2">Confirm Version :</label>
					<input type="number" class="form-control col-10" id="newPriorityConfirmation" name="newVersionConfirmation" value="${newVersionConfirmation}">
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col">
				<button type="submit" name="doStuff" value="changeVersion" class="btn btn-info">Change Version</button>
			</div>
		</div>
	</form>
	<form class="form-horizontal" method="post">
		<div class="row">
			<div class="col">
				<h2>Change Description</h2>
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
		</div>
		<div class="row">
			<div class="col">
				<button type="submit" name="doStuff" value="changeDescription" class="btn btn-info">Change Description</button>
			</div>
		</div>
	</form>
</div>
</body>
</html>