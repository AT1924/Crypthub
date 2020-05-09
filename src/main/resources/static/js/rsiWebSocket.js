$(document).ready(() => {
	setup_RSI_socket(); 
});

const MESSAGE_TYPE = {
  CONNECT: 0,
  UPDATE_RSI: 1, 
  UPDATE_TRADE: 2,
  SET_USERNAME: 3,
  START_RSI: 4,
  WALLET_INFO: 5,
  UPDATE_AUTOMATIC: 6
};

let conn;
let currAmount = 0; 

const setup_RSI_socket = () => {
     conn = new WebSocket("ws://localhost:4567/rsi")

    conn.onerror = err => {
        console.log("Connection error: ", err);
    };

    //setInterval(getRSI, 3000);
    conn.onmessage = msg => {
        const data = JSON.parse(msg.data);
        switch (data.type) {
            default:
                console.log('Unkown message type!', data.type);
                break;
            case MESSAGE_TYPE.CONNECT:
            	sendUsername();  //when the client connects send over the username of the tab. 
		        break;
	      	case MESSAGE_TYPE.UPDATE_RSI:
	      		console.log("update RSI"); 
		        updateMessage = data.payload.message;
		        let newHTML = []; 
		        for (let i = 0; i < updateMessage.length; i++) {
		        	newHTML.push('<span class = "rsi">' + updateMessage[i] + '</span>');
		        }
		        		        
		        $('#rsiValues').html(newHTML.join(""));	
		        break;
		        
		   	case MESSAGE_TYPE.UPDATE_TRADE:
		   		console.log('sent update'); 
		   		displayTrades(); 
		        updateTrade(data.payload); 
		        break;
            case MESSAGE_TYPE.WALLET_INFO:
                console.log("update wallet");
                wallet = data.payload.amount;
                newAmount = parseFloat(wallet).toFixed(2); 
                $('#total').html("Total Amount: $" + newAmount);
                
                if (newAmount > currAmount){
                	$('#total').append('<i class="ti-arrow-up"></i>'); 
                    $('#total').css('color', "#2dd3aa");
                } else if (newAmount < currAmount) {
                	$('#total').append('<i class="ti-arrow-down"></i>'); 
                    $('#total').css('color', "#ff7979");
                } else {
                    $('#total').css('color', "#75849a");
                }
                
                currAmount = newAmount; 
                 
                getInitial(); 
                             
                break;
                
           case MESSAGE_TYPE.UPDATE_AUTOMATIC: 
           		console.log("update automatic"); 
           		updateAutomatic(data.payload); 
           		break; 
        }
    }
};

const updateAutomatic = (payload) => {
	message = payload.message; 
	$("#automatic-trade").text(message);
}

const updateTrade = (payload) => {
	orderId = payload.orderId; 
	message = payload.message; 
	
	$("#" + orderId + " .trade-message").text(message);
}

const sendUsername = () => {
	let username = getCookie("username");
    // TODO Send a SCORE message to the server using `conn`
    usernameMessage = {"type": MESSAGE_TYPE.SET_USERNAME, "payload" : {"username" : username}}
    conn.send(JSON.stringify(usernameMessage)); 
}

const startRSIThread = () => {	
  let username = getCookie("username");
  // TODO Send a SCORE message to the server using `conn`
  startRSIMessage = {"type": MESSAGE_TYPE.START_RSI, "payload" : {"username" : username}}
  conn.send(JSON.stringify(startRSIMessage));
}