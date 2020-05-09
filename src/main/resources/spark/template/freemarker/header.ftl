<!DOCTYPE html>
<html lang="en">


<#macro header showLogo = "true">

	<!-- Header section -->
	<header class="header-section clearfix">
	  <h6 id = "welcomeHeader"></h6>	
	  <div class="container-fluid">
	    <div class="responsive-bar"><i class="fa fa-bars"></i></div>
	   	<#if showLogo == "true">
		    <a href="/home" class="site-logo">
		      <img id = "header-logo" src="img/Crypthub_logo_formatted.png" alt="Crypthub logo">
		    </a>
	    </#if>
	    <a href="/profile" class="user"><i class="fa fa-user"></i></a>
	    <a href="/login" id = "loginAccount" class="site-btn, site-btn" onclick="checkLogout()" >Log In</a>
	    <nav class="main-menu">
	      <ul id = "nav_list" class="menu-list">
	        <li><a href="/about">About</a></li>
	        <li><a href="/contact">Contact</a></li>
	        <li><a href="/strategy">Strategy</a></li>
			  <li><a id = "profile" href="/login">Profile</a></li>
	        <li><a id = "trades-nav-btn" href="/trades">Trades</a></li>
	      </ul>
	    </nav>
	  </div>
	</header>
	<!-- Header section end -->

</#macro>

</html>
<script src="js/header.js"></script>
