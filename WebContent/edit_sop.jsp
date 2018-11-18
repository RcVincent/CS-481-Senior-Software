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
		<div class="col">
			<c:if test="${! empty successMessage}">
				<p class="alert alert-success">${successMessage}</p>
			</c:if>
		</div>
	</div>
	<div class="row">
		<div class="col-6">
			<h1>Current SOP Details</h1>
		</div>
		<div class="col-6 text-right">
			<form class="form-horizontal" method="post">
				<input type="hidden" name="sopID" value="${sopID}">
				<!-- TODO: Check archived vs not archived -->
				<c:if test="${archived == false}">
					<button type="submit" name="doStuff" value="archiveSOP" class="btn btn-danger">Archive SOP</button>
				</c:if>
				<c:if test="${archived == true}">
					<button type="submit" name="doStuff" value="unarchiveSOP" class="btn btn-danger">Unarchive SOP</button>
				</c:if>
			</form>
		</div>
	</div>
	<div class="row">
		<div class="col">
			ID: ${sopID}
		</div>
		<div class="col">
			Title: ${title}
		</div>
		<div class="col">
			Version: ${version}
		</div>
		<div class="col">
			Author ID: ${authorID}
		</div>
		<div class="col">
			Priority: ${priority}
		</div>
	</div>
	<div class="row">
		<div class="col-2">
			Description: 
		</div>
		<div class="col-10">
			${description}
		</div>
	</div>
	<h2>Change Basic Details</h2>
	<form class="form-horizontal" method="post">
		<input type="hidden" name="sopID" value="${sopID}">
		<div class="row">
			<div class="col-1">
			
			</div>
			<div class="col-5">
				<c:if test="${!empty titleError}">
					<p class="alert alert-warning">${titleError}</p>
				</c:if>
			</div>
			<div class="col-1">
				
			</div>
			<div class="col-5">
				<c:if test="${!empty titleConfirmError}">
					<p class="alert alert-warning">${titleConfirmError}</p>
				</c:if>
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
		<input type="hidden" name="sopID" value="${sopID}">
		<div class="row">
			<div class="col-1">
			
			</div>
			<div class="col-5">
				<c:if test="${!empty priorityError}">
					<p class="alert alert-warning">${priorityError}</p>
				</c:if>
			</div>
			<div class="col-1">
			
			</div>
			<div class="col-5">
				<c:if test="${!empty priorityConfirmError}">
					<p class="alert alert-warning">${priorityConfirmError}</p>
				</c:if>
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
					<input type="number" class="form-control col-10" id="newPriorityConfirm" name="newPriorityConfirm" value="${newPriorityConfirm}">
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
		<input type="hidden" name="sopID" value="${sopID}">
		<div class="row">
			<div class="col-1">
			
			</div>
			<div class="col-5">
				<c:if test="${!empty versionError}">
					<p class="alert alert-warning">${versionError}</p>
				</c:if>
			</div>
			<div class="col-1">
			
			</div>
			<div class="col-5">
				<c:if test="${!empty versionConfirmError}">
					<p class="alert alert-warning">${versionConfirmError}</p>
				</c:if>
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
					<input type="number" class="form-control col-10" id="newPriorityConfirm" name="newVersionConfirm" value="${newVersionConfirm}">
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
		<input type="hidden" name="sopID" value="${sopID}">
		<div class="row">
			<div class="col">
				<h2>Change Description</h2>
			</div>
		</div>
		<div class="row">
			<div class="col-1">
			
			</div>
			<div class="col-5">
				<c:if test="${!empty descriptionError}">
					<p class="alert alert-warning">${changeDescriptionError}</p>
				</c:if>
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