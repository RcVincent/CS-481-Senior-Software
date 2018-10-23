<!DOCTYPE html>
<html>
<head><title>CS481 Project - Create Account</title></head>
<body>
<h1>Create Account</h1>
<form  method="post">
	<table>
		<tr>
			<td class="label">First Name: </td>
			<td><input type="text" name="first_name" size="12" value="${first_name}" /></td>
			<td class="label">Last Name: </td>
			<td><input type="text" name="last_name" size="12" value="${last_name}" /></td>
		</tr>
		<tr>
			<td class="label">Email: </td>
			<td><input type="text" name="email" size="12" value="${email}" /></td>
			<td class="label">Confirm Email: </td>
			<td><input type="text" name="email_confirm" size="12" value="${email_confirm}" /></td>
		</tr>
		<tr>
			<td class="label">Password: </td>
			<td><input type="password" name="password" size="12" value="${password}" /></td>
			<td class="label">Confirm Password: </td>
			<td><input type="password" name="password_confirm" size="12" value="${password_confirm}" /></td>
		</tr>
	</table>
<input type="Submit" name="submit" value="Submit">
<li><a href="login">login</a></li>
<p>Want to head back to the index? <a href="index">Index</a></p>
</form>
</body></html>