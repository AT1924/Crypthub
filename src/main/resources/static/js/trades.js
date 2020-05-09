$(document).ready(() => {
	displayTrades(); 
	showTrades(); 
});

const showTrades = () => {
	let username = getCookie("username");
	if (username != "") {
		getUserTrades(username); 
	} 
}

const displayTrades = () => {
	if (userLoggedIn()) {
		$('#trade-before-log-in').css('display','none'); 
		$('#trade-after-log-in').css('display', 'block');
    }   
    else {
    	$('#trade-after-log-in').css('display','none'); 
		$('#trade-before-log-in').css('display', 'block');
    }
}; 

const getUserTrades = (user) => {

	const postParametersPath = {username: user};

	$.post("/getUserTrades", postParametersPath, responseJSON => {
		const responseObject = JSON.parse(responseJSON);
		let trades = responseObject.trades;
 		
		//let newHTML = [];
		let tradeTable = []; 
		for (let i = 0; i < trades.length; i++) {
       		let trade = trades[i];
            let price = trade.price; 
            let type = trade.type; 
            let quantity = trade.quantity; 
            let symbol = trade.symbol; 
            let time = trade.time;  
            let id = trade.order_id; 
            let status = trade.status; 
            
            //newHTML.push('<div class = "trade-container">'); 
            tradeTable.push('<tr id = "' + id + '">'); 
            
            //newHTML.push('<span class = "trade">' + 'id: ' + id + ' price: ' + price + ' type: ' + type + ' quantity: ' + 
            //quantity + ' symbol: ' + symbol + ' time: ' + time + ' status: ' + status + '</span>');
            //newHTML.push('<span id = "' + id + '"></span>'); 
            
            tradeTable.push('<td class = "trade-time">' + time + '</td>'); 
            tradeTable.push('<td class = "trade-symbol">' + symbol + '</td>'); 
            tradeTable.push('<td class = "trade-type">' + type + '</td>'); 
            tradeTable.push('<td class = "trade-price">' + price + '</td>'); 
            tradeTable.push('<td class = "trade-quantity">' + quantity + '</td>'); 
            tradeTable.push('<td class = trade-status">' + status + '<p class = "trade-message"></p></td>'); 
            
            
            
            
            //newHTML.push('</div>'); 
            tradeTable.push('</tr>'); 
            
        }
        
        $('#trade-table-body').html(tradeTable.join("")); 
        
        //$('#trades').html(newHTML.join(""));
	});
	
}