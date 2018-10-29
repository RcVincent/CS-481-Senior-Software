<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
	<title>CTM mkii - Login</title>
	<link rel="stylesheet" href="css/bootstrap.css">
</head>
<body>
<h1>Login</h1>
<div class="fluid-container">
	<form  class="form-horizontal" method="post">
		<c:if test="${! empty errorMessage}">
			<div class="row">
				<div class="col">
					<p class="alert alert-warning">${errorMessage}</p>
				</div>
			</div>
		</c:if>
		<div class="form-group row">
					<label for="email" class="control-label col-2">Email:</label>
					<input type="text" class="form-control col-10" id="email" name="email" value="${email}">
		</div>
		<div class="row">
					<label for="password" class="control-label col-2">Password:</label>
					<input type="password" class="form-control col-10" id="password" name="password" value="${password}">
		</div>
		<div class="row">
			<div class="col">
				<button type="submit" class="btn btn-info">Submit</button>
			</div>
		</div>
	</form>
	<p class="note">Don't have an account? <a href="./create_account">Create an Account</a></p>
</div>

<script type="text/javascript">

function setCookie(cname, cvalue, exhours) {
    var d = new Date();
    d.setTime(d.getTime() + (exhours*60*60*1000));
    var expires = "expires="+ d.toUTCString();
    document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
}

function getCookie(cname) {
    var name = cname + "=";
    var decodedCookie = decodeURIComponent(document.cookie);
    var ca = decodedCookie.split(';');
    for(var i = 0; i <ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) == ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) == 0) {
            return c.substring(name.length, c.length);
        }
    }
    return "";
}

function checklogin(cname) {
    var user = getCookie("username");
    if (loginStatus == true && user != "") {
        alert("Welcome again " + user);
        return true;
    } else {
       return false
    }
}


</script>
</body></html>