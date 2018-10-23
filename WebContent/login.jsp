<!DOCTYPE html>
<html>
<head><title>CS481 Project - Login</title></head>
<body>
<h1>Login</h1>
<form  method="post">
	<table>
		<tr>
			<td class="label">Email: </td>
			<td><input type="text" name="email" size="12" value="${email}" /></td>
		</tr>
		<tr>
			<td class="label">Password: </td>
			<td><input type="password" name="password" size="12" value="${password}" /></td>
		</tr>
	</table>
<td><input type="Submit" name="submit" value="Submit"> </td>
<td><input type = "Submit" name = "Index" value = "Index" /> </td>
</form>
<p>Don't have an account? <a href="create_account">Create an Account</a></p>
<p>Want to head back to the index? <a href="index">Index</a>
</p>

</body></html>