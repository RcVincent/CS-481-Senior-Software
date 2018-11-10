
function setCookie(cname,cvalue,exdays) {
    var d = new Date();
    d.setTime(d.getTime() + (exdays*24*60*60*1000));
    var expires = "expires=" + d.toGMTString();
    document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
}

function getCookie(cname) {
    var name = cname + "=";
    var decodedCookie = decodeURIComponent(document.cookie);
    var ca = decodedCookie.split(';');
    for(var i = 0; i < ca.length; i++) {
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

function checkCookie() {
    var user=getCookie("username");
    if (user != "") {
        alert("Welcome " + user);
    } 
    else {
        alert("Your not logged in :(");
        window.location.href="login.jsp";
    }
}


function logout() {
    var user=getCookie("username");
    	setCookie("username","",9999);
        alert(user+" has been logged out.");
        window.location.href="login.jsp";
}



function checkCookieLogin() {
    var user=getCookie("username");
    if (user != "") {
        alert("Your currently logged in as " + user);
        window.location.href="user_home";
    } 
    else {
        
    }
}



