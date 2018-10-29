<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
	<title>CTM mkii - User Home</title>
	<link rel="stylesheet" href="css/bootstrap.css">
</head>
<body>
<h1>Home</h1>
<div class="fluid-container">
	<div class="row">
		<div class="col">
			<div class="text-center">
				<a href="account_settings" class="btn btn-info act-settings-btn" role="button">Account Settings</a>
			</div>
		</div>
	</div>
	<div class="row">
		<div class="col">
			<a href="create_account" class="btn btn-info btn-block create-btns left-side-btn" role="button">Create Account</a>
		</div>
		<div class="col">
			<a href="create_position" class="btn btn-info btn-block create-btns" role="button">Create Position</a>
		</div>
		<div class="col">
			<a href="create_sop" class="btn btn-info btn-block create-btns right-side-btn" role="button">Create SOP</a>
		</div>
	</div>
	<div class="row">
		<div class="col">
			<a href="search_users" class="btn btn-info btn-block search-btns left-side-btn" role="button">Search Users</a>
		</div>
		<div class="col">
			<a href="search_positions" class="btn btn-info btn-block search-btns" role="button">Search Positions</a>
		</div>
		<div class="col">
			<a href="search_sops" class="btn btn-info btn-block search-btns right-side-btn" role="button">Search SOPs</a>
		</div>
	</div>
</div>
</body>
</html>