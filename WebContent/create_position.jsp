<!DOCTYPE html>

<html>
	<head>
      <title>Create Position</title>
		<style type = "text/css">
		
		</style>
		
	</head>

	<body>

	<form action="${pageContext.servletContext.contextPath}/createPosition" method="post">
	Position Name: <input type="text" name="positionName"><br>
	Position Duty: <textarea rows="4" cols="50" name="positionDuty" >
	Enter Position Duty here...</textarea><br>
	Priority (1-10): <input type = "text" name = "priority"><br>
	<td><input type = "Submit" name = "submit" value = "Submit" /> </td>
	<td><input type = "Submit" name = "index" value = "Index" /> </td>

	</form>

	</body>
</html>		
