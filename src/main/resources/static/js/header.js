function checkLogout() {
    let username = getCookie("username");
    if (username !== "") {
        document.getElementById("welcomeHeader").innerHTML = "";
        deleteCookie("firstName");
        deleteCookie("lastName");
        deleteCookie("username");
    } else {

    }
}