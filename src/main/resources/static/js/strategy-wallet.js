const fillWalletStrategy = () => {
	$('#assets').empty(); 	
	let username = getCookie("username");
	if (username != "") {
		getWalletForStrategy(username); 
	}
}

const getWalletForStrategy = (user) => {

	const postParametersPath = {username: user};

	$.post("/getWallet", postParametersPath, responseJSON => {
		const responseObject = JSON.parse(responseJSON);
		let keys = responseObject.keys_set;
		let assets = responseObject.assets;
		let tradable = responseObject.tradable; 
		let total = responseObject.total.toFixed(2);
		let accurate = responseObject.accurateTotal;
		
		$("#custom-preloader").delay(400).fadeOut("slow");
		$("#manual-preloader").delay(400).fadeOut("slow");
			
		$("#assets-strategy-custom").delay(500).fadeIn("slow");
		$("#assets-strategy-manual").delay(500).fadeIn("slow");
		
		$(".wallet-next-btn").delay(500).fadeIn("slow");
			

		//if keys is true
		if (keys) {
			let strategyManualHTML = []; 
			let customManualHTML = []; 
			strategyManualHTML.push('<div>'); 
			customManualHTML.push('<div>');
	        
			for (let i = 0; i < tradable.length; i++) {
	       		let coin = tradable[i];
	            strategyManualHTML.push('<input type="radio" class = "coin-input" name = "strategy" id ="' + coin.asset + '" value = "' + coin.asset + '"> ' +  '<label class = "asset" for = "' + coin.asset + '">'  + coin.asset + ': free: ' + coin.free + ' locked: ' + coin.locked + '</label><br>');
	            customManualHTML.push('<input type="checkbox" class = "coin-input" name = "strategy_custom" id ="' + coin.asset + 'custom' +'" value = "' + coin.asset + '"> ' +  '<label class = "asset" for = "' + coin.asset + 'custom">'  + coin.asset + ': free: ' + coin.free + ' locked: ' + coin.locked + '</label><br>');
	        }
	        
	        strategyManualHTML.push('</div>'); 
	        customManualHTML.push('</div>'); 
        
        	$('#assets-strategy-manual').html(strategyManualHTML.join(""));			
        	$('#assets-strategy-custom').html(customManualHTML.join(""));			
		}
	});
}