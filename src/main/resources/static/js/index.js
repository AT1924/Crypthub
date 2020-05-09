$(document).ready(() => {
	updateIndexForCookie(); 
});


const updateIndexForCookie = () => {
	let username = getCookie("username");
	if (username != "") {
		$('#home-signup-btn').text("Start trading!"); 
		$('#home-signup-btn').attr("href", "/strategy")
    }   
}