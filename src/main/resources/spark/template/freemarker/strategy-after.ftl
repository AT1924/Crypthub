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


<!-- Strategy section (post user logs in) -->
<div class = "after-log-in">
	
	<div id = "actual-strategy-forms">
		
		<#include "strategy-form.ftl">
		
		<div style = "display: none; margin: 5em" id = "success-message" class="text-center">
			<img style = "margin-bottom: 3em" src="img/about-img.png" alt="truck with a question mark">
			<h2 id = success-message-title>Sorry! :(</h2>
			<h3 class = "my-4" id = man_error> hey</h3>
			<a style = "display: inline" id = "track-your-trade-btn" href="/trades" target = "_blank" class="site-btn sb-gradients">Track your trade!</a>
		</div>
		
		<h4 id = "tradeProgress"></h4>
		<canvas id="pjs"> </canvas>
	</div>
</div>
<!-- Strategy section end (post log in) -->

<#include "newsletter.ftl">

<#include "footer.ftl">


<!--====== Javascripts & Jquery ======-->

 <script src="js/jquery.min.js"></script>
 <script src='http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.5/jquery-ui.min.js'>
 <script src="js/owl.carousel.min.js"></script>
 <script src="js/main.js"></script>
 <script src="js/login.js"></script>
 <script src="js/popper.min.js"></script>
 <script src="js/bootstrap.js"></script>
 <script src="js/bootstrap-slider.min.js"></script>
 <script src="js/moment.js"></script>
 <script src="js/waypoints.min.js"></script>
 <script src="js/parallax.js"></script>
 <script src="js/magnific-popup.min.js"></script> 
 <script src="js/scripts.js"></script>

<script src="js/strategy.js"></script>
<script src="js/strategy-wallet.js"></script>
<script src="js/strategy-form.js"></script>
<script src="js/strategy-after.js"></script>

</body>
</html>


