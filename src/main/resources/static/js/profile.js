$(document).ready(() => {
	$('.welcome').append(getCookie("firstname") + "!"); 
	fillWalletProfile(); 
	$('#update-keys-btn').click(updateKeys);
	$('#open-private-modal').click(clearModal); 
	$('#start-rsi-btn').click(showRSINotifications);
	getInitial(); 
	$('[data-toggle="popover"]').popover({
	  trigger: 'focus'
	});
});

const updatePageContent = () => {
	params = parseURLParams(window.location.href); 
	if (params.tab !== undefined) {
		console.log(params.tab[0]); 
		if (params.tab[0] === "performance") {
			$('#profile-performance-container').css('display', 'block');
			$('#profile-account-container').css('display', 'none');
		}
		else {		
			$('#profile-performance-container').css('display', 'none');
			$('#profile-account-container').css('display', 'block');
		}
	}
}

const showAccountContent = () => {	
	$('#profile-performance-container').css('display', 'none');
	$('#profile-account-container').css('display', 'block');
	$(".profile-account-tab").css('color', 'black');
	$(".profile-performance-tab").css('color', '#75849a');
}

const showPerformanceContent = () => {
	$('#profile-performance-container').css('display', 'block');
	$('#profile-account-container').css('display', 'none');
	$(".profile-performance-tab").css('color', 'black');
	$(".profile-account-tab").css('color', '#75849a');
}


const showRSINotifications = () => {
	$('#rsi-update-container').css('display', 'block');
	startRSIThread(); 
}

const clearModal = () => {
	console.log("clear modal"); 
	$('form').find('input[type=text]').val('');
	$('#update-key-success').css('display', 'none');
	$('#private-required').css('display', 'none');
	$('#public-required').css('display', 'none');
}

const checkBothKeysEntered = () => {
	let newPrivateKey = $('#new-private-key').val(); 
	let newPublicKey = $('#new-public-key').val();
	let success = true;  
	
	if (newPrivateKey === "") {
		$('#private-required').css('display', 'block');
		success = false; 
	}
	else {
		$('#private-required').css('display', 'none');
	}
	
	if (newPublicKey === "") {
		$('#public-required').css('display', 'block');
		success = false; 
	}
	else {
		$('#public-required').css('display', 'none');
	}
	
	return success; 
}

const updateKeys = event => {	
	let user = getCookie("username");
	let newPrivateKey = $('#new-private-key').val(); 
	let newPublicKey = $('#new-public-key').val(); 

	if (!checkBothKeysEntered()) {
		return; 
	}
	
	const postParametersPath = {username: user, privateKey: newPrivateKey, publicKey: newPublicKey};
	
	$.post("/updateKeys", postParametersPath, responseJSON => {
		const responseObject = JSON.parse(responseJSON);
		let success = responseObject.success; 
		console.log("SUCCESS: " + success); 
		
		//if keys is true
		if (success) {
        	$('#update-key-success').text("Success!"); 
        	fillWalletProfile(); 
		}
		else {
			$('#update-key-success').text("Invalid key, try again"); 
			emptyWallet(); 
		}
		$('#update-key-success').css('display', 'inline-block');
		$('#new-private-key').empty();
	});
}

