<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
	<title>CTM mkii - Search Positions</title>
	<link rel="stylesheet" href="css/bootstrap.css">
</head>
<body>
<h1>Search Positions</h1>
<p class="note">Want to return to the Homepage? <a href="./user_home">Home</a></p>
<div class="fluid-container">
	<form class="form-horizontal" method="post">
		<div class="form-group row">
			<div class="fieldset col-4">
				<input type="number" class="form-control" id="positionID" name="positionID" value="${positionID}" placeholder="Position ID">
			</div>
			<div class="fieldset col-4">
				<input type="text" class="form-control" id="title" name="title" value="${title}" placeholder="Title">
			</div>
		</div>
		<div class="form-group row">
			<div class="fieldset col-4">
				<input type="text" class="form-control" id="description" name="description" value="${description}" placeholder="Description">
			</div>
			<div class="fieldset col-4">
				<input type="number" class="form-control" id="priority" name="priority" value="${priority}" placeholder="Priority">
			</div>
			<div class="fieldset col-4">
				<button type="submit" class="btn btn-info">Search</button>
			</div>
		</div>
	</form>
	<div class="row">
		<div class="col-3">
			<p><b>Position ID</b></p>
		</div>
		<div class="col-3">
			<p><b>Title</b></p>
		</div>
		<div class="col-3">
			<p><b>Description</b></p>
		</div>
		<div class="col-3">
			<p><b>Priority</b></p>
		</div>
	</div>
	<!-- TODO: Find a better way -->
	<c:if test="${!empty positionID1}">
		<div class="row">
			<div class="col-3">
				<p>${positionID1}</p>
			</div>
			<div class="col-3">
				<p>${title1}</p>
			</div>
			<div class="col-3">
				<p>${description1}</p>
			</div>
			<div class="col-3">
				<p>${priority1}</p>
			</div>
		</div>
	</c:if>
	<c:if test="${!empty positionID2}">
		<div class="row">
			<div class="col-3">
				<p>${positionID2}</p>
			</div>
			<div class="col-3">
				<p>${title2}</p>
			</div>
			<div class="col-3">
				<p>${description2}</p>
			</div>
			<div class="col-3">
				<p>${priority2}</p>
			</div>
		</div>
	</c:if>
	<c:if test="${!empty positionID3}">
		<div class="row">
			<div class="col-3">
				<p>${positionID3}</p>
			</div>
			<div class="col-3">
				<p>${title3}</p>
			</div>
			<div class="col-3">
				<p>${description3}</p>
			</div>
			<div class="col-3">
				<p>${priority3}</p>
			</div>
		</div>
	</c:if>
	<c:if test="${!empty positionID4}">
		<div class="row">
			<div class="col-3">
				<p>${positionID4}</p>
			</div>
			<div class="col-3">
				<p>${title4}</p>
			</div>
			<div class="col-3">
				<p>${description4}</p>
			</div>
			<div class="col-3">
				<p>${priority4}</p>
			</div>
		</div>
	</c:if>
	<c:if test="${!empty positionID5}">
		<div class="row">
			<div class="col-3">
				<p>${positionID5}</p>
			</div>
			<div class="col-3">
				<p>${title5}</p>
			</div>
			<div class="col-3">
				<p>${description5}</p>
			</div>
			<div class="col-3">
				<p>${priority5}</p>
			</div>
		</div>
	</c:if>
	<c:if test="${!empty positionID6}">
		<div class="row">
			<div class="col-3">
				<p>${positionID6}</p>
			</div>
			<div class="col-3">
				<p>${title6}</p>
			</div>
			<div class="col-3">
				<p>${description6}</p>
			</div>
			<div class="col-3">
				<p>${priority6}</p>
			</div>
		</div>
	</c:if>
	<c:if test="${!empty positionID7}">
		<div class="row">
			<div class="col-3">
				<p>${positionID7}</p>
			</div>
			<div class="col-3">
				<p>${title7}</p>
			</div>
			<div class="col-3">
				<p>${description7}</p>
			</div>
			<div class="col-3">
				<p>${priority7}</p>
			</div>
		</div>
	</c:if>
	<c:if test="${!empty positionID8}">
		<div class="row">
			<div class="col-3">
				<p>${positionID8}</p>
			</div>
			<div class="col-3">
				<p>${title8}</p>
			</div>
			<div class="col-3">
				<p>${description8}</p>
			</div>
			<div class="col-3">
				<p>${priority8}</p>
			</div>
		</div>
	</c:if>
	<c:if test="${!empty positionID9}">
		<div class="row">
			<div class="col-3">
				<p>${positionID9}</p>
			</div>
			<div class="col-3">
				<p>${title9}</p>
			</div>
			<div class="col-3">
				<p>${description9}</p>
			</div>
			<div class="col-3">
				<p>${priority9}</p>
			</div>
		</div>
	</c:if>
	<c:if test="${!empty positionID10}">
		<div class="row">
			<div class="col-3">
				<p>${positionID10}</p>
			</div>
			<div class="col-3">
				<p>${title10}</p>
			</div>
			<div class="col-3">
				<p>${description10}</p>
			</div>
			<div class="col-3">
				<p>${priority10}</p>
			</div>
		</div>
	</c:if>
</div>
</body>
</html>