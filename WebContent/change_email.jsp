<!DOCTYPE html>

<html>
<title>Change Password</title>
		<style type = "text/css">
		
		</style>
		
	<body>	
		<form action="${pageContext.servletContext.contextPath}/change_email" method="post">
		<h1>Make sure the new emails match <br></h1>
		<h3>Old Email: <input type="email" name="oldEmail">   <br></h3>
		<h3>New Email: <input type="email" name="newEmail"></h3>
		<h3><br>Re-Enter <br></h3>
		<h3>New Email: <input type="email" name="newEmail2"></h3>
				<td><input type = "Submit" name = "submit" value = "Submit" /> </td>
				
				
</form>
<p>Want to head back to the index? <a href="index">Index</a></p>
<p><b>Note:</b> The characters in a password field are masked (shown as asterisks or circles).</p>
	</body>
	
</html>	