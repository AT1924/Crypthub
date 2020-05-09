<!DOCTYPE html>
<html lang="en">

<#include "head.ftl">
<@head title = "Trading Page"/>

<!-- Page Preloder -->
<div id="preloder">
  <div class="loader"></div>
</div>

<#include "header.ftl">
<@header/>

<#include "page_info.ftl">
<@pageInfo name = "Strategy"/>


<div class = "before-log-in spad">
	<div class="container">
		<div class = "before-container">
	    <div class="row">
			<div class = "col-md-5 offset-5-md">
			<img src="img/connect.jpg" alt="truck with a question mark">
			</div>
			<div class="col-md-5 about-text">
				<h2>What is a Trading Strategy?</h2>
				<p>Users on Crypthub have the ability to modfiy just how they would like to set trade parameters. Even though our technology features a robust algorithm that calculates financial indicators in order to safely invest currency, users still have the capability to personalize the algorithm.</p>
			</div>
	    </div>
		<div class="row">
			<div class="col-md-5 about-text">
				<h2>Our Trading Strategies Include</h2>
				<p>Constant trades using the Crypthub's investing algorithm and manual trades that allow the user to directly communicate with the crypto market.</p>
			</div>
			<div class = "col-md-5 offset-5-md">
				<img src="img/circle.png" alt="circle picture">
			</div>
		</div>
		</div>
	</div>
</div>

<!-- Strategy section (post user logs in) -->
<div class = "after-log-in">
	<div id = "opening-for-strategy">
		<section style = "margin: 0; padding: 0" class="strategy-page">
		    <div style = "margin: 0; padding: 0" class="row text-center justify-content-center strategy-row">
		         <!--<h3 class = "text-center" id = "title">Build your trading strategy!</h3> 
		          <div class="row text-center justify-content-center strategy-row">
		              <div class="col-md-5 mx-4">
			  		    <div class="wallet">
					      <div class="wallet-text">
					        <h2 class = "text-center">Wallet</h2>
							<div class = "assets-container">
							  <ul id="assets"> </ul>
							</div>
					      </div>
					    </div>
		              </div>
		          </div>-->
		          
		      	<div id = "manual-side" class="strategy-column text-center justify-content-center">	 
		      	  <h3 style = "color: #424a56" class = "my-5 text-center form-info-title" id = "manual-info-title">Manually conduct buy, sell, and stop orders</h3>       
		      	  <p style = "margin: 3em; color: #424a56" >Use our trade manager to automatically post and manage your trades at set prices.</p>       
	              <button type = "button" id = "manual-button" class="mx-3 site-btn sb-gradients form-btn">Manual</button>
		        </div>
		        
		        <div id = "custom-side" class="strategy-column text-center justify-content-center">	      
		        	<h3 style = "color: #424a56" class = "my-5 text-center form-info-title" id = "custom-info-title">Automatically trade using our custom algorithm</h3>       
		      	  <p style = "margin: 3em; color: #424a56">Use our algorithm with custom parameters you set to find beneficial trades.</p>
	           	  <button type = "button" id = "custom-button" class="mx-3 site-btn sb-gradients form-btn">Automatic</button>
		        </div>
		        
		    </div>    
		</section>
	</div>
</div>
<!-- Strategy section end (post log in) -->


<#include "footer.ftl">


<!--====== Javascripts & Jquery ======-->

<#include "scripts.ftl">

<script src="js/strategy.js"></script>
<script src="js/wallet.js"></script>
<script src="js/strategy-form.js"></script>

</body>
</html>


