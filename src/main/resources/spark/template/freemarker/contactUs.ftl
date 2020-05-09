<!DOCTYPE html>
<html lang="en">

<#include "head.ftl">
<@head title = "Contact Us Page"/>

<body>
<!-- Page Preloder -->
<div id="preloder">
  <div class="loader"></div>
</div>

<#include "header.ftl">
<@header/>

<#include "page_info.ftl">
<@pageInfo name = "Contact Us"/>

<!-- Contact section -->
<section class="contact-page spad">
  <div class="container">
    <div class="row">
      <div class="col-lg-7">
        <form class="contact-form">
          <div class="row">
            <div class="col-md-6">
              <div class="form-group">
                <input class="check-form" type="text" placeholder="First Name*:">
                <span><i class="ti-check"></i></span>
              </div>
            </div>
            <div class="col-md-6">
              <div class="form-group">
                <input class="check-form" type="text" placeholder="Last Name*:">
                <span><i class="ti-check"></i></span>
              </div>
            </div>
            <div class="col-md-6">
              <div class="form-group">
                <input class="check-form" type="text" placeholder="Email Adress*:">
                <span><i class="ti-check"></i></span>
              </div>
            </div>
            <div class="col-md-6">
              <div class="form-group">
                <input class="check-form" type="text" placeholder="Phone Number*:">
                <span><i class="ti-check"></i></span>
              </div>
            </div>
            <div class="col-md-12">
              <div class="form-group">
                <textarea placeholder="Tell us about your question!"></textarea>
              </div>
              <h5 class="mb-3">Way to Contact:</h5>
              <div class="contact-type">
                <label class="ct-label">Phone
                  <input type="radio" name="radio">
                  <span class="checkmark"></span>
                </label>
                <label class="ct-label">Email
                  <input type="radio" name="radio">
                  <span class="checkmark"></span>
                </label>
                <label class="ct-label">Other
                  <input type="radio" name="radio">
                  <span class="checkmark"></span>
                </label>
              </div>

              <button class="site-btn sb-gradients mt-4">Submit form</button>
            </div>
          </div>
        </form>
      </div>
      
      <div class="col-lg-5 mt-lg-3">
      	<!--<img src="img/animated-img1.gif" alt="truck with a question mark">-->
      	<div class="gt-transparency-anim relative">
	       <img class="gt-transparency-anim-bg" src="img/main-bg2.png" alt="">
	       <img class="gt-transparency-anim-rain-1 rain" src="img/rain-1.png" alt="">
	       <img class="gt-transparency-anim-rain-2 rain ad-2-5" src="img/rain-2.png" alt="">
	       <img class="gt-transparency-anim-cloud-1 cloudmove-1" src="img/cloud-1.png" alt="">
	       <img class="gt-transparency-anim-cloud-2 cloudmove-2" src="img/cloud-2.png" alt="">
	     </div>
        <!--<img src="img/faq.jpg" alt="truck with a question mark">-->
      </div>
      
      <!--<div class="col-lg-5 mt-5 mt-lg-0">
        <div class="map" id="map-canvas"></div>
      </div>-->
    </div>
  </div>
</section>
<!-- Contact section end -->

<#include "newsletter.ftl">

<#include "footer.ftl">


<!--====== Javascripts & Jquery ======-->
<#include "scripts.ftl">

<script src="js/main.js"></script>


<!-- load for map 
<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyB0YyDTa0qqOjIerob2VTIwo_XVMhrruxo"></script>
<script src="js/map.js"></script>-->

</body>
</html>
