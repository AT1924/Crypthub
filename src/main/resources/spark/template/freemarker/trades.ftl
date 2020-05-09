<!DOCTYPE html>
<html lang="en">

<#include "head.ftl">
<@head title = "Your Trades"/>

<body>
<!-- Page Preloder -->
<div id="preloder">
  <div class="loader"></div>
</div>

<#include "header.ftl">
<@header/>

<#include "page_info.ftl">
<@pageInfo name = "Your Trades"/>

<div id = "trade-before-log-in">
    <div class="container spad">
            <div id = "trade-before-row"class="row">
                <div class = "col-lg-5 offset-5-lg str">
                    <img id = "blockchain" src="img/blockchain.png" alt="blockchain picture">
                </div>
                <div class="col-lg-5 about-text">
                    <h2>Personalized Realtime Trade Information.</h2>
                    <span id = trade-before-info>Users have access to detailed realtime information about all trades they have submitted and the status of their open algorithmic trades.</span>
                </div>
            </div>

    </div>
</div>
<!-- Post Sign up -->

<div id = "trade-after-log-in">
	<h3 style = "margin: 50px">Manage your trades here</h3>
	<#include "trade-table.ftl">
	
	<h4 style = "margin-bottom: 3em" class = "text-center" id = "automatic-trade"></h4>

</div>

<#include "newsletter.ftl">

<#include "footer.ftl">

<!--====== Javascripts & Jquery ======-->
<#include "scripts.ftl">

<script src="https://raw.githubusercontent.com/processing-js/processing-js/v1.4.8/processing.min.js"></script>
<script src="js/trades.js"></script>
<script src="js/rsiWebSocket.js"></script>
<script src="js/trade-table.js"></script>

</body>
</html>
