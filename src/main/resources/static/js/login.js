// Waits for DOM to load before running
$(document).ready(() => {
	$('#login_button').click(validateLogin);
	$('#login-show-password').click(showPasswordFunction);
	updateWelcome(); 

});

const showPasswordFunction = event => {
	console.log("show password clicked"); 
	let passwordInput = document.getElementById("login-password");
	  if (passwordInput.type === "password") {
	    passwordInput.type = "text";
	    $("#login-password-icon").removeClass('fa-eye-slash').addClass('fa-eye');
	    
	  } else {
	    passwordInput.type = "password";
	    $("#login-password-icon").removeClass('fa-eye').addClass('fa-eye-slash');	    
	  }
}

const validateLogin = event => {
	let username = $("#username");
    let password = $("#login-password");
    
	let userVal = username.val();
    let passVal = password.val();

    const postParams = {username: userVal, password: passVal};
    $.post("/verifyLogin", postParams, responseJSON => {
        response = JSON.parse(responseJSON);
        if(response.error === true){
            window.alert("An error occurred, Try again later.");
        } else{
            if (response.login_found === false){
                window.alert("Please input a correct username-password combination");
            } else{
                document.cookie = "firstname=" + response.firstname;
                document.cookie = "lastname=" + response.lastname;
                document.cookie = "username=" + userVal;
                let link = "/profile?username=" + userVal;
                window.open(link, "_parent");
            }
        }
    });
    
}

const deleteCookie = (name) => {
  document.cookie = name + '=; expires=Thu, 01 Jan 1970 00:00:01 GMT;';
}

const getCookie = (cname) => {
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

const userLoggedIn = () => {
	let username = getCookie("username");
	if (username != "") {
		return true; 
	}
	else {
		return false; 
	}
}

const updateWelcome = () => {
	let username = getCookie("username");
	let firstname = getCookie("firstname");
	if (username != "") {
		document.getElementById("welcomeHeader").innerHTML = "Welcome " + firstname + "!";
    	document.getElementById("loginAccount").innerHTML = "Log out";
    	let link = "/profile?username=" + username;
    	let tradesLink = "/trades?username=" + username;
    	let logout = "/home";
        document.getElementById("profile").href = link;
    	document.getElementById("loginAccount").href = logout;
    	$("#trades-nav-btn").attr("href", tradesLink); 
    	$("#track-your-trade-btn").attr("href", tradesLink); 
    }   
}