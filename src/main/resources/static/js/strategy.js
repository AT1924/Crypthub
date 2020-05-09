$(document).ready(() => {
	displayStrategy();
	verifyKeys();
	
	$('#custom-button').click(showCustomForm);
	$('#manual-button').click(showManualForm);
});

let customChosen = true;

const hideForms = () => {
	$('#manual-form').css('display', 'none');
	$('#custom-form').css('display', 'none');
}

const clearForms = () => {
	$('form').find('input[type=text]').val('');
	$('form').find('input[type=radio]').prop('checked', false);
	$('form').find('input[type=checkbox]').prop('checked', false);
	firstPageCustom = true;
	firstPageManual = true;
}


const showCustomForm = () => {
	let link = "/strategyForm?type=custom"; 
	customChosen = true;
    window.open(link, "_parent");

}

const showManualForm = () => {
	let link = "/strategyForm?type=manual"; 
	customChosen = false;
    window.open(link, "_parent");
}


const conductCustomTrade = event => {
	$('#are-you-sure').hide(); 
	$('#manual-form').css('display', 'none');
	$('#custom-form').css('display', 'none');

	$('#success-message').css('display', 'block');
	$('#success-message-title').text("Congratulations!");
	document.getElementById("man_error").innerHTML = "Your trade was a success!";
	$('#track-your-trade-btn').css('display', 'inline');
	
	current_fs = $(this).parent();
	previous_fs = $(this).parent().prev();
	first_fs = $(this).parent().prev().prev();

	previous_fs.css({'transform': 'scale('+1+')', 'opacity': 1});
	$("#progressbar li").eq($("fieldset").index(previous_fs)).removeClass("active");

	first_fs.show();
	first_fs.css({'transform': 'scale('+1+')', 'opacity': 1});

	current_fs.hide();
	current_fs.css({'transform': 'scale('+1+')', 'opacity': 1});

	$("#progressbar li").eq($("fieldset").index(current_fs)).removeClass("active");

	let selectedCoins = "";
	$.each($("input[name='strategy_custom']:checked"), function(){
		fullCoin = String($(this).attr('id'));
		coin = fullCoin.substring(0, fullCoin.length-6);
		if(coin === "BTC"){
			selectedCoins = selectedCoins + "BTCUSDT";
		} else if (coin === "ETH"){
			selectedCoins = selectedCoins + "ETHUSDT";
		} else{
			selectedCoins = selectedCoins + coin + "ETH";
		}
		selectedCoins = selectedCoins + " ";
	});
	
	let upper_limit = $("#upper_limit").val();
	let lower_limit = $("#lower_limit").val();
	let multiplier = $("#multiplier").val();
	let fraction = $("#fraction").val();
	const postParams = {coins: selectedCoins, upper_limit: upper_limit, lower_limit: lower_limit,
		multiplier: multiplier, username: getCookie("username"), fraction: fraction};
	console.log(postParams);
	//TODO: error checking
	$.post("/tradingAlgo", postParams, responseJSON => {
		console.log(responseJSON);
	});

	clearForms();
	hideForms();
}

const conductManualTrade = event => {
	let coin = $('input[name=strategy]:checked').attr('id');
	let quantity = $('#man_quant').val();
	let buy_price = $('#man_buy_price').val();
	let sell_price = $('#man_sell_price').val();
	let stop_price = $('#man_stop_price').val();
	let type = $('input[name=which-type]:checked').attr('id').split("-")[0];

	let accountParam = {symbol: ""};

	if (checkNumericalComplexity(quantity)) {
		// error
		console.log(quantity);
		document.getElementById("man_error").innerHTML = "Quantity is a required field and must be a number";
		$('#man_error').css('display', 'block');

	} else 	if (checkNumericalComplexity(sell_price)) {
		// error
		document.getElementById("man_error").innerHTML = "Sell price is a required field and must be a number";
		$('#man_error').css('display', 'block');

	} else 	if (checkNumericalComplexity(buy_price)) {
		// error
		document.getElementById("man_error").innerHTML = "Buy price is a required field and must be a number";
		$('#man_error').css('display', 'block');

	} else 	if (checkNumericalComplexity(stop_price)) {
		// error
		document.getElementById("man_error").innerHTML = "Stop price is a required field and must be a number";
		$('#man_error').css('display', 'block');

	} else if (coin === undefined) {
		document.getElementById("man_error").innerHTML = "Please input an account";
		$('#man_error').css('display', 'block');
	}
	else {
            if (coin === "BTC"){
                // append an eth
                accountParam = Object.assign({}, accountParam, {symbol: "BTCUSDT"});
            } else if (coin == "ETH") {
				accountParam = Object.assign({}, accountParam, {symbol: "ETHUSDT"})
			} else if (coin == "USDT"){
				accountParam = Object.assign({}, accountParam, {symbol: "ETHUSDT"})
            } else {
                let symbol = coin + "ETH";
                accountParam = Object.assign({}, accountParam, {symbol: symbol});
            }
	}

	const postParameters ={username: getCookie("username"), quantity: quantity, sell_price: sell_price, buy_price: buy_price,
		stop_price: stop_price, symbol: accountParam.symbol, type: type};
	//console.log(postParameters);
	//Make a POST request to the "/suggestions" endpoint with the word information
	$.post("/validManual", postParameters, responseJSON => {
		let responseObject = JSON.parse(responseJSON);
		console.log(responseObject);
		const success = responseObject.success;
		if (success === true){
			if(type === "sell"){
				$.post("/startFullTradeSell", postParameters, sellResponse => {
					console.log(sellResponse);
				});
			} else if(type === "buy"){
				$.post("/startFullTradeBuy", postParameters, buyResponse => {
					console.log(buyResponse);
				});
			} else{
				console.log("An error has occured in the selection of your type.")
			}
		}

		$('#success-message').css('display', 'block');

		if (success === false) {
			$('#success-message-title').text("Sorry :( ");
			let error = responseObject.error.split(":");
			error = error[error.length - 1].trim();
			if (error === "PERCENT_PRICE") {
				document.getElementById("man_error").innerHTML = "Sell or Buy price is either too high or low from last weighted average price";
			} else if (error === "MIN_NOTIONAL") {
				document.getElementById("man_error").innerHTML = "Quantity too low";
			} else if (error === "PRICE_FILTER") {
				document.getElementById("man_error").innerHTML = "Incorrect price precision, please check the price.";
			} else if (error === "A parameter caused overflow.") {
				document.getElementById("man_error").innerHTML = "A parameter cause overflow. Check your quantity";
			} else if (error === "Price is over the symbol's maximum price.") {
				document.getElementById("man_error").innerHTML = "Set price is over symbol's maximum price";
			} else if (error === "QTY is over the symbol's maximum QTY.") {
				document.getElementById("man_error").innerHTML = "Set quantity is over symbol's maximum quantity";
			} else {
				document.getElementById("man_error").innerHTML = "An error has occurred with your trade. Please verify your set values are possible for a trade.";
			}

			$('#man_error').css({'display': 'block', 'color': '#ff4d4d'});

		} else {
			// trade was a success
			console.log("manual success"); 
			$('#success-message-title').text("Congratulations!");
			document.getElementById("man_error").innerHTML = "Your trade was a success!";
			$('#track-your-trade-btn').css('display', 'inline');
		}
			
	});
	
	clearForms();
	hideForms();	

}

// $("#manual-button").click(function() {
//
//    	$("#manual_submit").click(function() {
//
//    		current_fs = $(this).parent();
// 		previous_fs = $(this).parent().prev();
// 		first_fs = $(this).parent().prev().prev();
//
// 		previous_fs.css({'transform': 'scale('+1+')', 'opacity': 1});
// 		$("#progressbar li").eq($("fieldset").index(previous_fs)).removeClass("active");
//
// 		first_fs.show();
// 		first_fs.css({'transform': 'scale('+1+')', 'opacity': 1});
//
// 		current_fs.hide();
// 		current_fs.css({'transform': 'scale('+1+')', 'opacity': 1});
//
// 		$("#progressbar li").eq($("fieldset").index(current_fs)).removeClass("active");
//
// 		let coin = $('input[name=strategy]:checked').attr('id');
// 		let quantity = $('#man_quant').val();
// 		let buy_price = $('#man_buy_price').val();
// 		let sell_price = $('#man_sell_price').val();
// 		let stop_price = $('#man_stop_price').val();
// 		let type = $('input[name=which-type]:checked').attr('id').split("-")[0];
//
// 		let accountParam = {symbol: ""};
//
// 		if (checkNumericalComplexity(quantity)) {
// 			// error
// 			console.log(quantity);
// 			document.getElementById("man_error").innerHTML = "Quantity is a required field and must be a number";
// 			$('#man_error').css('display', 'block');
//
// 		} else 	if (checkNumericalComplexity(sell_price)) {
// 			// error
// 			document.getElementById("man_error").innerHTML = "Sell price is a required field and must be a number";
// 			$('#man_error').css('display', 'block');
//
// 		} else 	if (checkNumericalComplexity(buy_price)) {
// 			// error
// 			document.getElementById("man_error").innerHTML = "Buy price is a required field and must be a number";
// 			$('#man_error').css('display', 'block');
//
// 		} else 	if (checkNumericalComplexity(stop_price)) {
// 			// error
// 			document.getElementById("man_error").innerHTML = "Stop price is a required field and must be a number";
// 			$('#man_error').css('display', 'block');
//
// 		} else if (coin === undefined) {
// 			document.getElementById("man_error").innerHTML = "Please input an account";
// 			$('#man_error').css('display', 'block');
// 		}
// 		else {
//                 if (coin === "BTC"){
//                     // append an eth
//                     accountParam = Object.assign({}, accountParam, {symbol: "BTCUSDT"});
//                 } else if (coin == "ETH") {
// 					accountParam = Object.assign({}, accountParam, {symbol: "ETHUSDT"})
// 				} else if (coun == "USDT"){
// 					accountParam = Object.assign({}, accountParam, {symbol: "ETHUSDT"})
//                 } else {
//                     let symbol = coin + "ETH";
//                     accountParam = Object.assign({}, accountParam, {symbol: symbol});
//                 }
// 		}
//
// 		const postParameters ={username: getCookie("username"), quantity: quantity, sell_price: sell_price, buy_price: buy_price,
// 			stop_price: stop_price, symbol: accountParam.symbol, type: type};
// 		//console.log(postParameters);
// 		//Make a POST request to the "/suggestions" endpoint with the word information
// 		$.post("/validManual", postParameters, responseJSON => {
// 			let responseObject = JSON.parse(responseJSON);
// 			console.log(responseObject);
// 			const success = responseObject.success;
// 			if (success === true){
// 				if(type === "sell"){
// 					$.post("/startFullTradeSell", postParameters, sellResponse => {
// 						console.log(sellResponse);
// 					});
// 				} else if(type === "buy"){
// 					$.post("/startFullTradeBuy", postParameters, buyResponse => {
// 						console.log(buyResponse);
// 					});
// 				} else{
// 					console.log("An error has occured in the selection of your type.")
// 				}
// 			}
//
// 			$('#success-message').css('display', 'block');
//
// 			if (success === false) {
// 				$('#success-message-title').text("Sorry :( ");
// 				let error = responseObject.error.split(":");
// 				error = error[error.length - 1].trim();
// 				if (error === "PERCENT_PRICE") {
// 					document.getElementById("man_error").innerHTML = "Sell or Buy price is either too high or low from last weighted average price";
// 				} else if (error === "MIN_NOTIONAL") {
// 					document.getElementById("man_error").innerHTML = "Quantity too low";
// 				} else if (error === "PRICE_FILTER") {
// 					document.getElementById("man_error").innerHTML = "Incorrect price precision, please check the price.";
// 				} else if (error === "A parameter caused overflow.") {
// 					document.getElementById("man_error").innerHTML = "A parameter cause overflow. Check your quantity";
// 				} else if (error === "Price is over the symbol's maximum price.") {
// 					document.getElementById("man_error").innerHTML = "Set price is over symbol's maximum price";
// 				} else if (error === "QTY is over the symbol's maximum QTY.") {
// 					document.getElementById("man_error").innerHTML = "Set quantity is over symbol's maximum quantity";
// 				} else {
// 					document.getElementById("man_error").innerHTML = "An error has occurred with your trade. Please verify your set values are possible for a trade.";
// 				}
//
// 				$('#man_error').css({'display': 'block', 'color': '#ff4d4d'});
//
// 			} else {
// 				// trade was a success
// 				$('#success-message-title').text("Congratulations!");
// 				document.getElementById("man_error").innerHTML = "Your trade was a success!";
// 			}
// 		});
//
// 		clearForms();
// 		hideForms();
//
// 	});
// });
//
// $("#custom-button").click(function(){
// 	$("#custom_submit").click(function() {
//
// 		current_fs = $(this).parent();
// 		previous_fs = $(this).parent().prev();
// 		first_fs = $(this).parent().prev().prev();
//
// 		previous_fs.css({'transform': 'scale('+1+')', 'opacity': 1});
// 		$("#progressbar li").eq($("fieldset").index(previous_fs)).removeClass("active");
//
// 		first_fs.show();
// 		first_fs.css({'transform': 'scale('+1+')', 'opacity': 1});
//
// 		current_fs.hide();
// 		current_fs.css({'transform': 'scale('+1+')', 'opacity': 1});
//
// 		$("#progressbar li").eq($("fieldset").index(current_fs)).removeClass("active");
//
// 		let selectedCoins = "";
// 		$.each($("input[name='strategy_custom']:checked"), function(){
// 			if($(this).attr('id') === "BTC"){
// 				selectedCoins = selectedCoins + "BTCUSDT";
// 			} else if ($(this).attr('id') === "ETH"){
// 				selectedCoins = selectedCoins + "ETHUSDT";
// 			} else{
// 				selectedCoins = selectedCoins + $(this).attr('id') + "ETH";
// 			}
// 			selectedCoins = selectedCoins + " ";
// 		});
// 		let upper_limit = $("#upper_limit").val();
// 		let lower_limit = $("#lower_limit").val();
// 		let multiplier = $("#multiplier").val();
// 		let fraction = $("#fraction").val();
// 		const postParams = {coins: selectedCoins, upper_limit: upper_limit, lower_limit: lower_limit,
// 			multiplier: multiplier, username: getCookie("username"), fraction: fraction};
// 		console.log(postParams);
// 		//TODO: error checking
// 		$.post("/tradingAlgo", postParams, responseJSON => {
// 			console.log(responseJSON);
// 		});
//
// 		clearForms();
// 		hideForms();
// 	});
// });

function checkNumericalComplexity(pwd) {
	if (pwd !== "") {
		let number = /[0-9]/;
		let valid = number.test(pwd);
		return !valid;
	} else {
		return true;
	}
}


const displayStrategy = () => {
	if (userLoggedIn()) {
		$('.before-log-in').css('display','none');
		$('.after-log-in').css('display', 'block');
    }
    else {
    	$('.after-log-in').css('display','none');
		$('.before-log-in').css('display', 'block');
    }
};

const verifyKeys = () => {
	let username = getCookie("username");
	if (username != ""){
		const postParams = {username: username};
		$.post("/checkKeyValidity", postParams, responseJSON => {
			response = JSON.parse(responseJSON);
			if(response.keys_exist === false){
				//TODO: create popup stating key does not exist in the database
			} else if(response.keys_valid === false){
				//TODO: create popup stating key is not valid

			}
		})
	}
};

const performTrade = () => {

};
