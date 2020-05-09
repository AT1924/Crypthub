const fillWalletProfile = () => {
	emptyWallet(); 
	let username = getCookie("username");
	if (username != "") {
		getWalletForProfile(username); 
	}
}

const emptyWallet = () => {
	$('#assets').empty(); 	
}

const getWalletForProfile = (user) => {

	const postParametersPath = {username: user};

	$.post("/getWallet", postParametersPath, responseJSON => {
		const responseObject = JSON.parse(responseJSON);
		console.log(responseObject);
		let keys = responseObject.keys_set;
		let assets = responseObject.assets;
		let tradable = responseObject.tradable; 
		let total = responseObject.total.toFixed(2);
		let accurate = responseObject.accurateTotal;
		
		$(".loader").fadeOut(); 
		$("#profile-preloader").delay(400).fadeOut("slow");
		$("#assets").delay(500).fadeIn("slow");
	
			
		//if keys is true
		if (keys) {
			$.post("/populateValue", postParametersPath, responseWallet => {
				console.log(responseWallet);
			});
			
			let assetsHTML = [];
			let totalHTML = []; 

			totalHTML.push('<span style = "display : inline" id="total" style = "margin-bottom: 0px">'+ 'Total Amount: ');
			totalHTML.push('$' + total +'</span>');
			totalHTML.push('<i style = "display: none" id = "total-arrow-up" class="ti-arrow-up"></i>');
			totalHTML.push('<i style = "display: none" id = "total-arrow-down" class="ti-arrow-down"></i>');
			
			if(accurate === false){
				totalHTML.push('<span style = "font-size: 11px"> Some balances may not be accounted for </span>');
			}

			for (let i = 0; i < assets.length; i++) {
	       		let coin = assets[i];
	            assetsHTML.push('<span class = "asset">' + coin.asset + ': free: ' + coin.free + ' locked: ' + coin.locked + '</span>');
	        }

        	$('#assets').html(assetsHTML.join(""));	
        	$('#total-assets-container').html(totalHTML.join("")); 
			
		}
	});
}