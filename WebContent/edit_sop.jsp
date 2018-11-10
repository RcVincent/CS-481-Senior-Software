<!DOCTYPE html>


<html>
<head>
<title>Revise SOP Priority</title>
<style type="text/css">
</style>
</head>
<body>
	<h1 style="text-align: center;">
		<span style="text-decoration: underline;">Revise SOP Priority</span>
	</h1>
	<form action="${pageContext.servletContext.contextPath}/change_sopPriority"
		method="post">
		<h2>
			SOP ID: <input type="text" name="sopID"> <br>
		</h2>
		<h3>
			Priority (1-10): <input type="text" name="newPriority"><br>
		</h3>


		<td><input type="Submit" name="submit" value="Submit" /></td>
		<td><input type="Submit" name="index" value="Index" /></td>
	</form>

	</form>


</body>
</html>