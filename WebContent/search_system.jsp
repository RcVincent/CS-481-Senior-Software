<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- The Pinkerton JSP -->
<html>

<head>
	<title>CTM mkii - The Pinkerton System Searcher</title>
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
	
	
	<form class="form-horizontal" method="post">
		<div class="col">
			<button type="submit" name="WhatToSniff" value="searchAll" class="btn btn-danger">Search System</button>
		</div>	
	</form>  

	<form class="form-horizontal" method="post">
		<input type="hidden" name="user_id" value="${userID}">
		<div class="row">
			<div class="col-1">
			
			</div>
			<div class="col-5">
				<c:if test="${!empty historyNotEmpty}">
					<p class="alert alert-warning">${historyNotEmpty}</p>
				</c:if>
			</div>
			<div class="col-1">
			
			</div>
		</div>
		<div class="row">
			<div class="col">
				<div class="form-group row">
					<label for="newPositionID" class="control-label col-2">User ID:</label>
					<input type="number" class="form-control col-5" id="user_id" name="user_id" value="${user_id}">
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col">
				<button type="submit" name="WhatToSniff" value="searchSpecific" class="btn btn-info">Search User History</button>
			</div>
		</div>
	</form>
	
</div>
</body>	
	
</html>