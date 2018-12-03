<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
	<title>CTM mkii - Complete SOP</title>
	<link rel="stylesheet" href="css/bootstrap.css">
</head>

<body>
<p class="note">Want to return to the Homepage? <a href="./user_home">Home</a></p>
<div class="fluid-container">
	<div class="row">
		<div class="col">
			<c:if test="${! empty sopComplete}">
				<p class="alert alert-success">${sopComplete}</p>
			</c:if>
		</div>
	</div>
	<div class="col-6">
		<h1>Current SOP Details</h1>
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
			Priority: ${priority}
		</div>
	</div>
	
	
	<div class="row">
			<div class="col">
				<button type="submit" name="sopAction" value="completeSOP" class="btn btn-info">Complete SOP</button>
			</div>
	</div>
	
		
	<div class="row">
			<div class="col">
				<button type="submit" name="sopAction" value="return" class="btn btn-info">Back To Profile</button>
			</div>
	</div>
</div>
</body>
</html> 