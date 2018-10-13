<!DOCTYPE html>

<html>
<title>Change Password</title>
		<style type = "text/css">
		
		</style>
		
	<body>	
		<form action="${pageContext.servletContext.contextPath}/changePassword" method="post">
		<h1>Passwords did not match. Re-enter Passwords <br></h1>
		<h3>Old Password: <input type="password" name="oldPassword">   <br></h3>
		<h3>New Password: <input type="password" name="password"></h3>
		<h3><br>Re-Enter <br></h3>
		<h3>New Password: <input type="password" name="password2"></h3>
				<td><input type = "Submit" name = "submit" value = "Submit" /> </td>
				<td><input type = "Submit" name = "Index" value = "Index" /> </td>
				
</form>

<p><b>Note:</b> The characters in a password field are masked (shown as asterisks or circles).</p>
	</body>
	
</html>	