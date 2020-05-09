<!DOCTYPE html>
<html lang="en">

<#include "head.ftl">
<@head title = "About Page"/>

<body>
<!-- Page Preloder -->
<div id="preloder">
  <div class="loader"></div>
</div>

<#include "header.ftl">
<@header/>

<#include "page_info.ftl">
<@pageInfo name = "About Us"/>

<!-- About section -->
<section class="about-section spad">
  <div class="container">
    <div class="row">
	   	<div style = "flex: 1" class="col-lg-6">
		    <div class="gt-start-earning-anim relative">
			    <img class="gt-start-earning-anim-bg" src="../img/main-bg.png" alt="">
			    <img class="gt-start-earning-anim-m2 floater-2 ad-3-5" src="../img/btc2.png" alt="">
			    <img class="gt-start-earning-anim-m3 floater-2 ad-2-5" src="../img/btc3.png" alt="">
			</div>
		</div>
      <div style = "flex: 1" class="col-lg-6 about-text">
        <h2>What is Crypthub?</h2>
        <p>Crypthub is a consumer based cryptocurrency trading software. Using algorithmic trading strategies Crypthub safely and efficiently invests your capital into the cryptocurrency market using the Binance API.</p>
        <a href="/signup" class="site-btn sb-gradients sbg-line mt-5">Get Started</a>
      </div>
    </div>
    <!--<div class="about-img">
      <img id = "bitcoin" src="img/bank-bitcoin.jpg" alt="">
    </div>-->
   
  </div>
</section>
<!-- About section end -->

<#include "newsletter.ftl">

<!-- Team section -->
<section class="team-section spad">
  <div class="container">
    <div class="section-title text-center">
      <h2 class="animation animated fadeInLeft" data-animation="fadeInUp" data-animation-delay="0.05s" style="animation-delay: 0.05s;">Meet Our Team</h2>
      <p class="animation animated fadeInLeft" data-animation="fadeInUp" data-animation-delay="0.05s" style="animation-delay: 0.05s;">Our experts can always help you with any of your questions!</p>        
    </div>
  </div>
  <div class="team-members">
    <!-- Team member -->
  
    <div class="member team_box animation animated fadeInLeft" data-animation="fadeInUp" data-animation-delay="0.1s" style="animation-delay: 0.1s;">

      <div class="member-text">
        <div class="member-img set-bg" data-setbg="img/pavlo.jpg"></div>
        <h2>Pavlo Lyalyutskyy</h2>
        <span>Algorithmic Consultant</span>
      </div>
      <div class="member-social">
        <a href="https://www.facebook.com/pavlolyalyutskyy"><i class="fa fa-facebook"></i></a>
        <a href="https://www.linkedin.com/in/pavlo-lyalyutskyy-878b3a139/"><i class="fa fa-linkedin"></i></a>
        <a href="https://twitter.com/Lyalyutskyy"><i class="fa fa-twitter"></i></a>
      </div>
      <div class="member-info">
        <div class="member-img mf set-bg" data-setbg="img/pavlo.jpg"></div>
        <div class="member-meta">
          <h2>Pavlo Lyalyutskyy</h2>
          <span>Algorithmic Consultant</span>
        </div>
        <p>"I built my first depth first search at age 3, and it only took 3 lines of code."</p>
      </div>
    </div>
    <!-- Team member -->
    <div class="member team_box animation animated fadeInLeft" data-animation="fadeInUp" data-animation-delay="0.2s" style="animation-delay: 0.2s;">
      <div class="member-text">
        <div class="member-img set-bg" data-setbg="img/sean.jpg"></div>
        <h2>Sean Nathan</h2>
        <span>Cryptocurrency Expert</span>
      </div>
      <div class="member-social">
        <a href="https://www.facebook.com/patriots21595"><i class="fa fa-facebook"></i></a>
        <a href="https://www.linkedin.com/"><i class="fa fa-linkedin"></i></a>
        <a href="https://twitter.com/"><i class="fa fa-twitter"></i></a>
      </div>
      <div class="member-info">
        <div class="member-img mf set-bg" data-setbg="img/sean.jpg"></div>
        <div class="member-meta">
          <h2>Sean Nathan</h2>
          <span>Cryptocurrency Expert</span>
        </div>
        <p>Born and raised in the gritty south, Sean Nathan spent his childhood fighting tooth and nail to become one of the best traders this side of the Mississippi.</p>
      </div>
    </div>

    <!-- Team member -->
    <div class="member team_box animation animated fadeInLeft" data-animation="fadeInUp" data-animation-delay="0.3s" style="animation-delay: 0.3s;">
      <div class="member-text">
        <div class="member-img set-bg" data-setbg="img/christine.jpeg"></div>
        <h2>Christine Wang</h2>
        <span>Head of Design</span>
      </div>
      <div class="member-social">
        <a href="https://www.facebook.com/christine.wang.2315"><i class="fa fa-facebook"></i></a>
        <a href="https://www.linkedin.com/in/christinewangcw/"><i class="fa fa-linkedin"></i></a>
        <a href="https://twitter.com/chrissyw247"><i class="fa fa-twitter"></i></a>
      </div>
      <div class="member-info">
        <div class="member-img mf set-bg" data-setbg="img/christine.jpeg"></div>
        <div class="member-meta">
          <h2>Christine Wang</h2>
          <span>Head of Design</span>
        </div>
        <p>"while (! (succeed = try()));"</p>
      </div>
    </div>
    <!-- Team member -->
    <div class="member team_box animation animated fadeInLeft" data-animation="fadeInUp" data-animation-delay="0.4s" style="animation-delay: 0.4s;">
      <div class="member-text">
        <div class="member-img set-bg" data-setbg="img/akhil.JPG"></div>
        <h2>Akhil Trehan</h2>
        <span>Product Manager</span>
      </div>
      <div class="member-social">
        <a href="https://www.facebook.com/profile.php?id=100005109496356"><i class="fa fa-facebook"></i></a>
        <a href="https://www.linkedin.com/in/akhil-trehan-594283153/"><i class="fa fa-linkedin"></i></a>
        <a href="https://twitter.com/"><i class="fa fa-twitter"></i></a>
      </div>
      <div class="member-info">
        <div class="member-img mf set-bg" data-setbg="img/akhil.JPG"></div>
        <div class="member-meta">
          <h2>Akhil Trehan</h2>
          <span>Product Manager</span>
        </div>
        <p>"If all else fails, try text-align: center.g"</p>
      </div>
    </div>
  </div>
</section>
<!-- Team section -->


<#include "footer.ftl">

<!--====== Javascripts & Jquery ======-->

<#include "scripts.ftl">

</body>
</html>
