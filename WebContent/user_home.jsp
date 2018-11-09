<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
	<title>CTM mkii - User Home</title>
	<link rel="stylesheet" href="css/bootstrap.css">
</head>
<script src="./js/cookies.js"></script>
<script src="./js/clock.js"></script>
<body onload="checkCookie()">

	
<div class="fluid-container">
	<div class="header" style="background-color:#A0B8C4; padding:6px;">
		<h1>Home</h1>
		<div class="row">
			<div class="col-10">
			
			</div>
			<div class="col-2">
				<form method="post">
					<button type="submit" name="logout" class="btn btn-danger" value="logout">Logout</button>
				</form>
			</div>
		</div>
	</div>
<div style="padding:4px;margin:8px;">
	<div class="row">
		<div class="col">
		
		</div>
		<div class="col text-center">
				<a href="account_settings" class="btn btn-info btn-block" role="button" style="padding:4px;margin:2px;">Account Settings</a>
		</div>
		<div class="col">
		
		</div>
	</div>
	<div class="row">
		<div class="col text-center">
			<a href="create_account" class="btn btn-info btn-block" role="button" style="padding:4px;margin:2px;">Create Account</a>
		</div>
		<div class="col text-center">
			<a href="create_position" class="btn btn-info btn-block" role="button" style="padding:4px;margin:2px;">Create Position</a>
		</div>
		<div class="col text-center">
			<a href="create_sop" class="btn btn-info btn-block" role="button" style="padding:4px;margin:2px;">Create SOP</a>
		</div>
	</div>
	<div class="row">
		<div class="col">
			<a href="search_users" class="btn btn-info btn-block" role="button" style="padding:4px;margin:2px;">Search Users</a>
		</div>
		<div class="col">
			<a href="search_positions" class="btn btn-info btn-block" role="button" style="padding:4px;margin:2px;">Search Positions</a>
		</div>
		<div class="col">
			<a href="search_sops" class="btn btn-info btn-block" role="button" style="padding:4px;margin:2px;">Search SOPs</a>
		</div>
	</div>
</div>	
</div>
<div class="row align-items-center " style="padding:4px;margin:6px;">

	<div class="col-md-3 offset-md-4">
		<canvas id="canvas" width="250" height="250"
			style="background-color:white">
		</canvas>
	</div>
	<div class="col">
			<button class="btn btn-success" id=punchButton>Clock in</button>
	</div>
</div>
</body>
<script>
	var canvas = document.getElementById("canvas");
	var ctx = canvas.getContext("2d");
	var radius = canvas.height / 2;
	ctx.translate(radius, radius);
	radius = radius * 0.90
	setInterval(drawClock, 1000);
	//if user is clocked in use document.getElementById("punchButton").innerHTML="Clock out";
</script>
</html>