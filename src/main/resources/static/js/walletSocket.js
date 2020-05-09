$(document).ready(()=>{
    setup_Wallet();
});

let conn;

const setup_Wallet = () => {
    conn = new WebSocket("ws://localhost:4567/rsi")

    conn.onerror = err => {
        console.log("Connection error: ", err);
    };

    conn.onmessage = msg => {
        const data = JSON.parse(msg.data);
        switch (data.type) {
            default:
                console.log('Unknown message type!', data.type);
                break;
            case MESSAGE_TYPE.CONNECT:
                console.log('connected');
                sendUsername();
                break;
            case MESSAGE_TYPE.WALLET_INFO:
                console.log("update wallet");
                wallet = data.payload.amount;

                $('#total').html("Total Amount: $" + amount);
                break;

        }
    }
}

const sendUsername = () => {
    let username = getCookie("username");
    // TODO Send a SCORE message to the server using `conn`
    usernameMessage = {"type": MESSAGE_TYPE.SET_USERNAME, "payload" : {"username" : username}}
    conn.send(JSON.stringify(usernameMessage));
}