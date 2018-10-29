<!DOCTYPE html>  

 
<html>
	<head>
      <title> Create SOP </title>
		
		<link rel="stylesheet" href="css/bootstrap.css">
		

	<body>

	<h1 style="text-align: center;"><span style="text-decoration: underline;">Create SOP</span></h1>
  
	<form action="${pageContext.servletContext.contextPath}/create_sop" method="post">
	SOP Title: <input type="text" name="title"><br>
	SOP Purpose: <textarea rows="4" cols="50" name="description" >
	Enter SOP Purpose here...</textarea><br>
	Priority (1-10): <input type="text" name="priority"><br>
	Revision Number : <input type="text" name="revision"><br>

	<form action="/action_page.php">
	Select Files Associated with SOP: <input type="file" name="files" multiple>
	<input type="submit">
	<td> <input type = "Submit" name = "index" value = "Index" /> </td>
	</form>
	
	</form>
	<p>Want to head back to the index? <a href="index">Index</a>
	</body>
</html>		