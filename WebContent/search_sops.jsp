<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
	<title>CTM mkii - Search SOPs</title>
	<link rel="stylesheet" href="css/bootstrap.css">
</head>
<body>
<h1>Search SOPs</h1>
<div class="fluid-container">
	<form class="form-horizontal" method="post">
		<div class="form-group row">
			<div class="fieldset col-3">
				<input type="number" class="form-control" id="sopID" name="sopID" value="${sopID}" placeholder="SOP ID">
			</div>
			<div class="fieldset col-3">
				<input type="text" class="form-control" id="title" name="title" value="${title}" placeholder="Title">
			</div>
			<div class="fieldset col-3">
				<input type="text" class="form-control" id="description" name="description" value="${description}" placeholder="Description">
			</div>
		</div>
		<div class="form-group row">
			<div class="fieldset col-3">
				<input type="number" class="form-control" id="priority" name="priority" value="${priority}" placeholder="Priority">
			</div>
			<div class="fieldset col-3">
				<input type="number" class="form-control" id="version" name="version" value="${version}" placeholder="Version">
			</div>
			<div class="fieldset col-3">
				<input type="number" class="form-control" id="authorID" name="authorID" value="${authorID}" placeholder="Author ID">
			</div>
			<div class="fieldset col-3">
				<button type="submit" class="btn btn-info">Search</button>
			</div>
		</div>
	</form>
	<div class="row">
		<div class="col-3">
			<p><b>SOP ID / Version / Author ID</b></p>
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
	<c:if test="${!empty sopID1}">
		<div class="row">
			<div class="col-3">
				<p>${sopID1} / ${version1} / ${authorID1}</p>
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
	<c:if test="${!empty sopID2}">
		<div class="row">
			<div class="col-3">
				<p>${sopID2} / ${version2} / ${authorID2}</p>
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
	<c:if test="${!empty sopID3}">
		<div class="row">
			<div class="col-3">
				<p>${sopID3} / ${version3} / ${authorID3}</p>
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
	<c:if test="${!empty sopID4}">
		<div class="row">
			<div class="col-3">
				<p>${sopID4} / ${version4} / ${authorID4}</p>
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
	<c:if test="${!empty sopID5}">
		<div class="row">
			<div class="col-3">
				<p>${sopID5} / ${version5} / ${authorID5}</p>
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
	<c:if test="${!empty sopID6}">
		<div class="row">
			<div class="col-3">
				<p>${sopID6} / ${version6} / ${authorID6}</p>
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
	<c:if test="${!empty sopID7}">
		<div class="row">
			<div class="col-3">
				<p>${sopID7} / ${version7} / ${authorID7}</p>
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
	<c:if test="${!empty sopID8}">
		<div class="row">
			<div class="col-3">
				<p>${sopID8} / ${version8} / ${authorID8}</p>
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
	<c:if test="${!empty sopID9}">
		<div class="row">
			<div class="col-3">
				<p>${sopID9} / ${version9} / ${authorID9}</p>
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
	<c:if test="${!empty sopID10}">
		<div class="row">
			<div class="col-3">
				<p>${sopID10} / ${version10} / ${authorID10}</p>
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