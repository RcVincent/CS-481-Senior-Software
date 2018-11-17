<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
	<title>CTM mkii - Create SOP</title>
	<link rel="stylesheet" href="css/bootstrap.css">
</head>
<body>
<h1>Create SOP</h1>
<div class="container-fluid">
	<form class="form-horizontal" method="post">
		<div class="row">
			<div class="col">
				<c:if test="${! empty titleError}">
					<p class="alert alert-warning">${titleError}</p>
				</c:if>
			</div>
			<div class="col">
				<c:if test="${! empty descriptionError}">
					<p class="alert alert-warning">${descriptionError}</p>
				</c:if>
			</div>
		</div>
		<div class="row">
			<div class="col">
				<div class="form-group row">
					<label for="title" class="control-label col-2">SOP Title:</label>
					<input type="text" class="form-control col-10" id="title" name="title" value="${title}">
				</div>
			</div>
			<div class="col">
				<div class="form-group row">
					<label for="description" class="control-label col-2">SOP Description:</label>
					<textarea rows = "4" cols = "50" name = "description"></textarea>
					<br>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col">
				<c:if test="${! empty priorityError}">
					<p class="alert alert-warning">${priorityError}</p>
				</c:if>
			</div>
			<div class="col">
				<c:if test="${! empty versionError}">
					<p class="alert alert-warning">${versionError}</p>
				</c:if>
			</div>
		</div>
		<div class="row">
			<div class="col">
				<div class="form-group row">
					<label for="priority" class="form-label col-2">Priority (1-10):</label>
					<input type="number" class="form-control col-10" id="priority" name="priority" value="${priority}">
				</div>
			</div>
			<div class="col">
				<div class="form-group row">
					<label for="emailConfirm" class="form-label col-2">Version:</label>
					<input type="number" class="form-control col-10" id="revision" name="revision" value="${revision}">
				</div>
			</div>
		</div>
		
		<div class="row">
			<div class="col">
				<button type="submit" class="btn btn-info">Submit</button>
			</div>
		</div>
	</form>
</div>
<p class="note">Want to return to the Homepage? <a href="./user_home">Home</a></p>

</body></html>