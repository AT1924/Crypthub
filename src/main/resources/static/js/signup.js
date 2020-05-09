$(document).ready(() => {
	$('#signup-show-password').click(showPasswordSignup);
	$('#verify-show-password').click(showPasswordVerify);
});

const showPasswordSignup = event => {
	let passwordInput = document.getElementById("signup-password");
	  if (passwordInput.type === "password") {
	    passwordInput.type = "text";
	    $("#signup-password-icon").removeClass('fa-eye-slash').addClass('fa-eye');
	    
	  } else {
	    passwordInput.type = "password";
	    $("#signup-password-icon").removeClass('fa-eye').addClass('fa-eye-slash');	    
	  }
}

const showPasswordVerify = event => {
	let passwordInput = document.getElementById("verify-password");
	  if (passwordInput.type === "password") {
	    passwordInput.type = "text";
	    $("#verify-password-icon").removeClass('fa-eye-slash').addClass('fa-eye');
	    
	  } else {
	    passwordInput.type = "password";
	    $("#verify-password-icon").removeClass('fa-eye').addClass('fa-eye-slash');	    
	  }
}

function checkAlphaNumericComplexity(pwd) {
    let letter = /[a-zA-Z]/;
    let number = /[0-9]/;
    let valid = number.test(pwd) && letter.test(pwd); //match a letter _and_ a number
    return valid;
}

function checkAlphabeticalComplexity(pwd) {
    if (pwd != null){
    let letter = /[a-zA-Z]/;
    let valid = letter.test(pwd);
    return valid;
    } else {
        return false;
    }
}

function checkNumericalComplexity(pwd) {
    if (pwd != null) {
        let number = /[0-9]/;
        let valid = number.test(pwd);
        return valid;
    } else {
        return false;
    }
}

function registerUser() {

    let $inputs = $("#signUpForm").serializeArray();
    let firstName = $("#firstName").val();
    let lastName = $("#lastName").val();
    let username = $("#username").val();
    let number = $("#number").val();
    let password = $("#signup-password").val();
    let verifyPass = $("#verify-password").val();
    let signUp = true;

    if (!checkAlphabeticalComplexity(firstName)) {
        signUp = false;
        alert("First name is a required field and can only have letters.");
    }
    else if (!checkAlphabeticalComplexity(lastName)) {
        signUp = false;
        alert("Last name is a required field and can only have letters.");
    }

    else if (username == null) {
        signUp = false;
        alert("Username is a required field.");
    }
    else if (!checkNumericalComplexity(number)) {
        signUp = false;
        alert("Phone number is a required field and can only have numbers.");
    }

    else if (!checkAlphaNumericComplexity(password)) {
        signUp = false;
        alert("Password is a required field and must have both numbers and letters.");
    }

    else if (password !== verifyPass) {
        signUp = false;
        alert("ERROR: passwords do not match");
    }
    if (signUp === true){
        let success = false;
        const postParametersSource ={firstName: firstName, lastName: lastName, username: username, number: number, password: password};
        //Make a POST request to the "/suggestions" endpoint with the word information
        $.post("/checkUser", postParametersSource, responseJSON => {
            response = JSON.parse(responseJSON);
            if(response.error === true){
                window.alert("An error has occurred during sign-up, please try again later.")
            } else if(response.exists === true){
                window.alert("This username already exists in the database. Please try a different one")
            } else{
                success = true;
            }

            if(success === true){
                $.post("/signUpUser", postParametersSource, responseJSON2 => {
                    response2 = JSON.parse(responseJSON2);
                    if(response2.exists === true){
                        document.cookie = "firstname=" + firstName;
                        document.cookie = "lastname=" + lastName;
                        document.cookie = "username=" + username;
                        let link = "/profile?username=" + username;
                        window.open(link, "_parent");
                    } else{
                        window.alert("An error has occurred during sign-up, please try again later.")
                    }
                })
            }

        });

    }




}


