let ctx = document.getElementById('myChart');
let initial = {};

let myChart = new Chart(ctx, {
    type: 'line',
    data: {
        labels: [],
        datasets: [{
            label: 'Total USDT Wallet Value',
            data: [],
            backgroundColor: [
                'rgba(30, 255, 210, 0.2)',
                'rgba(54, 162, 235, 0.2)',
                'rgba(255, 206, 86, 0.2)',
                'rgba(75, 192, 192, 0.2)',
                'rgba(153, 102, 255, 0.2)',
                'rgba(255, 159, 64, 0.2)'
            ],

            borderWidth: 1
        }]
    },
    options: {
        scales: {
            yAxes: [{
                ticks: {
                }
            }]
        },
        elements: {
            line: {
                tension: false // disables bezier curves
            }
        },
        animation: {
            duration: 0 // general animation time
        }
    }
});

function getInitial() {
    let responseJSON;
    values = [];
    times = [];
    let max;
    let min;
    const postParamater = {username: getCookie("username")};

    $.post("/getGraphData", postParamater, response => {
        responseJSON = JSON.parse(response);
       //console.log(responseJSON);
       
		if (responseJSON.success) {
	        for (let i = responseJSON.amounts.length-1; i >=0; i--) {
	            //times.push(new Date(responseJSON.amounts[i].time).getDate() + "/" + new Date(responseJSON.amounts[i].time).getDate());
	            times.push(moment(responseJSON.amounts[i].time).format('h:mm:ss a'));
	            values.push(responseJSON.amounts[i].value);
	        }
	
	
	        myChart.data.labels = times;
	        myChart.data.datasets[0].data = values;
	        myChart.update();
        }
    });
    //console.log(times);

    //return {times:initial.times, values:initial.values}

}


//setInterval(getInitial, 3000);