<!DOCTYPE html>

<html>
	<head>
      <title>Create Position</title>
		<style type = "text/css">
		
		</style>
		
	</head>

	<script src="./js/cookies.js"></script>
	<body onload="checkCookie()">

	<form action="${pageContext.servletContext.contextPath}/create_position" method="post">
	Position Name: <input type="text" name="positionName"><br>
	Position Duty: <textarea rows="4" cols="50" name="positionDuty" >
	Enter Position Duty here...</textarea><br>
	Priority (1-10): <input type = "text" name = "priority"><br>
	<td><input type = "Submit" name = "submit" value = "Submit" /> </td>

	</form>
	<li><a href="login">login</a></li>
	</body>
</html>		
