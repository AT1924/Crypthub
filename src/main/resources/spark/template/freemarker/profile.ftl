<!DOCTYPE html>
<html lang="en">

<#include "head.ftl">
<@head title = "Profile Page"/>

<body>
<!-- Page Preloder -->
<div id="preloder">
  <div class="loader"></div>
</div>

<#include "header.ftl">
<@header/>

<#include "page_info.ftl">
<@pageInfo name = "Profile"/>

<script src="https://cdnjs.cloudflare.com/ajax/libs/p5.js/0.7.1/p5.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/p5.js/0.7.1/addons/p5.dom.min.js"></script>
<script src = "https://cdn.jsdelivr.net/npm/chart.js@2.8.0/dist/Chart.js"></script>

<#--<script>-->
	<#--let sketch = function(p) {-->
		<#--let responseJSON;-->
		<#--let zoom = 1.00;-->
		<#--let zMin = 0.05;-->
		<#--let zMax = 9.00;-->
		<#--let sensativity = 0.5;-->
		<#--const WIDTH =650;-->
		<#--const HEIGHT = 400;-->
		<#--const PAD = 100;-->
		<#--let values = [];-->
		<#--let times = [];-->

		<#--p.setup = function(){-->

			<#--p.createCanvas(WIDTH+PAD, HEIGHT+PAD);-->
			<#--p.background(255);-->
			<#--const postParamater = {username: getCookie("username")};-->

			<#--$.post("/getGraphData", postParamater, response => {-->
				<#--responseJSON = JSON.parse(response);-->
				<#--console.log(responseJSON);-->
				<#--let maxTime = 0;-->
				<#--let minTime = Number.MAX_SAFE_INTEGER;-->
				<#--let maxValue = 0;-->
				<#--let minValue = Number.MAX_SAFE_INTEGER;-->
				<#--function convert(x, y){-->
					<#--return [(x-minTime)/(maxTime-minTime)*WIDTH,(y-minValue)/(maxValue-minValue)*HEIGHT];-->
				<#--}-->

				<#--for (let i = 0; i < responseJSON.amounts.length; i++) {-->
					<#--times.push(responseJSON.amounts[i].time);-->
					<#--values.push(responseJSON.amounts[i].value);-->
					<#--if(responseJSON.amounts[i].time > maxTime){-->
						<#--maxTime = responseJSON.amounts[i].time;-->
					<#--}-->
					<#--if(responseJSON.amounts[i].time < minTime){-->
						<#--minTime = responseJSON.amounts[i].time;-->
					<#--}-->
					<#--if(responseJSON.amounts[i].value > maxValue){-->
						<#--maxValue = responseJSON.amounts[i].value;-->
					<#--}-->
					<#--if(responseJSON.amounts[i].value < minValue){-->
						<#--minValue = responseJSON.amounts[i].value;-->
					<#--}-->
				<#--}-->
				<#--p.line(PAD,0,PAD,HEIGHT);-->
				<#--p.line(PAD,HEIGHT-1,WIDTH+PAD,HEIGHT-1);-->
				<#--p.textSize(13);-->
				<#--p.text(maxValue.toFixed(2), PAD-40, 10);-->

				<#--p.text(minValue.toFixed(2), PAD-40, HEIGHT-10);-->

				<#--p.text(new Date(maxTime).getMonth() + "/" + new Date(maxTime).getDay() + "/" + String(new Date(maxTime).getFullYear()).substring(2) , WIDTH+PAD-50, HEIGHT +20);-->
				<#--//p.fill(0, 102, 153);-->
				<#--p.strokeWeight(4);-->
				<#--p.stroke(0);-->
				<#--for (let i = 0; i < responseJSON.amounts.length -1; i++){-->
					<#--[x1, y1] = convert(responseJSON.amounts[i].time, responseJSON.amounts[i].value);-->
					<#--[x2, y2] = convert(responseJSON.amounts[i+1].time, responseJSON.amounts[i+1].value);-->
					<#--if(y2 < y1){-->
						<#--p.stroke(0,250,154);-->
					<#--} else {-->
						<#--p.stroke(250,128,114);-->
					<#--}-->
					<#--p.line(x1+PAD,HEIGHT-y1, x2+PAD, HEIGHT-y2);-->

				<#--}-->


			<#--});-->
		<#--}-->

		<#--p.draw = function() {-->
			<#--p.background(255);-->
			<#--p.strokeWeight(0.5);-->
			<#--if(responseJSON != undefined) {-->
				<#--let maxTime = 0;-->
				<#--let minTime = Number.MAX_SAFE_INTEGER;-->
				<#--let maxValue = 0;-->
				<#--let minValue = Number.MAX_SAFE_INTEGER;-->

				<#--function convert(x, y) {-->
					<#--return [(x - minTime) / (maxTime - minTime) * WIDTH, (y - minValue) / (maxValue - minValue) * HEIGHT];-->
				<#--}-->

				<#--for (let i = 0; i < responseJSON.amounts.length; i++) {-->
					<#--times.push(responseJSON.amounts[i].time);-->
					<#--values.push(responseJSON.amounts[i].value);-->
					<#--if (responseJSON.amounts[i].time > maxTime) {-->
						<#--maxTime = responseJSON.amounts[i].time;-->
					<#--}-->
					<#--if (responseJSON.amounts[i].time < minTime) {-->
						<#--minTime = responseJSON.amounts[i].time;-->
					<#--}-->
					<#--if (responseJSON.amounts[i].value > maxValue) {-->
						<#--maxValue = responseJSON.amounts[i].value;-->
					<#--}-->
					<#--if (responseJSON.amounts[i].value < minValue) {-->
						<#--minValue = responseJSON.amounts[i].value;-->
					<#--}-->
				<#--}-->
				<#--p.line(PAD, 0, PAD, HEIGHT);-->
				<#--p.line(PAD, HEIGHT - 1, WIDTH + PAD, HEIGHT - 1);-->
				<#--p.textSize(13);-->
				<#--p.text(maxValue.toFixed(2), PAD - 40, 10);-->

				<#--p.text(minValue.toFixed(2), PAD - 40, HEIGHT - 10);-->

				<#--p.text(new Date(maxTime).getMonth() + "/" + new Date(maxTime).getDay() + "/" + String(new Date(maxTime).getFullYear()).substring(2), WIDTH + PAD - 50, HEIGHT + 20);-->
				<#--//p.fill(0, 102, 153);-->
				<#--p.strokeWeight(4);-->
				<#--p.stroke(0);-->
				<#--for (let i = 0; i < responseJSON.amounts.length - 1; i++) {-->
					<#--[x1, y1] = convert(responseJSON.amounts[i].time, responseJSON.amounts[i].value);-->
					<#--[x2, y2] = convert(responseJSON.amounts[i + 1].time, responseJSON.amounts[i + 1].value);-->
					<#--// console.log([x1, y1]);-->
					<#--if (y2 < y1) {-->
						<#--p.stroke(0, 250, 154);-->
					<#--} else {-->
						<#--p.stroke(250, 128, 114);-->
					<#--}-->
					<#--p.line(x1 + PAD, HEIGHT - y1, x2 + PAD, HEIGHT - y2);-->

				<#--}-->
			<#--}-->
		<#--}-->


	<#--};-->
	<#--new p5(sketch, 'canvasHolder');-->
<#--</script>-->



<#--<script src="js/processing.js"></script>-->
<#--<script type="application/processing" data-processing-target="bingo" src="js/profile_graph.js">-->
		<#--// Pressing Control-R will render this sketch.-->
<#--&lt;#&ndash;line(30, 20, 85, 75);&ndash;&gt;-->
<#--&lt;#&ndash;let i = 0;&ndash;&gt;-->

<#--&lt;#&ndash;void setup() {  // this is run once.&ndash;&gt;-->

    <#--&lt;#&ndash;// set the background color&ndash;&gt;-->
    <#--&lt;#&ndash;background(255);&ndash;&gt;-->

    <#--&lt;#&ndash;// canvas size (Integers only, please.)&ndash;&gt;-->
    <#--&lt;#&ndash;size(800, 150);&ndash;&gt;-->

    <#--&lt;#&ndash;// smooth edges&ndash;&gt;-->
    <#--&lt;#&ndash;smooth();&ndash;&gt;-->

    <#--&lt;#&ndash;// limit the number of frames per second&ndash;&gt;-->
    <#--&lt;#&ndash;frameRate(30);&ndash;&gt;-->

    <#--&lt;#&ndash;// set the width of the line.&ndash;&gt;-->
    <#--&lt;#&ndash;strokeWeight(12);&ndash;&gt;-->
<#--&lt;#&ndash;}&ndash;&gt;-->

<#--&lt;#&ndash;void draw() {  // this is run repeatedly.&ndash;&gt;-->

    <#--&lt;#&ndash;// set the color&ndash;&gt;-->
    <#--&lt;#&ndash;stroke(random(50), random(255), random(255), 100);&ndash;&gt;-->

    <#--&lt;#&ndash;// draw the line&ndash;&gt;-->
    <#--&lt;#&ndash;line(i, 0, random(0, width), height);&ndash;&gt;-->

    <#--&lt;#&ndash;// move over a pixel&ndash;&gt;-->
    <#--&lt;#&ndash;if (i < width) {&ndash;&gt;-->
        <#--&lt;#&ndash;i++;&ndash;&gt;-->
    <#--&lt;#&ndash;} else {&ndash;&gt;-->
        <#--&lt;#&ndash;i = 0;&ndash;&gt;-->
    <#--&lt;#&ndash;}&ndash;&gt;-->
<#--&lt;#&ndash;}&ndash;&gt;-->
<#--</script>-->

<section id = "profile-account-container" class="contact-page spad">
 <div class="container">
    <div class="row justify-content-center">
      <div class="col-lg-11">
      	<div class = "row"> 
			<h2 class = "pb-3 welcome">Welcome </h2>    
		</div>
		<p class = "profile-subtitle">
		<a class = "profile-account-tab" style = "color: black; margin-right: 3em; margin-left: 0.5em" onClick = "showAccountContent()"><i class="fa fa-user-circle"></i> Account</a>    
		<a class = "profile-performance-tab" onClick = "showPerformanceContent()"> <i class="fa fa-line-chart"></i> Performance</a> 
		</p> 
		<p style = "margin-bottom: 2.5em; margin-left: 0.5em"> Manage your account settings and your wallet here.</p>    
	      <div class="row">
	      	<div class="col-lg-6 text-center">
	  		    <div style = "margin-bottom: 10px" class="wallet">
	  		    
		  		   	<div id="profile-preloader">
					  <div class="loader"></div>
					</div>
			      <div class="wallet-text">
			        <h2 class = "text-center">Wallet <i class="fa fa-bitcoin"></i></h2>
			        
			        <div id = "total-assets-container">
			        </div>
			        
					<div class = "assets-container">
					  <ul style = "display: none" id="assets"> </ul>
					</div>
			      </div>
			    </div>
			    <div id = "rsi-update-container" style = "margin-top: 2em; display: none">
			    	<h5 class = "my-3">RSI Updates</h5>
				  	<div class = "col-lg-12 my-4">
				  		<ul id="rsiValues"> </ul>
				  	</div>
				</div>


			 </div>
			 <div class="col-lg-6 mt-5 mt-lg-0">
			 	<div class = "text-center">
				 	<div class = "row">
				 		<div class="col-lg-12">
			  		   		<button id = "open-private-modal" type = "button" class="site-btn sb-gradients mx-2" data-toggle="modal" data-target="#update-keys-modal">
			  		   			Update key pair
			  		   		</button>
		  		   		</div>
		  		   	</div>
		  		   	<form class="strategy-form">
			  		   	<div class = "row my-5">
					 		<div class="col-lg-12">
					            <h5 >RSI Text Notifier  
					            <a tabindex="1" data-trigger="focus" data-toggle="popover" data-placement="top" title="What is this?" data-content="This will start text notifications to your phone that will notify you when RSI hits a certain point"><i class="ti-info-alt popover-icon-strategy"></i></a>
					            </h5>
					            <div class="form-group">
					            	<button id = "start-rsi-btn" type = "button" class="site-btn form-btn mt-4">Start!</button>
					            </div>
				            </div>
		  		    	</div>
		  		    	
		  		    	<div class = "row my-5">
		  		    		<div class="col-lg-12">
				              <h5 class="mb-3">Trailing Stop
				              <a tabindex="1" data-trigger="focus" data-toggle="popover" data-placement="top" title="What is this?" data-content="This is the trailing stop percentage"><i class="ti-info-alt popover-icon-strategy"></i></a>
				              </h5>
					          <div class="form-group">
					            <input class="check-form" type="text" placeholder="Percent:">
				              	<button class="site-btn form-btn mt-4">Lock Trailing Stop!</button>
					          </div>
					        </div>
		  		    	</div>
	  		    	</form>		
				</div>			
			 </div>	 
	     </div>
	     
	     <!--<div style = "margin-top: 6em">
	     	<p class = "profile-subtitle" >Track your porfolio growth here</p>      
		    <div class = "row">
			  	<div class = "col-lg-10">
			  		<div id = "canvasHolder">
			  			<canvas id="myChart" width="300" height="200"></canvas>
			  		</div>
			  	</div>
			</div>
		</div>-->
	</div>
  </div>
</section>

<section style = "display: none" id = "profile-performance-container" class="contact-page spad">
 <div class="container">
 	<div class="row justify-content-center">
      <div class="col-lg-11">
      	<div class = "row"> 
			<h2 class = "pb-3 welcome">Welcome </h2>    
		</div>
		<p class = "profile-subtitle">
		<a class = "profile-account-tab" style = "color: black; margin-right: 3em; margin-left: 0.5em" onClick = "showAccountContent()"><i class="fa fa-user-circle"></i> Account</a>    
		<a class = "profile-performance-tab" onClick = "showPerformanceContent()"> <i class="fa fa-line-chart"></i> Performance</a> 
		</p> 
		<p style = "margin-bottom: 2.5em; margin-left: 0.5em"> Track your porfolio growth here</p>      
		
	     <div style = "margin-top: 2em">
		    <div class = "row">
			  	<div class = "col-lg-10">
			  		<div id = "canvasHolder">
			  			<canvas id="myChart" width="300" height="200"></canvas>
			  		</div>
			  	</div>
			</div>
		</div>
	</div>
</div>
</section>

<!-- Modal -->
<div class="modal fade" id="update-keys-modal" tabindex="-1" role="dialog" aria-labelledby="modal-dialog-title" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="modal-dialog-title">Update your keys</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">
        <form id = "update-keys-form">
          <div class="form-group">
            <label for="new-public-key" class="col-form-label">New public key:</label>
            <input type="text" class="form-control" id="new-public-key">
            <p style = "color: red; display: none" id = "public-required" >This is a required field </p>
          </div>
          <div class="form-group">
            <label for="new-private-key" class="col-form-label">New private key:</label>
            <input type="text" class="form-control" id="new-private-key">
            <p style = "color: red; display: none" id = "private-required">This is a required field </p>
          </div>
        </form>
      </div>
      <div class="modal-footer">
      	<h5 style = "display: none" id = "update-key-success">Success!</h5>
        <button type="button" class="btn btn-secondary" data-dismiss="modal" id = "close-modal">Close</button>
        <button type="button" class="btn btn-primary" id = "update-keys-btn">Update!</button>
      </div>
    </div>
  </div>
</div>

<script>


</script>

<#include "newsletter.ftl">


<#include "footer.ftl">

<!--====== Javascripts & Jquery ======-->
<#include "scripts.ftl">


<script src="js/profile.js"></script>
<script src="js/wallet.js"></script>
<script src="js/trades.js"></script>
<script src="js/rsiWebSocket.js"></script>
<script src="js/valueChart.js"></script>-->

</body>

</html>
