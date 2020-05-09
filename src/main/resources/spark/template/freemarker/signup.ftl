<!DOCTYPE html>
<html lang="en">

<#include "head.ftl">
<@head title = "Sign Up Page"/>

<body>
<!-- Page Preloder -->
<div id="preloder">
  <div class="loader"></div>
</div>

<#include "header.ftl">
<@header/>

<#include "page_info.ftl">
<@pageInfo name = "Sign up"/>


<!-- Sign-up section -->
<section class="signup-page spad">
  <div class="container">
    <div class="row">
      <div class="col-lg-7 mt-lg-5">
        <form class="signup-form" method="post">
          <div class="row">
            <div class="col-md-6">
              <div id = "signUpForm"class="form-group">
                <input id = "firstName" class="check-form" type="text" placeholder="First Name:">
              </div>
            </div>
            <div class="col-md-6">
              <div class="form-group">
                <input id = "lastName" class="check-form" type="text" placeholder="Last Name:">
              </div>
            </div>
            <div class="col-md-6">
              <div class="form-group">
                <input id = "username" class="check-form" type="text" placeholder="Username:">
              </div>
            </div>
            <div class="col-md-6">
              <div class="form-group">
                <input id = "number" class="check-form" type="text" placeholder="Phone Number:">
              </div>
            </div>
            <div class="col-md-6">
              <div class="password-container form-group">
              	<div id = "signup-show-password"><i id = "signup-password-icon" class="fa fa-eye-slash"></i></div>
                <input id = "signup-password" class="check-form" type="password" placeholder="Password:">
              </div>
            </div>
            <div class="col-md-6">
              <div class="password-container form-group">
              	<div id = "verify-show-password"><i id = "verify-password-icon" class="fa fa-eye-slash"></i></div>
                <input id = "verify-password" class="check-form" type="password" placeholder="Verify Password:">
              </div>
            </div>



            <!--<div class = "col-md-12">
              <input id = "signupSubmit" class = "subNav" type="submit" placeholder="Sign Up!" onclick="registerUser(document.getElementById('signUpForm'))">
            </div>-->
          </div>
          <div id = "have-account">
            Have an account?
            <a href = "/login">Log in!</a>
          </div>
        </form>
        <div class="col-md-12">
          <button id = "signup_button" class="site-btn sb-gradients mt-4" onclick="registerUser()">Sign Up!</button>
        </div>
      </div>
      
      <div class="col-lg-5 mt-5 mt-lg-0">
        <img src="img/contact.jpg" alt="truck with a question mark">
        <p id = "safety-guaranteed">Backed by secure blockchain technologies, Crypthub uses its 
        algorithm trading strategies to guarantee safe and profitable investments. </p>
      </div>
      
    </div>
  </div>
</section>
<!-- sign up section end -->


<#include "newsletter.ftl">

<#include "footer.ftl">

<!--====== Javascripts & Jquery ======-->
<#include "scripts.ftl">

<script src="js/signup.js"></script>

</body>
</html>
