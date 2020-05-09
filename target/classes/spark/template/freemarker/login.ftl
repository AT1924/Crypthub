<!DOCTYPE html>
<html lang="en">

<#include "head.ftl">
<@head title = "Log in Page"/>

<body>
<!-- Page Preloder -->
<div id="preloder">
  <div class="loader"></div>
</div>

<#include "header.ftl">
<@header/>

<#include "page_info.ftl">
<@pageInfo name = "Log in"/>

<!-- Log-in section -->
<section class="login-page spad">
  <div class="container">
    <div class="row">
      <div class="col-lg-7 mt-lg-5">
        <form class="login-form" method = "post">
          <div class="row">
            <div class="col-md-6">
              <div class="form-group">
                <input id = "username" class="check-form" type="text" placeholder="Username">
              </div>
            </div>
            <div class="col-md-6">
              <div class="password-container form-group">
              	<div id = "login-show-password"><i id = "login-password-icon" class="fa fa-eye-slash"></i></div>
                <input id = "login-password" class="check-form" type="password" placeholder="Password:">
              </div>
            </div>


          </div>
        </form>

        <div>
          <button id="login_button" class="site-btn sb-gradients mt-4">Log in!</button>
        </div>

        <div id = "have-account">
          Don't have an account? 
          <a href = "/signup">Sign up!</a>
        </div>

      </div>
      
      <div class="col-lg-5 mt-5 mt-lg-0">
      	<div class="map-container">
	        <img src="img/map.svg" alt="map" style="width: 100%">
	        <div class="indicator indicator-west">
	            <span class="indicator-item"></span>
	            <span class="indicator-content">
	                <span class="indicator-text">
	                    <span class="indicator-inner">Hey there!<br>We absolutely love crypthub!</span>
	                </span>
	            </span>
	        </div>
	        <div class="indicator indicator-west">
	            <span class="indicator-item"></span>
	            <span class="indicator-content">
	                <span class="indicator-text">
	                    <span class="indicator-inner">Hi, Join us now!!<br>Crythub has made day trading so much easier</span>
	                </span>
	            </span>
	        </div>
	        <div class="indicator indicator-west">
	            <span class="indicator-item"></span>
	            <span class="indicator-content">
	                <span class="indicator-text">
	                    <span class="indicator-inner">Whats up?!<br>The manual trades have saved me so much time.</span>
	                </span>
	            </span>
	        </div>
	        <div class="indicator indicator-east" style="display:none">
	            <span class="indicator-item"></span>
	            <span class="indicator-content">Life is really short, you know? Get out there!</span>
	        </div>
	
	    </div>
      </div>
      
    </div>
  </div>
</section>
<!-- Contact section end -->

<#include "newsletter.ftl">

<#include "footer.ftl">

<!--====== Javascripts & Jquery ======-->
<#include "scripts.ftl">


<script src="js/main.js"></script>

</body>
</html>
